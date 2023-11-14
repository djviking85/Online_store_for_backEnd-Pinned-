package com.example.store.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class CreateOrUpdateAd {
    //заголовок объявления
    private String title;
    //цена объявления
    private Integer price;
    //описание объявления
    private String description;

    public CreateOrUpdateAd(String title, int price, String description) {
        if (title.length() >= 4 && title.length() <= 32){
            this.title = title;
        }else{
            throw new IndexOutOfBoundsException("Title должен иметь длинну от 4 до 32 включительно");
        }
        if (price >= 0 && price <= 10000000){
            this.price = price;
        }else{
            throw new IndexOutOfBoundsException("price должен быть от 0 до 10000000");
        }
        if (description.length() >= 8 && description.length() <= 64){
            this.description = description;
        }else{
            throw new IndexOutOfBoundsException("Description должен иметь длинну от 8 до 64 включительно");
        }
    }

    public void setTitle(String title) {
        if (title.length() >= 4 && title.length() <= 32){
            this.title = title;
        }else{
            throw new IndexOutOfBoundsException("Title должен иметь длинну от 4 до 32 включительно");
        }
    }

    public void setPrice(int price) {
        if (price >= 0 && price <= 10000000){
            this.price = price;
        }else{
            throw new IndexOutOfBoundsException("price должен быть от 0 до 10000000");
        }
    }

    public void setDescription(String description) {
        if (description.length() >= 8 && description.length() <= 64){
            this.description = description;
        }else{
            throw new IndexOutOfBoundsException("Description должен иметь длинну от 8 до 64 включительно");
        }
    }
}