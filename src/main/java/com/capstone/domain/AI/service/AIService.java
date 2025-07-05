package com.capstone.domain.AI.service;

import com.capstone.domain.AI.dto.AIRequest;
import com.capstone.domain.AI.dto.AIReviseRequest;
import com.capstone.domain.AI.exception.AIException;
import com.capstone.domain.AI.prompt.AiPromptBuilder;
import com.capstone.domain.user.entity.MembershipType;
import com.capstone.domain.user.entity.User;
import com.capstone.domain.user.exception.UserNotFoundException;
import com.capstone.domain.user.repository.UserRepository;

import com.capstone.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.capstone.domain.AI.message.AIMessages.AI_LIMIT_EXCEEDED;
import static com.capstone.domain.user.message.UserMessages.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AIService
{
    @Value("${openai.api.key}")
    private String apiKey;

    private static final int AI_LIMIT =5;

    //24시간마다 리셋
    private static final int EXPIRATION_TIME = 24;

    private final WebClient webClient;

    private final StringRedisTemplate redisTemplate;

    private final UserRepository userRepository;

    public boolean isUsageLimitExceeded(String userEmail)
    {
        String key = "ai_limit:" + userEmail;
        String value = redisTemplate.opsForValue().get(key);
        int usageCount = value == null ? 0 : Integer.parseInt(value);

        return usageCount >= AI_LIMIT;
    }

    private void incrementUsage(String userEmail)
    {
        String key = "ai_limit:" + userEmail;
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, EXPIRATION_TIME, TimeUnit.HOURS); // 24시간 후 초기화
    }

    public void checkUserMembership(String email)
    {
        User user = userRepository.findUserByEmail(email);
        if(user==null)
        {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        if(user.getMembership().equals(MembershipType.FREE_USER))
        {
            if (isUsageLimitExceeded(email)) {
                throw new AIException(AI_LIMIT_EXCEEDED);
            }
            incrementUsage(email);
        }

    }
    public String correctGrammar(AIRequest aiRequest, CustomUserDetails userDetails)
    {

        String userEmail= userDetails.getEmail();
        checkUserMembership(userEmail);

        String request= aiRequest.getRequest();

        String prompt = AiPromptBuilder.correctPrompt(request);
        String response= askGemini(prompt).block();
        return response;
    }

    public String sumUpDocument(AIRequest aiRequest,CustomUserDetails userDetails)
    {
        String userEmail= userDetails.getEmail();
        checkUserMembership(userEmail);

        String request= aiRequest.getRequest();

        String prompt = AiPromptBuilder.summarizePrompt(request);
        String response= askGemini(prompt).block();
        return response;
    }

    public String reviseSummary(AIReviseRequest aiReviseRequest, CustomUserDetails userDetails)
    {
        String userEmail= userDetails.getEmail();
        checkUserMembership(userEmail);
        String originalSummary=aiReviseRequest.getRequest();
        String feedback =aiReviseRequest.getReviseRequest();

        String prompt=AiPromptBuilder.revisePrompt(originalSummary,feedback);
        String response= askGemini(prompt).block();
        return response;
    }


    public Mono<String> askGemini(String prompt)
    {

        return webClient.post()
                .header("Content-Type", "application/json")
                .header("x-goog-api-key", apiKey)
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
                .onErrorResume(e -> {
                    ///
                    return Mono.error(new AIException("AI 응답 처리 중 오류가 발생했습니다."));
                });



    }


}
