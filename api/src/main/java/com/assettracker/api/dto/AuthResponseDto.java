package com.assettracker.api.dto;

public record AuthResponseDto(
    String token,
    String fullName,
    String email
) {}
