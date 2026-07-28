package com.assettracker.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.assettracker.api.model.AssetCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AssetRequestDto(
    @NotBlank(message = "Name is required")
    String name,

    @NotNull(message = "Category is required")
    AssetCategory category,

    @NotBlank(message = "Serial number is required")
    String serialNumber,

    @NotNull(message = "Value is required")
    @Positive(message = "Value must be greater than zero")
    BigDecimal value,

    @NotNull(message = "Purchase date is required")
    LocalDate purchaseDate
) {}
