# Initial Data Files

This folder contains JSON files for initializing the database with sample data.

## Files

### `students.json`
Contains initial student data with the following structure:
```json
{
  "nim": "1020200001",          // Student ID (format: AABBBBCCCC)
  "name": "Andi Pratama",       // Full name
  "email": "andi.pratama@student.ac.id",
  "major": "Sistem Informasi",  // Major
  "batch": 2020,                // Year entered university
  "status": "ACTIVE"            // Status: ACTIVE, NOT_ACTIVE, or DROPOUT
}
```

### `subjects.json`
Contains initial subject data with the following structure:
```json
{
  "code": "SI001",              // Subject code
  "name": "Basis Data",         // Subject name
  "sks": 3                      // Credit hours (1-6)
}
```

### `classes.json`
Contains initial class data with the following structure:
```json
{
  "code": "KLS001",                     // Class code (auto-generated if not provided)
  "name": "Basis Data - 2024 - Ganjil", // Class name (format: Subject - Year - Semester)
  "subjectId": null,                    // Leave as null, will be matched by subjectName
  "subjectName": "Basis Data",          // Subject name (must match exactly with subjects.json)
  "semester": "Ganjil",                 // Semester: "Ganjil" or "Genap"
  "year": 2024,                         // Academic year
  "studentNims": ["1020200001", "1020200002"]  // List of student NIMs to enroll
}
```

## How to Use

### 1. Editing Data
Simply edit the JSON files to add, modify, or remove data:
- Add more students to `students.json`
- Add more subjects to `subjects.json`
- Add more classes to `classes.json`
- Modify existing entries

### 2. NIM Format
Student NIM follows this format: **AABBBBCCCC**
- **AA**: Major code
  - `10` = Sistem Informasi
  - `11` = Teknologi Informasi
- **BBBB**: Batch year (e.g., 2024)
- **CCCC**: Order number (e.g., 0001, 0002)

Example: `1020240001`
- Major: Sistem Informasi (10)
- Batch: 2024
- Order: 0001

### 3. Valid Student Status
- `ACTIVE`: Currently studying
- `NOT_ACTIVE`: Temporarily not studying
- `DROPOUT`: Dropped out

### 4. Data Loading
Data is automatically loaded when:
- The application starts
- The database collections are empty

Loading order:
1. Students are loaded first
2. Subjects are loaded second
3. Classes are loaded last (requires students and subjects to exist)

To reload data:
1. Drop/delete the MongoDB collections (students, subjects, classes)
2. Restart the application

## Tips for Students

1. **Practice JSON**: Learn JSON format by editing these files
2. **Add More Data**: Add your own sample data
3. **Test Scenarios**: Create different test scenarios (e.g., all active students, mixed statuses)
4. **Version Control**: Track changes to see how data evolves

## Example: Adding a New Student

```json
{
  "nim": "1120240005",
  "name": "Your Name Here",
  "email": "your.name@student.ac.id",
  "major": "Teknologi Informasi",
  "batch": 2024,
  "status": "ACTIVE"
}
```

Don't forget to add a comma after the previous entry!

## Example: Adding a New Class

```json
{
  "code": "KLS011",
  "name": "Struktur Data - 2024 - Ganjil",
  "subjectId": null,
  "subjectName": "Struktur Data",
  "semester": "Ganjil",
  "year": 2024,
  "studentNims": ["1020240001", "1120240001"]
}
```

**Notes:**
- The `subjectName` must match exactly with a subject in `subjects.json`
- The `studentNims` array should contain valid NIMs from `students.json`
- Students will be automatically enrolled when the class is loaded
