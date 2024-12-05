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
    private String ip_address;
    private String email;
    private String name;
    private String password;
}
