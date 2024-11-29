package com.kafka_village_j2.global.domain.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RequestDto {
    private final JsonNode filter;
    private final JsonNode action;

    @Builder
    public RequestDto(JsonNode filter, JsonNode action) {
        this.filter = filter;
        this.action = action;
    }
}
