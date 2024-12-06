package com.schedule.controller.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ScheduleRequestDto {

    private UUID author_id;

    @NotNull(message = "할 일을 입력해주세요.")
    @NotBlank(message = "할 일을 입력해주세요.")
    @Size(max = 200, message = "입력은 최대 200자까지 가능합니다.")
    private String content;
}
