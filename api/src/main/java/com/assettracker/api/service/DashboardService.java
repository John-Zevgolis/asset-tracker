package com.assettracker.api.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.assettracker.api.dto.DashboardDto;
import com.assettracker.api.model.Asset;
import com.assettracker.api.model.User;
import com.assettracker.api.repository.AssetRepository;
import com.assettracker.api.repository.EmployeeRepository;
import com.assettracker.api.repository.UserRepository;

@Service
public class DashboardService {
    private final AssetRepository assetRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public DashboardService(AssetRepository assetRepository, EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.assetRepository = assetRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    public DashboardDto getDashboardStats(String userEmail) {
        User user = getUserByEmail(userEmail);

        List<Asset> userAssets = assetRepository.findByUserId(user.getId());
        long totalEmployees = employeeRepository.findByUserId(user.getId()).size();

        long totalAssets = userAssets.size();

        BigDecimal totalValue = userAssets.stream().map(asset -> asset.getValue()).reduce(BigDecimal.ZERO, BigDecimal::add);

        long assignedCount = userAssets.stream().filter(asset -> asset.getEmployee() != null).count();

        long unassignedCount = totalAssets - assignedCount;

        return new DashboardDto(
            totalAssets,
            totalValue,
            assignedCount,
            unassignedCount,
            totalEmployees
        );
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
