package task.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDto {
    @Nullable // 신규 작성 시에는 없음.
    private String id; // taskId
    @Nullable
    private String title;
    private String modifiedBy;
    private String version;
    private String summary;
    private String content;
}
