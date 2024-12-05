package com.schedule.controller.schedule;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Schedule {

    public Schedule(String name, String content, String password) {
        this();
        this.name = name;
        this.content = content;
        this.password = password;
    }

    public Schedule(){
        this.id = UUID.randomUUID();
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    private UUID id;
    private String name;
    private String content;
    private String password;
    private LocalDateTime created;
    private LocalDateTime updated;

    public void updateSchedule(String name, String content) {
        this.name = name;
        this.content = content;
        this.updated = LocalDateTime.now();
    }
}