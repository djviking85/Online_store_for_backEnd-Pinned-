package com.example.store.service;

import com.example.store.dto.Register;

public interface AuthService {
    /**
     * @param userName - user's login (email)
     * @param password - user's password
     */
    boolean login(String userName, String password);
    /**
     * @param register - full user information
     */

    boolean register(Register register);
}
