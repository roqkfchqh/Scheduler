package com.schedule.controller.author;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Author {

    private UUID id;
    private String ipAddress;
    private String email;
    private String name;
    private String password;

    public Author(String ipAddress, String email, String name, String password){
        this();
        this.ipAddress = ipAddress;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public Author(){
        this.id = UUID.randomUUID();
    }

    public void updateAuthor(String name, String email, String ipAddress, String password){
        this.name = name;
        this.email = email;
        this.ipAddress = ipAddress;
        this.password = password;
    }
}
