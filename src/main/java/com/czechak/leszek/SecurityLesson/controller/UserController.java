package com.czechak.leszek.SecurityLesson.controller;

import com.czechak.leszek.SecurityLesson.dto.AuthenticationRequest;
import com.czechak.leszek.SecurityLesson.dto.AuthenticationResponse;
import com.czechak.leszek.SecurityLesson.dto.NewUserRequest;
import com.czechak.leszek.SecurityLesson.model.user.UserEntity;
import com.czechak.leszek.SecurityLesson.service.UserService;
import com.czechak.leszek.SecurityLesson.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{

     try {
         Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
     } catch (BadCredentialsException e){
         throw new Exception("Incorrect username or password",e);
     }

        UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Set<UserEntity>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable(value = "id") Long id) throws AccessDeniedException {
        UserEntity userEntity = userService.getUserById(id);
        return ResponseEntity.ok(userEntity);
    }

    @PutMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody NewUserRequest newUserRequest){
        userService.registerUser(newUserRequest);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

}
