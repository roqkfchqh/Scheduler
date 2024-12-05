package com.schedule.controller.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class ScheduleResponseDto {

    private UUID id;
    private String name;
    private String content;
    private String password;
    private LocalDateTime created;
    private LocalDateTime updated;
}
