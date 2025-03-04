package task.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Version {
    private String taskId;
    private String version;
    private String modifiedDateTime;
    private String modifiedBy;
    private String summary;
    private String content;
    private List<String> attachmentList; // 사진, 표 등 첨부 파일 리스트.
}

