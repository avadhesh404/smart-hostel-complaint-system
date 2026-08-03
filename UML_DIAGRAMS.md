# UML Diagrams - Smart Hostel Complaint Management System

## Class Diagram

### Core Model Classes

```
┌─────────────────────────────────┐
│         Admin                   │
├─────────────────────────────────┤
│ - adminId: int                 │
│ - username: String             │
│ - password: String             │
│ - fullName: String             │
│ - email: String                │
│ - phone: String                │
│ - createdAt: LocalDateTime     │
│ - updatedAt: LocalDateTime     │
│ - lastLogin: LocalDateTime     │
│ - isActive: boolean            │
├─────────────────────────────────┤
│ + getAdminId(): int            │
│ + setAdminId(int): void        │
│ + getUsername(): String         │
│ + setUsername(String): void     │
│ + getPassword(): String         │
│ + setPassword(String): void     │
│ + getFullName(): String         │
│ + setFullName(String): void     │
│ + getEmail(): String            │
│ + setEmail(String): void        │
│ + getPhone(): String            │
│ + setPhone(String): void        │
│ + getCreatedAt(): LocalDateTime │
│ + setCreatedAt(LocalDateTime): void│
│ + getUpdatedAt(): LocalDateTime │
│ + setUpdatedAt(LocalDateTime): void│
│ + getLastLogin(): LocalDateTime │
│ + setLastLogin(LocalDateTime): void│
│ + isActive(): boolean           │
│ + setActive(boolean): void      │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│         Student                  │
├─────────────────────────────────┤
│ - studentId: int               │
│ - studentNumber: String         │
│ - username: String              │
│ - password: String              │
│ - fullName: String              │
│ - email: String                 │
│ - phone: String                 │
│ - blockId: Integer             │
│ - roomId: Integer               │
│ - course: String                │
│ - yearOfStudy: Integer         │
│ - createdAt: LocalDateTime       │
│ - updatedAt: LocalDateTime       │
│ - lastLogin: LocalDateTime       │
│ - isActive: boolean            │
│ - blockName: String             │
│ - roomNumber: String            │
├─────────────────────────────────┤
│ + getStudentId(): int           │
│ + setStudentId(int): void       │
│ + getStudentNumber(): String     │
│ + setStudentNumber(String): void│
│ + getUsername(): String          │
│ + setUsername(String): void      │
│ + getPassword(): String          │
│ + setPassword(String): void      │
│ + getFullName(): String          │
│ + setFullName(String): void      │
│ + getEmail(): String             │
│ + setEmail(String): void         │
│ + getPhone(): String             │
│ + setPhone(String): void         │
│ + getBlockId(): Integer         │
│ + setBlockId(Integer): void      │
│ + getRoomId(): Integer          │
│ + setRoomId(Integer): void       │
│ + getCourse(): String            │
│ + setCourse(String): void         │
│ + getYearOfStudy(): Integer      │
│ + setYearOfStudy(Integer): void  │
│ + getCreatedAt(): LocalDateTime  │
│ + setCreatedAt(LocalDateTime): void│
│ + getUpdatedAt(): LocalDateTime  │
│ + setUpdatedAt(LocalDateTime): void│
│ + getLastLogin(): LocalDateTime  │
│ + setLastLogin(LocalDateTime): void│
│ + isActive(): boolean            │
│ + setActive(boolean): void       │
│ + getBlockName(): String         │
│ + setBlockName(String): void     │
│ + getRoomNumber(): String        │
│ + setRoomNumber(String): void    │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│       HostelStaff               │
├─────────────────────────────────┤
│ - staffId: int                 │
│ - employeeNumber: String         │
│ - username: String              │
│ - password: String              │
│ - fullName: String              │
│ - email: String                 │
│ - phone: String                 │
│ - role: StaffRole              │
│ - specialization: String         │
│ - blockId: Integer             │
│ - isAvailable: boolean         │
│ - createdAt: LocalDateTime       │
│ - updatedAt: LocalDateTime       │
│ - lastLogin: LocalDateTime       │
│ - isActive: boolean            │
│ - blockName: String             │
├─────────────────────────────────┤
│ + StaffRole enum               │
│   WARDEN, MAINTENANCE,          │
│   CLEANING, SECURITY, OTHER      │
├─────────────────────────────────┤
│ + getStaffId(): int             │
│ + setStaffId(int): void         │
│ + getEmployeeNumber(): String   │
│ + setEmployeeNumber(String): void│
│ + getUsername(): String          │
│ + setUsername(String): void      │
│ + getPassword(): String          │
│ + setPassword(String): void      │
│ + getFullName(): String          │
│ + setFullName(String): void      │
│ + getEmail(): String             │
│ + setEmail(String): void         │
│ + getPhone(): String             │
│ + setPhone(String): void         │
│ + getRole(): StaffRole          │
│ + setRole(StaffRole): void       │
│ + getSpecialization(): String    │
│ + setSpecialization(String): void│
│ + getBlockId(): Integer         │
│ + setBlockId(Integer): void      │
│ + isAvailable(): boolean         │
│ + setAvailable(boolean): void    │
│ + getCreatedAt(): LocalDateTime  │
│ + setCreatedAt(LocalDateTime): void│
│ + getUpdatedAt(): LocalDateTime  │
│ + setUpdatedAt(LocalDateTime): void│
│ + getLastLogin(): LocalDateTime  │
│ + setLastLogin(LocalDateTime): void│
│ + isActive(): boolean            │
│ + setActive(boolean): void       │
│ + getBlockName(): String         │
│ + setBlockName(String): void     │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│       Complaint                 │
├─────────────────────────────────┤
│ - complaintId: int             │
│ - complaintNumber: String       │
│ - studentId: int               │
│ - categoryId: int               │
│ - assignedStaffId: Integer      │
│ - title: String                 │
│ - description: String           │
│ - blockId: int                 │
│ - roomId: int                  │
│ - priority: Priority           │
│ - status: ComplaintStatus      │
│ - submittedAt: LocalDateTime    │
│ - assignedAt: LocalDateTime     │
│ - inProgressAt: LocalDateTime   │
│ - resolvedAt: LocalDateTime     │
│ - closedAt: LocalDateTime       │
│ - resolutionNotes: String       │
│ - completionImagePath: String    │
│ - createdAt: LocalDateTime      │
│ - updatedAt: LocalDateTime      │
│ - studentName: String           │
│ - studentNumber: String         │
│ - categoryName: String          │
│ - blockName: String             │
│ - roomNumber: String            │
│ - staffName: String             │
│ - staffRole: String             │
│ - images: List<ComplaintImage> │
│ - statusHistory: List<History> │
├─────────────────────────────────┤
│ + Priority enum                │
│   LOW, MEDIUM, HIGH, CRITICAL   │
│ + ComplaintStatus enum          │
│   SUBMITTED, ASSIGNED,          │
│   IN_PROGRESS, RESOLVED, CLOSED │
├─────────────────────────────────┤
│ + getComplaintId(): int         │
│ + setComplaintId(int): void     │
│ + getComplaintNumber(): String   │
│ + setComplaintNumber(String): void│
│ + getStudentId(): int           │
│ + setStudentId(int): void       │
│ + getCategoryId(): int          │
│ + setCategoryId(int): void       │
│ + getAssignedStaffId(): Integer│
│ + setAssignedStaffId(Integer): void│
│ + getTitle(): String            │
│ + setTitle(String): void        │
│ + getDescription(): String      │
│ + setDescription(String): void    │
│ + getBlockId(): int             │
│ + setBlockId(int): void         │
│ + getRoomId(): int              │
│ + setRoomId(int): void          │
│ + getPriority(): Priority       │
│ + setPriority(Priority): void   │
│ + getStatus(): ComplaintStatus  │
│ + setStatus(ComplaintStatus): void│
│ + getResolutionTimeHours(): long│
│ + addImage(ComplaintImage): void│
│ + addStatusHistory(History): void│
└─────────────────────────────────┘
```

