package notification.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "notification")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    private String id;
    private String email; // 사용자명 + 깃허브처럼 STMP 를 통한 메일 전송도 고려.
    private String content;
    private String expiredDate;
    private boolean isRead;
}
