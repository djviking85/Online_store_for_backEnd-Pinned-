package com.example.store.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class CreateOrUpdateComment {
    //текст комментария
    private String text;

    public CreateOrUpdateComment(String text) {
        if (text.length() >= 8 && text.length() <= 64){
            this.text = text;
        }else{
            throw new IndexOutOfBoundsException("Text должен иметь длинну от 8 до 64 включительно");
        }
    }

    public void setText(String text) {
        if (text.length() >= 8 && text.length() <= 64){
            this.text = text;
        }else{
            throw new IndexOutOfBoundsException("Text должен иметь длинну от 8 до 64 включительно");
        }
    }
}