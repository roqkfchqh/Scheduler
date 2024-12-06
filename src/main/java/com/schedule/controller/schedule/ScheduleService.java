package com.schedule.controller.schedule;

import com.schedule.controller.common.exception.CustomException;
import com.schedule.controller.common.exception.ErrorCode;
import com.schedule.controller.schedule.dto.ScheduleMapper;
import com.schedule.controller.schedule.dto.ScheduleRequestDto;
import com.schedule.controller.schedule.dto.ScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleDao scheduleDao;
    private final RestTemplate restTemplate;

    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto){
        Schedule schedule = ScheduleMapper.toEntity(dto, UUID.randomUUID());
        scheduleDao.saveSchedule(schedule);
        return ScheduleMapper.toDto(schedule);
    }

    public List<ScheduleResponseDto> getPagedSchedules(String authorName, LocalDate date, int page, int size){
        if(page < 1 || size < 1){
            throw new CustomException(ErrorCode.BAD_GATEWAY);
        }

        List<Schedule> schedules = scheduleDao.findAllSchedule(authorName, date);
        if(schedules == null){
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        int start = (page - 1) * size;
        int end = Math.min(start + size, schedules.size());
        if(start >= schedules.size()){
            return List.of();
        }

        return schedules.subList(start, end).stream()
                .map(ScheduleMapper::toDto)
                .collect(Collectors.toList());
    }

    public ScheduleResponseDto getSchedule(UUID scheduleId){
        Schedule schedule = scheduleDao.findScheduleById(scheduleId);
        if(schedule == null){
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        return ScheduleMapper.toDto(schedule);
    }

    public ScheduleResponseDto updateSchedule(UUID scheduleId, ScheduleRequestDto dto, String authorPassword){
        Schedule schedule = validateIdAndPassword(scheduleId, authorPassword);
        schedule.updateSchedule(
                dto.getContent()
        );
        scheduleDao.updateSchedule(schedule);
        return ScheduleMapper.toDto(schedule);
    }

    public void deleteSchedule(UUID scheduleId, String authorPassword){
        validateIdAndPassword(scheduleId, authorPassword);
        scheduleDao.deleteSchedule(scheduleId);
    }

    private Schedule validateIdAndPassword(UUID scheduleId, String authorPassword) {
        Schedule schedule = scheduleDao.findScheduleById(scheduleId);
        if(schedule == null){
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        try{
            String url = "http://localhost:8080/authors/validate-password";
            ResponseEntity<Boolean> response = restTemplate.postForEntity(
                    url,
                    Map.of("id", schedule.getAuthor_id(), "password", authorPassword),
                    Boolean.class);

            if(Boolean.FALSE.equals(response.getBody())){
                throw new CustomException(ErrorCode.BAD_REQUEST);
            }
        }catch(Exception e){
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return schedule;
    }
}
