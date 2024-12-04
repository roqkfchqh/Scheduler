package com.schedule.controller.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {
    private String name;
    private String content;
    private LocalDateTime created;
    private LocalDateTime updated;
}
