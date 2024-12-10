package com.schedule.controller.author.service;

import com.schedule.controller.author.dao.AuthorDao;
import com.schedule.controller.author.dto.AuthorResponseDto;
import com.schedule.controller.author.model.Author;
import com.schedule.common.exception.CustomException;
import com.schedule.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorValidationService {

    private final AuthorDao authorDao;
    private final PasswordEncoder passwordEncoder;

    //password & authorId validate
    public boolean validatePassword(UUID authorId, String password){
        AuthorResponseDto author = authorDao.findAuthorById(authorId);
        if(author == null){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return passwordEncoder.matches(password, author.getPassword());
    }

    //authorId validate
    public boolean validateAuthor(UUID authorId){
        AuthorResponseDto author = authorDao.findAuthorById(authorId);
        if(author == null){
            throw new CustomException(ErrorCode.FORBIDDEN_OPERATION);
        }
        return true;
    }
}
