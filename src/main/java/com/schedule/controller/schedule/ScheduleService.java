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

    public ScheduleDto updateSchedule(UUID id, String password, ScheduleDto dto){
        Schedule schedule = scheduleDao.findById(id);
        if(schedule == null){
            throw new BadInputException("오류요");
        }

        if (!passwordEncoder.matches(password, schedule.getPassword())) {
            throw new BadInputException("오류요");
        }

        schedule.setName(dto.getName());
        schedule.setContent(dto.getContent());
        schedule.updateTimestamp();
        scheduleDao.update(schedule);

        return ScheduleMapper.toDto(schedule);
    }

    public void deleteSchedule(UUID id, String password){
        Schedule schedule = scheduleDao.findById(id);
        if(schedule == null){
            throw new BadInputException("오류요");
        }
        if (!passwordEncoder.matches(password, schedule.getPassword())) {
            throw new BadInputException("오류요");
        }
        scheduleDao.deleteByID(id);
    }
}
