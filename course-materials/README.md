# Pemrograman Web - Course Materials

Complete course materials for Web Programming course (Semester 3).

## Course Overview

This course teaches students how to build a full-stack web application from scratch using:
- **Frontend**: HTML, CSS, Bootstrap 5, JavaScript, Vue.js 3
- **Backend**: Java 21, Spring Boot 3.2.1, MVC Architecture
- **Database**: MongoDB
- **Security**: Spring Security, OAuth2, BCrypt

**Project**: Academic Management System (Student, Subject, Class CRUD)

---

## 📚 Module Structure

### Module 1: Frontend Fundamentals & Modern Web Development
**Duration**: 2-3 weeks

**Topics**:
- Web development basics (Client-Server architecture)
- HTML structure, forms, tables
- CSS styling with Bootstrap 5
- Responsive design
- JavaScript fundamentals (DOM, events, arrays)
- Vue.js 3 reactive framework
- Building login UI with validation

**Files**:
- `module-01-frontend/presentation.html` - Reveal.js presentation
- Screenshots folder for examples
- Lab exercises

**Learning Outcomes**:
- ✅ Understand how the web works
- ✅ Build responsive UIs with Bootstrap
- ✅ Write interactive JavaScript code
- ✅ Create reactive applications with Vue.js

---

### Module 2: Backend Development with Spring Boot & MVC Architecture
**Duration**: 3-4 weeks

**Topics**:
- Java review (classes, annotations, collections)
- Introduction to Spring Boot
- MVC Architecture (Model-View-Controller)
- Layered architecture (Controller-Service-Repository)
- HTTP methods (GET, POST, PUT, DELETE)
- Building RESTful APIs
- Testing with Postman
- Complete CRUD operations

**Files**:
- `module-02-backend-mvc/presentation.html` - Reveal.js presentation
- Code examples for each layer
- Postman collection

**Learning Outcomes**:
- ✅ Create Spring Boot projects
- ✅ Understand and implement MVC architecture
- ✅ Build REST APIs with proper layering
- ✅ Test APIs with Postman

---

### Module 3: Database Integration & Full Stack Connection
**Duration**: 2-3 weeks

**Topics**:
- MongoDB installation (Windows/Mac/Linux)
- NoSQL vs SQL databases
- MongoDB basics (collections, documents)
- Spring Data MongoDB
- Connecting Frontend to Backend
- Fetch API / AJAX
- CORS configuration
- Complete full-stack CRUD
- Relationship management

**Files**:
- `module-03-database-integration/presentation.html`
- MongoDB installation guide (step-by-step)
- Integration examples
- Full-stack code samples

**Learning Outcomes**:
- ✅ Install and configure MongoDB
- ✅ Connect Spring Boot to MongoDB
- ✅ Integrate Vue.js frontend with Spring Boot backend
- ✅ Build complete full-stack application

---

### Module 4: Authentication, Security & Validation
**Duration**: 2-3 weeks

**Topics**:
- Data validation (Frontend & Backend)
- Jakarta Bean Validation
- User authentication concepts
- Form-based login
- Password encryption (BCrypt)
- Spring Security basics
- OAuth2 introduction (Google login)
- Role-Based Access Control (RBAC)
- Session management

**Files**:
- `module-04-authentication-security/presentation.html`
- Authentication code examples
- Security configuration samples
- OAuth2 setup guide

**Learning Outcomes**:
- ✅ Implement frontend and backend validation
- ✅ Build secure login system
- ✅ Encrypt passwords with BCrypt
- ✅ Implement role-based access control

---

### Module 5: Advanced Features, Best Practices & Deployment
**Duration**: 2-3 weeks

**Topics**:
- Code organization best practices
- Auto-generation features (NIM, codes)
- Data relationships
- Data migration & initialization
- Building for production (JAR files)
- Deployment options:
  - Local deployment
  - Cloud deployment (Heroku, AWS, Azure)
  - VPS deployment
- Environment configuration
- Documentation writing
- Final project presentation

**Files**:
- `module-05-advanced-deployment/presentation.html`
- Deployment guides (Heroku, AWS, VPS)
- Best practices checklist
- Final project rubric

**Learning Outcomes**:
- ✅ Write clean, maintainable code
- ✅ Implement advanced features
- ✅ Deploy applications to production
- ✅ Create comprehensive documentation

---

## 🚀 How to Use These Materials

### For Instructors:

1. **Open presentations in browser**:
   ```bash
   # Navigate to module folder
   cd module-01-frontend
   # Open presentation.html in browser
   ```

2. **Presentation controls**:
   - **Arrow keys** / **Space**: Navigate slides
   - **Esc**: Overview mode (see all slides)
   - **F**: Fullscreen
   - **S**: Speaker notes (press 'S' key)
   - **B**: Blackout screen

3. **Customize slides**:
   - Edit `presentation.html` files
   - Add your own screenshots in `assets/images/`
   - Modify code examples as needed

4. **Print to PDF** (optional):
   - Open presentation in browser
   - Add `?print-pdf` to URL
   - Example: `file:///path/presentation.html?print-pdf`
   - Print to PDF using browser

### For Students:

1. **View presentations**:
   - Open `presentation.html` in any modern browser
   - Navigate with arrow keys or spacebar

2. **Lab exercises**:
   - Each module has hands-on labs
   - Follow instructions in presentation
   - Submit code + screenshots

