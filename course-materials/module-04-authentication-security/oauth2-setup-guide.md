# OAuth2 Setup Guide

Complete guide for implementing OAuth2 login with Google in Spring Boot.

---

## Table of Contents

1. [What is OAuth2?](#what-is-oauth2)
2. [Google OAuth2 Setup](#google-oauth2-setup)
3. [Spring Boot Configuration](#spring-boot-configuration)
4. [Frontend Integration](#frontend-integration)
5. [Advanced Configuration](#advanced-configuration)
6. [Troubleshooting](#troubleshooting)

---

## What is OAuth2?

### Overview

**OAuth 2.0** (Open Authorization) is an authorization framework that enables applications to obtain limited access to user accounts on an HTTP service.

**Benefits**:
- Users don't need to create new passwords
- More secure (Google handles authentication)
- Faster registration process
- Users trust established providers
- Social features (access to profile, email, etc.)

### OAuth2 Flow

```
┌──────────────┐                                  ┌──────────────┐
│   Browser    │                                  │  Your App    │
│              │                                  │ (Spring Boot)│
└──────────────┘                                  └──────────────┘
       │                                                 │
       │  1. Click "Login with Google"                  │
       │────────────────────────────────────────────────>│
       │                                                 │
       │  2. Redirect to Google                         │
       │<────────────────────────────────────────────────│
       │                                                 │
       ▼                                                 │
┌──────────────┐                                        │
│    Google    │                                        │
│   Login      │                                        │
└──────────────┘                                        │
       │                                                 │
       │  3. User logs in with Google                   │
       │                                                 │
       │  4. Google redirects with code                 │
       │─────────────────────────────────────────────────>│
       │                                                 │
       │                                  5. Exchange    │
       │                                     code for    │
       │                                     token with  │
       │                                     Google      │
       │                                                 │
       │  6. Send user info back                        │
       │<────────────────────────────────────────────────│
       │                                                 │
       │  7. User is logged in!                         │
       │                                                 │
```

---

## Google OAuth2 Setup

### Step 1: Create Google Cloud Project

1. Go to: https://console.cloud.google.com/
2. Click **Select a project** → **New Project**
3. Enter project name: "Academic Management System"
4. Click **Create**
5. Wait for project to be created

### Step 2: Enable Google+ API

1. In the search bar, type "Google+ API"
2. Click **Google+ API**
3. Click **Enable**
4. Wait for API to be enabled

### Step 3: Configure OAuth Consent Screen

1. Go to **APIs & Services** → **OAuth consent screen**
2. Select **External** (for testing)
3. Click **Create**
4. Fill in the form:
   - **App name**: Academic Management System
   - **User support email**: your email
   - **Developer contact email**: your email
5. Click **Save and Continue**
6. **Scopes**: Click **Add or Remove Scopes**
   - Select: `email`, `profile`, `openid`
   - Click **Update**
   - Click **Save and Continue**
7. **Test users** (for External apps):
   - Click **Add Users**
   - Add your email and test user emails
   - Click **Add**
   - Click **Save and Continue**
8. Click **Back to Dashboard**

### Step 4: Create OAuth 2.0 Credentials

1. Go to **APIs & Services** → **Credentials**
2. Click **Create Credentials** → **OAuth client ID**
3. Application type: **Web application**
4. Name: "Academic System Web Client"
5. **Authorized JavaScript origins**:
   ```
   http://localhost:8080
   ```
6. **Authorized redirect URIs**:
   ```
   http://localhost:8080/login/oauth2/code/google
   ```
7. Click **Create**
8. **IMPORTANT**: Copy the following:
   - **Client ID**: `123456789-abc...xyz.apps.googleusercontent.com`
   - **Client Secret**: `GOCSPX-abc...xyz`
9. Click **OK**

### Step 5: Save Credentials Securely

**DO NOT commit credentials to Git!**

Create `.env` file or use environment variables:
```properties
GOOGLE_CLIENT_ID=your-client-id-here
GOOGLE_CLIENT_SECRET=your-client-secret-here
```

---

## Spring Boot Configuration

### Step 1: Add Dependencies

**pom.xml**:
```xml
<dependencies>
    <!-- Spring Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- OAuth2 Client -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-oauth2-client</artifactId>
    </dependency>

    <!-- Spring Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

### Step 2: Configure application.properties

**src/main/resources/application.properties**:
```properties
# Google OAuth2 Configuration
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.scope=profile,email
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/login/oauth2/code/{registrationId}

# Optional: Custom authorization endpoint
spring.security.oauth2.client.provider.google.authorization-uri=https://accounts.google.com/o/oauth2/v2/auth
spring.security.oauth2.client.provider.google.token-uri=https://oauth2.googleapis.com/token
spring.security.oauth2.client.provider.google.user-info-uri=https://www.googleapis.com/oauth2/v3/userinfo
spring.security.oauth2.client.provider.google.user-name-attribute=sub
```

**Or use YAML format** (application.yml):
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
            scope:
              - profile
              - email
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
```

### Step 3: Create Security Configuration

**SecurityConfig.java**:
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/error", "/webjars/**", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
            );

        return http.build();
    }
}
```

### Step 4: Create Login Controller

**LoginController.java**:
```java
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}
```

### Step 5: Create REST Controller for User Info

**UserInfoController.java**:
```java
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserInfoController {

    @GetMapping("/info")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> userInfo = new HashMap<>();

        if (principal != null) {
            userInfo.put("name", principal.getAttribute("name"));
            userInfo.put("email", principal.getAttribute("email"));
            userInfo.put("picture", principal.getAttribute("picture"));
            userInfo.put("sub", principal.getAttribute("sub"));  // Google user ID
            userInfo.put("authenticated", true);
        } else {
            userInfo.put("authenticated", false);
        }

        return userInfo;
    }

    @GetMapping("/profile")
    public Map<String, Object> getUserProfile(@AuthenticationPrincipal OAuth2User principal) {
        return principal.getAttributes();
    }
}
```

### Step 6: Create Custom OAuth2 User Service (Optional)

If you want to save OAuth2 users to your database:

**CustomOAuth2UserService.java**:
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        // Extract user information
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");

        // Check if user exists in database
        User user = userRepository.findByEmail(email)
            .orElseGet(() -> {
                // Create new user
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName(name);
                newUser.setPicture(picture);
                newUser.setProvider("GOOGLE");
                newUser.setRole("USER");
                return userRepository.save(newUser);
            });

        return oauth2User;
    }
}
```

Update SecurityConfig:
```java
@Autowired
private CustomOAuth2UserService customOAuth2UserService;

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // ... other config
        .oauth2Login(oauth2 -> oauth2
            .loginPage("/login")
            .userInfoEndpoint(userInfo -> userInfo
                .userService(customOAuth2UserService)
            )
            .defaultSuccessUrl("/dashboard", true)
        );
    return http.build();
}
```

---

## Frontend Integration

### Option 1: Server-Side Rendered (Thymeleaf)

**login.html** (Thymeleaf template):
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Login</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-4">
                <h2>Login</h2>

                <div th:if="${param.error}" class="alert alert-danger">
                    Invalid credentials or authentication failed
                </div>

                <!-- Traditional login form -->
                <form th:action="@{/login}" method="post">
                    <div class="mb-3">
                        <input type="text" name="username" class="form-control" placeholder="Username" required>
                    </div>
                    <div class="mb-3">
                        <input type="password" name="password" class="form-control" placeholder="Password" required>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Login</button>
                </form>

                <hr>

                <!-- Google Login -->
                <a href="/oauth2/authorization/google" class="btn btn-danger w-100">
                    <i class="bi bi-google"></i> Login with Google
                </a>

                <p class="mt-3">
                    Don't have an account? <a href="/register">Register here</a>
                </p>
            </div>
        </div>
    </div>
</body>
</html>
```

**dashboard.html**:
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
<head>
    <title>Dashboard</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h1>Dashboard</h1>

        <div class="card">
            <div class="card-body">
                <h5>Welcome, <span sec:authentication="principal.attributes['name']">User</span>!</h5>
                <p>Email: <span sec:authentication="principal.attributes['email']">email@example.com</span></p>
                <img sec:src="${#authentication.principal.attributes['picture']}" alt="Profile" class="rounded-circle" width="100">
            </div>
        </div>

        <a href="/logout" class="btn btn-secondary mt-3">Logout</a>
    </div>
</body>
</html>
```

### Option 2: Client-Side (Vue.js/Vanilla JS)

**login.html**:
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-4">
                <h2>Login</h2>

                <!-- Google Login Button -->
                <a href="http://localhost:8080/oauth2/authorization/google" class="btn btn-danger w-100">
                    Login with Google
                </a>
            </div>
        </div>
    </div>
</body>
</html>
```

**dashboard.html** (with Vue.js):
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://unpkg.com/vue@3"></script>
</head>
<body>
    <div id="app" class="container mt-5">
        <div v-if="loading">Loading...</div>
        <div v-else-if="user">
            <h1>Dashboard</h1>
            <div class="card">
                <div class="card-body">
                    <img :src="user.picture" alt="Profile" class="rounded-circle" width="100">
                    <h5>Welcome, {{ user.name }}!</h5>
                    <p>Email: {{ user.email }}</p>
                </div>
            </div>
            <a href="http://localhost:8080/logout" class="btn btn-secondary mt-3">Logout</a>
        </div>
        <div v-else>
            <p>Not authenticated. <a href="/login.html">Login here</a></p>
        </div>
    </div>

    <script>
        const app = Vue.createApp({
            data() {
                return {
                    user: null,
                    loading: true
                };
            },
            methods: {
                async fetchUserInfo() {
                    try {
                        const response = await fetch('http://localhost:8080/api/user/info', {
                            credentials: 'include'
                        });
                        if (response.ok) {
                            const data = await response.json();
                            if (data.authenticated) {
                                this.user = data;
                            }
                        }
                    } catch (error) {
                        console.error('Error fetching user info:', error);
                    } finally {
                        this.loading = false;
                    }
                }
            },
            mounted() {
                this.fetchUserInfo();
            }
        });
        app.mount('#app');
    </script>
</body>
</html>
```

---

## Advanced Configuration

### Multiple OAuth2 Providers

**application.properties**:
```properties
# Google
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.scope=profile,email

# GitHub
spring.security.oauth2.client.registration.github.client-id=${GITHUB_CLIENT_ID}
spring.security.oauth2.client.registration.github.client-secret=${GITHUB_CLIENT_SECRET}
spring.security.oauth2.client.registration.github.scope=user:email

# Facebook
spring.security.oauth2.client.registration.facebook.client-id=${FACEBOOK_CLIENT_ID}
spring.security.oauth2.client.registration.facebook.client-secret=${FACEBOOK_CLIENT_SECRET}
spring.security.oauth2.client.registration.facebook.scope=email,public_profile
```

**Login page with multiple providers**:
```html
<a href="/oauth2/authorization/google" class="btn btn-danger">
    Login with Google
</a>
<a href="/oauth2/authorization/github" class="btn btn-dark">
    Login with GitHub
</a>
<a href="/oauth2/authorization/facebook" class="btn btn-primary">
    Login with Facebook
</a>
```

### Custom Success Handler

**OAuth2LoginSuccessHandler.java**:
```java
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // Custom logic after successful OAuth2 login
        System.out.println("User logged in via OAuth2: " + authentication.getName());

        // Redirect to dashboard
        response.sendRedirect("/dashboard");
    }
}
```

Update SecurityConfig:
```java
@Autowired
private OAuth2LoginSuccessHandler successHandler;

.oauth2Login(oauth2 -> oauth2
    .loginPage("/login")
    .successHandler(successHandler)
)
```

---

## Troubleshooting

### Issue 1: "redirect_uri_mismatch" Error

**Cause**: Redirect URI in Google Console doesn't match Spring Boot

**Solution**:
1. Check Google Console redirect URI: `http://localhost:8080/login/oauth2/code/google`
2. Ensure Spring Boot is running on port 8080
3. Restart Spring Boot after changing configuration

### Issue 2: "Invalid client" Error

**Cause**: Incorrect Client ID or Client Secret

**Solution**:
1. Double-check credentials in Google Console
2. Ensure no extra spaces in application.properties
3. Restart Spring Boot

### Issue 3: "Access blocked: This app's request is invalid"

**Cause**: OAuth consent screen not configured or app not verified

**Solution**:
1. Complete OAuth consent screen configuration
2. Add your email as test user
3. For production, submit app for verification

### Issue 4: User info not accessible

**Cause**: Missing scopes

**Solution**:
Add required scopes in application.properties:
```properties
spring.security.oauth2.client.registration.google.scope=profile,email,openid
```

### Issue 5: CORS errors

**Cause**: Frontend and backend on different origins

**Solution**:
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:5500", "http://127.0.0.1:5500")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowCredentials(true);
    }
}
```

---

## Security Best Practices

1. **Never commit credentials**:
   - Use environment variables
   - Add `.env` to `.gitignore`

2. **Use HTTPS in production**:
   ```properties
   server.servlet.session.cookie.secure=true
   ```

3. **Validate redirect URIs**:
   - Only whitelist your actual domains

4. **Implement CSRF protection** (if using cookies):
   ```java
   .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
   ```

5. **Set session timeout**:
   ```properties
   server.servlet.session.timeout=30m
   ```

---

## Next Steps

After successful OAuth2 setup:

1. Test login flow with multiple users
2. Implement role-based access control
3. Add profile editing
4. Implement account linking (traditional + OAuth2)
5. Add more OAuth2 providers (GitHub, Facebook)

---

## Additional Resources

- **Google OAuth2 Documentation**: https://developers.google.com/identity/protocols/oauth2
- **Spring Security OAuth2**: https://spring.io/guides/tutorials/spring-boot-oauth2/
- **OAuth 2.0 Simplified**: https://www.oauth.com/
- **RFC 6749 (OAuth 2.0)**: https://datatracker.ietf.org/doc/html/rfc6749

---

**Last Updated**: November 10, 2025
**Spring Boot Version**: 3.2.x
**Status**: Production-ready
