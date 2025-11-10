package com.example.webapp.service;

import com.example.webapp.model.Student;
import com.example.webapp.model.StudentStatus;
import com.example.webapp.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service implementation for Student operations
 * Contains all business logic for student management
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Value("${major.options}")
    private String majorOptions;

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> getStudentById(String id) {
        return studentRepository.findById(id);
    }

    @Override
    public Student createStudent(Student student) {
        // Business logic: validate student data before saving
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty");
        }
        if (student.getMajor() == null || student.getMajor().trim().isEmpty()) {
            throw new IllegalArgumentException("Student major cannot be empty");
        }
        if (student.getBatch() == null) {
            throw new IllegalArgumentException("Student batch cannot be empty");
        }

        // Auto-generate NIM if not provided
        if (student.getNim() == null || student.getNim().trim().isEmpty()) {
            student.setNim(generateNim(student.getMajor(), student.getBatch()));
        }

        return studentRepository.save(student);
    }

    /**
     * Generate NIM with format: AABBBBCCCC
     * AA = major code (10=SI, 11=TI)
     * BBBB = batch year
     * CCCC = sequence number
     */
    private String generateNim(String major, Integer batch) {
        // Determine major code
        String majorCode = "Sistem Informasi".equals(major) ? "10" : "11";

        // Find the highest sequence number for this major and batch
        List<Student> existingStudents = studentRepository.findAll();
        int maxSequence = 0;

        String prefix = majorCode + batch;
        for (Student s : existingStudents) {
            if (s.getNim() != null && s.getNim().startsWith(prefix)) {
                try {
                    int sequence = Integer.parseInt(s.getNim().substring(6));
                    if (sequence > maxSequence) {
                        maxSequence = sequence;
                    }
                } catch (Exception e) {
                    // Skip invalid NIM format
                }
            }
        }

        // Generate new NIM with next sequence number
        int newSequence = maxSequence + 1;
        return String.format("%s%04d", prefix, newSequence);
    }

    @Override
    public Student updateStudent(String id, Student student) {
        // Business logic: check if student exists before updating
        Optional<Student> existingStudent = studentRepository.findById(id);
        if (existingStudent.isEmpty()) {
            throw new IllegalArgumentException("Student not found with id: " + id);
        }

        student.setId(id);
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(String id) {
        // Business logic: check if student exists before deleting
        Optional<Student> student = studentRepository.findById(id);
        if (student.isEmpty()) {
            throw new IllegalArgumentException("Student not found with id: " + id);
        }

        studentRepository.deleteById(id);
    }

    @Override
    public List<String> getMajorOptions() {
        return Arrays.asList(majorOptions.split(","));
    }

    @Override
    public Map<String, Object> getStatistics() {
        List<Student> allStudents = studentRepository.findAll();

        Map<String, Object> stats = new HashMap<>();

        // Count by major
        long siTotal = allStudents.stream()
            .filter(s -> "Sistem Informasi".equals(s.getMajor()))
            .count();
        long tiTotal = allStudents.stream()
            .filter(s -> "Teknologi Informasi".equals(s.getMajor()))
            .count();

        // Count active by major
        long siActive = allStudents.stream()
            .filter(s -> "Sistem Informasi".equals(s.getMajor()) && StudentStatus.ACTIVE.equals(s.getStatus()))
            .count();
        long tiActive = allStudents.stream()
            .filter(s -> "Teknologi Informasi".equals(s.getMajor()) && StudentStatus.ACTIVE.equals(s.getStatus()))
            .count();

        // Count not active by major
        long siNotActive = allStudents.stream()
            .filter(s -> "Sistem Informasi".equals(s.getMajor()) && StudentStatus.NOT_ACTIVE.equals(s.getStatus()))
            .count();
        long tiNotActive = allStudents.stream()
            .filter(s -> "Teknologi Informasi".equals(s.getMajor()) && StudentStatus.NOT_ACTIVE.equals(s.getStatus()))
            .count();

        stats.put("siTotal", siTotal);
        stats.put("tiTotal", tiTotal);
        stats.put("siActive", siActive);
        stats.put("tiActive", tiActive);
        stats.put("siNotActive", siNotActive);
        stats.put("tiNotActive", tiNotActive);
        stats.put("totalStudents", allStudents.size());

        return stats;
    }

    @Override
    public long countStudents() {
        return studentRepository.count();
    }
}
