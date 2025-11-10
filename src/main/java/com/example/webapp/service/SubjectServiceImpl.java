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
        if (subject.getMajor() == null || subject.getMajor().trim().isEmpty()) {
            throw new IllegalArgumentException("Major cannot be empty");
        }
        if (subject.getSks() == null || subject.getSks() < 1 || subject.getSks() > 6) {
            throw new IllegalArgumentException("SKS must be between 1 and 6");
        }

        // Auto-generate code based on major
        subject.setCode(generateSubjectCode(subject.getMajor()));

        return subjectRepository.save(subject);
    }

    /**
     * Generate subject code with format: XXYYY
     * XX = SI (Sistem Informasi) or TI (Teknologi Informasi)
     * YYY = sequence number (001, 002, etc.)
     */
    private String generateSubjectCode(String major) {
        // Determine prefix based on major
        String prefix;
        if (major.equals("Sistem Informasi")) {
            prefix = "SI";
        } else if (major.equals("Teknologi Informasi")) {
            prefix = "TI";
        } else {
            throw new IllegalArgumentException("Invalid major: " + major);
        }

        // Find max sequence for this major
        List<Subject> existingSubjects = subjectRepository.findAll();
        int maxSequence = 0;

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

        // Keep the existing code, regenerate if major changed
        Subject existing = existingSubject.get();

        // Check if major has changed (handle null values)
        boolean majorChanged = false;
        if (existing.getMajor() == null && subject.getMajor() != null) {
            // Major was null, now has value - don't regenerate code
            majorChanged = false;
        } else if (existing.getMajor() != null && !existing.getMajor().equals(subject.getMajor())) {
            // Major actually changed
            majorChanged = true;
        }

        if (majorChanged) {
            // Major changed, regenerate code
            subject.setCode(generateSubjectCode(subject.getMajor()));
        } else {
            // Keep existing code
            subject.setCode(existing.getCode());
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
