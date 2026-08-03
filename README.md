# Smart Hostel Complaint Management System

A comprehensive desktop application for managing hostel complaints using Java 17, Java Swing, JDBC, MySQL, Maven, MVC Architecture, and DAO Pattern.

## Features

### Authentication
- Student Registration
- Student Login
- Hostel Staff Login
- Admin Login
- Forgot Password
- Change Password
- Password hashing using BCrypt
- Session management

### Student Module
- Dashboard
- Submit Complaint
- Upload Complaint Image
- Track Complaint
- Complaint Timeline
- Complaint History
- Edit Profile
- View Notifications
- Rate Resolved Complaint

### Hostel Staff Module
- Dashboard
- View Assigned Complaints
- Update Complaint Status
- Add Resolution Notes
- Upload Completion Image
- View Daily Tasks

### Admin Module
- Dashboard with analytics
- Manage Students
- Manage Hostel Staff
- Manage Complaint Categories
- Manage Hostel Blocks and Rooms
- Assign Complaints to Staff
- Change Priority
- View Complaint Statistics
- Export Reports to PDF and Excel
- View Audit Logs

### Complaint Management
- Auto-generate Complaint ID
- Categories: Electrical, Plumbing, Internet, Cleaning, Furniture, Water Supply, Food, Security, Room Maintenance, Other
- Priority Levels: Low, Medium, High, Critical
- Status Flow: Submitted → Assigned → In Progress → Resolved → Closed
- Maintain complete status history

### Dashboard Analytics
- Total Complaints
- Pending
- Assigned
- Resolved
- Closed
- Critical Complaints
- Complaints by Category
- Monthly Complaint Trends
- Average Resolution Time

### Search & Filter
- Complaint ID
- Student Name
- Category
- Hostel Block
- Room Number
- Status
- Priority
- Date Range

## Technology Stack

- **Java Version**: Java 17
- **Build Tool**: Maven
- **Database**: MySQL 8.0+
- **UI Framework**: Java Swing
- **ORM Pattern**: JDBC with DAO Pattern
- **Architecture**: MVC (Model-View-Controller)
- **Password Hashing**: BCrypt
- **Logging**: SLF4J with Logback
- **Reporting**: Apache POI (Excel), iText (PDF)

## Project Structure

```
smart-hostel-complaint-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/hostel/complaint/
│   │   │       ├── controller/      # Controller classes
│   │   │       ├── model/          # Entity classes
│   │   │       ├── dao/            # DAO interfaces
│   │   │       ├── dao/impl/       # DAO implementations
│   │   │       ├── service/        # Service layer
│   │   │       ├── database/       # Database configuration
│   │   │       ├── validation/     # Validation logic
│   │   │       ├── util/           # Utility classes
│   │   │       ├── view/           # Swing UI components
│   │   │       └── resources/      # Configuration files
│   │   └── resources/
│   │       ├── config.properties   # Application configuration
│   │       ├── logback.xml        # Logging configuration
│   │       ├── schema.sql         # Database schema
│   │       └── sample_data.sql    # Sample data
│   └── test/
│       └── java/
└── pom.xml
```

## Database Schema

The application uses a normalized MySQL database with the following tables:

- `admin` - Administrator accounts
- `students` - Student information
- `hostel_staff` - Staff information
- `hostel_blocks` - Hostel blocks
- `rooms` - Room information
- `categories` - Complaint categories
- `complaints` - Complaint records
- `complaint_images` - Complaint images
- `complaint_status_history` - Status change history
- `notifications` - User notifications
- `feedback` - Complaint feedback
- `audit_logs` - System audit logs

## Installation

### Quick Setup (macOS/Linux)

**Automated Setup Script:**
```bash
# Run the setup script (macOS)
./setup.sh

# Run the application
./run.sh
```

### Manual Setup

#### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher
- IDE (IntelliJ IDEA, Eclipse, or NetBeans)

#### Database Setup

1. Create a MySQL database:
```sql
CREATE DATABASE hostel_complaint_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Run the schema:
```bash
mysql -u root -p hostel_complaint_db < src/main/resources/schema.sql
```

3. (Optional) Load sample data:
```bash
mysql -u root -p hostel_complaint_db < src/main/resources/sample_data.sql
```

#### Configuration

Edit `src/main/resources/config.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/hostel_complaint_db
db.username=your_mysql_username
db.password=your_mysql_password
```

#### Build and Run

**Using scripts:**
```bash
# macOS/Linux
./run.sh

# Windows
run.bat
```

**Using Maven:**
```bash
# Build the project
mvn clean install

# Run the application
mvn exec:java -Dexec.mainClass="com.hostel.complaint.view.MainApplication"
```

**Using IDE:**
Run directly from your IDE by executing the `MainApplication` class.

**For detailed instructions, see [HOW_TO_RUN.md](HOW_TO_RUN.md)**

## Default Credentials

### Admin
- Username: `admin`
- Password: `admin123`

### Student
- Username: `student1`
- Password: `student123`

### Staff
- Username: `warden1`
- Password: `staff123`

## Architecture

### MVC Pattern

The application follows the Model-View-Controller pattern:

- **Model**: Entity classes in `com.hostel.complaint.model`
- **View**: Swing UI components in `com.hostel.complaint.view`
- **Controller**: Service layer in `com.hostel.complaint.service`

### DAO Pattern

Data Access Object pattern for database operations:

- **DAO Interfaces**: Define contract for data operations
- **DAO Implementations**: Concrete implementations with JDBC
- **Connection Pooling**: Efficient database connection management

### Service Layer

Business logic layer that:

- Validates input data
- Enforces business rules
- Manages transactions
- Coordinates between DAOs
- Handles authentication and authorization

## Security Features

- BCrypt password hashing
- Prepared statements for SQL injection prevention
- Input validation
- Role-based authorization
- Session management
- Audit logging

## Code Quality

- **Clean Architecture**: Separation of concerns
- **SOLID Principles**: Single responsibility, Open/closed, Liskov substitution, Interface segregation, Dependency inversion
- **Reusable Code**: Utility classes and common patterns
- **Proper Package Separation**: Organized by layer and functionality
- **Documentation**: Comprehensive comments and Javadoc
- **Logging**: Structured logging with SLF4J

## Testing

Run tests with Maven:
```bash
mvn test
```

## Future Enhancements

- Email notifications
- Real-time updates
- Mobile app version
- Advanced reporting features
- Analytics dashboard improvements
- Integration with other hostel management systems

## Troubleshooting

### Database Connection Issues
- Ensure MySQL is running
- Check database credentials in config.properties
- Verify database name matches schema

### Build Issues
- Ensure Java 17 is installed and configured
- Update Maven dependencies: `mvn clean install`
- Check for network connectivity for dependency downloads

### UI Issues
- Ensure system has proper display support
- Check Java Swing compatibility
- Verify look and feel settings

## License

This project is for educational purposes.

## Contributing

This is a demonstration project for interview purposes. For contributions, please follow standard coding practices and maintain the existing architecture.

## Support

For issues or questions, please refer to the code documentation or contact the development team.

---

**Built with Java 17, Maven, and best practices in software engineering.**
