package com.assettracker.api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.assettracker.api.dto.AssetRequestDto;
import com.assettracker.api.dto.AssetResponseDto;
import com.assettracker.api.model.Asset;
import com.assettracker.api.model.Employee;
import com.assettracker.api.model.User;
import com.assettracker.api.repository.AssetRepository;
import com.assettracker.api.repository.EmployeeRepository;
import com.assettracker.api.repository.UserRepository;

@Service
public class AssetService {
    private final AssetRepository assetRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    public AssetService(AssetRepository assetRepository, UserRepository userRepository, EmployeeRepository employeeRepository) {
        this.assetRepository = assetRepository;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<AssetResponseDto> getUserAssets(String userEmail) {
        User user = getUserByEmail(userEmail);

        return assetRepository.findByUserId(user.getId()).stream().map(asset -> mapToResponseDto(asset)).toList();
    }

    public AssetResponseDto createAsset(AssetRequestDto assetRequestDto, String userEmail) {
        User user = getUserByEmail(userEmail);

        if(assetRepository.existsBySerialNumberAndUserId(assetRequestDto.serialNumber(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An asset with this serial number already exists");
        }

        Asset asset = new Asset(
            assetRequestDto.name(),
            assetRequestDto.category(),
            assetRequestDto.serialNumber(),
            assetRequestDto.value(),
            assetRequestDto.purchaseDate(),
            user
        );

        Asset savedAsset = assetRepository.save(asset);
        return mapToResponseDto(savedAsset);
    }

    public AssetResponseDto updateAsset(Long assetId, AssetRequestDto assetRequestDto, String userEmail) {
        User user = getUserByEmail(userEmail);

        Asset asset = assetRepository.findByIdAndUserId(assetId, user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asset not found or access denied"));

        if(!asset.getSerialNumber().equals(assetRequestDto.serialNumber()) && assetRepository.existsBySerialNumberAndUserId(assetRequestDto.serialNumber(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An employee with this email already exists");
        }

        asset.setName(assetRequestDto.name());
        asset.setCategory(assetRequestDto.category());
        asset.setSerialNumber(assetRequestDto.serialNumber());
        asset.setValue(assetRequestDto.value());
        asset.setPurchaseDate(assetRequestDto.purchaseDate());

        Asset updatedAsset = assetRepository.save(asset);
        return mapToResponseDto(updatedAsset);
    }

    public void deleteAsset(Long assetId, String userEmail) {
        User user = getUserByEmail(userEmail);

        Asset asset = assetRepository.findByIdAndUserId(assetId, user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asset not found or access denied"));
        assetRepository.delete(asset);
    }

    public AssetResponseDto assignAssetToEmployee(Long assetId, Long employeeId, String userEmail) {
        User user = getUserByEmail(userEmail);

        Asset asset = assetRepository.findByIdAndUserId(assetId, user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asset not found or access denied"));
        
        if(employeeId != null) {
            Employee employee = employeeRepository.findByIdAndUserId(employeeId, user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found or access denied"));
            asset.setEmployee(employee);
        } else {
            asset.setEmployee(null);
        }

        Asset updatedAsset = assetRepository.save(asset);
        return mapToResponseDto(updatedAsset);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private AssetResponseDto mapToResponseDto(Asset asset) {
        Long employeeId = asset.getEmployee() != null ? asset.getEmployee().getId() : null;
        String employeeName = asset.getEmployee() != null ? asset.getEmployee().getFullName() : null;

        return new AssetResponseDto(
            asset.getId(),
            asset.getName(),
            asset.getCategory(),
            asset.getSerialNumber(),
            asset.getValue(),
            asset.getPurchaseDate(),
            employeeId,
            employeeName
        );
    }
}
