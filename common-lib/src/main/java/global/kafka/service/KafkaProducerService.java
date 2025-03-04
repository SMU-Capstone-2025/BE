package global.kafka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import global.kafka.dto.RequestPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import task.dto.TaskDto;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendTaskEvent(String topic, String action, TaskDto taskDto, String email) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, Object> data = new HashMap<>();
            data.put("version", taskDto.getVersion());
            data.put("content", taskDto.getContent());

            @SuppressWarnings("unchecked")
            Map<String, Object>[] requestData = new Map[]{data};

            RequestPayload<Map<String, Object>[]> payload = new RequestPayload<>(
                    taskDto.getId(),
                    email,
                    action,
                    requestData
            );

            String message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}