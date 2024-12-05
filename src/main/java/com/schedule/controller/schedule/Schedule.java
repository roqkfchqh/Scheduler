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

    public Schedule(String content) {
        this();
        this.content = content;
    }

    public Schedule(){
        this.id = UUID.randomUUID();
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    private UUID id;
    private UUID author_id;
    private String content;
    private LocalDateTime created;
    private LocalDateTime updated;

    public void updateSchedule(String content) {
        this.content = content;
        this.updated = LocalDateTime.now();
    }
}