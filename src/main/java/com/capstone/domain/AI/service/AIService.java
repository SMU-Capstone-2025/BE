package com.capstone.domain.AI.service;

import com.capstone.domain.AI.message.AIMessages;
import com.capstone.global.jwt.JwtUtil;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AIService
{
    //TODO: 멤버쉽 기능 개발 후 멤버쉽에 따라 gemini 버전 차별화
    @Value("${openai.api.url}")
    private String geminiUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    private static final int AI_LIMIT =5;

    private final JwtUtil jwtUtil;

    private final WebClient webClient;

    private final Map<String, AtomicInteger> userRequestCounts = new ConcurrentHashMap<>();

    public AIService(JwtUtil jwtUtil, WebClient webClient) {
        this.jwtUtil = jwtUtil;
        this.webClient = webClient;
    }

    public boolean isUsageLimitExceeded(String userEmail)
    {
        userRequestCounts.putIfAbsent(userEmail, new AtomicInteger(0));
        return userRequestCounts.get(userEmail).get() >= AI_LIMIT;
    }

    private void incrementUsage(String userEmail)
    {
        userRequestCounts.putIfAbsent(userEmail, new AtomicInteger(0));
        userRequestCounts.get(userEmail).incrementAndGet();
    }

    public String correctGrammar(String data,String token)
    {

        String userEmail= jwtUtil.getEmail(token);
        if (isUsageLimitExceeded(userEmail)) {
            return AIMessages.AI_LIMIT_EXCEEDED;
        }
        incrementUsage(userEmail);

        String prompt = "다음 문서의 맞춤법을 수정하고 문단 구조를 개선해줘:\n\n" + data + "\n\n수정된 문서만 반환해줘.";
        return askGemini(prompt).block();
    }

    public String sumUpDocument(String data,String token)
    {
        String userEmail= jwtUtil.getEmail(token);
        if (isUsageLimitExceeded(userEmail)) {
            return AIMessages.AI_LIMIT_EXCEEDED;
        }
        incrementUsage(userEmail);

        String prompt = "다음 문서를 간결하게 요약해줘:\n\n" + data + "\n\n요약된 내용만을 반환해줘.";
        return askGemini(prompt).block();
    }

    public String reviseSummary(String originalSummary, String feedback,String token)
    {
        String userEmail= jwtUtil.getEmail(token);
        if (isUsageLimitExceeded(userEmail)) {
            return AIMessages.AI_LIMIT_EXCEEDED;
        }

        String prompt = "다음은 기존 요약이야:\n\n" + originalSummary +
                "\n\n사용자가 다음 피드백을 제공했어:\n" + feedback +
                "\n\n이를 반영하여 개선된 요약본만을 반환해줘";

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
