package com.schedule.controller.schedule;

import com.schedule.controller.schedule.dto.ScheduleRequestDto;
import com.schedule.controller.schedule.dto.ScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ScheduleResponseDto> saveSchedule(@RequestBody ScheduleRequestDto dto) {
        return ResponseEntity.ok(scheduleService.saveSchedule(dto));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getSchedules(@RequestParam(required = false) String authorName, @RequestParam(required = false) LocalDate date) {
        return ResponseEntity.ok(scheduleService.getAllSchedules(authorName, date));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable UUID scheduleId) {
        return ResponseEntity.ok(scheduleService.getSchedule(scheduleId));
    }

    @PostMapping("/{scheduleId}/update")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable UUID scheduleId, @RequestBody ScheduleRequestDto dto, @RequestHeader String authorPassword) {
        return ResponseEntity.ok(scheduleService.updateSchedule(scheduleId, dto, authorPassword));
    }

    @PostMapping("/{scheduleId}/delete")
    public ResponseEntity<ScheduleResponseDto> deleteSchedule(@PathVariable UUID scheduleId, @RequestBody String authorPassword) {
        scheduleService.deleteSchedule(scheduleId, authorPassword);
        return ResponseEntity.noContent().build();
    }
}
