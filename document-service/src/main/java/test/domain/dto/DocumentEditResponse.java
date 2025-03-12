package test.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DocumentEditResponse {
    private String email;
    private String message;

    public DocumentEditResponse(String email, String message) {
        this.email = email;
        this.message = message;
    }
}
