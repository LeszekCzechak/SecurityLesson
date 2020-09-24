package com.czechak.leszek.SecurityLesson.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UsernameAndPasswordAuthRequest {

    private String username;
    private String password;

}
