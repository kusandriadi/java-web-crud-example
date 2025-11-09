package com.example.webapp.service;

import com.example.webapp.model.Subject;
import com.example.webapp.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for Subject operations
 * Contains all business logic for subject management
 */
@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public Optional<Subject> getSubjectById(String id) {
        return subjectRepository.findById(id);
    }

    @Override
    public Subject createSubject(Subject subject) {
        // Business logic: validate subject data before saving
        if (subject.getName() == null || subject.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Subject name cannot be empty");
        }
        if (subject.getCode() == null || subject.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Subject code cannot be empty");
        }
        if (subject.getSks() == null || subject.getSks() < 1 || subject.getSks() > 6) {
            throw new IllegalArgumentException("SKS must be between 1 and 6");
        }

        return subjectRepository.save(subject);
    }

    @Override
    public Subject updateSubject(String id, Subject subject) {
        // Business logic: check if subject exists before updating
        Optional<Subject> existingSubject = subjectRepository.findById(id);
        if (existingSubject.isEmpty()) {
            throw new IllegalArgumentException("Subject not found with id: " + id);
        }

        subject.setId(id);
        return subjectRepository.save(subject);
    }

    @Override
    public void deleteSubject(String id) {
        // Business logic: check if subject exists before deleting
        Optional<Subject> subject = subjectRepository.findById(id);
        if (subject.isEmpty()) {
            throw new IllegalArgumentException("Subject not found with id: " + id);
        }

        subjectRepository.deleteById(id);
    }

    @Override
    public long countSubjects() {
        return subjectRepository.count();
    }
}
