package com.example.store.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@ToString
@EqualsAndHashCode
public class UpdateUser {
    //имя пользователя
    private String firstName;
    //фамилия пользователя
    private String lastName;
    //телефон пользователя
    private String phone;

    public UpdateUser(String firstName, String lastName, String phone) {
        Pattern PATTERN = Pattern.compile("\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}");
        Matcher matcher = PATTERN.matcher(phone);
        if (matcher.matches()){
            this.phone = phone;
        }else{
            throw new RuntimeException("Телефон должен быть написан в формате: +7 (777) 777-77-77");
        }
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        Pattern PATTERN = Pattern.compile("\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}");
        Matcher matcher = PATTERN.matcher(phone);
        if (matcher.matches()){
            this.phone = phone;
        }else{
            throw new RuntimeException("Телефон должен быть написан в формате: +7 (777) 777-77-77");
        }
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
