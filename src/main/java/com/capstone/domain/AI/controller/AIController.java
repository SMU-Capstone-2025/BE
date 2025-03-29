//package com.capstone.domain.AI.controller;
//
//import com.capstone.domain.AI.dto.AIRequest;
//import com.capstone.domain.AI.dto.AIReviseRequest;
//import com.capstone.domain.AI.service.AIService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/ai/text")
//@CrossOrigin("*")
//public class AIController
//{
//    private final AIService aiService;
//
//    @PostMapping("/correct")
//    public ResponseEntity<Map<String, Object>>ModifyGrammar (@RequestHeader("Authorization") String token, @RequestBody AIRequest aiRequest)
//    {
//        String response= aiService.correctGrammar(aiRequest,token);
//        return ResponseEntity.ok(Map.of("correctedText", response));
//    }
//    @PostMapping("/summarize")
//    public ResponseEntity<Map<String, Object>>ModifyDocument (@RequestHeader("Authorization") String token,@RequestBody AIRequest aiRequest)
//    {
//        String response= aiService.sumUpDocument(aiRequest,token);
//        return ResponseEntity.ok(Map.of("summaryText", response));
//    }
//    @GetMapping("/preview")
//    public ResponseEntity<Map<String, Object>> previewSummary(@RequestHeader("Authorization") String token,@RequestParam AIRequest aiRequest)
//    {
//        String response = aiService.sumUpDocument(aiRequest,token);
//        return ResponseEntity.ok(Map.of("preview", response));
//    }
//    @PostMapping("/revise")
//    public ResponseEntity<Map<String, Object>> reviseSummary(@RequestHeader("Authorization") String token, @RequestBody AIReviseRequest request)
//    {
//        String revisedSummary = aiService.reviseSummary(request,token);
//        return ResponseEntity.ok(Map.of("revisedText", revisedSummary));
//    }
//}
