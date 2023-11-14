package com.example.store.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Entity class for advertisement
 *
 * @autor Kirill
 */
@Entity
@Data
public class AdEntity {
    /**
     * Field image
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ad")
    private ImageEntity image;
    /**
     * Field primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
    /**
     * Field price
     */
    private Integer price;
    /**
     * Field tittle
     */
    private String title;
    /**
     * Field user
     */
    @ManyToOne
    @JoinColumn (name = "id")
    private UserEntity user;
    /**
     * Field description
     */
    private String description;
    /**
     * Field comments
     */
    @OneToMany
    private List<CommentEntity> commentEntities;



}
