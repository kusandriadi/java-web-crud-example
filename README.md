# Java Web CRUD Example

A modern web application built with Spring Boot, VueJS, and Bootstrap featuring dual authentication (Form-based & Google OAuth2).

## Tech Stack

- **Backend:** Spring Boot 3.2.1, Spring Security, OAuth2 Client
- **Frontend:** VueJS 3, Bootstrap 5
- **Build Tool:** Maven
- **Authentication:** Form-based Login & Google OAuth2

## Features

- **Dual Authentication System:**
  - Traditional username/password login
  - Google OAuth2 Login
- Responsive Dashboard with Sidebar
- Modern UI with Bootstrap 5
- VueJS 3 for interactive frontend
- RESTful API endpoints
- Secure authentication and session management
- In-memory user management

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Google Cloud Console account (for OAuth2 credentials)

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd java-web-crud-example
```

### 2. Configure Google OAuth2 (Optional - for Google Login)

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable Google+ API
4. Go to **Credentials** → **Create Credentials** → **OAuth 2.0 Client ID**
5. Configure OAuth consent screen
6. Create OAuth 2.0 Client ID:
   - Application type: Web application
   - Authorized redirect URIs: `http://localhost:8080/login/oauth2/code/google`
7. Copy the **Client ID** and **Client Secret**

### 3. Update Application Properties (Optional - for Google Login)

Edit `src/main/resources/application.properties`:

```properties
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
```

Replace `YOUR_GOOGLE_CLIENT_ID` and `YOUR_GOOGLE_CLIENT_SECRET` with your actual credentials.

**Note:** If you skip Google OAuth2 setup, you can still use the form-based login with these credentials:
- Username: `admin` / Password: `admin`
- Username: `user` / Password: `user`

### 4. Build and Run

```bash
# Build the project
mvn clean install

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
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   └── WebController.java
│   │   │   └── WebAppApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   └── style.css
│   │       │   ├── js/
│   │       │   │   └── app.js
│   │       │   ├── index.html
│   │       │   └── login.html
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

## Usage

1. Open browser and navigate to `http://localhost:8080`
2. Choose your preferred login method:
   - **Form Login:** Use username `admin` and password `admin`
   - **Google Login:** Sign in with your Google account
3. You will be redirected to the dashboard

## API Endpoints

- `GET /` - Home page with login options
- `GET /login-form.html` - Form-based login page
- `GET /login.html` - Google OAuth2 login page
- `GET /index` - Dashboard (requires authentication)
- `GET /api/user` - Get current user information (requires authentication)
- `POST /login` - Process form login
- `POST /logout` - Logout

## Features Overview

### Login Options
- **Form-based Login:**
  - Username/password authentication
  - In-memory user storage
  - Remember me functionality
  - Default users: admin/admin, user/user
- **Google OAuth2 Login:**
  - Secure OAuth2 flow
  - Auto-populate user information
  - Profile picture support

### Dashboard
- Responsive sidebar navigation
- Header with user profile dropdown
- Statistics cards
- User information display
- Multiple page sections (Dashboard, Users, Products, Orders, Analytics, Settings)

## Development

### Hot Reload

The project includes Spring Boot DevTools for automatic application restart during development.

### Adding New Features

1. Create new controllers in `src/main/java/com/example/webapp/controller/`
2. Add frontend components in `src/main/resources/static/`
3. Update VueJS app in `js/app.js`
4. Style with custom CSS in `css/style.css`

## Security

- Spring Security with OAuth2 Client
- CSRF protection enabled
- Session management
- Secure authentication flow

## Troubleshooting

### OAuth2 Error
- Verify Client ID and Client Secret in `application.properties`
- Check redirect URI in Google Cloud Console matches `http://localhost:8080/login/oauth2/code/google`

### Port Already in Use
- Change port in `application.properties`: `server.port=8081`

## License

MIT License
