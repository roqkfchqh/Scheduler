package com.schedule.controller.schedule.service;

import com.schedule.controller.common.exception.CustomException;
import com.schedule.controller.common.exception.ErrorCode;
import com.schedule.controller.schedule.model.Schedule;
import com.schedule.controller.schedule.dao.ScheduleDao;
import com.schedule.controller.schedule.dto.ScheduleRequestDto;
import com.schedule.controller.schedule.dto.ScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleCRUDService {

    private final ScheduleDao scheduleDao;
    private final ServiceValidationService serviceValidationService;

    //create
    public ScheduleResponseDto createSchedule(ScheduleRequestDto dto){
        Schedule schedule = new Schedule(dto.getContent(), dto.getAuthor_id());
        scheduleDao.createSchedule(schedule);
        return readSchedule(schedule.getId());
    }

    //read
    public ScheduleResponseDto readSchedule(UUID scheduleId){
        ScheduleResponseDto schedule = scheduleDao.findScheduleById(scheduleId);
        if(schedule == null){
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        return schedule;
    }

    //update
    public ScheduleResponseDto updateSchedule(UUID scheduleId, ScheduleRequestDto dto, String authorPassword){
        ScheduleResponseDto existingSchedule = serviceValidationService.validateIdAndPassword(scheduleId, authorPassword);

        Schedule updatedSchedule = Schedule.builder()
                .id(scheduleId)
                .content(dto.getContent())
                .created(LocalDateTime.parse(existingSchedule.getCreated()))
                .updated(LocalDateTime.now())
                .build();

        scheduleDao.updateSchedule(updatedSchedule);
        return readSchedule(updatedSchedule.getId());
    }

    //delete
    public void deleteSchedule(UUID scheduleId, String authorPassword){
        serviceValidationService.validateIdAndPassword(scheduleId, authorPassword);
        scheduleDao.deleteSchedule(scheduleId);
    }
}
