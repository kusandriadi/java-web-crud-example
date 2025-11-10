package com.example.webapp.service;

import com.example.webapp.model.ClassRoom;
import com.example.webapp.repository.ClassRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for ClassRoom operations
 * Contains all business logic for class management
 */
@Service
public class ClassRoomServiceImpl implements ClassRoomService {

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Override
    public List<ClassRoom> getAllClasses() {
        return classRoomRepository.findAll();
    }

    @Override
    public Optional<ClassRoom> getClassById(String id) {
        return classRoomRepository.findById(id);
    }

    @Override
    public ClassRoom createClass(ClassRoom classRoom) {
        // Validate class data
        if (classRoom.getName() == null || classRoom.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Class name cannot be empty");
        }
        if (classRoom.getSubjectName() == null || classRoom.getSubjectName().trim().isEmpty()) {
            throw new IllegalArgumentException("Subject name cannot be empty");
        }

        // Auto-generate code if not provided
        if (classRoom.getCode() == null || classRoom.getCode().trim().isEmpty()) {
            classRoom.setCode(generateClassCode());
        }

        // Initialize empty student list if null
        if (classRoom.getStudentIds() == null) {
            classRoom.setStudentIds(new ArrayList<>());
        }

        return classRoomRepository.save(classRoom);
    }

    @Override
    public ClassRoom updateClass(String id, ClassRoom classRoom) {
        // Check if class exists
        Optional<ClassRoom> existingClass = classRoomRepository.findById(id);
        if (existingClass.isEmpty()) {
            throw new IllegalArgumentException("Class not found with id: " + id);
        }

        classRoom.setId(id);
        return classRoomRepository.save(classRoom);
    }

    @Override
    public void deleteClass(String id) {
        // Check if class exists
        Optional<ClassRoom> classRoom = classRoomRepository.findById(id);
        if (classRoom.isEmpty()) {
            throw new IllegalArgumentException("Class not found with id: " + id);
        }

        classRoomRepository.deleteById(id);
    }

    @Override
    public ClassRoom addStudentToClass(String classId, String studentId) {
        Optional<ClassRoom> classRoomOpt = classRoomRepository.findById(classId);
        if (classRoomOpt.isEmpty()) {
            throw new IllegalArgumentException("Class not found with id: " + classId);
        }

        ClassRoom classRoom = classRoomOpt.get();

        // Initialize student list if null
        if (classRoom.getStudentIds() == null) {
            classRoom.setStudentIds(new ArrayList<>());
        }

        // Add student if not already in the class
        if (!classRoom.getStudentIds().contains(studentId)) {
            classRoom.getStudentIds().add(studentId);
            return classRoomRepository.save(classRoom);
        }

        return classRoom;
    }

    @Override
    public ClassRoom removeStudentFromClass(String classId, String studentId) {
        Optional<ClassRoom> classRoomOpt = classRoomRepository.findById(classId);
        if (classRoomOpt.isEmpty()) {
            throw new IllegalArgumentException("Class not found with id: " + classId);
        }

        ClassRoom classRoom = classRoomOpt.get();

        if (classRoom.getStudentIds() != null) {
            classRoom.getStudentIds().remove(studentId);
            return classRoomRepository.save(classRoom);
        }

        return classRoom;
    }

    @Override
    public long countClasses() {
        return classRoomRepository.count();
    }

    /**
     * Generate class code with format: KLS###
     * KLS = Kelas
     * ### = sequence number (001, 002, etc.)
     */
    private String generateClassCode() {
        List<ClassRoom> existingClasses = classRoomRepository.findAll();
        int maxSequence = 0;

        String prefix = "KLS";
        for (ClassRoom c : existingClasses) {
            if (c.getCode() != null && c.getCode().startsWith(prefix)) {
                try {
                    int sequence = Integer.parseInt(c.getCode().substring(3));
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
}
