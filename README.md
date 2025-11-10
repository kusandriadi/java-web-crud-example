# Java Web CRUD Application - Academic Management System

A comprehensive web application for managing academic data (Students, Subjects, and Classes) built with Spring Boot, VueJS, MongoDB, and Bootstrap featuring dual authentication (Form-based & Google OAuth2) and role-based access control.

## Tech Stack

- **Backend:** Spring Boot 3.2.1, Spring Security, OAuth2 Client, Spring Data MongoDB
- **Frontend:** VueJS 3, Bootstrap 5, Font Awesome
- **Database:** MongoDB
- **Build Tool:** Maven
- **Java Version:** Java 21
- **Authentication:** Form-based Login with BCrypt & Google OAuth2
- **Validation:** Jakarta Bean Validation

## Features

### Authentication & Authorization
- **Dual Authentication System:**
  - Traditional username/password login with BCrypt password encryption
  - Google OAuth2 Login
- **Role-Based Access Control:**
  - Admin role: Full CRUD operations on all entities
  - User role: Read-only access
- Secure session management

### Academic Management

#### Student Management
- ✅ Complete CRUD operations (Create, Read, Update, Delete)
- ✅ Auto-generated NIM (Student ID) based on major and batch
  - Format: `XXYYYY###` (e.g., SI2020001, TI2021002)
  - XX: SI (Sistem Informasi) or TI (Teknologi Informasi)
  - YYYY: Batch year (2010-2030)
  - ###: Sequential number
- ✅ Student attributes:
  - NIM (auto-generated, readonly in edit mode)
  - Name (alphabetic characters only)
  - Email (validated email format)
  - Major (Sistem Informasi / Teknologi Informasi)
  - Batch year (2010-2030, readonly in edit mode)
  - Status (ACTIVE, NOT_ACTIVE, DROPOUT)
- ✅ Server-side validation for all fields

#### Subject Management
- ✅ Complete CRUD operations
- ✅ Auto-generated subject code based on major
  - Format: `XXYYY` (e.g., SI001, TI002)
  - XX: SI (Sistem Informasi) or TI (Teknologi Informasi)
  - YYY: Sequential number per major
- ✅ Subject attributes:
  - Code (auto-generated)
  - Name (alphabetic characters only)
  - Major (Sistem Informasi / Teknologi Informasi)
  - SKS (Credits: 1-6)
- ✅ Server-side validation

#### Class Management
- ✅ Complete CRUD operations
- ✅ Auto-generated class code and name
  - Format: `XXYYYYS###` for code
  - Name: `[Subject Name] - [Year] - [Semester]`
  - XX: Subject code prefix (SI/TI)
  - YYYY: Year
  - S: Semester (G/E for Ganjil/Genap)
  - ###: Sequential number
- ✅ Student enrollment management
  - Add students to classes
  - Remove students from classes
  - Real-time student count updates
- ✅ Class attributes:
  - Code (auto-generated)
  - Name (auto-generated)
  - Subject
  - Semester (Ganjil/Genap)
  - Year
  - Enrolled students list

### Dashboard & Statistics
- 📊 Student statistics by major (SI/TI):
  - Total students per major
  - Active students count
  - Not active students count
  - Total students (all majors)
- 📊 Subject statistics by major:
  - Total subjects for SI
  - Total subjects for TI
  - Total subjects (all majors)
- 📊 Real-time data updates

### User Interface
- Responsive modern design with Bootstrap 5
- Collapsible sidebar navigation
- Interactive modals for CRUD operations
- Custom notification and confirmation modals
- Form validation with user-friendly error messages
- Vue.js reactivity for instant UI updates

## Prerequisites

- **Java 21** or higher
- **Maven 3.6+**
- **MongoDB 4.0+** (running on localhost:27017 or configured server)
- Google Cloud Console account (optional, for OAuth2 credentials)

## Setup Instructions

### 1. Install MongoDB

