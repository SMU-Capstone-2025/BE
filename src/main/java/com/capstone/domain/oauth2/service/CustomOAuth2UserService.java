package com.capstone.domain.oauth2.service;

import com.capstone.domain.oauth2.exception.OAuth2AuthenticationProcessingException;
import com.capstone.domain.oauth2.user.OAuth2UserInfo;
import com.capstone.domain.oauth2.user.OAuth2UserInfoFactory;
import com.capstone.domain.user.entity.User;
import com.capstone.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private OAuth2UserRequest oAuth2UserRequest;
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationProcessingException {
        log.info("🔍 OAuth2UserService: 사용자 정보 요청 시작");
        this.oAuth2UserRequest = oAuth2UserRequest;

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        log.info("🔍 OAuth2UserService: 사용자 정보 로드 완료 -> {}", oAuth2User.getAttributes());


        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        String registrationId = userRequest.getClientRegistration()
                .getRegistrationId();

        String accessToken = userRequest.getAccessToken().getTokenValue();


        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId,
                accessToken,
                oAuth2User.getAttributes());

        log.info("userInfoAtt: {}", oAuth2UserInfo.getAttributes());
        log.info("userInfo: {}", oAuth2UserInfo.getEmail());
        log.info("userProfile: {}", oAuth2UserInfo.getProfileImage());

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        User existingUser = userRepository.findUserByEmail(oAuth2UserInfo.getEmail());
        log.info("user", existingUser);

        if (existingUser == null){
            User user = User.builder()
                    .email(oAuth2UserInfo.getEmail())
                    .name(oAuth2UserInfo.getName())
                    .social(registrationId)
                    .profileImage(oAuth2UserInfo.getProfileImage())
                    .build();
            userRepository.save(user);
        }
        return new OAuth2UserPrincipal(oAuth2UserInfo);
    }
}