package com.schedule.controller.author.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class AuthorResponseDto {

    private UUID id;
    private String name;
    private String email;
}
