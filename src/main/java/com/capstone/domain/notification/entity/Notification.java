package com.capstone.domain.notification.entity;


import com.capstone.global.entity.BaseDocument;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Document(collection = "notification")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseDocument {
    @Id
    private String id;
    private String content;
    private String expiredDate;
    private List<String> owners;
    private boolean isRead;

    public static Notification createNotification(String content, List<String> emails){
        return Notification.builder()
                .content(content)
                .expiredDate(LocalDateTime.now().toString())
                .owners(emails)
                .isRead(false)
                .build();
    }
}
