package com.example.store.dto;

import lombok.Data;

@Data
public class User {
    //id пользователя
    private int id;
    //логин пользователя
    private String email;
    //имя пользователя
    private String firstName;
    //фамилия пользователя
    private String lastName;
    //телефон пользователя
    private String phone;
    //ссылка на аватар пользователя
    private String image;
    private String role;

}