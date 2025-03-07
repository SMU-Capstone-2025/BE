package domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "user")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String profileImage;
    // 기본 회원가입 시 null.
    private String social;
    private String password;
    private List<String> projectIds;



    public User(String email){
        this.email = email;
    }
}