3. **Code examples**:
   - All code is copy-paste ready
   - Test in your own projects
   - Modify and experiment

---

## 📂 Folder Structure

```
course-materials/
├── module-01-frontend/
│   ├── presentation.html
│   ├── lab-exercises/
│   └── code-examples/
├── module-02-backend-mvc/
│   ├── presentation.html
│   ├── lab-exercises/
│   └── code-examples/
├── module-03-database-integration/
│   ├── presentation.html
│   ├── mongodb-installation-guide.md
│   └── code-examples/
├── module-04-authentication-security/
│   ├── presentation.html
│   ├── oauth2-setup-guide.md
│   └── code-examples/
├── module-05-advanced-deployment/
│   ├── presentation.html
│   ├── deployment-guides/
│   │   ├── heroku-deployment.md
│   │   ├── aws-deployment.md
│   │   └── vps-deployment.md
│   └── code-examples/
├── assets/
│   ├── images/          # Screenshots, diagrams
│   └── code-examples/   # Reusable code snippets
└── README.md
```

---

## 📋 Assessment & Grading

### Module Labs (40%)
- Module 1 Lab: Student Management UI (10%)
- Module 2 Lab: Subject Backend API (10%)
- Module 3 Lab: Full-stack Integration (10%)
- Module 4 Lab: Authentication System (10%)

### Quizzes (15%)
- 5 quizzes (3% each)
- Multiple choice + short answer
- Covers theory and concepts

### Mid-term Project (20%)
- Modules 1-3 combined
- Working full-stack CRUD application
- Presentation required

### Final Project (25%)
- Complete application with all features
- Deployed to cloud/server
- Documentation + presentation
- Individual or group (max 3 students)

---

## 💻 Required Software

Students must install before Module 1:

1. **Java 21 JDK**
   - Download: https://www.oracle.com/java/technologies/downloads/
   - Or use OpenJDK: https://adoptium.net/

2. **Maven 3.6+**
   - Download: https://maven.apache.org/download.cgi
   - Verify: `mvn -version`

3. **MongoDB Community Server**
   - Download: https://www.mongodb.com/try/download/community
   - Verify: `mongod --version`

4. **IDE (choose one)**:
   - IntelliJ IDEA Community (recommended): https://www.jetbrains.com/idea/download/
   - Eclipse IDE: https://www.eclipse.org/downloads/
   - VS Code with Java extensions: https://code.visualstudio.com/

5. **Postman**
   - Download: https://www.postman.com/downloads/
   - Or use web version

6. **Git** (optional but recommended)
   - Download: https://git-scm.com/downloads

7. **MongoDB Compass** (GUI for MongoDB)
   - Included with MongoDB installation
   - Or download separately: https://www.mongodb.com/try/download/compass

8. **Modern Web Browser**
   - Chrome, Firefox, or Edge (latest version)

---

## 🎯 Learning Path

Recommended weekly schedule (14-week semester):

| Week | Module | Topics | Deliverables |
|------|--------|--------|--------------|
| 1-2 | Module 1 | Frontend basics, HTML/CSS | Lab 1: Login UI |
| 3 | Module 1 | JavaScript, Vue.js | Lab 1: Student List UI |
| 4-5 | Module 2 | Spring Boot, MVC | Lab 2: Backend API |
| 6-7 | Module 2 | HTTP, REST APIs | Lab 2: Complete CRUD |
| 8 | Module 3 | MongoDB, Integration | **Mid-term Project** |
| 9-10 | Module 3 | Full-stack connection | Lab 3: Full-stack app |
| 11-12 | Module 4 | Authentication, Security | Lab 4: Login system |
| 13-14 | Module 5 | Deployment, Best practices | **Final Project** |

---

## 🔧 Troubleshooting

### Presentations not loading?
- Ensure you have internet connection (uses CDN for reveal.js)
- Try different browser
- Check browser console for errors (F12)

### Code examples not working?
- Check Java version: `java -version` (should be 21)
- Check Maven: `mvn -version`
- Ensure MongoDB is running: `mongod --version`
- Check port 8080 is not in use

### MongoDB connection issues?
- Verify MongoDB is running: `mongod`
- Check `application.properties` configuration
- Default port is 27017

---

## 📞 Support

**Instructor Contact**:
- Email: instructor@university.edu
- Office Hours: Monday & Wednesday, 2-4 PM
- Lab Sessions: Friday, 1-4 PM

**Course Resources**:
- Course GitHub: [repository-url]
- Discussion Forum: [forum-url]
- Slides & Videos: [lms-url]

---

## 📄 License

These course materials are created for educational purposes.

**Attribution**: Based on the Java Web CRUD Example project

**Usage**:
- ✅ Use for teaching
- ✅ Modify for your courses
- ✅ Share with students
- ❌ Commercial use without permission

---

## 🎓 About This Course

**Course Code**: IF-304
**Course Name**: Pemrograman Web (Web Programming)
**Semester**: 3 (Third Semester)
**Credits**: 3 SKS
**Prerequisites**:
- Algoritma & Pemrograman
- Basis Data (Database)
- Pemrograman Berorientasi Objek

**Course Objectives**:
By the end of this course, students will be able to design, develop, and deploy full-stack web applications using modern technologies and best practices.

---

**Last Updated**: November 10, 2025
**Version**: 1.0
