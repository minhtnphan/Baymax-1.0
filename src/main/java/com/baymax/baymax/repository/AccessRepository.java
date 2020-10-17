package com.baymax.baymax.repository;

import com.baymax.baymax.entity.Access;
import com.baymax.baymax.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessRepository extends JpaRepository<Access, Long> {
    List<Access> findByUpdate(String access);
    List<Access> findByUsers(User user);
}
