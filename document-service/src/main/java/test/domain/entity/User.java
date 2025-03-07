package test.domain.entity;

import test.domain.register.dto.RegisterRequest;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private String password;

    public static User createUser(RegisterRequest registerRequest){
        return User.builder()
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .name(registerRequest.getName())
                .build();
    }

    public User(String email){
        this.email = email;
    }
}