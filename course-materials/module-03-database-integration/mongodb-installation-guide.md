# MongoDB Installation Guide

Complete guide for installing MongoDB Community Server on Windows, macOS, and Linux.

---

## Table of Contents

1. [Windows Installation](#windows-installation)
2. [macOS Installation](#macos-installation)
3. [Linux Installation (Ubuntu/Debian)](#linux-installation-ubuntudebian)
4. [Verifying Installation](#verifying-installation)
5. [MongoDB Compass (GUI Tool)](#mongodb-compass-gui-tool)
6. [Starting MongoDB](#starting-mongodb)
7. [Troubleshooting](#troubleshooting)

---

## Windows Installation

### Step 1: Download MongoDB

1. Visit: https://www.mongodb.com/try/download/community
2. Select:
   - **Version**: Latest (7.x or higher)
   - **Platform**: Windows
   - **Package**: MSI
3. Click **Download**

### Step 2: Run the Installer

1. Double-click the downloaded `.msi` file
2. Click **Next** on the welcome screen
3. Accept the license agreement
4. Choose **Complete** installation (recommended)
5. **Service Configuration**:
   - ✅ Install MongoDB as a Service (recommended)
   - Service Name: `MongoDB`
   - Data Directory: `C:\Program Files\MongoDB\Server\7.0\data\`
   - Log Directory: `C:\Program Files\MongoDB\Server\7.0\log\`
6. **Install MongoDB Compass**: ✅ Check this option (GUI tool)
7. Click **Install**
8. Wait for installation to complete
9. Click **Finish**

### Step 3: Add MongoDB to PATH (Optional but recommended)

1. Open **Environment Variables**:
   - Right-click **This PC** → **Properties**
   - Click **Advanced system settings**
   - Click **Environment Variables**
2. Under **System variables**, find **Path**
3. Click **Edit** → **New**
4. Add: `C:\Program Files\MongoDB\Server\7.0\bin`
5. Click **OK** on all windows

### Step 4: Verify Installation

Open Command Prompt and run:

```bash
mongod --version
```

Expected output:
```
db version v7.0.x
Build Info: ...
```

---

## macOS Installation

### Method 1: Using Homebrew (Recommended)

#### Step 1: Install Homebrew (if not already installed)

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

#### Step 2: Install MongoDB

```bash
# Add MongoDB tap
brew tap mongodb/brew

# Install MongoDB Community Edition
brew install mongodb-community@7.0

# Verify installation
mongod --version
```

#### Step 3: Start MongoDB Service

```bash
# Start MongoDB service
brew services start mongodb-community@7.0

# Check if MongoDB is running
brew services list | grep mongodb
```

### Method 2: Manual Installation

1. Download: https://www.mongodb.com/try/download/community
2. Select: macOS, TGZ package
3. Extract the archive:
   ```bash
   tar -zxvf mongodb-macos-x86_64-7.0.x.tgz
   ```
4. Move to `/usr/local`:
   ```bash
   sudo mv mongodb-macos-x86_64-7.0.x /usr/local/mongodb
   ```
5. Add to PATH in `~/.zshrc` or `~/.bash_profile`:
   ```bash
   export PATH="/usr/local/mongodb/bin:$PATH"
   ```
6. Create data directory:
   ```bash
   sudo mkdir -p /usr/local/var/mongodb
   sudo chown -R $(whoami) /usr/local/var/mongodb
   ```

---

## Linux Installation (Ubuntu/Debian)

### Step 1: Import MongoDB Public GPG Key

```bash
wget -qO - https://www.mongodb.org/static/pgp/server-7.0.asc | sudo apt-key add -
```

### Step 2: Create MongoDB Repository List File

**Ubuntu 22.04 (Jammy)**:
```bash
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/7.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-7.0.list
```

**Ubuntu 20.04 (Focal)**:
```bash
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu focal/mongodb-org/7.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-7.0.list
```

**Debian 11 (Bullseye)**:
```bash
echo "deb http://repo.mongodb.org/apt/debian bullseye/mongodb-org/7.0 main" | sudo tee /etc/apt/sources.list.d/mongodb-org-7.0.list
```

### Step 3: Update Package Database

```bash
sudo apt-get update
```

### Step 4: Install MongoDB

```bash
sudo apt-get install -y mongodb-org
```

### Step 5: Start MongoDB Service

```bash
# Start MongoDB
sudo systemctl start mongod

# Enable MongoDB to start on boot
sudo systemctl enable mongod

# Check status
sudo systemctl status mongod
```

### Step 6: Verify Installation

```bash
mongod --version
```

---

## Verifying Installation

### Test MongoDB Connection

1. **Start MongoDB** (if not already running)
2. **Open MongoDB Shell**:

   ```bash
   mongosh
   ```

   Or for older versions:
   ```bash
   mongo
   ```

3. **Run Test Commands**:

   ```javascript
   // Show current database
   db

   // Show all databases
   show dbs

   // Create a test database
   use testdb

   // Insert a test document
   db.test.insertOne({ name: "Test", value: 123 })

   // Find documents
   db.test.find()

   // Expected output:
   // { _id: ObjectId("..."), name: 'Test', value: 123 }

   // Exit
   exit
   ```

---

## MongoDB Compass (GUI Tool)

MongoDB Compass is a graphical interface for MongoDB.

### Installation

**Windows/macOS**: Automatically installed if you checked the option during MongoDB installation.

**Manual Download**: https://www.mongodb.com/try/download/compass

### Features

- Browse collections and documents
- Run queries visually
- Analyze schema
- Import/export data
- Create indexes
- Monitor performance

### Connect to MongoDB

1. Open MongoDB Compass
2. Connection String: `mongodb://localhost:27017`
3. Click **Connect**
4. You should see:
   - **Databases**: admin, config, local
   - You can create new databases and collections

---

## Starting MongoDB

### Windows

MongoDB should start automatically as a service.

**Manual Start**:
```bash
# Start service
net start MongoDB

# Stop service
net stop MongoDB
```

**Check if running**:
```bash
# Open Task Manager (Ctrl+Shift+Esc)
# Look for "mongod.exe" in Processes
```

### macOS

**With Homebrew**:
```bash
# Start
brew services start mongodb-community@7.0

# Stop
brew services stop mongodb-community@7.0

# Restart
brew services restart mongodb-community@7.0

# Check status
brew services list | grep mongodb
```

**Without Homebrew**:
```bash
# Start manually
mongod --config /usr/local/etc/mongod.conf --fork

# Stop
mongosh admin --eval "db.shutdownServer()"
```

### Linux

```bash
# Start
sudo systemctl start mongod

# Stop
sudo systemctl stop mongod

# Restart
sudo systemctl restart mongod

# Status
sudo systemctl status mongod

# Enable on boot
sudo systemctl enable mongod
```

---

## Troubleshooting

### Issue 1: "Command not found: mongod"

**Cause**: MongoDB not in system PATH

**Solution**:
- **Windows**: Add `C:\Program Files\MongoDB\Server\7.0\bin` to PATH
- **macOS/Linux**: Add MongoDB bin directory to PATH in shell config file

### Issue 2: "Failed to start mongod.service"

**Cause**: Data directory issues or port already in use

**Solution**:
```bash
# Check if port 27017 is in use
sudo lsof -i :27017

# Create data directory with correct permissions
sudo mkdir -p /var/lib/mongodb
sudo chown -R mongodb:mongodb /var/lib/mongodb

# Create log directory
sudo mkdir -p /var/log/mongodb
sudo chown -R mongodb:mongodb /var/log/mongodb

# Restart service
sudo systemctl restart mongod
```

### Issue 3: "Connection refused" when connecting

**Cause**: MongoDB not running

**Solution**:
```bash
# Check if MongoDB is running
# Windows:
tasklist | findstr mongod

# macOS/Linux:
ps aux | grep mongod

# Start MongoDB if not running (see "Starting MongoDB" section)
```

### Issue 4: "Access denied" errors

**Cause**: Insufficient permissions on data directory

**Solution**:

**Windows**:
1. Go to `C:\Program Files\MongoDB\Server\7.0\data`
2. Right-click → Properties → Security
3. Ensure your user has Full Control

**macOS/Linux**:
```bash
# Fix permissions
sudo chown -R $(whoami) /usr/local/var/mongodb
sudo chmod -R 755 /usr/local/var/mongodb
```

### Issue 5: Port 27017 already in use

**Cause**: Another MongoDB instance or application using port 27017

**Solution**:

**Find and kill the process**:
```bash
# Windows:
netstat -ano | findstr :27017
taskkill /PID <PID> /F

# macOS/Linux:
sudo lsof -i :27017
sudo kill -9 <PID>
```

**Or use different port**:
```bash
mongod --port 27018
```

---

## Configuration File

### Windows

Location: `C:\Program Files\MongoDB\Server\7.0\bin\mongod.cfg`

```yaml
systemLog:
  destination: file
  path: C:\Program Files\MongoDB\Server\7.0\log\mongod.log
storage:
  dbPath: C:\Program Files\MongoDB\Server\7.0\data
net:
  port: 27017
  bindIp: 127.0.0.1
```

### macOS (Homebrew)

Location: `/usr/local/etc/mongod.conf`

```yaml
systemLog:
  destination: file
  path: /usr/local/var/log/mongodb/mongo.log
storage:
  dbPath: /usr/local/var/mongodb
net:
  port: 27017
  bindIp: 127.0.0.1
```

### Linux

Location: `/etc/mongod.conf`

```yaml
systemLog:
  destination: file
  path: /var/log/mongodb/mongod.log
storage:
  dbPath: /var/lib/mongodb
net:
  port: 27017
  bindIp: 127.0.0.1
```

---

## Testing with Spring Boot

Once MongoDB is installed, test connection with Spring Boot:

**application.properties**:
```properties
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=academic_system
```

**Run Spring Boot**:
```bash
mvn spring-boot:run
```

**Expected output**:
```
...
MongoDB connection successful
...
Started Application in X seconds
```

---

## Useful Commands

```bash
# Show MongoDB version
mongod --version

# Show mongosh (shell) version
mongosh --version

# Connect to specific database
mongosh mongodb://localhost:27017/academic_system

# Check if MongoDB is running
# Windows:
tasklist | findstr mongod

# macOS/Linux:
ps aux | grep mongod

# View MongoDB logs
# Windows:
type "C:\Program Files\MongoDB\Server\7.0\log\mongod.log"

# macOS/Linux:
tail -f /var/log/mongodb/mongod.log
```

---

## Next Steps

After successful installation:

1. Start MongoDB service
2. Open MongoDB Compass and connect
3. Configure Spring Boot application (Module 3)
4. Test connection with Postman
5. Build your first full-stack CRUD application!

---

## Additional Resources

- **Official MongoDB Documentation**: https://docs.mongodb.com/manual/installation/
- **MongoDB University** (Free courses): https://university.mongodb.com/
- **MongoDB Community Forums**: https://www.mongodb.com/community/forums/
- **MongoDB Tutorial**: https://www.mongodb.com/docs/manual/tutorial/

---

**Last Updated**: November 10, 2025
**MongoDB Version**: 7.0.x
**Status**: Production-ready
