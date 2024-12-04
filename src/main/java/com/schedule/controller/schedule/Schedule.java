package com.schedule.controller.schedule;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Schedule {

    public Schedule(){
        this.id = UUID.randomUUID();
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    public void updateTimestamp() {
        this.updated = LocalDateTime.now();
    }

    public Schedule(String name, String content, String password) {
        this();
        this.name = name;
        this.content = content;
        this.password = password;
    }

    private UUID id;
    private String name;
    private String content;
    private String password;
    private LocalDateTime created;
    private LocalDateTime updated;
}