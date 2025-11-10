package com.example.webapp.service;

import com.example.webapp.model.ClassRoom;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for ClassRoom operations
 * Defines contract for class management business logic
 */
public interface ClassRoomService {
    List<ClassRoom> getAllClasses();
    Optional<ClassRoom> getClassById(String id);
    ClassRoom createClass(ClassRoom classRoom);
    ClassRoom updateClass(String id, ClassRoom classRoom);
    void deleteClass(String id);
    ClassRoom addStudentToClass(String classId, String studentId);
    ClassRoom removeStudentFromClass(String classId, String studentId);
    long countClasses();
}
