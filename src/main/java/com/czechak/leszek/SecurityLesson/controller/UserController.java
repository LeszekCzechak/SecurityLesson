package com.czechak.leszek.SecurityLesson.controller;

import com.czechak.leszek.SecurityLesson.dto.NewUserRequest;
import com.czechak.leszek.SecurityLesson.model.user.UserEntity;
import com.czechak.leszek.SecurityLesson.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Set<UserEntity>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable(value = "id") Long id){
        UserEntity userEntity = userService.getUserById(id);
        return ResponseEntity.ok(userEntity);
    }

    @PutMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody NewUserRequest newUserRequest){
        userService.registerUser(newUserRequest);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

}
