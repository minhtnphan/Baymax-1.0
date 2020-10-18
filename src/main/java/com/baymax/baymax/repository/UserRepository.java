package com.baymax.baymax.repository;

import com.baymax.baymax.entity.Access;
import com.baymax.baymax.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsernameAndPassword(String username, String password);
    List<User> findByUsername(String username);
    List<User> findByAccess(Access access);
}
