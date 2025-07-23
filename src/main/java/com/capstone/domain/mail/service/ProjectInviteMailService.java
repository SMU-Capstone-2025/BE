package com.capstone.domain.mail.service;

import com.capstone.domain.project.entity.Project;
import com.capstone.domain.user.entity.PendingUser;
import com.capstone.domain.user.entity.User;
import com.capstone.domain.user.repository.PendingUserRepository;
import com.capstone.domain.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectInviteMailService {
    private final JavaMailSender javaMailSender;
    private final PendingUserRepository pendingUserRepository;
    private final UserRepository userRepository;
    private String ePw;

    @Value("${spring.mail.username}")
    private String id;

    public MimeMessage createMessage(String invitor, String invitee, String projectId, String projectName) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, invitee);
        message.setSubject("[Doctalk] 프로젝트 초대 확인"); //메일 제목

        UUID userCredentialCode = UUID.randomUUID();

        User user = userRepository.findUserByEmail(invitee);

        PendingUser pendingUser = PendingUser.builder()
                .projectId(projectId)
                .userId(user.getId())
                .credentialCode(userCredentialCode.toString())
                .email(invitee)
                .build();

        pendingUserRepository.save(pendingUser);



        String msg = """
        <div style="font-family: Arial, sans-serif; background-color: #f7f9fc; padding: 30px;">
          <div style="max-width: 600px; margin: 0 auto; background: #ffffff; padding: 40px; border-radius: 10px; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);">
            <h2 style="color: #333333; font-size: 24px;">🚀 프로젝트 초대</h2>
            <p style="font-size: 16px; color: #555555; margin-bottom: 20px;">
              <strong style="color: #2c3e50;">%s</strong> 님이 당신을 <strong style="color: #3498db;">%s</strong> 프로젝트에 초대했습니다.
            </p>
            <p style="font-size: 15px; color: #666666;">
              아래 버튼을 눌러 프로젝트에 참여하세요.
            </p>
            <div style="text-align: center; margin: 30px 0;">
              <a href="%s" style="background-color: #3498db; color: #ffffff; padding: 12px 24px; text-decoration: none; border-radius: 6px; font-weight: bold;">
                👉 참여하기
              </a>
            </div>
            <p style="font-size: 13px; color: #999999; text-align: center;">
              본 이메일은 Doctalk 시스템에서 자동 발송되었습니다.
            </p>
          </div>
        </div>
        """.formatted(invitor, projectName, "http://localhost:8080/project/invite/accept?credentialCode=" + userCredentialCode);
        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress(id, "Doctalk"));

        return message;
    }

    public void sendSimpleMessage(String invitor, String invitee, String projectId, String projectName) throws Exception {
        MimeMessage message = createMessage(invitor, invitee, projectId, projectName);
        try{
            javaMailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
    }
}
