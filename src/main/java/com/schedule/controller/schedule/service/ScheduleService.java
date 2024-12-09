package com.schedule.controller.schedule.service;

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
public class ScheduleService {

    private final ScheduleDao scheduleDao;
    private final ServiceValidationService serviceValidationService;

    //save
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto){
        Schedule schedule = new Schedule(dto.getContent(), dto.getAuthor_id());
        scheduleDao.saveSchedule(schedule);
        return getSchedule(schedule.getId());
    }

    //scheduleId로 schedule 가져오기
    public ScheduleResponseDto getSchedule(UUID scheduleId){
        return scheduleDao.findScheduleById(scheduleId);
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
        return getSchedule(updatedSchedule.getId());
    }

    //delete
    public void deleteSchedule(UUID scheduleId, String authorPassword){
        serviceValidationService.validateIdAndPassword(scheduleId, authorPassword);
        scheduleDao.deleteSchedule(scheduleId);
    }
}
