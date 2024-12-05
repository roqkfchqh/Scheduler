package com.schedule.controller.author;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorDao authorDao;
    private final PasswordEncoder passwordEncoder;

    public boolean validPassword(UUID authorId, String password) {
        String authorPassword = authorDao.findPassword(authorId);
        return passwordEncoder.matches(password, authorPassword);
    }


}
