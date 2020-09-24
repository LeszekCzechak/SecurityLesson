package com.czechak.leszek.SecurityLesson.service;

import com.czechak.leszek.SecurityLesson.dto.NewUserRequest;
import com.czechak.leszek.SecurityLesson.model.user.UserEntity;
import com.czechak.leszek.SecurityLesson.model.user.UserRole;
import com.czechak.leszek.SecurityLesson.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDetails> response =userRepository.loadUserByUsername(username);
        if(response.isPresent()){
            System.out.println(response.get().getUsername());
            System.out.println(response.get().getPassword());
        }
        return response.orElseThrow(()-> new UsernameNotFoundException(String.format("Username %s not found",username)));
    }

    public Set<UserEntity> getAllUsers() {
        Set<UserEntity> response = userRepository.findAll().stream().collect(Collectors.toSet());
        return response;
    }

    public UserEntity getUserById(Long id) throws AccessDeniedException {

        Optional<UserEntity> response = userRepository.findById(id);
        UserEntity userEntity = response.orElseThrow(() -> new UsernameNotFoundException(String.format("Username with id:%s not found", id)));

        String username;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();

        }

        if(userEntity.getUsername().equals(username)) {
            return userEntity;
        } else {
            throw new AccessDeniedException("Access denied");
        }

    }

    @Transactional
    public void registerUser(NewUserRequest newUserRequest) {
        UserEntity userEntity= new UserEntity();
        userEntity.setUsername(newUserRequest.getUsername());
        userEntity.setPassword(passwordEncoder.encode(newUserRequest.getPassword()));
        userEntity.getRoles().addAll(newUserRequest.getRoles());
        userEntity.setAccountNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setCredentialsNonExpired(true);
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
    }
}
