# VPS Deployment Guide

Complete guide for deploying Spring Boot application to a VPS (Digital Ocean, Linode, Vultr, etc.)

---

## Prerequisites

- VPS account (Digital Ocean, Linode, Vultr)
- SSH access
- Domain name (optional but recommended)

---

## Step 1: Create VPS/Droplet

### Digital Ocean

1. Go to: https://www.digitalocean.com/
2. Create account
3. Click **Create** → **Droplets**
4. Choose:
   - **Image**: Ubuntu 22.04 LTS
   - **Plan**: Basic ($6/month, 1 GB RAM)
   - **Datacenter**: Closest to users
   - **Authentication**: SSH keys (recommended) or Password
5. Click **Create Droplet**
6. Note down IP address (e.g., `203.0.113.5`)

### Similar for Linode/Vultr

Process is very similar across providers.

---

## Step 2: Connect to VPS

### Using SSH

```bash
# Connect with password
ssh root@203.0.113.5

# Or with SSH key
ssh -i ~/.ssh/id_rsa root@203.0.113.5
```

### Windows Users

Use **PuTTY** or **Windows Terminal**:
```bash
ssh root@203.0.113.5
```

---

## Step 3: Initial Server Setup

### Update system

```bash
apt update && apt upgrade -y
```

### Create sudo user (best practice)

```bash
# Create user
adduser academic

# Add to sudo group
usermod -aG sudo academic

# Switch to new user
su - academic
```

### Configure firewall

```bash
# Enable UFW
sudo ufw allow OpenSSH
sudo ufw allow 80/tcp      # HTTP
sudo ufw allow 443/tcp     # HTTPS
sudo ufw allow 8080/tcp    # Spring Boot (temporary)
sudo ufw enable

# Check status
sudo ufw status
```

---

## Step 4: Install Java 21

```bash
# Install Java 21
sudo apt install -y openjdk-21-jdk

# Verify installation
java -version
# Output: openjdk version "21.0.x"

# Set JAVA_HOME
echo 'export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64' >> ~/.bashrc
source ~/.bashrc
```

---

## Step 5: Install MongoDB

```bash
# Import MongoDB public key
wget -qO - https://www.mongodb.org/static/pgp/server-7.0.asc | sudo apt-key add -

# Add MongoDB repository
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/7.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-7.0.list

# Update package list
sudo apt update

# Install MongoDB
sudo apt install -y mongodb-org

# Start MongoDB
sudo systemctl start mongod
sudo systemctl enable mongod

# Verify
sudo systemctl status mongod
```

### Secure MongoDB (Important!)

```bash
# Connect to MongoDB
mongosh

# Create admin user
use admin
db.createUser({
  user: "admin",
  pwd: "CHANGE_THIS_PASSWORD",
  roles: [ { role: "userAdminAnyDatabase", db: "admin" }, "readWriteAnyDatabase" ]
})

# Create database and user for app
use academic_system
db.createUser({
  user: "academic_user",
  pwd: "CHANGE_THIS_PASSWORD",
  roles: [ { role: "readWrite", db: "academic_system" } ]
})

exit
```

### Enable authentication

```bash
# Edit MongoDB config
sudo nano /etc/mongod.conf

# Add/modify:
security:
  authorization: enabled

# Restart MongoDB
sudo systemctl restart mongod
```

---

## Step 6: Deploy Spring Boot Application

### Method 1: Upload JAR file

**From local machine**:
```bash
# Build JAR
mvn clean package -DskipTests

# Upload to VPS
scp target/academic-system-0.0.1-SNAPSHOT.jar academic@203.0.113.5:/home/academic/
```

### Method 2: Clone from Git

**On VPS**:
```bash
# Install Git
sudo apt install -y git

# Clone repository
git clone https://github.com/yourusername/academic-system.git
cd academic-system

# Install Maven
sudo apt install -y maven

# Build
mvn clean package -DskipTests

# JAR will be in target/
```

---

## Step 7: Configure Application

### Create application.properties

```bash
# Create config directory
mkdir -p /home/academic/config

# Create properties file
nano /home/academic/config/application-prod.properties
```

**application-prod.properties**:
```properties
# Server
server.port=8080

# MongoDB
spring.data.mongodb.uri=mongodb://academic_user:CHANGE_THIS_PASSWORD@localhost:27017/academic_system?authSource=academic_system

# Logging
logging.level.root=INFO
logging.file.name=/home/academic/logs/academic-system.log

# Security
server.error.include-message=never
server.error.include-stacktrace=never

# OAuth2 (if using)
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
```

### Create logs directory

```bash
mkdir -p /home/academic/logs
```

---

