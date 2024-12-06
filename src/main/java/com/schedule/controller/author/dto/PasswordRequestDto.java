package com.schedule.controller.author.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PasswordRequestDto {

    @NotNull(message = "비밀번호를 입력해주세요.")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
