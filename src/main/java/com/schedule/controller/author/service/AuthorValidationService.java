package com.schedule.controller.author.service;

import com.schedule.controller.author.dao.AuthorDao;
import com.schedule.controller.author.model.Author;
import com.schedule.controller.common.exception.CustomException;
import com.schedule.controller.common.exception.ErrorCode;
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

    //validate
    public boolean validateAuthor(UUID authorId, String password){
        Author author = authorDao.findAuthorById(authorId);
        if(author == null){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        if(passwordEncoder.matches(password, author.getPassword())){
            return true;
        }else{
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }
    }
}
