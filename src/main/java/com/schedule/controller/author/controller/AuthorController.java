package com.schedule.controller.author.controller;

import com.schedule.controller.author.service.AuthorCRUDService;
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

    private final AuthorCRUDService authorCRUDService;
    private final AuthorValidationService authorValidationService;

    //create
    @PostMapping
    public ResponseEntity<AuthorResponseDto> createAuthor(@Valid @RequestBody AuthorRequestDto dto){
        return ResponseEntity.ok(authorCRUDService.createAuthor(dto));
    }

    //read
    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorResponseDto> readAuthor(@PathVariable UUID authorId){
        return ResponseEntity.ok(authorCRUDService.readAuthor(authorId));
    }

    //update
    @PostMapping("/{authorId}/update")
    public ResponseEntity<AuthorResponseDto> updateAuthor(@PathVariable UUID authorId, @Valid @RequestBody CombinedAuthorRequestDto dto){
        return ResponseEntity.ok(authorCRUDService.updateAuthor(authorId, dto));
    }

    //delete
    @PostMapping("/{authorId}/delete")
    public ResponseEntity<AuthorResponseDto> deleteAuthor(@PathVariable UUID authorId, @Valid @RequestBody PasswordRequestDto dto){
        authorCRUDService.deleteAuthor(authorId, dto);
        return ResponseEntity.noContent().build();
    }

    //validate(password&authorId)
    @PostMapping("/validate-password")
    public ResponseEntity<Boolean> validatePasswordForSchedule(@RequestBody Map<Object, String> payload){
        UUID authorId = UUID.fromString((payload.get("authorId")));
        String password = payload.get("password");
        boolean isValid = authorValidationService.validatePassword(authorId, password);
        return ResponseEntity.ok(isValid);
    }

    //validate(authorId)
    @PostMapping("/validate-author")
    public ResponseEntity<Boolean> validateAuthorForSchedule(@RequestBody UUID authorId){
        return ResponseEntity.ok(authorValidationService.validateAuthor(authorId));
    }

}
