package com.capstone.domain.task.entity;

import com.capstone.global.entity.BaseDocument;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attachment extends BaseDocument
{
    String fileId;
    String fileName;
}
