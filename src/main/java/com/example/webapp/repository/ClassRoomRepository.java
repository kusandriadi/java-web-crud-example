package com.example.webapp.repository;

import com.example.webapp.model.ClassRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for ClassRoom entity
 * Provides CRUD operations for ClassRoom collection
 */
@Repository
public interface ClassRoomRepository extends MongoRepository<ClassRoom, String> {
    // Custom query methods can be added here if needed
}
