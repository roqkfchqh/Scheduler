package com.schedule.controller.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ScheduleRequestDto {

    private UUID author_id;
    private String content;
}
