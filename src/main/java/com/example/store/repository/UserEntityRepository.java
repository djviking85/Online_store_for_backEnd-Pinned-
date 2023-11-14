package com.example.store.repository;

import com.example.store.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing UserEntity data from the database.
 */
@Repository
public interface UserEntityRepository extends JpaRepository <UserEntity, Integer> {
    /**
     * Retrieves a user entity based on the email.
     *
     * @param email The email associated with the user.
     * @return An Optional containing the user entity with the given email, if found.
     */
    Optional<UserEntity> findByEmail(String email);
}
