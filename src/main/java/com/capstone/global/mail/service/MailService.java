package com.capstone.global.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final String ePw = createKey();

    @Value("${spring.mail.username}")
    private String id;

    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();

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

    public static String createKey() {
        StringBuilder key = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }

    public String sendSimpleMessage(String email)throws Exception {
        MimeMessage message = createMessage(email);
        try{
            javaMailSender.send(message); // 메일 발송
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw; // 메일로 보냈던 인증 코드를 클라이언트로 리턴
    }
}