### DAO Layer

```
┌─────────────────────────────────┐
│     <<interface>> BaseDAO<T>    │
├─────────────────────────────────┤
│ + save(T): T                   │
│ + update(T): boolean            │
│ + delete(int): boolean          │
│ + findById(int): Optional<T>    │
│ + findAll(): List<T>            │
│ + exists(int): boolean           │
│ + count(): int                  │
└─────────────────────────────────┘
           △
           │
           │ implements
           │
┌─────────────────────────────────┐
│     ComplaintDAO               │
├─────────────────────────────────┤
│ + findByComplaintNumber(String):│
│   Optional<Complaint>           │
│ + findByStudentId(int):         │
│   List<Complaint>               │
│ + findByStaffId(int):           │
│   List<Complaint>               │
│ + findByStatus(Status):          │
│   List<Complaint>               │
│ + findByPriority(Priority):     │
│   List<Complaint>               │
│ + updateStatus(int, Status,     │
│   String, Role, String): boolean│
│ + assignToStaff(int, int):      │
│   boolean                       │
│ + getStatistics(): Map<String,  │
│   Long>                         │
│ + generateNextComplaintNumber():│
│   String                        │
└─────────────────────────────────┘
```

### Service Layer

```
┌─────────────────────────────────┐
│  AuthenticationService          │
├─────────────────────────────────┤
│ - studentDAO: StudentDAO        │
│ - staffDAO: HostelStaffDAO      │
│ - adminDAO: AdminDAO            │
│ - currentUserSession: UserSession│
├─────────────────────────────────┤
│ + authenticateStudent(String,    │
│   String): UserSession          │
│ + authenticateStaff(String,      │
│   String): UserSession          │
│ + authenticateAdmin(String,      │
│   String): UserSession          │
│ + hashPassword(String): String  │
│ + verifyPassword(String, String):│
│   boolean                       │
│ + getCurrentSession():          │
│   UserSession                   │
│ + isLoggedIn(): boolean         │
│ + logout(): void                │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│     ComplaintService            │
├─────────────────────────────────┤
│ - complaintDAO: ComplaintDAO    │
│ - statusHistoryDAO: HistoryDAO  │
│ - imageDAO: ImageDAO            │
│ - categoryDAO: CategoryDAO      │
│ - studentDAO: StudentDAO        │
│ - staffDAO: HostelStaffDAO      │
│ - notificationDAO: NotificationDAO│
│ - feedbackDAO: FeedbackDAO      │
├─────────────────────────────────┤
│ + submitComplaint(Complaint):    │
│   Complaint                     │
│ + getComplaintWithDetails(int):  │
│   Complaint                     │
│ + assignComplaint(int, int,     │
│   String, Role): void           │
│ + updateComplaintStatus(int,     │
│   Status, String, Role, String):│
│   void                          │
│ + resolveComplaint(int, String,  │
│   String, String, Role): void    │
│ + submitFeedback(Feedback): void│
│ + getStatistics(): Map<String,  │
│   Long>                         │
└─────────────────────────────────┘
```

