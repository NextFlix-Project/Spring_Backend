package com.nextflix.app.repositories.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nextflix.app.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u LEFT JOIN u.subscription s ORDER BY u.id")
    List<User> findAllUsers();

    Optional<User> findById(Long userId);

    User findByEmail(String email);
}