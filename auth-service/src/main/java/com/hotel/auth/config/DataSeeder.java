package com.hotel.auth.config;

import com.hotel.auth.model.User;
import com.hotel.auth.repository.UserRepository;
import com.hotel.common.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create Manager
        if (!userRepository.existsByUsername("manager")) {
            User manager = new User();
            manager.setUsername("manager");
            manager.setPassword(passwordEncoder.encode("manager123"));
            manager.setEmail("manager@hotel.com");
            manager.setFirstName("Manager");
            manager.setLastName("Admin");
            manager.setRole(Role.ROLE_MANAGER);

            userRepository.save(manager);
            logger.info("Default manager created: username={}, role={}", manager.getUsername(), manager.getRole());
        }

        // Create Receptionist
        if (!userRepository.existsByUsername("receptionist")) {
            User receptionist = new User();
            receptionist.setUsername("receptionist");
            receptionist.setPassword(passwordEncoder.encode("receptionist123"));
            receptionist.setEmail("receptionist@hotel.com");
            receptionist.setFirstName("Receptionist");
            receptionist.setLastName("Staff");
            receptionist.setRole(Role.ROLE_RECEPTIONIST);

            userRepository.save(receptionist);
            logger.info("Default receptionist created: username={}, role={}", receptionist.getUsername(), receptionist.getRole());
        }

        // Create Guest
        if (!userRepository.existsByUsername("guest")) {
            User guest = new User();
            guest.setUsername("guest");
            guest.setPassword(passwordEncoder.encode("guest123"));
            guest.setEmail("guest@hotel.com");
            guest.setFirstName("Guest");
            guest.setLastName("User");
            guest.setRole(Role.ROLE_USER);

            userRepository.save(guest);
            logger.info("Default guest created: username={}, role={}", guest.getUsername(), guest.getRole());
        }
    }
}
