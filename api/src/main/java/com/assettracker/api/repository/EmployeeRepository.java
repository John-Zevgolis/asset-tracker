package com.assettracker.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assettracker.api.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByUserId(Long userId);
    Optional<Employee> findByIdAndUserId(Long id, Long userId);
    boolean existsByEmailAndUserId(String email, Long userId);
}
