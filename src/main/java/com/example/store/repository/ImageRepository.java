package com.example.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository interface for accessing ImageEntity data from the database.
 */

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
}
