package com.schedule.controller.author;

import com.schedule.controller.author.dto.*;
import com.schedule.controller.common.exception.CustomException;
import com.schedule.controller.common.exception.CustomSQLException;
import com.schedule.controller.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorDao authorDao;
    private final PasswordEncoder passwordEncoder;

    //create
    public AuthorResponseDto createAuthor(AuthorRequestDto dto) throws CustomSQLException {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Author author = AuthorMapper.toEntity(dto, encodedPassword);

        authorDao.createAuthor(author);
        return AuthorMapper.toDto(author);
    }

    //update
    public AuthorResponseDto updateAuthor(UUID authorId, CombinedAuthorRequestDto dto) throws CustomSQLException {
        Author author = authorDao.findAuthorById(authorId);
        validateAuthor(authorId, dto.getPasswordDto().getPassword());
        String encodedPassword = passwordEncoder.encode(dto.getAuthorDto().getPassword());

        author.updateAuthor(
                dto.getAuthorDto().getName(),
                dto.getAuthorDto().getEmail(),
                encodedPassword
        );
        authorDao.updateAuthor(author);
        return AuthorMapper.toDto(author);
    }

    //delete
    public void deleteAuthor(UUID authorId, PasswordRequestDto dto) throws CustomSQLException {
        validateAuthor(authorId, dto.getPassword());
        authorDao.deleteAuthor(authorId);
    }

    //validate
    public boolean validateAuthor(UUID authorId, String password) throws CustomSQLException {
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
