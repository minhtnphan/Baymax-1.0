package com.example.baymax.repository;

import com.example.baymax.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsernameAndPassword(@Param("email") String username, @Param("pass") String password);
    List<User> findByUsername(String username);
}
