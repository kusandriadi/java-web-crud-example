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
        if (subject.getSks() == null || subject.getSks() < 1 || subject.getSks() > 6) {
            throw new IllegalArgumentException("SKS must be between 1 and 6");
        }

        // Auto-generate code if not provided
        if (subject.getCode() == null || subject.getCode().trim().isEmpty()) {
            subject.setCode(generateSubjectCode());
        }

        return subjectRepository.save(subject);
    }

    /**
     * Generate subject code with format: MK###
     * MK = Mata Kuliah
     * ### = sequence number (001, 002, etc.)
     */
    private String generateSubjectCode() {
        List<Subject> existingSubjects = subjectRepository.findAll();
        int maxSequence = 0;

        String prefix = "MK";
        for (Subject s : existingSubjects) {
            if (s.getCode() != null && s.getCode().startsWith(prefix)) {
                try {
                    int sequence = Integer.parseInt(s.getCode().substring(2));
                    if (sequence > maxSequence) {
                        maxSequence = sequence;
                    }
                } catch (Exception e) {
                    // Skip invalid code format
                }
            }
        }

        // Generate new code with next sequence number
        int newSequence = maxSequence + 1;
        return String.format("%s%03d", prefix, newSequence);
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
