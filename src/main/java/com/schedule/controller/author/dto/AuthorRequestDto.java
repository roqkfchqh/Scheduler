package com.schedule.controller.author.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthorRequestDto {

    private String name;
    private String email;
    private String password;
}
