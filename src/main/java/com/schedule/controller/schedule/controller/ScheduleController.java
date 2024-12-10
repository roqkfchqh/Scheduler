package com.schedule.controller.schedule.controller;

import com.schedule.controller.schedule.service.ScheduleCRUDService;
import com.schedule.controller.schedule.service.SchedulePagingService;
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

    private final ScheduleCRUDService scheduleCRUDService;
    private final SchedulePagingService schedulePagingService;

    //save
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> saveSchedule(@Valid @RequestBody ScheduleRequestDto dto){
        return ResponseEntity.ok(scheduleCRUDService.createSchedule(dto));
    }

    //paging
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getPagedSchedules(@RequestParam(required = false) String authorName, @RequestParam(required = false) LocalDate date, @RequestParam int page, @RequestParam int size){
        return ResponseEntity.ok(schedulePagingService.getPagedSchedules(authorName, date, page, size));
    }

    //scheduleId로 schedule 가져오기
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable UUID scheduleId){
        return ResponseEntity.ok(scheduleCRUDService.readSchedule(scheduleId));
    }

    //update
    @PostMapping("/{scheduleId}/update")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable UUID scheduleId, @Valid @RequestBody ScheduleRequestDto dto, @RequestHeader String authorPassword){
        return ResponseEntity.ok(scheduleCRUDService.updateSchedule(scheduleId, dto, authorPassword));
    }

    //delete
    @PostMapping("/{scheduleId}/delete")
    public ResponseEntity<ScheduleResponseDto> deleteSchedule(@PathVariable UUID scheduleId, @RequestBody String authorPassword, @RequestHeader UUID authorId){
        scheduleCRUDService.deleteSchedule(scheduleId, authorPassword, authorId);
        return ResponseEntity.noContent().build();

    }
}
