package com.czechak.leszek.SecurityLesson.dto;

import com.czechak.leszek.SecurityLesson.model.user.UserRole;
import lombok.Data;

import java.util.Set;

@Data
public class NewUserRequest {

    private final String username;
    private final String password;
    private final Set<UserRole> roles;

}