### View Layer

```
┌─────────────────────────────────┐
│       BaseFrame                 │
├─────────────────────────────────┤
│ - mainPanel: JPanel             │
│ - contentPanel: JPanel          │
│ - titleLabel: JLabel           │
├─────────────────────────────────┤
│ + BaseFrame(String)             │
│ + initializeFrame(): void       │
│ + createButton(String): JButton │
│ + createTextField(int): JTextField│
│ + createTextArea(int, int):    │
│   JTextArea                     │
│ + createComboBox(T[]): JComboBox│
│ + createLabel(String): JLabel    │
│ + showError(String): void       │
│ + showInfo(String): void        │
│ + showConfirmation(String):     │
│   boolean                       │
└─────────────────────────────────┘
           △
           │
           │ extends
           │
┌─────────────────────────────────┐
│       LoginView                 │
├─────────────────────────────────┤
│ - usernameField: JTextField     │
│ - passwordField: JPasswordField│
│ - userTypeCombo: JComboBox      │
│ - loginButton: JButton          │
│ - loginListener: LoginListener  │
├─────────────────────────────────┤
│ + LoginView()                   │
│ + setLoginListener(LoginListener):│
│   void                          │
│ + clearFields(): void           │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│   StudentDashboard             │
├─────────────────────────────────┤
│ - welcomeLabel: JLabel          │
│ - submitComplaintButton: JButton│
│ - viewComplaintsButton: JButton │
│ - statsPanel: JPanel            │
│ - listener: DashboardListener  │
├─────────────────────────────────┤
│ + StudentDashboard(String,      │
│   String)                       │
│ + updateStatistics(Map): void   │
│ + setListener(DashboardListener):│
│   void                          │
└─────────────────────────────────┘
```