## Step 8: Create Systemd Service

Create service file:
```bash
sudo nano /etc/systemd/system/academic-system.service
```

**academic-system.service**:
```ini
[Unit]
Description=Academic Management System
After=syslog.target network.target mongod.service

[Service]
Type=simple
User=academic
WorkingDirectory=/home/academic
ExecStart=/usr/bin/java -jar /home/academic/academic-system-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod --spring.config.location=/home/academic/config/application-prod.properties
StandardOutput=journal
StandardError=journal
Restart=always
RestartSec=10

# Environment variables
Environment="GOOGLE_CLIENT_ID=your-client-id"
Environment="GOOGLE_CLIENT_SECRET=your-client-secret"

[Install]
WantedBy=multi-user.target
```

### Enable and start service

```bash
# Reload systemd
sudo systemctl daemon-reload

# Enable service (start on boot)
sudo systemctl enable academic-system

# Start service
sudo systemctl start academic-system

# Check status
sudo systemctl status academic-system

# View logs
sudo journalctl -u academic-system -f
```

---

## Step 9: Install and Configure Nginx

### Install Nginx

```bash
sudo apt install -y nginx

# Start and enable
sudo systemctl start nginx
sudo systemctl enable nginx
```

### Configure reverse proxy

```bash
sudo nano /etc/nginx/sites-available/academic-system
```

**academic-system**:
```nginx
server {
    listen 80;
    server_name your-domain.com www.your-domain.com;  # Or use IP: 203.0.113.5

    # Logs
    access_log /var/log/nginx/academic-system-access.log;
    error_log /var/log/nginx/academic-system-error.log;

    # Reverse proxy
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        # WebSocket support (if needed)
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }

    # Static files (optional)
    location /static/ {
        alias /home/academic/static/;
    }

    # Increase timeout for large uploads
    client_max_body_size 10M;
}
```

### Enable site

```bash
# Create symlink
sudo ln -s /etc/nginx/sites-available/academic-system /etc/nginx/sites-enabled/

# Test configuration
sudo nginx -t

# Reload Nginx
sudo systemctl reload nginx
```

---

## Step 10: Configure Domain (Optional)

### Add DNS records

In your domain registrar (GoDaddy, Namecheap, Cloudflare):

**A Record**:
```
Type: A
Name: @
Value: 203.0.113.5
TTL: 3600
```

**CNAME Record** (for www):
```
Type: CNAME
Name: www
Value: your-domain.com
TTL: 3600
```

Wait for DNS propagation (5-30 minutes).

---

## Step 11: Enable HTTPS with Let's Encrypt

### Install Certbot

```bash
sudo apt install -y certbot python3-certbot-nginx
```

### Obtain SSL certificate

```bash
# Obtain and install certificate
sudo certbot --nginx -d your-domain.com -d www.your-domain.com

# Follow prompts:
# - Enter email
# - Agree to terms
# - Choose whether to redirect HTTP to HTTPS (recommended: yes)
```

### Auto-renewal

```bash
# Test renewal
sudo certbot renew --dry-run

# Certbot automatically sets up cron job for renewal
# Check: /etc/cron.d/certbot
```

Now your site is accessible at `https://your-domain.com`!

---

## Management Commands

### Application management

```bash
# Start
sudo systemctl start academic-system

# Stop
sudo systemctl stop academic-system

# Restart
sudo systemctl restart academic-system

# Status
sudo systemctl status academic-system

# View logs
sudo journalctl -u academic-system -f

# View last 100 lines
sudo journalctl -u academic-system -n 100
```

### Nginx management

```bash
# Start
sudo systemctl start nginx

# Stop
sudo systemctl stop nginx

# Restart
sudo systemctl restart nginx

# Reload config (no downtime)
sudo systemctl reload nginx

# Test config
sudo nginx -t

# View logs
sudo tail -f /var/log/nginx/academic-system-access.log
sudo tail -f /var/log/nginx/academic-system-error.log
```

### MongoDB management

```bash
# Start
sudo systemctl start mongod

# Stop
sudo systemctl stop mongod

# Restart
sudo systemctl restart mongod

# Status
sudo systemctl status mongod

# Connect
mongosh -u academic_user -p --authenticationDatabase academic_system
```

---

## Updating Application

### Method 1: Upload new JAR

```bash
# From local machine
scp target/academic-system-0.0.1-SNAPSHOT.jar academic@203.0.113.5:/home/academic/

# On VPS
sudo systemctl restart academic-system
```

### Method 2: Pull from Git

```bash
# On VPS
cd academic-system
git pull
mvn clean package -DskipTests
cp target/*.jar /home/academic/
sudo systemctl restart academic-system
```

