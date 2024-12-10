package com.schedule.controller.author.service;

import com.schedule.controller.author.dao.AuthorDao;
import com.schedule.controller.author.dto.*;
import com.schedule.controller.author.model.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        Author author = new Author(dto.getName(), dto.getEmail(), encodedPassword);
        authorDao.createAuthor(author);
        return authorDao.findAuthorById(author.getId());
    }

    //read
    @Transactional(readOnly = true)
    public AuthorResponseDto readAuthor(UUID authorId){
        return authorDao.findAuthorById(authorId);
    }

    //update
    public AuthorResponseDto updateAuthor(UUID authorId, CombinedAuthorRequestDto dto){
        AuthorResponseDto existingAuthor = authorDao.findAuthorById(authorId);

        authorValidationService.validatePassword(authorId, dto.getPasswordDto().getPassword());
        String encodedPassword = passwordEncoder.encode(dto.getAuthorDto().getPassword());

        Author updatedAuthor = Author.builder()
                .id(authorId)
                .password(encodedPassword)
                .name(dto.getAuthorDto().getName())
                .email(dto.getAuthorDto().getEmail())
                .created(LocalDateTime.parse(existingAuthor.getCreated()))
                .updated(LocalDateTime.now())
                .build();

        authorDao.updateAuthor(updatedAuthor);
        return authorDao.findAuthorById(authorId);
    }

    //delete
    public void deleteAuthor(UUID authorId, PasswordRequestDto dto){
        authorValidationService.validatePassword(authorId, dto.getPassword());
        authorDao.deleteAuthor(authorId);
    }
}
