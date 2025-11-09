package com.example.webapp.service;

import com.example.webapp.model.Student;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service interface for Student operations
 * This layer handles business logic between Controller and Repository
 */
public interface StudentService {

    /**
     * Get all students
     */
    List<Student> getAllStudents();

    /**
     * Get student by ID
     */
    Optional<Student> getStudentById(String id);

    /**
     * Create new student
     */
    Student createStudent(Student student);

    /**
     * Update existing student
     */
    Student updateStudent(String id, Student student);

    /**
     * Delete student by ID
     */
    void deleteStudent(String id);

    /**
     * Get major options
     */
    List<String> getMajorOptions();

    /**
     * Get student statistics for dashboard
     */
    Map<String, Object> getStatistics();

    /**
     * Count total students
     */
    long countStudents();
}
