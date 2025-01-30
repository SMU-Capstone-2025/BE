package com.capstone.domain.AI.controller;

import com.capstone.domain.AI.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin("*")
public class AIController
{
    private final AIService aiService;

    @PostMapping("/text/correct")
    public ResponseEntity<Map<String, Object>>ModifyGrammar (@RequestBody String data)
    {
        String response= aiService.correctGrammar(data);
        return ResponseEntity.ok(Map.of("correctedText", response));
    }
    @PostMapping("/text/correct")
    public ResponseEntity<Map<String, Object>>ModifyDocument (@RequestBody String data)
    {
        String response= aiService.sumUpDocument(data);
        return ResponseEntity.ok(Map.of("correctedText", response));
    }
}
