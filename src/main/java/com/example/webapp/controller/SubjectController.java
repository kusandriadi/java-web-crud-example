package com.example.webapp.controller;

import com.example.webapp.model.Subject;
import com.example.webapp.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Subject operations
 * This layer handles HTTP requests/responses
 * Business logic is delegated to SubjectService
 */
@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    /**
     * Get all subjects
     */
    @GetMapping
    public ResponseEntity<List<Subject>> getAllSubjects() {
        List<Subject> subjects = subjectService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }

    /**
     * Get subject by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Subject> getSubjectById(@PathVariable String id) {
        return subjectService.getSubjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create new subject
     */
    @PostMapping
    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject) {
        try {
            Subject savedSubject = subjectService.createSubject(subject);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSubject);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update existing subject
     */
    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateSubject(@PathVariable String id, @RequestBody Subject subject) {
        try {
            Subject updatedSubject = subjectService.updateSubject(id, subject);
            return ResponseEntity.ok(updatedSubject);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete subject by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable String id) {
        try {
            subjectService.deleteSubject(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
