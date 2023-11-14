package com.example.store.dto;

import lombok.Data;

import java.util.List;

@Data
public class Comments {
    //общее количество комментариев
    private Integer count;
    private List<Comment> results;
}