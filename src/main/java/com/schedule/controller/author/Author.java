package com.schedule.controller.author;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Author {

    private UUID id;
    private String email;
    private String name;
    private String password;

    public static Author create(String email, String name, String password) {
        return Author.builder()
                .id(UUID.randomUUID())
                .email(email)
                .name(name)
                .password(password)
                .build();
    }

    public void updateAuthor(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
