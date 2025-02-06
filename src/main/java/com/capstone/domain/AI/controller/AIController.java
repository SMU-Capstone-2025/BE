package com.capstone.domain.AI.controller;

import com.capstone.domain.AI.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/text")
@CrossOrigin("*")
public class AIController
{
    private final AIService aiService;

    @PostMapping("/correct")
    public ResponseEntity<Map<String, Object>>ModifyGrammar ( @RequestHeader("Authorization") String token,@RequestBody String data)
    {
        System.out.println(token+" "+data);
        String response= aiService.correctGrammar(data,token);
        return ResponseEntity.ok(Map.of("correctedText", response));
    }
    @PostMapping("/summarize")
    public ResponseEntity<Map<String, Object>>ModifyDocument (@RequestHeader("Authorization") String token,@RequestBody String data)
    {
        String response= aiService.sumUpDocument(data,token);
        return ResponseEntity.ok(Map.of("summaryText", response));
    }
    @GetMapping("/preview")
    public ResponseEntity<Map<String, Object>> previewSummary(@RequestHeader("Authorization") String token,@RequestParam String text)
    {
        String response = aiService.sumUpDocument(text,token);
        return ResponseEntity.ok(Map.of("preview", response));
    }
    @PostMapping("/revise")
    public ResponseEntity<Map<String, Object>> reviseSummary(@RequestHeader("Authorization") String token,@RequestBody Map<String, String> request)
    {
        String originalSummary = request.get("originalSummary");
        String userFeedback = request.get("feedback");
        String revisedSummary = aiService.reviseSummary(originalSummary, userFeedback,token);
        return ResponseEntity.ok(Map.of("revisedText", revisedSummary));
    }
}
