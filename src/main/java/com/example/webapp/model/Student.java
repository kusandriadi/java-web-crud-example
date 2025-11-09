package com.example.webapp.model;

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
    private String name;
    private String email;
    private String major;
    private Integer batch;  // Year entered university
    private StudentStatus status;  // ACTIVE, NOT_ACTIVE, DROPOUT
}
