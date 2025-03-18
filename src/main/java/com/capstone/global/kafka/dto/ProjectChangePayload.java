package com.capstone.global.kafka.dto;

import java.util.List;
import java.util.Map;

public record ProjectChangePayload(String target, Map<String, String> authorities, List<String> emails) {

}