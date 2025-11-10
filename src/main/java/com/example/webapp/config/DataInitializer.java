package com.example.webapp.config;

import com.example.webapp.model.ClassRoom;
import com.example.webapp.model.Student;
import com.example.webapp.model.Subject;
import com.example.webapp.service.ClassRoomService;
import com.example.webapp.service.StudentService;
import com.example.webapp.service.SubjectService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Data initializer component
 * Runs on application startup to populate initial data from JSON files
 * Uses Service layer instead of direct Repository access
 */
@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ClassRoomService classRoomService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(String... args) {
        try {
            log.info("Starting data initialization...");

            // Only initialize if collections are empty
            if (studentService.countStudents() == 0) {
                initializeStudents();
            } else {
                log.info("Students collection already has data, skipping initialization");
            }

            if (subjectService.countSubjects() == 0) {
                initializeSubjects();
            } else {
                log.info("Subjects collection already has data, checking for major field migration...");
                migrateSubjectMajors();
            }

            if (classRoomService.countClasses() == 0) {
                initializeClasses();
            } else {
                log.info("Classes collection already has data, skipping initialization");
            }

            log.info("Data initialization completed");
        } catch (Exception e) {
            log.error("Error initializing data: {}", e.getMessage(), e);
        }
    }

    /**
     * Load students from JSON file
     */
    private void initializeStudents() {
        try {
            log.info("Loading students from students.json...");

            // Read JSON file from resources/data folder
            ClassPathResource resource = new ClassPathResource("data/students.json");
            InputStream inputStream = resource.getInputStream();

            // Parse JSON to List of Student objects
            List<Student> students = objectMapper.readValue(
                inputStream,
                new TypeReference<List<Student>>() {}
            );

            // Save all students using service layer
            for (Student student : students) {
                studentService.createStudent(student);
            }

            log.info("Successfully initialized {} students from students.json", students.size());

        } catch (Exception e) {
            log.error("Failed to load students from JSON: {}", e.getMessage(), e);
        }
    }

    /**
     * Load subjects from JSON file
     */
    private void initializeSubjects() {
        try {
            log.info("Loading subjects from subjects.json...");

            // Read JSON file from resources/data folder
            ClassPathResource resource = new ClassPathResource("data/subjects.json");
            InputStream inputStream = resource.getInputStream();

            // Parse JSON to List of Subject objects
            List<Subject> subjects = objectMapper.readValue(
                inputStream,
                new TypeReference<List<Subject>>() {}
            );

            // Save all subjects using service layer
            for (Subject subject : subjects) {
                subjectService.createSubject(subject);
            }

            log.info("Successfully initialized {} subjects from subjects.json", subjects.size());

        } catch (Exception e) {
            log.error("Failed to load subjects from JSON: {}", e.getMessage(), e);
        }
    }

    /**
     * Migrate existing subjects to add major field based on code prefix
     */
    private void migrateSubjectMajors() {
        try {
            log.info("Migrating subjects to add major field...");

            List<Subject> allSubjects = subjectService.getAllSubjects();
            int migrated = 0;

            for (Subject subject : allSubjects) {
                // Only update if major is null or empty
                if (subject.getMajor() == null || subject.getMajor().isEmpty()) {
                    // Determine major based on code prefix
                    if (subject.getCode() != null) {
                        if (subject.getCode().startsWith("SI")) {
                            subject.setMajor("Sistem Informasi");
                            subjectService.updateSubject(subject.getId(), subject);
                            migrated++;
                        } else if (subject.getCode().startsWith("TI")) {
                            subject.setMajor("Teknologi Informasi");
                            subjectService.updateSubject(subject.getId(), subject);
                            migrated++;
                        }
                    }
                }
            }

            if (migrated > 0) {
                log.info("Successfully migrated {} subjects with major field", migrated);
            } else {
                log.info("No subjects needed migration");
            }

        } catch (Exception e) {
            log.error("Failed to migrate subjects: {}", e.getMessage(), e);
        }
    }

    /**
     * Load classes from JSON file
     */
    private void initializeClasses() {
        try {
            log.info("Loading classes from classes.json...");

            // Read JSON file from resources/data folder
            ClassPathResource resource = new ClassPathResource("data/classes.json");
            InputStream inputStream = resource.getInputStream();

            // Parse JSON to List of ClassRoom objects
            List<ClassRoom> classes = objectMapper.readValue(
                inputStream,
                new TypeReference<List<ClassRoom>>() {}
            );

            // Get all subjects to match subject names to IDs
            List<Subject> allSubjects = subjectService.getAllSubjects();

            // Save all classes using service layer
            for (ClassRoom classRoom : classes) {
                // Try to find matching subject by name
                allSubjects.stream()
                    .filter(s -> s.getName().equals(classRoom.getSubjectName()))
                    .findFirst()
                    .ifPresent(subject -> classRoom.setSubjectId(subject.getId()));

                // Convert student NIMs to student IDs
                if (classRoom.getStudentNims() != null && !classRoom.getStudentNims().isEmpty()) {
                    List<String> studentIds = new ArrayList<>();
                    for (String nim : classRoom.getStudentNims()) {
                        studentService.getAllStudents().stream()
                            .filter(s -> nim.equals(s.getNim()))
                            .findFirst()
                            .ifPresent(student -> studentIds.add(student.getId()));
                    }
                    classRoom.setStudentIds(studentIds);
                    log.info("Added {} students to class {}", studentIds.size(), classRoom.getName());
                }

                classRoomService.createClass(classRoom);
            }

            log.info("Successfully initialized {} classes from classes.json", classes.size());

        } catch (Exception e) {
            log.error("Failed to load classes from JSON: {}", e.getMessage(), e);
        }
    }
}
