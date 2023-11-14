package com.example.store.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode

public class NewPassword {
    //текущий пароль
    private String currentPassword;
    //новый пароль
    private String newPassword;

    public NewPassword(String currentPassword, String newPassword) {
        if (currentPassword.length() >= 8 ){
            this.currentPassword = currentPassword;
        }else{
            throw new IndexOutOfBoundsException("Прежний пароль составляет меньше 8 цифр");
        }
        if (newPassword.length() >= 8 && newPassword.length() <= 16){
            this.newPassword = newPassword;
        }else{
            throw new IndexOutOfBoundsException("Новый пароль составляет либо меньше 8 цифр , либо больше 16 цифр");
        }
    }

    public void setCurrentPassword(String currentPassword) {
        if (currentPassword.length() >= 8 && currentPassword.length() <= 16){
            this.currentPassword = currentPassword;
        }else{
            throw new IndexOutOfBoundsException("Прежний пароль состалняет либо меньше 8 цифр , либо больше 16 цифр");
        }
    }

    public void setNewPassword(String newPassword) {
        if (newPassword.length() >= 8 && newPassword.length() <= 16){
            this.newPassword = newPassword;
        }else{
            throw new IndexOutOfBoundsException("Новый пароль состалняет либо меньше 8 цифр , либо больше 16 цифр");
        }
    }
}
