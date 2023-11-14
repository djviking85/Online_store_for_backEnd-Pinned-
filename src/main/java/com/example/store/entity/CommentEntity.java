package com.example.store.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a CommentEntity, which holds information about comments on advertisements.
 * This entity is mapped to a database table using JPA annotations.
 */
@Entity
@Data
public class CommentEntity {
    /**
     * Primary key of the CommentEntity, automatically generated upon insertion.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;

    /**
     * The user who made the comment. This is a Many-to-One relationship with UserEntity.
     */
    @ManyToOne()
    @JoinColumn (name = "id")
    private UserEntity user;

    /**
     * The date and time when the comment was created.
     */
    private LocalDateTime createdAt;

    /**
     * The text content of the comment.
     */
    private String text;

    /**
     * The advertisement associated with the comment. This is a Many-to-One relationship with AdEntity.
     */
    @ManyToOne
    @JoinColumn (name = "adEntity.pk")
    private AdEntity adEntity;
}
