package com.schedule.controller.author;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping("/valid-password")
    public ResponseEntity<Boolean> validPassword(@RequestBody Map<String, Object> payload) {
        UUID authorId = UUID.fromString(payload.get("uuid").toString());
        String password = payload.get("password").toString();
        boolean isValid = authorService.validPassword(authorId, password);
        return ResponseEntity.ok(isValid);
    }

}
