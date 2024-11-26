package com.kafka_village_j2.postgres;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Log {
    @Id
    private String uuid;
    private String name;
    private int age;
}
