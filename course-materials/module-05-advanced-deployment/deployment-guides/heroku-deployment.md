# Heroku Deployment Guide

Complete guide for deploying Spring Boot application to Heroku.

---

## Prerequisites

- Heroku account (free): https://signup.heroku.com/
- Git installed
- Heroku CLI installed
- Spring Boot application ready

---

## Step 1: Install Heroku CLI

### Windows

**Option 1: Direct Download**
- Download: https://devcenter.heroku.com/articles/heroku-cli
- Run installer

**Option 2: Chocolatey**
```bash
choco install heroku-cli
```

### macOS

```bash
brew tap heroku/brew && brew install heroku
```

### Linux

```bash
curl https://cli-assets.heroku.com/install.sh | sh
```

**Verify installation**:
```bash
heroku --version
# heroku/8.x.x
```

---

## Step 2: Login to Heroku

```bash
heroku login
```

This will open a browser window. Login with your Heroku credentials.

---

## Step 3: Prepare Spring Boot Application

### 1. Create Procfile

Create `Procfile` in project root (no file extension):

```
web: java -jar target/*.jar --server.port=$PORT
```

### 2. Create system.properties

Create `system.properties` in project root:

```properties
java.runtime.version=21
```

### 3. Update application.properties

```properties
# Use environment variable for port
server.port=${PORT:8080}

# MongoDB connection (will use Heroku addon)
spring.data.mongodb.uri=${MONGODB_URI:mongodb://localhost:27017/academic}

# Production settings
spring.profiles.active=${SPRING_PROFILES_ACTIVE:prod}
```

### 4. Ensure Maven wrapper exists

```bash
mvn -N io.takari:maven:wrapper
```

---

## Step 4: Initialize Git Repository

If not already a Git repository:

```bash
git init
git add .
git commit -m "Initial commit"
```

---

## Step 5: Create Heroku App

```bash
# Create app with random name
heroku create

# Or create with specific name
heroku create academic-system-demo

# Output:
# Creating ⬢ academic-system-demo... done
# https://academic-system-demo.herokuapp.com/ | https://git.heroku.com/academic-system-demo.git
```

This creates:
- A Heroku app
- A remote Git repository
- A public URL

**Check remotes**:
```bash
git remote -v
# heroku  https://git.heroku.com/academic-system-demo.git (fetch)
# heroku  https://git.heroku.com/academic-system-demo.git (push)
```

---

## Step 6: Add MongoDB Atlas

### Option 1: Heroku Addon (Recommended)

```bash
# Add MongoDB addon (free tier)
heroku addons:create mongolab:sandbox

# Or ObjectRocket MongoDB
heroku addons:create ormongo:5-mlab
```

This automatically sets `MONGODB_URI` environment variable.

### Option 2: MongoDB Atlas (Free)

1. Go to: https://www.mongodb.com/cloud/atlas/register
2. Create free cluster (M0)
3. Create database user
4. Get connection string
5. Set in Heroku:

```bash
heroku config:set MONGODB_URI="mongodb+srv://username:password@cluster.mongodb.net/academic?retryWrites=true&w=majority"
```

---

## Step 7: Configure Environment Variables

```bash
# Set environment variables
heroku config:set SPRING_PROFILES_ACTIVE=prod

# Optional: OAuth2 credentials
heroku config:set GOOGLE_CLIENT_ID="your-client-id"
heroku config:set GOOGLE_CLIENT_SECRET="your-client-secret"

# View all config vars
heroku config

# Output:
# === academic-system-demo Config Vars
# MONGODB_URI: mongodb+srv://...
# SPRING_PROFILES_ACTIVE: prod
```

---

## Step 8: Deploy to Heroku

```bash
# Deploy main branch
git push heroku main

# Or if using master branch
git push heroku master
```

**Deployment process**:
```
Counting objects: 100, done.
Compressing objects: 100% (80/80), done.
Writing objects: 100% (100/100), 50.00 KiB | 5.00 MiB/s, done.

-----> Building on the Heroku-22 stack
-----> Using buildpack: heroku/java
-----> Java app detected
-----> Installing JDK 21... done
-----> Executing Maven...
       [INFO] BUILD SUCCESS
-----> Discovering process types
       Procfile declares types -> web
-----> Launching...
       Released v1
       https://academic-system-demo.herokuapp.com/ deployed to Heroku
```

---

## Step 9: Open Application

```bash
heroku open
```

This opens your app in the browser: `https://academic-system-demo.herokuapp.com/`

---

## Step 10: View Logs

```bash
# View recent logs
heroku logs

# Tail logs in real-time
heroku logs --tail

# View specific number of lines
heroku logs -n 1000

# Filter by source
heroku logs --source app
```

---

## Common Heroku Commands

