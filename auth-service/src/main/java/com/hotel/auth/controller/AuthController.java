package com.hotel.auth.controller;

import com.hotel.auth.dto.AuthResponseDTO;
import com.hotel.auth.dto.LoginRequestDTO;
import com.hotel.auth.dto.RegisterRequestDTO;
import com.hotel.auth.dto.StaffRequestDTO;
import com.hotel.auth.model.User;
import com.hotel.auth.service.AuthService;
import com.hotel.common.exception.UserAlreadyExistsException;
import com.hotel.common.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) 
            throws UserAlreadyExistsException {
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO response = authService.login(request);
        logger.info("User logged in: username={}, role={}", response.getUsername(), response.getRole());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-staff")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> createStaff(@Valid @RequestBody StaffRequestDTO request) 
            throws UserAlreadyExistsException {
        User staff = authService.createStaff(request);
        logger.info("Staff user created: username={}, role={}", staff.getUsername(), staff.getRole());
        return new ResponseEntity<>("Staff created successfully: " + staff.getUsername(), HttpStatus.CREATED);
    }

    @GetMapping("/staff")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<User>> getAllStaff() {
        List<User> staff = authService.getAllStaff();
        return ResponseEntity.ok(staff);
    }

    @PutMapping("/staff/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> updateStaff(@PathVariable Long id, @Valid @RequestBody StaffRequestDTO request)
            throws ResourceNotFoundException, UserAlreadyExistsException {
        User staff = authService.updateStaff(id, request);
        logger.info("Staff user updated: id={}, username={}", id, staff.getUsername());
        return ResponseEntity.ok("Staff updated successfully: " + staff.getUsername());
    }

    @DeleteMapping("/staff/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> deleteStaff(@PathVariable Long id) throws ResourceNotFoundException {
        authService.deleteStaff(id);
        logger.info("Staff user deleted: id={}", id);
        return ResponseEntity.ok("Staff deleted successfully");
    }
}
