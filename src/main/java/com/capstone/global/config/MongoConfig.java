package com.capstone.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoConfig {
    @Bean
    public MappingMongoConverter mappingMongoConverter(
            MongoDatabaseFactory factory, MongoMappingContext context, MongoCustomConversions conversions) {
        MappingMongoConverter converter = new MappingMongoConverter(factory, context);
        converter.setCustomConversions(conversions);
        converter.setMapKeyDotReplacement("_"); // 점(.)을 밑줄(_)로 자동 변환
        return converter;
    }
}