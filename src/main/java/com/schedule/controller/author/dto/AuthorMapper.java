package com.schedule.controller.author.dto;

import com.schedule.controller.author.Author;

import java.util.UUID;

public class AuthorMapper {

    //entity -> dto
    public static AuthorResponseDto toDto(Author author) {
        return AuthorResponseDto.builder()
                .id(author.getId())
                .name(author.getName())
                .email(author.getEmail())
                .build();
    }

    public static Author toEntity(AuthorRequestDto dto, String encodedPassword) {
        return Author.create(
                dto.getEmail(),
                dto.getName(),
                encodedPassword
                );
    }
}
