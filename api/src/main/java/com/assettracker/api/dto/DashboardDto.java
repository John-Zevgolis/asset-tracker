package com.assettracker.api.dto;

import java.math.BigDecimal;

public record DashboardDto(
    long totalAssets,
    BigDecimal totalAssetsValue,
    long assignedAssetsCount,
    long unassignedAssetsCount,
    long totalEmployees
) {}
