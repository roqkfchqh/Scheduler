package com.schedule.controller.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Schedule {

    //save 할 때 씀
    public Schedule(String content) {
        this();
        this.content = content;
    }

    //기본생성자
    public Schedule(){
        this.id = UUID.randomUUID();
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    private UUID id;
    private UUID author_id;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime created;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime updated;
}