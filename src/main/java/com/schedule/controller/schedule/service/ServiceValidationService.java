package com.schedule.controller.schedule.service;

import com.schedule.common.exception.CustomException;
import com.schedule.common.exception.ErrorCode;
import com.schedule.controller.schedule.dao.ScheduleDao;
import com.schedule.controller.schedule.dto.ScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceValidationService {

    private final RestTemplate restTemplate;
    private final ScheduleDao scheduleDao;

    //authorId와 password 검증
    public ScheduleResponseDto validateIdAndPassword(UUID scheduleId, String authorPassword){
        ScheduleResponseDto existingSchedule = scheduleDao.findScheduleById(scheduleId);
        if(existingSchedule == null){
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        try{
            String url = "http://localhost:8080/authors/validate-password";
            ResponseEntity<Boolean> response = restTemplate.postForEntity(
                    url,
                    Map.of("authorId", existingSchedule.getAuthorId(), "password", authorPassword),
                    Boolean.class);

            if(Boolean.FALSE.equals(response.getBody())){
                throw new CustomException(ErrorCode.WRONG_PASSWORD);
            }
        }catch(Exception e){
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return existingSchedule;
    }
}
