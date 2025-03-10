package com.capstone.domain.document.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@org.springframework.data.mongodb.core.mapping.Document(collection = "document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    @Id
    private String id;
    private String content;
    private List<String> logs;
    private List<String> attachments;

    public Document(String id, String content){
        this.id = id;
        this.content = content;
    }
}
