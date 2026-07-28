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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assettracker.api.dto.AssetRequestDto;
import com.assettracker.api.dto.AssetResponseDto;
import com.assettracker.api.model.AssetCategory;
import com.assettracker.api.service.AssetService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/assets")
public class AssetController {
    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping
    public ResponseEntity<List<AssetResponseDto>> getUserAssets(Authentication authentication) {
        String userEmail = authentication.getName();
        List<AssetResponseDto> assets = assetService.getUserAssets(userEmail);
        return ResponseEntity.ok(assets);
    }

    @PostMapping
    public ResponseEntity<AssetResponseDto> createAsset(@Valid @RequestBody AssetRequestDto assetRequestDto, Authentication authentication) {
        String userEmail = authentication.getName();
        AssetResponseDto createdAsset = assetService.createAsset(assetRequestDto, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAsset);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetResponseDto> updateAsset(@PathVariable Long id, @Valid @RequestBody AssetRequestDto assetRequestDto, Authentication authentication) {
        String userEmail = authentication.getName();
        AssetResponseDto updatedAsset = assetService.updateAsset(id, assetRequestDto, userEmail);
        return ResponseEntity.ok(updatedAsset);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();
        assetService.deleteAsset(id, userEmail);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<AssetResponseDto> assignAsset(@PathVariable Long id, @RequestParam(required = false) Long employeeId, Authentication authentication) {
        String userEmail = authentication.getName();
        AssetResponseDto updatedAsset = assetService.assignAssetToEmployee(id, employeeId, userEmail);
        return ResponseEntity.ok(updatedAsset);
    }

    @GetMapping("/categories")
    public ResponseEntity<AssetCategory[]> getCategories() {
        return ResponseEntity.ok(AssetCategory.values());
    }
}
