package com.schedule.controller.schedule;

import com.schedule.controller.schedule.dto.CombinedRequestDto;
import com.schedule.controller.schedule.dto.PasswordRequestDto;
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
    public ResponseEntity<ScheduleResponseDto> saveSchedule(@RequestBody CombinedRequestDto dto) {
        return ResponseEntity.ok(scheduleService.saveSchedule(dto));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getSchedules(@RequestParam(required = false) String name, @RequestParam(required = false) LocalDate date) {
        return ResponseEntity.ok(scheduleService.getAllSchedules(name, date));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable UUID id) {
        return ResponseEntity.ok(scheduleService.getSchedule(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable UUID id, @RequestBody CombinedRequestDto dto) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, dto));
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<ScheduleResponseDto> deleteSchedule(@PathVariable UUID id, @RequestBody PasswordRequestDto dto) {
        scheduleService.deleteSchedule(id, dto);
        return ResponseEntity.noContent().build();
    }
}
