package com.schedule.controller.schedule.service;

import com.schedule.common.exception.CustomException;
import com.schedule.common.exception.ErrorCode;
import com.schedule.controller.schedule.dao.ScheduleDao;
import com.schedule.controller.schedule.dto.ScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SchedulePagingService {

    private final ScheduleDao scheduleDao;

    //paging
    public List<ScheduleResponseDto> getPagedSchedules(String authorName, LocalDate date, int page, int size){
        if(page < 1 || size < 1){
            throw new CustomException(ErrorCode.PAGING_ERROR);
        }

        List<ScheduleResponseDto> schedules = scheduleDao.findAllSchedule(authorName, date);
        if(schedules.isEmpty()){
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        int start = (page - 1) * size;
        int end = Math.min(start + size, schedules.size());
        if(start >= schedules.size()){
            return List.of();
        }

        return schedules.subList(start, end);
    }
}
