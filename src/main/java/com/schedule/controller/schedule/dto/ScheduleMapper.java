package com.schedule.controller.schedule.dto;

import com.schedule.controller.schedule.Schedule;

import java.time.LocalDateTime;
import java.util.UUID;

public class ScheduleMapper {

    //entity -> dto
    public static ScheduleResponseDto toDto(Schedule schedule) {
        return ScheduleResponseDto.builder()
                .id(schedule.getId())
                .name(schedule.getName())
                .content(schedule.getContent())
                .password(schedule.getPassword())
                .created(schedule.getCreated())
                .updated(schedule.getUpdated())
                .build();

    }

    //dto -> entity
    public static Schedule toEntity(ScheduleRequestDto dto, String password, UUID id) {
        return Schedule.builder()
                .id(id)
                .name(dto.getName())
                .content(dto.getContent())
                .password(password)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();
    }
}
