package com.schedule.controller.schedule.dto;

import com.schedule.controller.schedule.Schedule;

import java.time.LocalDateTime;
import java.util.UUID;

public class ScheduleMapper {

    //entity -> dto
    public static ScheduleResponseDto toDto(Schedule schedule) {
        return ScheduleResponseDto.builder()
                .id(schedule.getId())
                .content(schedule.getContent())
                .author_id(schedule.getAuthor_id())
                .created(schedule.getCreated())
                .updated(schedule.getUpdated())
                .build();

    }

    //dto -> entity
    public static Schedule toEntity(ScheduleRequestDto dto, UUID id) {
        return Schedule.builder()
                .id(id)
                .content(dto.getContent())
                .author_id(dto.getAuthor_id())
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();
    }
}
