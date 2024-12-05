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
        scheduleDao.save(schedule);
        return ScheduleMapper.toDto(schedule);
    }

    public List<ScheduleResponseDto> getAllSchedules(String name, LocalDate date){
        List<Schedule> schedules = scheduleDao.findAll(name, date);
        if(schedules == null){
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        return schedules.stream()
                .map(ScheduleMapper::toDto)
                .collect(Collectors.toList());
    }

    public ScheduleResponseDto getSchedule(UUID id){
        Schedule schedule = scheduleDao.findById(id);
        if(schedule == null){
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        return ScheduleMapper.toDto(schedule);
    }

    public ScheduleResponseDto updateSchedule(UUID id, ScheduleRequestDto dto, String password){
        Schedule schedule = validIdAndPassword(id, password);
        schedule.updateSchedule(
                dto.getContent()
        );
        scheduleDao.update(schedule);
        return ScheduleMapper.toDto(schedule);
    }

    public void deleteSchedule(UUID id, String password){
        validIdAndPassword(id, password);
        scheduleDao.deleteByID(id);
    }

    private Schedule validIdAndPassword(UUID id, String password) {
        Schedule schedule = scheduleDao.findById(id);
        if(schedule == null){
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        try{
            String url = "http://localhost:8080/authors/valid-password";
            ResponseEntity<Boolean> response = restTemplate.postForEntity(
                    url,
                    Map.of("id", schedule.getAuthor_id(), "password", password),
                    Boolean.class);

            if(!Boolean.TRUE.equals(response.getBody())){
                throw new CustomException(ErrorCode.BAD_REQUEST);
            }
        }catch(Exception e){
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return schedule;
    }
}
