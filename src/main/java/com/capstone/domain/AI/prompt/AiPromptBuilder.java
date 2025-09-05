package com.capstone.domain.AI.prompt;

public class AiPromptBuilder
{
    public static String correctPrompt(String request) {
        return """
                아래 HTML 문장에서 태그는 절대 변경하지 말고, 태그 안의 **텍스트만 맞춤법**을 교정해줘.
                단어 선택이나 문장 구조는 바꾸지 마.
                
                HTML 태그 구조나 속성, 줄바꿈은 원본 그대로 유지해.
                
                %s
                
                출력은 수정된 HTML 전체만 그대로 출력하고, 그 외 어떤 설명도 하지 마.
                
                """.formatted(request);
    }
    public static String summarizePrompt(String request)
    {
        return """
                아래 HTML 문장에서 **태그 구조는 절대 변경하지 말고**, 태그 안의 텍스트만 요약해 주세요.
            
                요약 규칙:
                - HTML 태그 이름, 구조, 줄바꿈, 속성은 원본 그대로 유지
                - 텍스트는 핵심을 유지하면서 **맥락이 이어지도록** 풍부하게 요약
                - 불필요한 군더더기는 줄이되, 너무 짧거나 단편적이지 않게 작성
                - 글의 주요 의미, 원인·결과·대비 관계 같은 흐름을 살려서 요약
                - 최종 출력은 요약된 HTML 전체만 반환 (설명, 코드블록, 따옴표 금지)
            
                요약 대상 HTML:
                %s
                
                """.formatted(request);
    }
    public static String revisePrompt(String request,String reviseRequest)
    {
        return """
                아래 HTML 문장에서 태그는 절대 변경하지 말고, 태그 안의 텍스트를 요청에 맞게 수정.
                단어 선택이나 문장 구조는 바꾸지 마.
                
                HTML 태그 구조나 속성, 줄바꿈은 원본 그대로 유지해.
                
                원본문장: %s
                수정 요청:%s
                원본문장을 수정 요청에 맞춰서 원본을 수정해줘
                출력은 수정된 HTML 전체만 그대로 출력하고, 그 외 어떤 설명도 하지 마.
                
                """.formatted(request,reviseRequest);
    }

}
