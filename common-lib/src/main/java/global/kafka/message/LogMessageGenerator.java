package global.kafka.message;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Iterator;

public class LogMessageGenerator {

    // 메시지 템플릿
    public static final String TASK_CREATED = "작업이 생성되었습니다. 제목: {title}, 생성자: {createdBy}, ID: {id}";
    public static final String VERSION_ADDED = "버전이 추가되었습니다. 버전: {version}, 작업 ID: {taskId}, 수정자: {modifiedBy}";
    public static final String STATUS_UPDATED = "작업 상태가 업데이트되었습니다. 작업 ID: {taskId}, 상태: {status}";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * DTO 데이터를 기반으로 메시지를 생성하는 메서드
     */
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
                JsonNode valueNode = jsonNode.get(key);

                String value;
                if (valueNode.isArray() || valueNode.isObject()) {
                    // 배열 또는 객체는 문자열로 변환
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