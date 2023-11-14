package com.example.store.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class AvatarEntity {
    /**
     * Field id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * Field user
     */
    @OneToOne(mappedBy = "avatarEntity")
    @JoinColumn(name = "user_id")
    private UserEntity user;
    /**
     * Field data
     */
    private byte[] data;
    /**
     * Field name of file
     */
    private String fileName;
    /**
     * Field type of media
     */
    private String mediaType;
}