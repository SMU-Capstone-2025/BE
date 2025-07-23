package com.capstone.domain.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class ProjectAuthMailService {
    private final JavaMailSender javaMailSender;
    private String ePw;

    @Value("${spring.mail.username}")
    private String id;

    public MimeMessage createMessage(String userEmail, String projectName, String newRole) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, userEmail);
        message.setSubject("[Doctalk] 프로젝트 권한 변경"); //메일 제목



        String msg = """
        <div style="width: 100%%; font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
          <div style="max-width: 600px; margin: auto; background: white; padding: 30px; border-radius: 10px;">
            <h2 style="color: #333;">📢 프로젝트 권한 변경 안내</h2>
            <p style="font-size: 16px; color: #555;">
              <strong style="color: #007BFF;">%s</strong> 프로젝트에서 귀하의 역할이 아래와 같이 변경되었습니다.
            </p>
            <div style="margin: 20px 0; padding: 15px; background-color: #eef2ff; border-radius: 8px; text-align: center;">
              <span style="font-size: 18px; font-weight: bold; color: #333;">
                새로운 역할: <span style="color: #2c5282;">%s</span>
              </span>
            </div>
            <p style="font-size: 14px; color: #888;">
              변경된 권한은 즉시 적용되며, 기능 접근 범위가 달라질 수 있습니다.<br>
              자세한 사항은 프로젝트 관리자에게 문의해 주세요.
            </p>
            <div style="margin-top: 30px; text-align: center;">
              <a href="%s" style="padding: 10px 20px; background-color: #007BFF; color: white; border-radius: 5px; text-decoration: none;">
                프로젝트 바로가기
              </a>
            </div>
          </div>
        </div>
        """.formatted(projectName, newRole, "https://www.naver.com");

        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress(id, "Doctalk"));

        return message;
    }

    public void sendSimpleMessage(String userEmail, String projectName, String newRole) throws Exception {
        MimeMessage message = createMessage(userEmail, projectName, newRole);
        try{
            javaMailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
    }
}
