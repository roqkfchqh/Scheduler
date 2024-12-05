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
                .ipAddress(author.getIpAddress())
                .build();
    }

    public static Author toEntity(AuthorRequestDto dto, String encodedPassword, String clientIp) {
        return Author.create(
                clientIp,
                dto.getEmail(),
                dto.getName(),
                encodedPassword
                );
    }
}
