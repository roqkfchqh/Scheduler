package com.schedule.controller.author;

import com.schedule.controller.author.dto.AuthorRequestDto;
import com.schedule.controller.author.dto.AuthorResponseDto;
import com.schedule.controller.author.dto.CombinedAuthorRequestDto;
import com.schedule.controller.author.dto.PasswordRequestDto;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<AuthorResponseDto> createAuthor(HttpServletRequest request, @RequestBody AuthorRequestDto dto) {
        String clientIp = getClientIp(request);
        dto.setIpAddress(clientIp);
        return ResponseEntity.ok(authorService.createAuthor(dto));
    }

    @PostMapping("/{authorId}/update")
    public ResponseEntity<AuthorResponseDto> updateAuthor(@PathVariable UUID authorId, @RequestBody CombinedAuthorRequestDto dto) {
        return ResponseEntity.ok(authorService.updateAuthor(authorId, dto));
    }

    @PostMapping("/{authorId}/delete")
    public ResponseEntity<AuthorResponseDto> deleteAuthor(@PathVariable UUID authorId, @RequestBody PasswordRequestDto dto) {
        authorService.deleteAuthor(authorId, dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/validate-password")
    public ResponseEntity<Boolean> validatePassword(@RequestBody Map<String, Object> payload) {
        UUID authorId = UUID.fromString(payload.get("uuid").toString());
        String password = payload.get("password").toString();
        boolean isValid = authorService.validatePassword(authorId, password);
        return ResponseEntity.ok(isValid);
    }

    public String getClientIp(HttpServletRequest request){
        String ipAddress = request.getHeader("X-Forwarded-For");
        if(ipAddress == null || ipAddress.isEmpty()){
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

}
