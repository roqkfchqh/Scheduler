package com.schedule.controller.schedule;

import com.schedule.controller.common.exception.CustomSQLException;
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

    //save
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> saveSchedule(@Valid @RequestBody ScheduleRequestDto dto) throws CustomSQLException {
        return ResponseEntity.ok(scheduleService.saveSchedule(dto));
    }

    //paging
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getPagedSchedules(@RequestParam(required = false) String authorName, @RequestParam(required = false) LocalDate date, @RequestParam int page, @RequestParam int size) throws CustomSQLException {
        return ResponseEntity.ok(scheduleService.getPagedSchedules(authorName, date, page, size));
    }

    //scheduleId로 schedule 가져오기
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable UUID scheduleId) throws CustomSQLException {
        return ResponseEntity.ok(scheduleService.getSchedule(scheduleId));
    }

    //update
    @PostMapping("/{scheduleId}/update")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable UUID scheduleId, @Valid @RequestBody ScheduleRequestDto dto, @Valid @RequestHeader String authorPassword) throws CustomSQLException {
        return ResponseEntity.ok(scheduleService.updateSchedule(scheduleId, dto, authorPassword));
    }

    //delete
    @PostMapping("/{scheduleId}/delete")
    public ResponseEntity<ScheduleResponseDto> deleteSchedule(@PathVariable UUID scheduleId, @Valid @RequestBody String authorPassword) throws CustomSQLException {
        scheduleService.deleteSchedule(scheduleId, authorPassword);
        return ResponseEntity.noContent().build();
    }
}
