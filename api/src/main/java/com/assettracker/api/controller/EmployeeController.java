package com.assettracker.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assettracker.api.dto.EmployeeRequestDto;
import com.assettracker.api.dto.EmployeeResponseDto;
import com.assettracker.api.model.Department;
import com.assettracker.api.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDto>> getUserEmployees(Authentication authentication) {
        String userEmail = authentication.getName();
        List<EmployeeResponseDto> employees = employeeService.getUserEmployees(userEmail);
        return ResponseEntity.ok(employees);
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(@Valid @RequestBody EmployeeRequestDto employeeRequestDto, Authentication authentication) {
        String userEmail = authentication.getName();
        EmployeeResponseDto createdEmployee = employeeService.createEmployee(employeeRequestDto, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequestDto employeeRequestDto, Authentication authentication) {
        String userEmail = authentication.getName();
        EmployeeResponseDto updatedEmployee = employeeService.updateEmployee(id, employeeRequestDto, userEmail);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();
        employeeService.deleteEmployee(id, userEmail);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/departments")
    public ResponseEntity<Department[]> getDepartments() {
        return ResponseEntity.ok(Department.values());
    }
}
