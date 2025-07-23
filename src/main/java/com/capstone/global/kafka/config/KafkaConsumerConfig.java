package com.capstone.global.kafka.config;
import com.capstone.global.kafka.dto.ProjectAuthPayload;
import com.capstone.global.kafka.dto.ProjectChangePayload;
import com.capstone.global.kafka.dto.ProjectInvitePayload;
import com.capstone.global.kafka.dto.TaskChangePayload;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private Map<String, Object> baseConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.capstone.global.kafka.dto");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false); // 헤더에 타입 정보 생략 → DTO가 정확해야 함
        return props;
    }

    @Bean
    public ConsumerFactory<String, TaskChangePayload> taskConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                baseConsumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(TaskChangePayload.class, false)); // trusted type
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TaskChangePayload> taskKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, TaskChangePayload>();
        factory.setConsumerFactory(taskConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ProjectChangePayload> projectConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                baseConsumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(ProjectChangePayload.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProjectChangePayload> projectKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, ProjectChangePayload>();
        factory.setConsumerFactory(projectConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ProjectInvitePayload> projectInviteConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                baseConsumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(ProjectInvitePayload.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProjectInvitePayload> projectInviteKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, ProjectInvitePayload>();
        factory.setConsumerFactory(projectInviteConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ProjectAuthPayload> projectAuthConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                baseConsumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(ProjectAuthPayload.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProjectAuthPayload> projectAuthKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, ProjectAuthPayload>();
        factory.setConsumerFactory(projectAuthConsumerFactory());
        return factory;
    }
}