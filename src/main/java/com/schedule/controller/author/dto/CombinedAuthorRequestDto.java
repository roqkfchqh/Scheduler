package com.schedule.controller.author.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CombinedAuthorRequestDto {

    private AuthorRequestDto authorDto;
    private PasswordRequestDto passwordDto;
}
