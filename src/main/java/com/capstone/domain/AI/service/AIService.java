package com.capstone.domain.AI.service;

import com.capstone.domain.AI.dto.AIRequest;
import com.capstone.domain.AI.dto.AIReviseRequest;
import com.capstone.domain.AI.exception.AIException;
import com.capstone.domain.user.entity.MembershipType;
import com.capstone.domain.user.entity.User;
import com.capstone.domain.user.exception.UserNotFoundException;
import com.capstone.domain.user.repository.UserRepository;
import com.capstone.global.jwt.JwtUtil;
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

    private final JwtUtil jwtUtil;

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
    public String correctGrammar(AIRequest aiRequest, String token)
    {

        String userEmail= jwtUtil.getEmail(token);
        checkUserMembership(userEmail);

        String request= aiRequest.getRequest();

        String prompt = "다음 문서의 맞춤법을 수정하고 문단 구조를 개선해줘:\n\n" + request + "\n\n수정된 문서만 반환해줘.";
        return askGemini(prompt).block();
    }

    public String sumUpDocument(AIRequest aiRequest,String token)
    {
        String userEmail= jwtUtil.getEmail(token);
        checkUserMembership(userEmail);

        String request= aiRequest.getRequest();

        String prompt = "다음 문서를 간결하게 요약해줘:\n\n" + request + "\n\n요약된 내용만을 반환해줘.";
        return askGemini(prompt).block();
    }

    public String reviseSummary(AIReviseRequest aiReviseRequest, String token)
    {
        String userEmail= jwtUtil.getEmail(token);
        checkUserMembership(userEmail);
        String originalSummary=aiReviseRequest.getAiResponse();
        String feedback =aiReviseRequest.getReviseRequest();

        String prompt = "다음은 기존 요약이야:\n\n" + originalSummary +
                "\n\n사용자가 다음 피드백을 제공했어:\n" + feedback +
                "\n\n이를 반영하여 개선된 요약본만을 반환해줘";

        return askGemini(prompt).block();
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
                .onErrorResume(e->Mono.just(e.getMessage()));



    }


}
