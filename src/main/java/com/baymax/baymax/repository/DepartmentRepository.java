package com.baymax.baymax.repository;
import com.baymax.baymax.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findBySymptomAndRanking(String symptom, Long ranking);
    List<Department> findBySymptomAndLocation(String symptom, String location);
}
