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
                아래 HTML 문장에서 태그 구조는 그대로 유지하고,
                태그 안의 텍스트 내용을 간결하게 요약해 주세요.
                - HTML 태그 이름, 구조, 줄바꿈, 속성은 변경하지 말 것
                - 단, 태그 내부의 텍스트는 핵심만 남기고 요약할 것
                - 출력은 요약된 HTML 전체만 반환 (설명 없이)
                
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
