package com.schedule.controller.author.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthorRequestDto {

    @NotNull(message = "이름을 입력해주세요.")
    @Size(min = 2, max = 15, message = "이름은 2자에서 15자 사이여야 합니다.")
    private String name;

    @NotNull(message = "이메일을 입력해주세요.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @NotNull(message = "비밀번호를 입력해주세요.")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