## Sequence Diagrams

### Login Sequence

```
Actor -> LoginView: enter credentials
LoginView -> LoginView: validate input
LoginView -> AuthenticationService: authenticateStudent(username, password)
AuthenticationService -> StudentDAO: findByUsername(username)
StudentDAO -> Database: SELECT * FROM students
Database -> StudentDAO: Student data
StudentDAO -> AuthenticationService: Optional<Student>
AuthenticationService -> AuthenticationService: verifyPassword()
AuthenticationService -> StudentDAO: updateLastLogin(studentId)
AuthenticationService -> LoginView: UserSession
LoginView -> MainApplication: showDashboard(session)
MainApplication -> StudentDashboard: create and show
```

### Submit Complaint Sequence

```
Actor -> StudentDashboard: click Submit Complaint
StudentDashboard -> ComplaintService: submitComplaint(complaint)
ComplaintService -> ComplaintService: validateComplaint(complaint)
ComplaintService -> StudentDAO: exists(studentId)
StudentDAO -> Database: SELECT COUNT(*) FROM students
Database -> StudentDAO: count
StudentDAO -> ComplaintService: boolean
ComplaintService -> CategoryDAO: exists(categoryId)
CategoryDAO -> Database: SELECT COUNT(*) FROM categories
Database -> CategoryDAO: count
CategoryDAO -> ComplaintService: boolean
ComplaintService -> ComplaintDAO: generateNextComplaintNumber()
ComplaintDAO -> Database: SELECT MAX(complaint_number)
Database -> ComplaintDAO: number
ComplaintDAO -> ComplaintService: String
ComplaintService -> ComplaintDAO: save(complaint)
ComplaintDAO -> Database: INSERT INTO complaints
Database -> ComplaintDAO: generated ID
ComplaintDAO -> ComplaintService: Complaint
ComplaintService -> ComplaintImageDAO: save(images)
ComplaintImageDAO -> Database: INSERT INTO complaint_images
ComplaintService -> NotificationDAO: save(notification)
NotificationDAO -> Database: INSERT INTO notifications
ComplaintService -> StudentDashboard: Complaint
StudentDashboard -> Actor: show success message
```

### Assign Complaint Sequence

```
Actor -> AdminDashboard: select complaint and staff
AdminDashboard -> ComplaintService: assignComplaint(complaintId, staffId, admin, ADMIN)
ComplaintService -> ComplaintDAO: findById(complaintId)
ComplaintDAO -> Database: SELECT * FROM complaints
Database -> ComplaintDAO: Complaint
ComplaintDAO -> ComplaintService: Optional<Complaint>
ComplaintService -> HostelStaffDAO: findById(staffId)
HostelStaffDAO -> Database: SELECT * FROM hostel_staff
Database -> HostelStaffDAO: HostelStaff
HostelStaffDAO -> ComplaintService: Optional<HostelStaff>
ComplaintService -> ComplaintDAO: assignToStaff(complaintId, staffId)
ComplaintDAO -> Database: UPDATE complaints SET assigned_staff_id
ComplaintService -> ComplaintDAO: updateStatus(complaintId, ASSIGNED, admin, ADMIN, notes)
ComplaintDAO -> ComplaintStatusHistoryDAO: save(history)
ComplaintStatusHistoryDAO -> Database: INSERT INTO complaint_status_history
ComplaintService -> NotificationDAO: save(notification)
NotificationDAO -> Database: INSERT INTO notifications
ComplaintService -> AdminDashboard: success
AdminDashboard -> Actor: show success message
```

## Component Diagram

