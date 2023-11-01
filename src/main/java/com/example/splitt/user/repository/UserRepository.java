package com.example.splitt.user.repository;

import com.example.splitt.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndEmail(Long id, String email);

    @Query("select u from User u " +
            "where u.email in :emails")
    List<User> findByEmails(@Param("emails") List<String> emails);

}
