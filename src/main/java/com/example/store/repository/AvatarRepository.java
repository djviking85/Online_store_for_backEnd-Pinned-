package com.example.store.repository;

import com.example.store.entity.AvatarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository interface for accessing AvatarEntity data from the database.
 */
public interface AvatarRepository extends JpaRepository<AvatarEntity, Integer> {
}
