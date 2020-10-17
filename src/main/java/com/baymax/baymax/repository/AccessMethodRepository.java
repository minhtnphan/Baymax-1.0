package com.baymax.baymax.repository;

import com.baymax.baymax.entity.AccessMethod;
import com.baymax.baymax.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessMethodRepository extends JpaRepository<AccessMethod, Long> {
    List<AccessMethod> findByMethod(String method);
}
