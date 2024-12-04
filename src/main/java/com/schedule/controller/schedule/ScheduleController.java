package com.schedule.controller.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public void createSchedule(@RequestBody ScheduleDto dto, @RequestParam String password) {
        scheduleService.saveSchedule(dto, password);
    }

    @GetMapping
    public List<ScheduleDto> getSchedules(@RequestParam(required = false) String name, @RequestParam(required = false) LocalDate date) {
        return scheduleService.getAllSchedules(name, date);
    }

    @GetMapping("/{id}")
    public ScheduleDto getSchedule(@PathVariable UUID id) {
        return scheduleService.getSchedule(id);
    }

    @PostMapping("/{id}")
    public void updateSchedule(@PathVariable UUID id, @RequestParam String password, @RequestBody ScheduleDto dto) {
        scheduleService.updateSchedule(id, password, dto);
    }

    @PostMapping("/{id}/delete")
    public void deleteSchedule(@PathVariable UUID id, @RequestBody String password) {
        scheduleService.deleteSchedule(id, password);
    }
}
