package com.capstone.global.kafka.dto;

import java.util.List;
import java.util.Map;

public record TaskChangePayload(String id,
                                String title,
                                String modifiedBy,
                                String version,
                                String summary,
                                String content) {
}