```
┌─────────────────────────────────────────────────┐
│                 Presentation Layer              │
│  ┌──────────────┐  ┌──────────────┐            │
│  │  LoginView   │  │   Dashboard  │            │
│  └──────────────┘  └──────────────┘            │
└─────────────────────────────────────────────────┘
                    │ uses
                    ▼
┌─────────────────────────────────────────────────┐
│                 Service Layer                    │
│  ┌──────────────┐  ┌──────────────┐            │
│  │    Auth      │  │  Complaint   │            │
│  │   Service    │  │   Service    │            │
│  └──────────────┘  └──────────────┘            │
│  ┌──────────────┐  ┌──────────────┐            │
│  │   Student    │  │    Staff     │            │
│  │   Service    │  │   Service    │            │
│  └──────────────┘  └──────────────┘            │
│  ┌──────────────┐                                 │
│  │    Admin     │                                 │
│  │   Service    │                                 │
│  └──────────────┘                                 │
└─────────────────────────────────────────────────┘
                    │ uses
                    ▼
┌─────────────────────────────────────────────────┐
│                  DAO Layer                         │
│  ┌──────────────┐  ┌──────────────┐            │
│  │   Student    │  │ Complaint    │            │
│  │     DAO      │  │     DAO      │            │
│  └──────────────┘  └──────────────┘            │
│  ┌──────────────┐  ┌──────────────┐            │
│  │    Staff     │  │    Admin     │            │
│  │     DAO      │  │     DAO      │            │
│  └──────────────┘  └──────────────┘            │
│  ┌──────────────┐  ┌──────────────┐            │
│  │   Category   │  │    Block     │            │
│  │     DAO      │  │     DAO      │            │
│  └──────────────┘  └──────────────┘            │
└─────────────────────────────────────────────────┘
                    │ uses
                    ▼
┌─────────────────────────────────────────────────┐
│               Database Layer                      │
│  ┌──────────────┐  ┌──────────────┐            │
│  │ Database     │  │ Connection   │            │
│  │   Config     │  │    Pool      │            │
│  └──────────────┘  └──────────────┘            │
└─────────────────────────────────────────────────┘
                    │ JDBC
                    ▼
┌─────────────────────────────────────────────────┐
│              MySQL Database                        │
│  ┌───────────────────────────────────────┐     │
│  │           hostel_complaint_db          │     │
│  │  ┌──────────┐  ┌──────────┐          │     │
│  │  │ students  │  │complaints │          │     │
│  │  └──────────┘  └──────────┘          │     │
│  │  ┌──────────┐  ┌──────────┐          │     │
│  │  │   staff  │  │  blocks  │          │     │
│  │  └──────────┘  └──────────┘          │     │
│  └───────────────────────────────────────┘     │
└─────────────────────────────────────────────────┘
```

## Package Diagram

```
┌─────────────────────────────────────────────┐
│  com.hostel.complaint                         │
├─────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐           │
│  │  controller │  │    model    │           │
│  └─────────────┘  └─────────────┘           │
│  ┌─────────────┐  ┌─────────────┐           │
│  │     dao     │  │   service   │           │
│  └─────────────┘  └─────────────┘           │
│  ┌─────────────┐  ┌─────────────┐           │
│  │  database   │  │ validation  │           │
│  └─────────────┘  └─────────────┘           │
│  ┌─────────────┐  ┌─────────────┐           │
│  │    util     │  │    view     │           │
│  └─────────────┘  └─────────────┘           │
│  ┌─────────────┐                             │
│  │  resources  │                             │
│  └─────────────┘                             │
└─────────────────────────────────────────────┘
```

## Key Design Patterns Used

### 1. DAO Pattern
- Separates data access logic from business logic
- Provides abstraction over database operations
- Enables easy testing and maintenance

### 2. MVC Pattern
- Model: Entity classes represent data
- View: Swing components handle UI
- Controller: Service layer manages business logic

### 3. Factory Pattern
- DAO implementations are instantiated in service layer
- Centralized object creation

### 4. Singleton Pattern
- Database connection pool
- Authentication session management

### 5. Observer Pattern
- UI components listen to service events
- Dashboard updates on data changes

### 6. Strategy Pattern
- Different authentication strategies for user types
- Flexible validation strategies

## Design Principles Applied

### SOLID Principles
- **Single Responsibility**: Each class has one reason to change
- **Open/Closed**: Open for extension, closed for modification
- **Liskov Substitution**: DAO implementations can be substituted
- **Interface Segregation**: Specific interfaces for each entity
- **Dependency Inversion**: Depends on abstractions (interfaces)

### Clean Architecture
- Dependencies point inward
- Business logic independent of frameworks
- Database independent of business logic

### Design Patterns
- Reusable components
- Consistent coding standards
- Proper separation of concerns
