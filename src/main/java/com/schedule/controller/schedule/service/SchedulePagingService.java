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
public class SchedulePagingService {

    private final ScheduleDao scheduleDao;

    //paging
    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getPagedSchedules(String authorName, LocalDate date, int page, int size){
        if(page < 1 || size < 1){
            throw new CustomException(ErrorCode.PAGING_ERROR);
        }
        int offset = (page - 1) * size;

        List<ScheduleResponseDto> schedules = scheduleDao.findAllSchedule(authorName, date, offset, size);
        if(schedules.isEmpty()){
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        return schedules;
    }
}
