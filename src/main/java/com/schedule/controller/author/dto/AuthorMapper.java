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
                .ip_address(author.getIp_address())
                .build();
    }

    public static Author toEntity(AuthorRequestDto dto, UUID id) {
        return Author.builder()
                .id(id)
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .ip_address(dto.getIpAddress())
                .build();
    }
}
