package com.example.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

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
