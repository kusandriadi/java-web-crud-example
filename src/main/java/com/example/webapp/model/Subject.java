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
@Document(collection = "subjects")
public class Subject {

    @Id
    private String id;

    private String code;  // Auto-generated based on major (XXYYY format)

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name must contain only letters (a-z, A-Z) and spaces")
    private String name;

    @NotBlank(message = "Major is required")
    private String major;  // Sistem Informasi or Teknologi Informasi

    @NotNull(message = "SKS is required")
    @Min(value = 1, message = "SKS must be at least 1")
    @Max(value = 6, message = "SKS must not exceed 6")
    private Integer sks;
}
