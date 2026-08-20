# SmartHostelComplaint System

A console-based terminal application built natively using Java SE and plain SQL via JDBC.

## Project Structure
- `Main.java`: Root application dashboard router.
- `Student.java`: Student functional profile view.
- `Admin.java`: System admin console toolkit.
- `Complaint.java`: Struct object mapping a hostel issue entry.
- `ComplaintService.java`: Business operations mapping directly to SQL Statements.
- `DatabaseConnection.java`: Native DriverManager initialization.

## Requirements
- Java Development Kit (JDK 8 or above).
- MySQL Server database instance running.
- MySQL Connector/J driver dependency (`mysql-connector-j-26.7.0.jar`).

## Getting Started
1. Run the `schema.sql` queries inside your database client.
2. Update your credentials inside `DatabaseConnection.java`.
3. Compile all files from your source root directory:
   ```bash
   javac *.java
   ```
4. Run your application appending your downloaded driver package to the classpath:
   ```bash
   java -cp ".;mysql-connector-j-26.7.0.jar" Main
   ```