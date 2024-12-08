package com.schedule.controller.schedule;

import com.schedule.controller.common.exception.CustomException;
import com.schedule.controller.common.exception.CustomSQLException;
import com.schedule.controller.common.exception.ErrorCode;
import com.schedule.controller.schedule.dto.ScheduleRequestDto;
import com.schedule.controller.schedule.dto.ScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final TransactionTemplate transactionTemplate;
    private final ScheduleDao scheduleDao;
    private final RestTemplate restTemplate;

    //save
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto){
        Schedule schedule = new Schedule(dto.getContent(), dto.getAuthor_id());
        return saveScheduleWithTransaction(schedule);
    }

    //paging
    public List<ScheduleResponseDto> getPagedSchedules(String authorName, LocalDate date, int page, int size) throws CustomSQLException{
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

    //scheduleId로 schedule 가져오기
    public ScheduleResponseDto getSchedule(UUID scheduleId) throws CustomSQLException {
        return scheduleDao.findScheduleById(scheduleId);
    }

    //update
    public ScheduleResponseDto updateSchedule(UUID scheduleId, ScheduleRequestDto dto, String authorPassword) throws CustomSQLException {
        ScheduleResponseDto existingSchedule = validateIdAndPassword(scheduleId, authorPassword);

        Schedule updatedSchedule = Schedule.builder()
                .id(scheduleId)
                .content(dto.getContent())
                .created(LocalDateTime.parse(existingSchedule.getCreated()))
                .updated(LocalDateTime.now())
                .build();

        return updateScheduleWithTransaction(updatedSchedule);
    }

    //delete
    public void deleteSchedule(UUID scheduleId, String authorPassword){
        transactionTemplate.execute(status -> {
            try{
                validateIdAndPassword(scheduleId, authorPassword);
                scheduleDao.deleteSchedule(scheduleId);
                return null;
            }catch(Exception e){
                status.setRollbackOnly();
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }
        });
    }

    //authorId와 password 검증
    private ScheduleResponseDto validateIdAndPassword(UUID scheduleId, String authorPassword) throws CustomSQLException {
        ScheduleResponseDto existingSchedule = getSchedule(scheduleId);

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

    //save 과정 transactional
    public ScheduleResponseDto saveScheduleWithTransaction(Schedule schedule){
        return transactionTemplate.execute(status -> {
            try{
                System.out.println("author dㅏㅇ이디 : "+ schedule.getAuthor_id());
                scheduleDao.saveSchedule(schedule);
                System.out.println(schedule.getId());
                return getSchedule(schedule.getId());
            }catch(Exception e){
                status.setRollbackOnly();
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }
        });
    }

    //update 과정 transactional
    public ScheduleResponseDto updateScheduleWithTransaction(Schedule schedule){
        return transactionTemplate.execute(status -> {
            try{
                scheduleDao.updateSchedule(schedule);
                return getSchedule(schedule.getId());
            }catch(Exception e){
                status.setRollbackOnly();
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }
        });
    }
}
