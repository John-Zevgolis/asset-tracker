package com.assettracker.api.dto;

import java.util.List;

import com.assettracker.api.model.Department;

public record EmployeeResponseDto(
    Long id,
    String fullName,
    String email,
    Department department,
    List<AssetResponseDto> assets
) {}
