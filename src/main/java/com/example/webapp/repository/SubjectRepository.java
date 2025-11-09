package com.example.webapp.repository;

import com.example.webapp.model.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends MongoRepository<Subject, String> {
    Optional<Subject> findByCode(String code);
}
