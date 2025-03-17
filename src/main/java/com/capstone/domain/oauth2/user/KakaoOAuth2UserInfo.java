package com.capstone.domain.oauth2.user;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class KakaoOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;
    private final String accessToken;
    private final String id;
    private final String email = "glitt5384@naver.com"; // 하드 코딩한 이유: 카카오 개발자 사이트에서 이메일 권한 요청을 아직 하지 않음.
    private final String name;
    private final String profileUrl;

    public KakaoOAuth2UserInfo(String accessToken, Map<String, Object> attributes) {
        this.accessToken = accessToken;
        // attributes 맵의 kakao_account 키의 값에 실제 attributes 맵이 할당되어 있음
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
        this.attributes = kakaoProfile;
        this.id = ((Long) attributes.get("id")).toString();
        //this.email = (String) this.attributes.get("account_email");
        this.name = (String) this.attributes.get("nickname");
        this.profileUrl = (String) this.attributes.get("profile_image_url");
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.KAKAO;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() { return name;}

    @Override
    public String getProfileImage() { return profileUrl;}

}