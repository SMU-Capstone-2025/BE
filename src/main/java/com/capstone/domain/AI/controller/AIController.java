package com.capstone.domain.AI.controller;

import com.capstone.docs.AIControllerDocs;
import com.capstone.domain.AI.dto.AIRequest;
import com.capstone.domain.AI.dto.AIReviseRequest;
import com.capstone.domain.AI.service.AIService;
import com.capstone.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/text")
@CrossOrigin("*")
public class AIController implements AIControllerDocs
{
    private final AIService aiService;

    @PostMapping("/correct")
    public ResponseEntity<ApiResponse<Map<String, Object>>>ModifyGrammar (@RequestHeader("Authorization") String token, @RequestBody AIRequest aiRequest)
    {
        String response= aiService.correctGrammar(aiRequest,token);
        return ResponseEntity.ok(ApiResponse.onSuccess(Map.of("correctedText", response)));
    }
    @PostMapping("/summarize")
    public ResponseEntity<ApiResponse<Map<String, Object>>>ModifyDocument (@RequestHeader("Authorization") String token,@RequestBody AIRequest aiRequest)
    {
        String response= aiService.sumUpDocument(aiRequest,token);
        return ResponseEntity.ok(ApiResponse.onSuccess(Map.of("summaryText", response)));
    }
    @GetMapping("/preview")
    public ResponseEntity<ApiResponse<Map<String, Object>>> previewSummary(@RequestHeader("Authorization") String token,@RequestParam AIRequest aiRequest)
    {
        String response = aiService.sumUpDocument(aiRequest,token);
        return ResponseEntity.ok(ApiResponse.onSuccess(Map.of("preview", response)));
    }
    @PostMapping("/revise")
    public ResponseEntity<ApiResponse<Map<String, Object>>> reviseSummary(@RequestHeader("Authorization") String token, @RequestBody AIReviseRequest request)
    {
        String revisedSummary = aiService.reviseSummary(request,token);
        return ResponseEntity.ok(ApiResponse.onSuccess(Map.of("revisedText", revisedSummary)));
    }
}
