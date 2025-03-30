package com.capstone.global.mail.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private String ePw;
    private final ObjectMapper objectMapper;

    @Value("${spring.mail.username}")
    private String id;

    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        ePw = createKey();
        message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
        message.setSubject("[Doctalk] 회원가입 인증 코드"); //메일 제목

        String msg = "<div style=\"width: 100%; font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;\">"
                + "<div style=\"max-width: 600px; margin: auto; background: white; padding: 30px; border-radius: 10px; text-align: center;\">"
                + "<h1 style=\"color: #333; font-size: 24px;\">이메일 주소 확인</h1>"
                + "<p style=\"font-size: 16px; color: #555;\">아래 인증 코드를 회원가입 화면에서 입력해주세요.</p>"
                + "<div style=\"margin: 20px 0; padding: 15px; background-color: #F4F4F4; border-radius: 10px; display: inline-block;\">"
                + "<span style=\"font-size: 32px; font-weight: bold; color: #333; letter-spacing: 4px;\">" + ePw + "</span>"
                + "</div>"
                + "<p style=\"font-size: 14px; color: #888;\">이 코드는 10분 동안 유효합니다.</p>"
                + "</div>"
                + "</div>";

        message.setText(msg, "utf-8", "html"); //내용, charset타입, subtype
        message.setFrom(new InternetAddress(id,"Doctalk")); //보내는 사람의 메일 주소, 보내는 사람 이름

        return message;
    }

    public String createKey() {
        StringBuilder key = new StringBuilder();
        SecureRandom rnd = new SecureRandom();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }

    public String sendSimpleMessage(String email) throws Exception {
        MimeMessage message = createMessage(email);
        try{
            javaMailSender.send(message); // 메일 발송
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw; // 메일로 보냈던 인증 코드를 클라이언트로 리턴
    }

    public void sendMultipleMessages(List<String> emails){
        emails.forEach(email -> {
                    try{
                        sendSimpleMessage(email);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    public void processSendMessages(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, Object> map = objectMapper.readValue(
                    message, new TypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> data = (Map<String, Object>) map.get("data");

            List<String> emails = (List<String>) map.get("email");

            sendMultipleMessages(emails);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}