package com.example.webapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassRoom entity
 * Represents a class with enrolled students
 * Note: Named "ClassRoom" to avoid Java keyword "Class"
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "classes")
public class ClassRoom {
    @Id
    private String id;
    private String code;          // Class code (e.g., "KLS001")
    private String name;          // Class name (e.g., "Basis Data - Kelas A")
    private String subjectId;     // Reference to Subject
    private String subjectName;   // Denormalized for easier display
    private String semester;      // Semester (e.g., "Ganjil 2024/2025")
    private Integer year;         // Academic year
    private List<String> studentIds = new ArrayList<>();  // List of enrolled student IDs

    // Transient field used only during JSON deserialization for initialization
    @Transient
    private List<String> studentNims;  // Temporary list of student NIMs (not stored in DB)
}
