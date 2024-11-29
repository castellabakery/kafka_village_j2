package com.kafka_village_j2.log.mongodb.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "log")
@Getter
@Setter
public class Log {
    @Id
    private String id;
    private String uuid;
    private String name;
    private int age;

    @Builder
    public Log(String id, String uuid, String name, int age) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.age = age;
    }
}
