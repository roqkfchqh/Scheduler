package com.schedule.controller.schedule;

import com.schedule.controller.schedule.dto.ScheduleRequestDto;
import com.schedule.controller.schedule.dto.ScheduleResponseDto;
import jakarta.validation.Valid;
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
    public ResponseEntity<ScheduleResponseDto> saveSchedule(@Valid @RequestBody ScheduleRequestDto dto) {
        return ResponseEntity.ok(scheduleService.saveSchedule(dto));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getPagedSchedules(@RequestParam(required = false) String authorName, @RequestParam(required = false) LocalDate date, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(scheduleService.getPagedSchedules(authorName, date, page, size));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable UUID scheduleId) {
        return ResponseEntity.ok(scheduleService.getSchedule(scheduleId));
    }

    @PostMapping("/{scheduleId}/update")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable UUID scheduleId, @Valid @RequestBody ScheduleRequestDto dto, @Valid @RequestHeader String authorPassword) {
        return ResponseEntity.ok(scheduleService.updateSchedule(scheduleId, dto, authorPassword));
    }

    @PostMapping("/{scheduleId}/delete")
    public ResponseEntity<ScheduleResponseDto> deleteSchedule(@PathVariable UUID scheduleId, @Valid @RequestBody String authorPassword) {
        scheduleService.deleteSchedule(scheduleId, authorPassword);
        return ResponseEntity.noContent().build();
    }
}
