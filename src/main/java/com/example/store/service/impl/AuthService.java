package com.example.store.service.impl;

import org.springframework.stereotype.Service;

@Service
public class AuthService implements com.example.store.service.AuthService {
    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserService userService;

    public AuthService(UserDetailsManager manager,
                       PasswordEncoder passwordEncoder, UserService userService) {
        this.manager = manager;
        this.encoder = passwordEncoder;
        this.userService = userService;
    }
    /**
     * Method for user registration
     * @param userName - user's login (email)
     * @param password - user's password
     */
    @Override
    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    /**
     * Method for user authenticationM
     * @param register - full user information
     */
    @Override
    public boolean register(Register register) {
        if (manager.userExists(register.getUsername())) {
            return false;
        }
        manager.createUser(
                User.builder()
                        .passwordEncoder(this.encoder::encode)
                        .password(register.getPassword())
                        .username(register.getUsername())
                        .roles(register.getRole().name())
                        .build());
        userService.addInTable(register);
        return true;
    }
}
