package com.example.store.repository;

import com.example.store.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {
    /**
     * Retrieves a list of comments based on the primary key of the associated AdEntity.
     *
     * @param adEntity_pk The primary key of the associated AdEntity.
     * @return A list of comments associated with the given AdEntity primary key.
     */
    List<CommentEntity> findAllByAdEntity_Pk(Integer adEntity_pk);
}
