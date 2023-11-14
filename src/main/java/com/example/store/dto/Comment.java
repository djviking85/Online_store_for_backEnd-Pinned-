package com.example.store.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    //id автора комментария
    private Integer author;
    //ссылка на аватар автора комментария

    //дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
    private LocalDateTime createdAt;
    //id комментария
    private Integer pk;
    //текст комментария
    private String text;
}