---

## Database Backups

### Manual backup

```bash
# Create backup directory
mkdir -p /home/academic/backups

# Backup
mongodump --username academic_user --password CHANGE_THIS_PASSWORD --authenticationDatabase academic_system --db academic_system --out /home/academic/backups/$(date +%Y-%m-%d)

# Restore
mongorestore --username academic_user --password CHANGE_THIS_PASSWORD --authenticationDatabase academic_system --db academic_system /home/academic/backups/2024-11-10/academic_system
```

### Automated backups (cron)

```bash
# Edit crontab
crontab -e

# Add line (backup daily at 2 AM):
0 2 * * * mongodump --username academic_user --password CHANGE_THIS_PASSWORD --authenticationDatabase academic_system --db academic_system --out /home/academic/backups/$(date +\%Y-\%m-\%d) > /home/academic/logs/backup.log 2>&1

# Delete old backups (keep last 7 days)
0 3 * * * find /home/academic/backups -type d -mtime +7 -exec rm -rf {} +
```

---

## Monitoring

### Check disk space

```bash
df -h
```

### Check memory usage

```bash
free -h
```

### Monitor system resources

```bash
# Install htop
sudo apt install -y htop

# Run
htop
```

### Monitor application

```bash
# Watch logs in real-time
sudo journalctl -u academic-system -f

# Check if running
sudo systemctl status academic-system

# Test endpoint
curl http://localhost:8080/api/health
```

---

## Security Best Practices

1. **Change default ports** (optional):
   ```bash
   # Change SSH port
   sudo nano /etc/ssh/sshd_config
   # Change Port 22 to Port 2222
   sudo systemctl restart sshd
   ```

2. **Disable root login**:
   ```bash
   sudo nano /etc/ssh/sshd_config
   # Set: PermitRootLogin no
   sudo systemctl restart sshd
   ```

3. **Install fail2ban** (prevent brute force):
   ```bash
   sudo apt install -y fail2ban
   sudo systemctl enable fail2ban
   sudo systemctl start fail2ban
   ```

4. **Keep system updated**:
   ```bash
   sudo apt update && sudo apt upgrade -y
   ```

5. **Use strong passwords** for MongoDB

6. **Enable firewall** (UFW)

---

## Troubleshooting

### Application not starting

```bash
# Check logs
sudo journalctl -u academic-system -n 100

# Check Java version
java -version

# Check if port is in use
sudo lsof -i :8080

# Test JAR manually
java -jar /home/academic/academic-system-0.0.1-SNAPSHOT.jar
```

### MongoDB connection failed

```bash
# Check if MongoDB is running
sudo systemctl status mongod

# Check MongoDB logs
sudo tail -f /var/log/mongodb/mongod.log

# Test connection
mongosh -u academic_user -p --authenticationDatabase academic_system
```

### Nginx 502 Bad Gateway

```bash
# Check if Spring Boot is running
sudo systemctl status academic-system

# Check Nginx config
sudo nginx -t

# Check Nginx logs
sudo tail -f /var/log/nginx/error.log

# Verify proxy_pass URL
curl http://localhost:8080
```

### SSL certificate issues

```bash
# Renew certificate
sudo certbot renew

# Check certificate
sudo certbot certificates

# Restart Nginx
sudo systemctl restart nginx
```

---

## Cost Estimation

**Digital Ocean** (similar pricing across providers):
- **$6/month**: 1 GB RAM, 25 GB SSD, 1 TB transfer
- **$12/month**: 2 GB RAM, 50 GB SSD, 2 TB transfer
- **$18/month**: 2 GB RAM, 60 GB SSD, 3 TB transfer

**Additional costs**:
- Domain: $10-15/year
- Backups: $1.20/month (20% of droplet cost)

---

## Production Checklist

- [ ] VPS created and secured
- [ ] SSH key authentication enabled
- [ ] Firewall configured
- [ ] Java 21 installed
- [ ] MongoDB installed and secured
- [ ] Application deployed as systemd service
- [ ] Nginx configured as reverse proxy
- [ ] Domain configured (if applicable)
- [ ] HTTPS enabled with Let's Encrypt
- [ ] Automated backups scheduled
- [ ] Monitoring set up

---

## Additional Resources

- **Digital Ocean Tutorials**: https://www.digitalocean.com/community/tutorials
- **Nginx Documentation**: https://nginx.org/en/docs/
- **Let's Encrypt**: https://letsencrypt.org/
- **Ubuntu Server Guide**: https://ubuntu.com/server/docs

---

**Last Updated**: November 10, 2025
**Ubuntu Version**: 22.04 LTS
**Status**: Production-ready
