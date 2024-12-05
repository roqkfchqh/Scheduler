package com.schedule.controller.schedule;

import com.schedule.controller.common.BadInputException;
import com.schedule.controller.schedule.dto.CombinedRequestDto;
import com.schedule.controller.schedule.dto.PasswordRequestDto;
import com.schedule.controller.schedule.dto.ScheduleMapper;
import com.schedule.controller.schedule.dto.ScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleDao scheduleDao;
    private final PasswordEncoder passwordEncoder;

    public ScheduleResponseDto saveSchedule(CombinedRequestDto dto){
        String encodedPassword = passwordEncoder.encode(dto.getPasswordRequestDto().getPassword());
        Schedule schedule = ScheduleMapper.toEntity(dto.getScheduleRequestDto(), encodedPassword, UUID.randomUUID());
        scheduleDao.save(schedule);
        return ScheduleMapper.toDto(schedule);
    }

    public List<ScheduleResponseDto> getAllSchedules(String name, LocalDate date){
        List<Schedule> schedules = scheduleDao.findAll(name, date);
        if(schedules == null){
            throw new BadInputException("오류요");
        }
        return schedules.stream()
                .map(ScheduleMapper::toDto)
                .collect(Collectors.toList());
    }

    public ScheduleResponseDto getSchedule(UUID id){
        Schedule schedule = scheduleDao.findById(id);
        if(schedule == null){
            throw new BadInputException("오류요");
        }
        return ScheduleMapper.toDto(schedule);
    }

    public ScheduleResponseDto updateSchedule(UUID id, CombinedRequestDto dto){
        Schedule schedule = scheduleDao.findById(id);
        if(schedule == null){
            throw new BadInputException("오류요");
        }

        if (!passwordEncoder.matches(dto.getPasswordRequestDto().getPassword(), schedule.getPassword())) {
            throw new BadInputException("오류요");
        }

        schedule.updateSchedule(
                dto.getScheduleRequestDto().getName(),
                dto.getScheduleRequestDto().getContent()
        );
        scheduleDao.update(schedule);
        return ScheduleMapper.toDto(schedule);
    }

    public void deleteSchedule(UUID id, PasswordRequestDto dto){
        Schedule schedule = scheduleDao.findById(id);
        if(schedule == null){
            throw new BadInputException("오류요");
        }
        if (!passwordEncoder.matches(dto.getPassword(), schedule.getPassword())) {
            throw new BadInputException("오류요");
        }
        scheduleDao.deleteByID(id);
    }
}
