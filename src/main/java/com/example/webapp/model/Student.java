package com.example.webapp.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "students")
public class Student {

    @Id
    private String id;

    private String nim;

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name must contain only letters (a-z, A-Z) and spaces")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email format")
    private String email;

    private String major;

    @NotNull(message = "Batch is required")
    @Min(value = 2010, message = "Batch must be at least 2010")
    @Max(value = 2030, message = "Batch must not exceed 2030")
    private Integer batch;  // Year entered university

    private StudentStatus status;  // ACTIVE, NOT_ACTIVE, DROPOUT
}
