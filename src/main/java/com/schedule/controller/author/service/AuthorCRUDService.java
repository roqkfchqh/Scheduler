package com.schedule.controller.author.service;

import com.schedule.controller.author.dao.AuthorDao;
import com.schedule.controller.author.dto.*;
import com.schedule.controller.author.model.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorCRUDService {

    private final AuthorValidationService authorValidationService;
    private final AuthorDao authorDao;
    private final PasswordEncoder passwordEncoder;

    //create
    public AuthorResponseDto createAuthor(AuthorRequestDto dto){
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Author author = AuthorDtoMapper.toEntity(dto, encodedPassword);

        authorDao.createAuthor(author);
        return AuthorDtoMapper.toDto(author);
    }

    //read
    @Transactional(readOnly = true)
    public AuthorResponseDto readAuthor(UUID authorId){
        Author author = authorDao.findAuthorById(authorId);
        return AuthorDtoMapper.toDto(author);
    }

    //update
    public AuthorResponseDto updateAuthor(UUID authorId, CombinedAuthorRequestDto dto){
        Author author = authorDao.findAuthorById(authorId);
        authorValidationService.validateAuthor(authorId, dto.getPasswordDto().getPassword());
        String encodedPassword = passwordEncoder.encode(dto.getAuthorDto().getPassword());

        author.updateAuthor(
                dto.getAuthorDto().getName(),
                dto.getAuthorDto().getEmail(),
                encodedPassword
        );
        authorDao.updateAuthor(author);
        return AuthorDtoMapper.toDto(author);
    }

    //delete
    public void deleteAuthor(UUID authorId, PasswordRequestDto dto){
        authorValidationService.validateAuthor(authorId, dto.getPassword());
        authorDao.deleteAuthor(authorId);
    }
}
