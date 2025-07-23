package com.capstone.domain.mail.service;

import com.capstone.domain.mypage.dto.EmailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface MailService {
    MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException;

    String sendSimpleMessage(String email) throws Exception;

    void sendMultipleMessages(List<String> emails);

}
