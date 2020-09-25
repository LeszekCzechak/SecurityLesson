package com.czechak.leszek.SecurityLesson.repository;

import com.czechak.leszek.SecurityLesson.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

@Query(value = "SELECT u FROM UserEntity u WHERE u.username = :username")
    Optional<UserDetails> loadUserByUsername(@Param("username") String username);
}
