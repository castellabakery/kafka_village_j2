package com.kafka_village_j2.log.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.kafka_village_j2.kafka.enumeration.DdlType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class LogDto {

    @Getter
    public static class Filter {
        @NotNull
        @NotEmpty
        private String uuid;
        private String name;
        private int age;
    }

    @Getter
    public static class Action {
        private String name;
        private int age;
    }

    @Getter
    @NoArgsConstructor
    public static class Create {
        @NotNull
        @NotEmpty
        private String uuid;
        private String name;
        private int age;

        @Builder
        public Create(String uuid, String name, int age) {
            this.uuid = uuid;
            this.name = name;
            this.age = age;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Update {
        @NotNull
        @Valid
        private Filter filter;
        @NotNull
        private Action action;

        @Builder
        public Update(Filter filter, Action action) {
            this.filter = filter;
            this.action = action;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Delete {
        @NotNull
        @Valid
        private Filter filter;

        @Builder
        public Delete(Filter filter) {
            this.filter = filter;
        }
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class Message {
        private DdlType type;
        private JsonNode message;

        @Builder
        public Message(DdlType type, JsonNode message) {
            this.type = type;
            this.message = message;
        }
    }
}