Download and install MongoDB from [https://www.mongodb.com/try/download/community](https://www.mongodb.com/try/download/community)

Start MongoDB service:
```bash
# Windows
net start MongoDB

# Linux/Mac
sudo systemctl start mongod
```

### 2. Clone the Repository

```bash
git clone <your-repository-url>
cd java-web-crud-example
```

### 3. Configure MongoDB Connection

Edit `src/main/resources/application.properties`:

```properties
# MongoDB Configuration
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=academic_db
spring.data.mongodb.username=your_username (optional)
spring.data.mongodb.password=your_password (optional)
```

### 4. Configure Google OAuth2 (Optional - for Google Login)

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable Google+ API
4. Go to **Credentials** → **Create Credentials** → **OAuth 2.0 Client ID**
5. Configure OAuth consent screen
6. Create OAuth 2.0 Client ID:
   - Application type: Web application
   - Authorized redirect URIs: `http://localhost:8080/login/oauth2/code/google`
7. Copy the **Client ID** and **Client Secret**
8. Update `application.properties`:

```properties
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
```

**Note:** If you skip Google OAuth2 setup, you can still use the form-based login with default credentials:
- Username: `admin` / Password: `admin` (Admin role)
- Username: `user` / Password: `user` (User role)

### 5. Build and Run

```bash
# Build the project
mvn clean package

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Project Structure

```
java-web-crud-example/
├── src/
│   ├── main/
│   │   ├── java/com/example/webapp/
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java           # Security configuration
│   │   │   ├── controller/
│   │   │   │   ├── WebController.java            # Web pages controller
│   │   │   │   ├── StudentController.java        # Student REST API
│   │   │   │   ├── SubjectController.java        # Subject REST API
│   │   │   │   └── ClassController.java          # Class REST API
│   │   │   ├── model/
│   │   │   │   ├── Student.java                  # Student entity
│   │   │   │   ├── Subject.java                  # Subject entity
│   │   │   │   ├── ClassRoom.java                # Class entity
│   │   │   │   ├── StudentStatus.java            # Student status enum
│   │   │   │   └── User.java                     # User entity
│   │   │   ├── repository/
│   │   │   │   ├── StudentRepository.java        # Student MongoDB repository
│   │   │   │   ├── SubjectRepository.java        # Subject MongoDB repository
│   │   │   │   ├── ClassRepository.java          # Class MongoDB repository
│   │   │   │   └── UserRepository.java           # User MongoDB repository
│   │   │   ├── service/
│   │   │   │   ├── StudentService.java           # Student business logic
│   │   │   │   ├── SubjectService.java           # Subject business logic
│   │   │   │   ├── ClassService.java             # Class business logic
│   │   │   │   └── (Impl classes)
│   │   │   └── WebAppApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   └── style.css                 # Custom styles
│   │       │   ├── js/
│   │       │   │   ├── app.js                    # Main Vue.js application
│   │       │   │   ├── studentModule.js          # Student module
│   │       │   │   ├── subjectModule.js          # Subject module
│   │       │   │   ├── classModule.js            # Class module
│   │       │   │   └── dashboardModule.js        # Dashboard module
│   │       │   ├── index.html                    # Main dashboard
│   │       │   ├── login.html                    # Google OAuth2 login
│   │       │   └── login-form.html               # Form-based login
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

## API Endpoints

### Authentication
- `GET /` - Home page with login options
- `GET /login-form.html` - Form-based login page
- `GET /login.html` - Google OAuth2 login page
- `GET /index` - Dashboard (requires authentication)
- `GET /api/user` - Get current user information (requires authentication)
- `POST /logout` - Logout

### Student API
- `GET /api/students` - Get all students
- `GET /api/students/{id}` - Get student by ID
- `POST /api/students` - Create new student (Admin only)
- `PUT /api/students/{id}` - Update student (Admin only)
- `DELETE /api/students/{id}` - Delete student (Admin only)
- `GET /api/students/statistics` - Get student statistics
- `GET /api/students/major-options` - Get available major options

### Subject API
- `GET /api/subjects` - Get all subjects
- `GET /api/subjects/{id}` - Get subject by ID
- `POST /api/subjects` - Create new subject (Admin only)
- `PUT /api/subjects/{id}` - Update subject (Admin only)
- `DELETE /api/subjects/{id}` - Delete subject (Admin only)

### Class API
- `GET /api/classes` - Get all classes
- `GET /api/classes/{id}` - Get class by ID
- `POST /api/classes` - Create new class (Admin only)
- `PUT /api/classes/{id}` - Update class (Admin only)
- `DELETE /api/classes/{id}` - Delete class (Admin only)
- `POST /api/classes/{classId}/students/{studentId}` - Add student to class (Admin only)
- `DELETE /api/classes/{classId}/students/{studentId}` - Remove student from class (Admin only)

## Validation Rules

### Student
- **Name**: Required, alphabetic characters and spaces only
- **Email**: Required, valid email format
- **Major**: Required, must be "Sistem Informasi" or "Teknologi Informasi"
- **Batch**: Required, integer between 2010-2030 (readonly in edit mode)
- **Status**: Required, must be ACTIVE, NOT_ACTIVE, or DROPOUT

### Subject
- **Name**: Required, alphabetic characters and spaces only
- **Major**: Required, must be "Sistem Informasi" or "Teknologi Informasi"
- **SKS**: Required, integer between 1-6

### Class
- **Subject**: Required
- **Year**: Required, integer between 2020-2030
- **Semester**: Required, must be "Ganjil" or "Genap"

## Auto-Generation Features

### Student NIM (Student ID)
- Automatically generated on student creation
- Format: `XXYYYY###`
- Example: `SI2020001`, `TI2021002`
- Components:
  - `XX`: Major prefix (SI or TI)
  - `YYYY`: Batch year
  - `###`: Sequential number

### Subject Code
- Automatically generated on subject creation
- Format: `XXYYY`
- Example: `SI001`, `TI002`
- Regenerated if major changes during update
- Components:
  - `XX`: Major prefix (SI or TI)
  - `YYY`: Sequential number per major

### Class Code and Name
- Automatically generated on class creation
- Code format: `XXYYYYS###`
- Name format: `[Subject Name] - [Year] - [Semester]`
- Example:
  - Code: `SI2024G001`
  - Name: `Database Systems - 2024 - Ganjil`

## Usage

1. Open browser and navigate to `http://localhost:8080`
2. Choose your preferred login method:
   - **Form Login:** Use username `admin` and password `admin` (Admin role)
   - **Google Login:** Sign in with your Google account
3. You will be redirected to the dashboard

### User Roles

**Admin Role:**
- Full access to all CRUD operations
- Can manage students, subjects, and classes
- Can add/remove students from classes

**User Role:**
- Read-only access
- Can view all data but cannot modify

## Development

### Hot Reload

The project includes Spring Boot DevTools for automatic application restart during development.

### Modular JavaScript Architecture

The frontend is organized into modules:
- `app.js` - Main Vue.js application and state management
- `studentModule.js` - Student-related operations
- `subjectModule.js` - Subject-related operations
- `classModule.js` - Class and enrollment management
- `dashboardModule.js` - Dashboard statistics

### Adding New Features

1. **Backend:**
   - Create entities in `model/`
   - Create repositories in `repository/`
   - Implement services in `service/`
   - Create controllers in `controller/`

2. **Frontend:**
   - Add new modules in `static/js/`
   - Update Vue.js app in `app.js`
   - Add UI components in `index.html`
   - Style with custom CSS in `style.css`

## Security Features

- Spring Security with OAuth2 Client
- BCrypt password encryption
- Role-based access control (Admin/User)
- CSRF protection enabled
- Session management
- Secure authentication flow
- Server-side input validation
- Protection against XSS and injection attacks

## Database Initialization

The application includes data initializers that run on startup:
- Creates default admin and user accounts
- Initializes sample students (10 students across both majors)
- Initializes sample subjects (30 subjects total):
  - 15 Sistem Informasi subjects (SI001-SI015)
  - 15 Teknologi Informasi subjects (TI001-TI015)
- Initializes sample classes (2 classes)

Initial data is loaded from JSON files in `src/main/resources/data/`:
- `students.json` - Sample student data
- `subjects.json` - Subject data with code, name, major, and SKS
- `classes.json` - Sample class data

You can modify or disable these in the `config/DataInitializer.java` file.

### Automatic Migrations

The application includes automatic data migration features that run on startup:

**Subject Major Field Migration:**
- Automatically detects subjects without the `major` field
- Assigns major based on subject code prefix:
  - Codes starting with "SI" → Sistem Informasi
  - Codes starting with "TI" → Teknologi Informasi
- Only updates subjects that need migration
- Logs migration status on startup

This ensures backward compatibility when upgrading from older versions of the application.

## Troubleshooting

### MongoDB Connection Error
- Verify MongoDB is running: `mongod --version`
- Check connection settings in `application.properties`
- Ensure database name is correct

### OAuth2 Error
- Verify Client ID and Client Secret in `application.properties`
- Check redirect URI in Google Cloud Console matches `http://localhost:8080/login/oauth2/code/google`

### Port Already in Use
- Change port in `application.properties`: `server.port=8081`

### Build Errors
- Ensure Java 21 is installed: `java -version`
- Ensure Maven is installed: `mvn -version`
- Clean and rebuild: `mvn clean install`

## Technologies Used

- **Spring Boot 3.2.1** - Application framework
- **Spring Security** - Authentication and authorization
- **Spring Data MongoDB** - Database operations
- **Spring Validation** - Input validation
- **Vue.js 3** - Frontend framework
- **Bootstrap 5** - UI components
- **Font Awesome 6** - Icons
- **MongoDB** - NoSQL database
- **BCrypt** - Password encryption
- **OAuth2** - Google authentication
- **Lombok** - Reduce boilerplate code
- **Maven** - Build automation

## License

MIT License
