package com.capstone.global.kafka.config;


import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig  {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object> producerConfig() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);        // class 정보 제거
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);     // 중복 방지
        props.put(ProducerConfig.ACKS_CONFIG, "all");                  // 모든 replica 확인
        props.put(ProducerConfig.RETRIES_CONFIG, 10);                  // 재시도 횟수
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");      // 압축 전송
        props.put(ProducerConfig.LINGER_MS_CONFIG, 10);                // 전송 대기 시간
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);            // 배치 크기

        return props;
    }

    // 2번째 String 자리에 객체명 기입, Customer, User 등.
    @Bean
    public ProducerFactory<String, Object> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    // 2번째 String 자리에 객체명 기입, Customer, User 등.
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(
            ProducerFactory<String, Object> producerFactory
    ){
        return new KafkaTemplate<>(producerFactory);
    }

}