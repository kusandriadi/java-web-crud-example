package com.example.webapp.controller;

import com.example.webapp.model.ClassRoom;
import com.example.webapp.service.ClassRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for ClassRoom endpoints
 * Handles HTTP requests for class management
 */
@RestController
@RequestMapping("/api/classes")
public class ClassRoomController {

    @Autowired
    private ClassRoomService classRoomService;

    /**
     * Get all classes
     */
    @GetMapping
    public ResponseEntity<List<ClassRoom>> getAllClasses() {
        List<ClassRoom> classes = classRoomService.getAllClasses();
        return ResponseEntity.ok(classes);
    }

    /**
     * Get class by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClassRoom> getClassById(@PathVariable String id) {
        Optional<ClassRoom> classRoom = classRoomService.getClassById(id);
        return classRoom.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create new class
     */
    @PostMapping
    public ResponseEntity<ClassRoom> createClass(@RequestBody ClassRoom classRoom) {
        try {
            ClassRoom created = classRoomService.createClass(classRoom);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update existing class
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClassRoom> updateClass(@PathVariable String id, @RequestBody ClassRoom classRoom) {
        try {
            ClassRoom updated = classRoomService.updateClass(id, classRoom);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete class
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable String id) {
        try {
            classRoomService.deleteClass(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Add student to class
     */
    @PostMapping("/{classId}/students/{studentId}")
    public ResponseEntity<ClassRoom> addStudentToClass(@PathVariable String classId, @PathVariable String studentId) {
        try {
            ClassRoom updated = classRoomService.addStudentToClass(classId, studentId);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Remove student from class
     */
    @DeleteMapping("/{classId}/students/{studentId}")
    public ResponseEntity<ClassRoom> removeStudentFromClass(@PathVariable String classId, @PathVariable String studentId) {
        try {
            ClassRoom updated = classRoomService.removeStudentFromClass(classId, studentId);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
