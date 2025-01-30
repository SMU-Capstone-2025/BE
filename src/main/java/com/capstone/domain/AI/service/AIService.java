package com.capstone.domain.AI.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class AIService
{
    @Value("${openai.api.url}")
    private String geminiUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    private final WebClient webClient;

    public AIService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String correctGrammar(String data)
    {
        String prompt = "다음 문서의 맞춤법을 수정하고 문단 구조를 개선해줘:\n\n" + data + "\n\n수정된 문서만 반환해줘.";
        return askGemini(prompt).block();
    }

    public String sumUpDocument(String data) {
        String prompt = "다음 문서를 간결하게 요약해줘:\n\n" + data + "\n\n요약된 내용을 반환해줘.";
        return askGemini(prompt).block();
    }
    public Mono<String> askGemini(String prompt)
    {
        String url= geminiUrl+apiKey;
        System.out.println(url);

        return webClient.post()
                .uri(url)
                .header("Content-Type", "application/json")
                .bodyValue(Map.of(
                        "contents", new Object[]{
                                Map.of("parts", new Object[]{
                                        Map.of("text", prompt)
                                })
                        }
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .map(responseBody ->
                {

                    List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");

                    if (candidates != null && !candidates.isEmpty()) {

                        Map<String, Object> candidate = candidates.get(0);
                        Map<String, Object> content = (Map<String, Object>) candidate.get("content");


                        if (content != null) {
                            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                            if (parts != null && !parts.isEmpty()) {
                                String result = (String) parts.get(0).get("text");
                                return result;
                            }
                        }
                    }
                    return "no result";
                })
                .onErrorResume(e->Mono.just(e.getMessage()));



    }


}
