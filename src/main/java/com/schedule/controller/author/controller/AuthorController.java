package com.schedule.controller.author.controller;

import com.schedule.controller.author.service.AuthorService;
import com.schedule.controller.author.dto.AuthorRequestDto;
import com.schedule.controller.author.dto.AuthorResponseDto;
import com.schedule.controller.author.dto.CombinedAuthorRequestDto;
import com.schedule.controller.author.dto.PasswordRequestDto;
import com.schedule.controller.author.service.AuthorValidationService;
import jakarta.validation.Valid;
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
    private final AuthorValidationService authorValidationService;

    //create
    @PostMapping
    public ResponseEntity<AuthorResponseDto> createAuthor(@Valid @RequestBody AuthorRequestDto dto){
        return ResponseEntity.ok(authorService.createAuthor(dto));
    }

    //update
    @PostMapping("/{authorId}/update")
    public ResponseEntity<AuthorResponseDto> updateAuthor(@PathVariable UUID authorId, @Valid @RequestBody CombinedAuthorRequestDto dto){
        return ResponseEntity.ok(authorService.updateAuthor(authorId, dto));
    }

    //delete
    @PostMapping("/{authorId}/delete")
    public ResponseEntity<AuthorResponseDto> deleteAuthor(@PathVariable UUID authorId, @Valid @RequestBody PasswordRequestDto dto){
        authorService.deleteAuthor(authorId, dto);
        return ResponseEntity.noContent().build();
    }

    //validate
    @PostMapping("/validate-password")
    public ResponseEntity<Boolean> validatePasswordForSchedule(@RequestBody Map<Object, String> payload){
        UUID authorId = UUID.fromString((payload.get("authorId")));
        String password = payload.get("password");
        boolean isValid = authorValidationService.validateAuthor(authorId, password);
        return ResponseEntity.ok(isValid);
    }

}
