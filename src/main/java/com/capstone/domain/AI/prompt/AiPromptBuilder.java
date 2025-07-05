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

}
