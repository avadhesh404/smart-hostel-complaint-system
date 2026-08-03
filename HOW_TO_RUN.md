# How to Run Smart Hostel Complaint Management System

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher
- Internet connection (for Maven dependencies)

## Quick Start Guide

### Step 1: Install and Start MySQL

#### On macOS (using Homebrew):
```bash
# Install MySQL
brew install mysql@8.0

# Start MySQL service
brew services start mysql@8.0

# Set root password (if needed)
mysql_secure_installation
```

#### On Linux (Ubuntu/Debian):
```bash
# Install MySQL
sudo apt update
sudo apt install mysql-server

# Start MySQL service
sudo systemctl start mysql

# Secure installation
sudo mysql_secure_installation
```

#### On Windows:
1. Download MySQL Community Server from https://dev.mysql.com/downloads/mysql/
2. Install MySQL
3. Start MySQL from Windows Services

### Step 2: Create Database

#### Option A: Using MySQL Command Line
```bash
# Login to MySQL
mysql -u root -p

# Run the schema script
source /path/to/smart-hostel-complaint-system/src/main/resources/schema.sql

# (Optional) Load sample data
source /path/to/smart-hostel-complaint-system/src/main/resources/sample_data.sql

# Exit MySQL
exit;
```

#### Option B: Using MySQL Workbench
1. Open MySQL Workbench
2. Create a new connection to your MySQL server
3. Execute the SQL commands from `src/main/resources/schema.sql`
4. Optionally execute `src/main/resources/sample_data.sql`

### Step 3: Configure Database Connection

Edit `src/main/resources/config.properties`:

```properties
# Update these values with your MySQL credentials
db.url=jdbc:mysql://localhost:3306/hostel_complaint_db
db.username=root
db.password=YOUR_MYSQL_PASSWORD
db.driver=com.mysql.cj.jdbc.Driver
```

### Step 4: Build the Project

```bash
# Navigate to project directory
cd /Users/Asati_Bhanu/Desktop/Avdesh/smart-hostel-complaint-system

# Clean and compile
mvn clean compile

# Package (optional, creates JAR file)
mvn package
```

### Step 5: Run the Application

#### Option A: Using Maven
```bash
mvn exec:java -Dexec.mainClass="com.hostel.complaint.view.MainApplication"
```

#### Option B: Using Java directly
```bash
# Navigate to target directory
cd target

# Run the JAR file
java -cp smart-hostel-complaint-system-1.0.0.jar com.hostel.complaint.view.MainApplication
```

#### Option C: Using IDE (IntelliJ IDEA, Eclipse, NetBeans)
1. Open the project in your IDE
2. Locate `MainApplication.java` in `src/main/java/com/hostel/complaint/view/`
3. Right-click and select "Run 'MainApplication.main()'"
4. The application will start and display the login window

## Default Credentials

### Admin
- **Username**: `admin`
- **Password**: `admin123`

### Student
- **Username**: `samplestudent1`
- **Password**: `student123`

### Hostel Staff
- **Username**: `samplestaff1`
- **Password**: `staff123`

## Troubleshooting

### MySQL Connection Issues

**Error**: "Communications link failure"
```bash
# Check if MySQL is running
mysqladmin ping

# Start MySQL if not running
brew services start mysql@8.0  # macOS
sudo systemctl start mysql  # Linux
```

**Error**: "Access denied for user"
```bash
# Grant privileges to user
mysql -u root -p

GRANT ALL PRIVILEGES ON hostel_complaint_db.* TO 'your_username'@'localhost';
FLUSH PRIVILEGES;
```

### Build Issues

**Error**: "Could not resolve dependencies"
```bash
# Check internet connection
# Clear Maven cache
mvn clean install -U
```

**Error**: "Java version not supported"
```bash
# Check Java version
java -version

# Install Java 17 if needed
# macOS (Homebrew)
brew install openjdk@17

# Linux
sudo apt install openjdk-17-jdk
```

### Application Won't Start

**Error**: "ClassNotFound" or "NoClassDefFoundError"
```bash
# Rebuild the project
mvn clean package

# Check for missing dependencies
mvn dependency:tree
```

## Development Mode

### Running with Debug Logging
```bash
# Run with debug logging enabled
mvn exec:java -Dexec.mainClass="com.hostel.complaint.view.MainApplication" -Dexec.args="-Dlog.level=DEBUG"
```

### Running with Database
```bash
# Override database config at runtime
mvn exec:java -Dexec.mainClass="com.hostel.com plaint.view.MainApplication" \
  -Dexec.systemProperties="db.url=jdbc:mysql://localhost:3306/hostel_complaint_db,db.username=your_user,db.password=your_password"
```

## Testing the Application

### 1. Login Test
- Start the application
- Try logging in with default credentials
- Verify the appropriate dashboard appears based on user type

### 2. Database Connection Test
- Check `logs/application.log` for connection errors
- Verify database credentials in config.properties
- Ensure MySQL is accessible

### 3. UI Functionality Test
- Navigate through the login screen
- Verify all buttons and forms are displayed correctly
- Check that the application window opens and is responsive

## File Structure Reference

```
smart-hostel-complaint-system/
├── src/
│   ├── main/
│   │   ├── java/com/hostel/complaint/
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── dao/
│   │   │   │   ├── impl/
│   │   │   ├── service/
│   │   │   ├── database/
│   │   │   ├── validation/
│   │   │   ├── util/
│   │   │   └── view/
│   │   └── resources/
│   │       ├── config.properties
│   │       ├── logback.xml
│   │       ├── schema.sql
│   │       └── sample_data.sql
│   └── test/
└── pom.xml
```

## Additional Resources

- **README.md**: Full project documentation
- **ER_DIAGRAM.md**: Database schema and relationships
- **UML_DIAGRAMS.md**: Architecture and design patterns
- **logs/application.log**: Application logs (created at runtime)

## Performance Tips

### Database Optimization
- Ensure MySQL is properly configured for your system
- Add indexes to frequently queried columns
- Consider connection pooling for production

### JVM Memory Settings
```bash
# Run with increased memory
java -Xmx1024m -Xms512m -cp target/smart-hostel-complaint-system-1.0.0.jar com.hostel.complaint.view.MainApplication
```

### Disable Logging for Production
```bash
# Run without debug logging
mvn exec:java -Dexec.mainClass="com.hostel.complaint.view.MainApplication" -Dexec.args="-Dlog.level=WARN"
```

## Support

For issues or questions:
1. Check the logs in `logs/application.log`
2. Verify database connection
3. Ensure all dependencies are installed
4. Check Java version compatibility

---

**Project Status**: ✅ Production Ready
**Last Updated**: 2026-08-01
