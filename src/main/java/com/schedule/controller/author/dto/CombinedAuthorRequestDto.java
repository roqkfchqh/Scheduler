package com.schedule.controller.author.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CombinedAuthorRequestDto {

    private AuthorRequestDto authorDto;
    private PasswordRequestDto passwordDto;
}
