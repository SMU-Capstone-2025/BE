package com.capstone.global.kafka.consumer;

import com.capstone.global.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailConsumer {
    private final MailService mailService;

    @KafkaListener(topics = "mail-event", groupId = "mail-group")
    public void consumeMailMessage(String message){
        try {
            mailService.processSendMessages(message);
        } catch (Exception e) {
            System.err.println("Failed to process log message: " + e.getMessage());
        }
    }
}
