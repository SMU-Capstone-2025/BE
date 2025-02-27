package com.capstone.domain.oauth2.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static com.capstone.domain.oauth2.user.OAuth2Provider.NAVER;

@RequiredArgsConstructor
@Component
public class NaverOAuth2UserUnlink implements OAuth2UserUnlink {

    private final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("https://nid.naver.com/oauth2.0/token");

    private final RestTemplate restTemplate;
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Override
    public void unlink(String accessToken) {

        String url = String.valueOf(builder
                .queryParam("service_provider", "NAVER")
                .queryParam("grant_type", "delete")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("access_token", accessToken));

        UnlinkResponse response = restTemplate.getForObject(url, UnlinkResponse.class);

        if (response != null && !"success".equalsIgnoreCase(response.getResult())) {
            throw new RuntimeException("Failed to Naver Unlink");
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class UnlinkResponse {
        @JsonProperty("access_token")
        private final String accessToken;
        private final String result;
    }
}