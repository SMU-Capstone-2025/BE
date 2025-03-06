package domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

@SpringBootApplication(exclude = { R2dbcAutoConfiguration.class })  // ✅ R2DBC 자동 설정 제외
public class DocumentServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(DocumentServiceApplication.class, args);
	}
}