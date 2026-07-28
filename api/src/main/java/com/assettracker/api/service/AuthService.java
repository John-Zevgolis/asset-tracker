package com.assettracker.api.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.assettracker.api.dto.AuthResponseDto;
import com.assettracker.api.dto.LoginDto;
import com.assettracker.api.dto.RegisterDto;
import com.assettracker.api.model.User;
import com.assettracker.api.repository.UserRepository;
import com.assettracker.api.security.JwtService;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponseDto register(RegisterDto registerDto) {
        if(userRepository.existsByEmail(registerDto.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email address is already in use");
        }

        User user = new User(
            registerDto.fullName(),
            registerDto.email(),
            passwordEncoder.encode(registerDto.password())
        );

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user.getEmail());

        return new AuthResponseDto(jwtToken, user.getFullName(), user.getEmail());
    }

    public AuthResponseDto login(LoginDto loginDto) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password())
            );
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        User user = userRepository.findByEmail(loginDto.email()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    
        String jwtToken = jwtService.generateToken(user.getEmail());

        return new AuthResponseDto(jwtToken, user.getFullName(), user.getEmail());
    }
}
