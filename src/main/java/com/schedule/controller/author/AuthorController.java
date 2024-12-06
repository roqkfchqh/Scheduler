package com.schedule.controller.author;

import com.schedule.controller.author.dto.AuthorRequestDto;
import com.schedule.controller.author.dto.AuthorResponseDto;
import com.schedule.controller.author.dto.CombinedAuthorRequestDto;
import com.schedule.controller.author.dto.PasswordRequestDto;
import com.schedule.controller.common.exception.CustomSQLException;
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

    @PostMapping
    public ResponseEntity<AuthorResponseDto> createAuthor(@Valid @RequestBody AuthorRequestDto dto) throws CustomSQLException {
        return ResponseEntity.ok(authorService.createAuthor(dto));
    }

    @PostMapping("/{authorId}/update")
    public ResponseEntity<AuthorResponseDto> updateAuthor(@PathVariable UUID authorId, @Valid @RequestBody CombinedAuthorRequestDto dto) throws CustomSQLException {
        return ResponseEntity.ok(authorService.updateAuthor(authorId, dto));
    }

    @PostMapping("/{authorId}/delete")
    public ResponseEntity<AuthorResponseDto> deleteAuthor(@PathVariable UUID authorId, @Valid @RequestBody PasswordRequestDto dto) throws CustomSQLException {
        authorService.deleteAuthor(authorId, dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/validate-password")
    public ResponseEntity<Boolean> validatePasswordForSchedule(@RequestBody Map<Object, String> payload) throws CustomSQLException {
        UUID authorId = UUID.fromString((payload.get("authorId")));
        String password = payload.get("password");
        boolean isValid = authorService.validateAuthor(authorId, password);
        return ResponseEntity.ok(isValid);
    }

}
