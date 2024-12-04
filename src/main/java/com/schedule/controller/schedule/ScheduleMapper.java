package com.schedule.controller.schedule;

import java.time.LocalDateTime;
import java.util.UUID;

public class ScheduleMapper {

    //entity -> dto
    public static ScheduleDto toDto(Schedule schedule) {
        return new ScheduleDto(
                schedule.getName(),
                schedule.getContent(),
                schedule.getCreated(),
                schedule.getUpdated()
        );
    }

    //dto -> entity
    public static Schedule toEntity(ScheduleDto dto, String password, UUID id) {
        return new Schedule(
                id != null ? id : UUID.randomUUID(),
                dto.getName(),
                dto.getContent(),
                password,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
