package com.example.webapp.service;

import com.example.webapp.model.Subject;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Subject operations
 * This layer handles business logic between Controller and Repository
 */
public interface SubjectService {

    /**
     * Get all subjects
     */
    List<Subject> getAllSubjects();

    /**
     * Get subject by ID
     */
    Optional<Subject> getSubjectById(String id);

    /**
     * Create new subject
     */
    Subject createSubject(Subject subject);

    /**
     * Update existing subject
     */
    Subject updateSubject(String id, Subject subject);

    /**
     * Delete subject by ID
     */
    void deleteSubject(String id);

    /**
     * Count total subjects
     */
    long countSubjects();
}
