package com.example.webapp.config;

import com.example.webapp.model.Student;
import com.example.webapp.model.StudentStatus;
import com.example.webapp.model.Subject;
import com.example.webapp.service.StudentService;
import com.example.webapp.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Data initializer component
 * Runs on application startup to populate initial data
 * Uses Service layer instead of direct Repository access
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    private final Random random = new Random();

    // Indonesian first names
    private final String[] firstNames = {
        "Andi", "Budi", "Citra", "Dewi", "Eka", "Fajar", "Gita", "Hadi", "Indah", "Joko",
        "Kartika", "Lina", "Made", "Nur", "Oktaviani", "Putra", "Qori", "Rina", "Sari", "Tono",
        "Umar", "Vina", "Wati", "Yanto", "Zahra", "Agus", "Bambang", "Cahya", "Dian", "Endah",
        "Fitri", "Gilang", "Hendra", "Intan", "Jaka", "Kurnia", "Laras", "Mega", "Nanda", "Omar"
    };

    // Indonesian last names
    private final String[] lastNames = {
        "Pratama", "Santoso", "Wijaya", "Kusuma", "Nugroho", "Permata", "Saputra", "Wibowo", "Putra", "Utama",
        "Setiawan", "Rahayu", "Hidayat", "Syahputra", "Ramadhan", "Purnama", "Mahendra", "Pradana", "Saputri", "Lestari",
        "Pangestu", "Firmansyah", "Kurniawan", "Hartono", "Susanto", "Mulyadi", "Gunawan", "Wahyudi", "Suryadi", "Hakim"
    };

    // Subject names for Sistem Informasi
    private final String[] siSubjects = {
        "Basis Data", "Sistem Informasi Manajemen", "Analisis dan Desain Sistem", "Pemrograman Web",
        "Jaringan Komputer", "Sistem Operasi", "Struktur Data", "Algoritma Pemrograman",
        "Manajemen Proyek TI", "Keamanan Informasi", "Data Mining", "Business Intelligence",
        "E-Business", "Enterprise Resource Planning", "Customer Relationship Management",
        "Audit Sistem Informasi", "Tata Kelola TI", "Sistem Pendukung Keputusan", "Rekayasa Perangkat Lunak",
        "Interaksi Manusia dan Komputer", "Pemrograman Berorientasi Objek", "Kecerdasan Buatan"
    };

    // Subject names for Teknologi Informasi
    private final String[] tiSubjects = {
        "Pemrograman Java", "Pemrograman Python", "Pemrograman C++", "Pemrograman Mobile",
        "Cloud Computing", "DevOps", "Arsitektur Komputer", "Sistem Embedded",
        "Internet of Things", "Blockchain Technology", "Machine Learning", "Deep Learning",
        "Computer Vision", "Natural Language Processing", "Big Data Analytics",
        "Forensik Digital", "Cyber Security", "Ethical Hacking", "Network Security",
        "Software Testing", "Agile Development", "Version Control System"
    };

    @Override
    public void run(String... args) {
        // Only initialize if collections are empty
        if (studentService.countStudents() == 0) {
            initializeStudents();
        }

        if (subjectService.countSubjects() == 0) {
            initializeSubjects();
        }
    }

    private void initializeStudents() {
        List<Student> students = new ArrayList<>();
        int[] batches = {2020, 2021, 2022, 2023, 2024};
        String[] majors = {"Sistem Informasi", "Teknologi Informasi"};
        StudentStatus[] statuses = StudentStatus.values();

        // Counter for NIM generation per major and batch
        int[][] counters = new int[2][5]; // [major][batch]

        for (int i = 0; i < 100; i++) {
            // Select major and batch
            int majorIndex = i < 50 ? 0 : 1;  // First 50 SI, next 50 TI
            String major = majors[majorIndex];
            int batchIndex = i % 5;
            int batch = batches[batchIndex];

            // Generate NIM: AABBBBCCCC
            String majorCode = majorIndex == 0 ? "10" : "11";
            counters[majorIndex][batchIndex]++;
            String orderNumber = String.format("%04d", counters[majorIndex][batchIndex]);
            String nim = majorCode + batch + orderNumber;

            // Generate random name
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String fullName = firstName + " " + lastName;

            // Generate email
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@student.ac.id";

            // Assign status based on distribution
            StudentStatus status;
            if (i < 70) {
                status = StudentStatus.ACTIVE;  // 70% active
            } else if (i < 90) {
                status = StudentStatus.NOT_ACTIVE;  // 20% not active
            } else {
                status = StudentStatus.DROPOUT;  // 10% dropout
            }

            Student student = new Student(null, nim, fullName, email, major, batch, status);
            students.add(student);
        }

        // Use service layer to save students
        for (Student student : students) {
            studentService.createStudent(student);
        }
        System.out.println("✅ Initialized 100 students");
    }

    private void initializeSubjects() {
        List<Subject> subjects = new ArrayList<>();

        // Generate SI subjects (50 subjects)
        for (int i = 0; i < siSubjects.length && subjects.size() < 50; i++) {
            String code = "SI" + String.format("%03d", i + 1);
            int sks = (i % 3) + 2;  // SKS between 2-4
            Subject subject = new Subject(null, code, siSubjects[i], sks);
            subjects.add(subject);
        }

        // Add more SI subjects if needed
        int counter = siSubjects.length;
        while (subjects.size() < 50) {
            String code = "SI" + String.format("%03d", counter + 1);
            int sks = (counter % 3) + 2;
            Subject subject = new Subject(null, code, siSubjects[counter % siSubjects.length] + " " + (counter / siSubjects.length + 1), sks);
            subjects.add(subject);
            counter++;
        }

        // Generate TI subjects (50 subjects)
        for (int i = 0; i < tiSubjects.length && subjects.size() < 100; i++) {
            String code = "TI" + String.format("%03d", i + 1);
            int sks = (i % 3) + 2;  // SKS between 2-4
            Subject subject = new Subject(null, code, tiSubjects[i], sks);
            subjects.add(subject);
        }

        // Add more TI subjects if needed
        counter = tiSubjects.length;
        while (subjects.size() < 100) {
            String code = "TI" + String.format("%03d", counter + 1);
            int sks = (counter % 3) + 2;
            Subject subject = new Subject(null, code, tiSubjects[counter % tiSubjects.length] + " " + (counter / tiSubjects.length + 1), sks);
            subjects.add(subject);
            counter++;
        }

        // Use service layer to save subjects
        for (Subject subject : subjects) {
            subjectService.createSubject(subject);
        }
        System.out.println("✅ Initialized 100 subjects");
    }
}
