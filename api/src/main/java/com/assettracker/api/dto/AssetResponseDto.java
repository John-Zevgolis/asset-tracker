package com.assettracker.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.assettracker.api.model.AssetCategory;

public record AssetResponseDto(
    Long id,
    String name,
    AssetCategory category,
    String serialNumber,
    BigDecimal value,
    LocalDate purchaseDate,
    Long employeeId,
    String employeeName
) {}
