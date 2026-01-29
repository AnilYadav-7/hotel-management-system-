package com.hotel.auth.service;

import com.hotel.auth.dto.AuthResponseDTO;
import com.hotel.auth.dto.LoginRequestDTO;
import com.hotel.auth.dto.RegisterRequestDTO;
import com.hotel.auth.dto.StaffRequestDTO;
import com.hotel.auth.model.User;
import com.hotel.auth.repository.UserRepository;
import com.hotel.common.exception.UserAlreadyExistsException;
import com.hotel.common.exception.ResourceNotFoundException;
import com.hotel.common.model.Role;
import com.hotel.common.security.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO registerRequestDTO) throws UserAlreadyExistsException {
        if (userRepository.existsByUsername(registerRequestDTO.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + registerRequestDTO.getUsername());
        }

        User user = new User();
        user.setUsername(registerRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRole(Role.ROLE_USER);

        User savedUser = userRepository.save(user);

        String token = jwtUtils.generateToken(savedUser);

        return new AuthResponseDTO(token, savedUser.getUsername(), savedUser.getRole());
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()
                )
        );

        User user = userRepository.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + loginRequestDTO.getUsername()));

        String token = jwtUtils.generateToken(user);

        return new AuthResponseDTO(token, user.getUsername(), user.getRole());
    }

    @Override
    public User createStaff(@Valid StaffRequestDTO request) throws UserAlreadyExistsException {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + request.getUsername());
        }

        Role staffRole = request.getRole();
        if (staffRole == Role.ROLE_USER) {
            throw new IllegalArgumentException("Cannot create staff with ROLE_USER. Use RECEPTIONIST or MANAGER.");
        }

        User staff = new User();
        staff.setUsername(request.getUsername());
        staff.setPassword(passwordEncoder.encode(request.getPassword()));
        staff.setEmail(request.getEmail());
        staff.setFirstName(request.getFirstName());
        staff.setLastName(request.getLastName());
        staff.setRole(staffRole);

        return userRepository.save(staff);
    }

    @Override
    public List<User> getAllStaff() {
        return userRepository.findByRole(Role.ROLE_RECEPTIONIST);
    }

    @Override
    public User updateStaff(Long id, StaffRequestDTO request) throws ResourceNotFoundException, UserAlreadyExistsException {
        User staff = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with ID: " + id));

        if (!staff.getUsername().equals(request.getUsername()) && userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + request.getUsername());
        }

        staff.setUsername(request.getUsername());
        staff.setEmail(request.getEmail());
        staff.setFirstName(request.getFirstName());
        staff.setLastName(request.getLastName());
        staff.setRole(request.getRole());

        return userRepository.save(staff);
    }

    @Override
    public void deleteStaff(Long id) throws ResourceNotFoundException {
        User staff = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with ID: " + id));
        userRepository.delete(staff);
    }

}
