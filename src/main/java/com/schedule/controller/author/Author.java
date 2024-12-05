package com.schedule.controller.author;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Author {

    private UUID id;
    private String ipAddress;
    private String email;
    private String name;
    private String password;

    public static Author create(String ipAddress, String email, String name, String password) {
        return Author.builder()
                .id(UUID.randomUUID())
                .ipAddress(ipAddress)
                .email(email)
                .name(name)
                .password(password)
                .build();
    }

    public void updateAuthor(String name, String email, String ipAddress, String password){
        this.name = name;
        this.email = email;
        this.ipAddress = ipAddress;
        this.password = password;
    }
}
