package com.assettracker.api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.assettracker.api.dto.AssetResponseDto;
import com.assettracker.api.dto.EmployeeRequestDto;
import com.assettracker.api.dto.EmployeeResponseDto;
import com.assettracker.api.model.Employee;
import com.assettracker.api.model.User;
import com.assettracker.api.repository.EmployeeRepository;
import com.assettracker.api.repository.UserRepository;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public EmployeeService(EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    public List<EmployeeResponseDto> getUserEmployees(String userEmail) {
        User user = getUserByEmail(userEmail);

        return employeeRepository.findByUserId(user.getId()).stream().map(employee -> mapToResponseDto(employee)).toList();
    }

    public EmployeeResponseDto createEmployee(EmployeeRequestDto employeeRequestDto, String userEmail) {
        User user = getUserByEmail(userEmail);

        if(employeeRepository.existsByEmailAndUserId(employeeRequestDto.email(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An employee with this email already exists");
        }

        Employee employee = new Employee(employeeRequestDto.fullName(), employeeRequestDto.email(), employeeRequestDto.department(), user);
        
        Employee createdEmployee = employeeRepository.save(employee);
        return mapToResponseDto(createdEmployee);
    }

    public EmployeeResponseDto updateEmployee(Long employeeId, EmployeeRequestDto employeeRequestDto, String userEmail) {
        User user = getUserByEmail(userEmail);

        Employee employee = employeeRepository.findByIdAndUserId(employeeId, user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found or access denied"));

        if(!employee.getEmail().equals(employeeRequestDto.email()) && employeeRepository.existsByEmailAndUserId(employeeRequestDto.email(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An employee with this email already exists");
        }
        
        employee.setFullName(employeeRequestDto.fullName());
        employee.setEmail(employeeRequestDto.email());
        employee.setDepartment(employeeRequestDto.department());

        Employee updatedEmployee = employeeRepository.save((employee));
        return mapToResponseDto(updatedEmployee);
    }

    public void deleteEmployee(Long employeeId, String userEmail) {
        User user = getUserByEmail(userEmail);

        Employee employee = employeeRepository.findByIdAndUserId(employeeId, user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found or access denied"));
        employeeRepository.delete(employee);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private EmployeeResponseDto mapToResponseDto(Employee employee) {
        List<AssetResponseDto> assets = employee.getAssets() == null ? List.of() : 
            employee.getAssets().stream()
                                .map(asset -> new AssetResponseDto(
                                    asset.getId(),
                                    asset.getName(),
                                    asset.getCategory(),
                                    asset.getSerialNumber(),
                                    asset.getValue(),
                                    asset.getPurchaseDate(),
                                    employee.getId(),
                                    employee.getFullName()
                                )).toList();

        return new EmployeeResponseDto(
            employee.getId(),
            employee.getFullName(),
            employee.getEmail(),
            employee.getDepartment(),
            assets
        );
    }
}
