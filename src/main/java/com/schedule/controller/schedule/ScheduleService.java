package com.schedule.controller.schedule;

import com.schedule.controller.common.BadInputException;
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

    public void saveSchedule(ScheduleDto dto, String password){
        String encodedPassword = passwordEncoder.encode(password);
        Schedule schedule = ScheduleMapper.toEntity(dto, encodedPassword, UUID.randomUUID());
        scheduleDao.save(schedule);
    }

    public List<ScheduleDto> getAllSchedules(String name, LocalDate date){
        List<Schedule> schedules = scheduleDao.findAll(name, date);
        if(schedules == null){
            throw new BadInputException("오류요");
        }
        return schedules.stream()
                .map(ScheduleMapper::toDto)
                .collect(Collectors.toList());
    }

    public ScheduleDto getSchedule(UUID id){
        Schedule schedule = scheduleDao.findById(id);
        if(schedule == null){
            throw new BadInputException("오류요");
        }
        return ScheduleMapper.toDto(schedule);
    }
}
