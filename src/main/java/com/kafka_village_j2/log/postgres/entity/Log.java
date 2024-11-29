package com.kafka_village_j2.log.postgres.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Log {
    @Id
    private String uuid;
    private String name;
    private int age;

    @Builder
    public Log(String uuid, String name, int age) {
        this.uuid = uuid;
        this.name = name;
        this.age = age;
    }
}
