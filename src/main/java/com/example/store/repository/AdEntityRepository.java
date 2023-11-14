package com.example.store.repository;

import com.example.store.entity.AdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for accessing AdEntity data from the database.
 */

public interface AdEntityRepository  extends JpaRepository<AdEntity, Integer>{
    /**
     * Retrieves a list of advertisements based on the user ID.
     *
     * @param id The ID of the user.
     * @return A list of advertisements associated with the given user ID.
     */
    List<AdEntity> findAllByUserId(int id);
}
