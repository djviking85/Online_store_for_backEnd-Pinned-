package com.example.store.dto;

import lombok.Data;


@Data
public class Register {
    /**
     * Field username
     */
    private String username;
    /**
     * Field password
     */
    private String password;
    /**
     * Field first name
     */
    private String firstName;
    /**
     * Field last name
     */
    private String lastName;
    /**
     * Field phone
     */
    private String phone;
    /**
     * Field role
     */
    private Role role;

}
