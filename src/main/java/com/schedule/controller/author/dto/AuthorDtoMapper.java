package com.schedule.controller.author.dto;

import com.schedule.controller.author.model.Author;

public class AuthorDtoMapper {

    //entity -> dto
    public static AuthorResponseDto toDto(Author author) {
        return AuthorResponseDto.builder()
                .id(author.getId())
                .name(author.getName())
                .email(author.getEmail())
                .build();
    }

    //dto -> entity
    public static Author toEntity(AuthorRequestDto dto, String encodedPassword) {
        return Author.create(
                dto.getEmail(),
                dto.getName(),
                encodedPassword
                );
    }
}