```bash
# Check app status
heroku ps

# Restart app
heroku restart

# Scale dynos
heroku ps:scale web=1

# Run commands on Heroku
heroku run bash
heroku run java -version

# View config vars
heroku config

# Set config var
heroku config:set KEY=value

# Unset config var
heroku config:unset KEY

# View app info
heroku info

# Open app dashboard
heroku dashboard

# Delete app
heroku apps:destroy academic-system-demo
```

---

## Updating the Application

After making changes:

```bash
# Commit changes
git add .
git commit -m "Update feature X"

# Deploy
git push heroku main

# View logs
heroku logs --tail
```

---

## Troubleshooting

### Issue 1: Application Error (H10)

**Error**: "Application crashed"

**Solution**:
1. Check logs: `heroku logs --tail`
2. Ensure `Procfile` is correct
3. Check Java version in `system.properties`
4. Verify `PORT` environment variable is used

### Issue 2: Build Failed

**Error**: "Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin"

**Solution**:
1. Ensure Java 21 in `system.properties`
2. Check `pom.xml` for Java version:
   ```xml
   <properties>
       <java.version>21</java.version>
   </properties>
   ```
3. Run locally: `mvn clean package`

### Issue 3: MongoDB Connection Failed

**Error**: "MongoTimeoutException: Timed out after 30000 ms"

**Solution**:
1. Check `MONGODB_URI`: `heroku config`
2. Ensure MongoDB Atlas IP whitelist includes `0.0.0.0/0`
3. Test connection string locally

### Issue 4: Port Binding Failed

**Error**: "Web process failed to bind to $PORT"

**Solution**:
Update `application.properties`:
```properties
server.port=${PORT:8080}
```

Ensure `Procfile` uses `$PORT`:
```
web: java -jar target/*.jar --server.port=$PORT
```

### Issue 5: OAuth2 Redirect URI Mismatch

**Error**: "redirect_uri_mismatch"

**Solution**:
1. Go to Google Console
2. Add Heroku URL to authorized redirect URIs:
   ```
   https://academic-system-demo.herokuapp.com/login/oauth2/code/google
   ```

---

## Custom Domain (Optional)

### Add custom domain

```bash
# Add domain
heroku domains:add www.example.com

# View domains
heroku domains

# Output:
# === academic-system-demo Heroku Domain
# academic-system-demo.herokuapp.com

# === academic-system-demo Custom Domains
# Domain Name        DNS Target
# www.example.com    www.example.com.herokudns.com
```

### Configure DNS

Add CNAME record in your DNS provider:
```
CNAME  www  www.example.com.herokudns.com
```

### Enable HTTPS

```bash
heroku certs:auto:enable
```

---

## Database Backups

### Manual backup

```bash
# Create backup
heroku pg:backups:capture

# Download backup
heroku pg:backups:download
```

### Scheduled backups

```bash
# Schedule daily backups (paid plans)
heroku pg:backups:schedule --at '02:00 UTC'
```

---

## Monitoring

### View metrics

```bash
heroku metrics:web
```

Or visit: https://dashboard.heroku.com/apps/academic-system-demo/metrics

### Add-ons for monitoring

```bash
# New Relic (free tier)
heroku addons:create newrelic:wayne

# Papertrail (logging)
heroku addons:create papertrail:choklad
```

---

## Cost Optimization

**Free Tier**:
- 550-1000 dyno hours/month
- Sleeps after 30 minutes of inactivity
- MongoDB: 500 MB (mongolab:sandbox)

**Tips**:
1. Use eco dynos ($5/month, never sleeps)
2. Optimize app startup time
3. Use caching to reduce database calls
4. Monitor usage: https://dashboard.heroku.com/account/billing

---

## CI/CD with GitHub (Optional)

### Enable GitHub integration

1. Go to: https://dashboard.heroku.com/apps/academic-system-demo/deploy/github
2. Connect to GitHub repository
3. Enable automatic deploys from `main` branch
4. Optional: Enable "Wait for CI to pass" (if using GitHub Actions)

Now, every push to `main` automatically deploys to Heroku!

---

## Production Checklist

Before going live:

- [ ] Environment variables configured
- [ ] MongoDB connection tested
- [ ] OAuth2 redirect URIs updated
- [ ] Logs checked for errors
- [ ] Health check endpoint works
- [ ] Custom domain configured (optional)
- [ ] HTTPS enabled
- [ ] Backups scheduled

---

## Additional Resources

- **Heroku Dev Center**: https://devcenter.heroku.com/
- **Java Buildpack**: https://devcenter.heroku.com/articles/java-support
- **Spring Boot on Heroku**: https://devcenter.heroku.com/articles/deploying-spring-boot-apps-to-heroku
- **Heroku CLI**: https://devcenter.heroku.com/articles/heroku-cli

---

**Last Updated**: November 10, 2025
**Heroku Stack**: Heroku-22
**Status**: Production-ready
