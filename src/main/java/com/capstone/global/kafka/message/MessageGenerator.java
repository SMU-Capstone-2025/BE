package com.capstone.global.kafka.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class MessageGenerator {

    public static final String TASK_CREATED = "작업이 생성되었습니다. 제목: {title}, 작업자: {modifiedBy}";
    public static final String TASK_UPDATED = "작업에 변동사항이 있습니다. 제목: {title}, 작업자: {email}";
    public static final String TASK_DELETED = "작업이 삭제되었습니다. 제목: {title}, 작업자: {email}";

    public static final String PROJECT_UPDATED =
            "프로젝트에 변동사항이 있습니다! <br>"
                + "- 프로젝트 이름: {title}<br>"
                + "- 프로젝트 설명: {description}";
    public static final String PROJECT_CREATED =
            "새 프로젝트가 생성되었습니다! 버튼을 눌러 참여하세요!<br>"
                    + "- 프로젝트 이름: {projectName}<br>";

    public static final String PROJECT_INVITED =
            "프로젝트에 초대되었습니다! 버튼을 눌러 참여하세요!<br>"
                    + "- 프로젝트 이름: {projectName}<br>";

    public static final String AUTH_UPDATED = "프로젝트 내 권한이 변경되었습니다.<br>"
            + "- 프로젝트명: {projectName} <br>"
            + "- 대상: {names} <br>"
            + "- 권한: {authorities}";





    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String generateFromDto(String template, T dto) {
        try {


            // DTO를 JSON 문자열로 변환
            String json = objectMapper.writeValueAsString(dto);

            // JSON 데이터를 파싱
            JsonNode jsonNode = objectMapper.readTree(json);

            // JSON 필드를 순회하며 템플릿에 반영
            Iterator<String> fieldNames = jsonNode.fieldNames();

            while (fieldNames.hasNext()) {
                String key = fieldNames.next();
                log.info("key: {}", key);
                JsonNode valueNode = jsonNode.get(key);

                String value;
                if (valueNode.isArray()) {
                    // 배열을 쉼표로 구분된 문자열로 변환
                    value = StreamSupport.stream(valueNode.spliterator(), false)
                            .map(JsonNode::asText)
                            .collect(Collectors.joining(", "));
                } else if (valueNode.isObject()) {
                    // 객체는 JSON 문자열 유지
                    value = objectMapper.writeValueAsString(valueNode);
                } else {
                    // 단순 값은 텍스트로 변환
                    value = valueNode.asText();
                }

                // 템플릿에 값 삽입
                template = template.replace("{" + key + "}", value);
            }

            return template;
        } catch (Exception e) {
            throw new RuntimeException("로그 메시지 생성 중 오류 발생: " + e.getMessage(), e);
        }
    }
}