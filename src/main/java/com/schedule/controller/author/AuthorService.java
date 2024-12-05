package com.schedule.controller.author;

import com.schedule.controller.author.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorDao authorDao;
    private final PasswordEncoder passwordEncoder;

    public AuthorResponseDto createAuthor(AuthorRequestDto dto) {
        Author author = AuthorMapper.toEntity(dto, UUID.randomUUID());
        authorDao.createAuthor(author);
        return AuthorMapper.toDto(author);
    }

    public AuthorResponseDto updateAuthor(UUID authorId, CombinedAuthorRequestDto dto) {
        Author author = AuthorMapper.toEntity(dto.getAuthorDto(), authorId);
        authorDao.updateAuthor(author);
        return AuthorMapper.toDto(author);
    }

    public void deleteAuthor(UUID authorId, PasswordRequestDto dto) {

        authorDao.deleteAuthor(authorId);
    }



    public boolean validatePassword(UUID authorId, String password) {
        String authorPassword = authorDao.findPassword(authorId);
        return passwordEncoder.matches(password, authorPassword);
    }


}
