package com.assettracker.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assettracker.api.model.Asset;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByUserId(Long userId);

    Optional<Asset> findByIdAndUserId(Long id, Long userId);

    boolean existsBySerialNumberAndUserId(String serialNumber, Long userId);
}
