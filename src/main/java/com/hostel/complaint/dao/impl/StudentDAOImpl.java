package com.hostel.complaint.dao.impl;

import com.hostel.complaint.dao.StudentDAO;
import com.hostel.complaint.database.DatabaseConnection;
import com.hostel.complaint.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Student DAO implementation
 * Provides database operations for student management
 */
public class StudentDAOImpl implements StudentDAO {
    private static final Logger logger = LoggerFactory.getLogger(StudentDAOImpl.class);

    @Override
    public Student save(Student student) throws SQLException {
        String sql = "INSERT INTO students (student_number, username, password, full_name, email, phone, " +
                     "block_id, room_id, course, year_of_study) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            statement.setString(1, student.getStudentNumber());
            statement.setString(2, student.getUsername());
            statement.setString(3, student.getPassword());
            statement.setString(4, student.getFullName());
            statement.setString(5, student.getEmail());
            statement.setString(6, student.getPhone());
            
            if (student.getBlockId() != null) {
                statement.setInt(7, student.getBlockId());
            } else {
                statement.setNull(7, Types.INTEGER);
            }
            
            if (student.getRoomId() != null) {
                statement.setInt(8, student.getRoomId());
            } else {
                statement.setNull(8, Types.INTEGER);
            }
            
            statement.setString(9, student.getCourse());
            
            if (student.getYearOfStudy() != null) {
                statement.setInt(10, student.getYearOfStudy());
            } else {
                statement.setNull(10, Types.INTEGER);
            }
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating student failed, no rows affected.");
            }
            
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                student.setStudentId(resultSet.getInt(1));
            }
            
            connection.commit();
            logger.info("Student saved successfully: {}", student.getStudentNumber());
            return student;
            
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Error saving student", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean update(Student student) throws SQLException {
        String sql = "UPDATE students SET student_number = ?, username = ?, password = ?, full_name = ?, " +
                     "email = ?, phone = ?, block_id = ?, room_id = ?, course = ?, year_of_study = ?, " +
                     "updated_at = ? WHERE student_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            statement.setString(1, student.getStudentNumber());
            statement.setString(2, student.getUsername());
            statement.setString(3, student.getPassword());
            statement.setString(4, student.getFullName());
            statement.setString(5, student.getEmail());
            statement.setString(6, student.getPhone());
            
            if (student.getBlockId() != null) {
                statement.setInt(7, student.getBlockId());
            } else {
                statement.setNull(7, Types.INTEGER);
            }
            
            if (student.getRoomId() != null) {
                statement.setInt(8, student.getRoomId());
            } else {
                statement.setNull(8, Types.INTEGER);
            }
            
            statement.setString(9, student.getCourse());
            
            if (student.getYearOfStudy() != null) {
                statement.setInt(10, student.getYearOfStudy());
            } else {
                statement.setNull(10, Types.INTEGER);
            }
            
            statement.setTimestamp(11, Timestamp.valueOf(java.time.LocalDateTime.now()));
            statement.setInt(12, student.getStudentId());
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Student updated successfully: {}", student.getStudentId());
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Error updating student", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE student_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Student deleted successfully: {}", id);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Error deleting student", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public Optional<Student> findById(int id) throws SQLException {
        String sql = "SELECT s.*, hb.block_name, r.room_number FROM students s " +
                     "LEFT JOIN hostel_blocks hb ON s.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON s.room_id = r.room_id " +
                     "WHERE s.student_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToStudent(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding student by ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Student> findAll() throws SQLException {
        String sql = "SELECT s.*, hb.block_name, r.room_number FROM students s " +
                     "LEFT JOIN hostel_blocks hb ON s.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON s.room_id = r.room_id " +
                     "ORDER BY s.full_name";
        
        return executeQuery(sql);
    }

    @Override
    public boolean exists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM students WHERE student_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error checking student existence", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM students";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            
            return 0;
            
        } catch (SQLException e) {
            logger.error("Error counting students", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public Optional<Student> findByStudentNumber(String studentNumber) throws SQLException {
        String sql = "SELECT s.*, hb.block_name, r.room_number FROM students s " +
                     "LEFT JOIN hostel_blocks hb ON s.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON s.room_id = r.room_id " +
                     "WHERE s.student_number = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, studentNumber);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToStudent(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding student by student number", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public Optional<Student> findByUsername(String username) throws SQLException {
        String sql = "SELECT s.*, hb.block_name, r.room_number FROM students s " +
                     "LEFT JOIN hostel_blocks hb ON s.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON s.room_id = r.room_id " +
                     "WHERE s.username = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToStudent(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding student by username", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public Optional<Student> findByEmail(String email) throws SQLException {
        String sql = "SELECT s.*, hb.block_name, r.room_number FROM students s " +
                     "LEFT JOIN hostel_blocks hb ON s.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON s.room_id = r.room_id " +
                     "WHERE s.email = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToStudent(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding student by email", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Student> findByBlockId(int blockId) throws SQLException {
        String sql = "SELECT s.*, hb.block_name, r.room_number FROM students s " +
                     "LEFT JOIN hostel_blocks hb ON s.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON s.room_id = r.room_id " +
                     "WHERE s.block_id = ? ORDER BY s.full_name";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, blockId);
            
            resultSet = statement.executeQuery();
            
            List<Student> students = new ArrayList<>();
            while (resultSet.next()) {
                students.add(mapResultSetToStudent(resultSet));
            }
            
            return students;
            
        } catch (SQLException e) {
            logger.error("Error finding students by block ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Student> findByRoomId(int roomId) throws SQLException {
        String sql = "SELECT s.*, hb.block_name, r.room_number FROM students s " +
                     "LEFT JOIN hostel_blocks hb ON s.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON s.room_id = r.room_id " +
                     "WHERE s.room_id = ? ORDER BY s.full_name";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, roomId);
            
            resultSet = statement.executeQuery();
            
            List<Student> students = new ArrayList<>();
            while (resultSet.next()) {
                students.add(mapResultSetToStudent(resultSet));
            }
            
            return students;
            
        } catch (SQLException e) {
            logger.error("Error finding students by room ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Student> findByCourse(String course) throws SQLException {
        String sql = "SELECT s.*, hb.block_name, r.room_number FROM students s " +
                     "LEFT JOIN hostel_blocks hb ON s.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON s.room_id = r.room_id " +
                     "WHERE s.course = ? ORDER BY s.full_name";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, course);
            
            resultSet = statement.executeQuery();
            
            List<Student> students = new ArrayList<>();
            while (resultSet.next()) {
                students.add(mapResultSetToStudent(resultSet));
            }
            
            return students;
            
        } catch (SQLException e) {
            logger.error("Error finding students by course", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Student> findByYearOfStudy(int year) throws SQLException {
        String sql = "SELECT s.*, hb.block_name, r.room_number FROM students s " +
                     "LEFT JOIN hostel_blocks hb ON s.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON s.room_id = r.room_id " +
                     "WHERE s.year_of_study = ? ORDER BY s.full_name";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, year);
            
            resultSet = statement.executeQuery();
            
            List<Student> students = new ArrayList<>();
            while (resultSet.next()) {
                students.add(mapResultSetToStudent(resultSet));
            }
            
            return students;
            
        } catch (SQLException e) {
            logger.error("Error finding students by year of study", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Student> searchByName(String name) throws SQLException {
        String sql = "SELECT s.*, hb.block_name, r.room_number FROM students s " +
                     "LEFT JOIN hostel_blocks hb ON s.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON s.room_id = r.room_id " +
                     "WHERE s.full_name LIKE ? ORDER BY s.full_name";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + name + "%");
            
            resultSet = statement.executeQuery();
            
            List<Student> students = new ArrayList<>();
            while (resultSet.next()) {
                students.add(mapResultSetToStudent(resultSet));
            }
            
            return students;
            
        } catch (SQLException e) {
            logger.error("Error searching students by name", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean studentNumberExists(String studentNumber) throws SQLException {
        String sql = "SELECT COUNT(*) FROM students WHERE student_number = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, studentNumber);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error checking student number existence", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM students WHERE username = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error checking username existence", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM students WHERE email = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error checking email existence", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean updateLastLogin(int studentId) throws SQLException {
        String sql = "UPDATE students SET last_login = ? WHERE student_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setTimestamp(1, Timestamp.valueOf(java.time.LocalDateTime.now()));
            statement.setInt(2, studentId);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Error updating last login", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public boolean updatePassword(int studentId, String newPassword) throws SQLException {
        String sql = "UPDATE students SET password = ?, updated_at = ? WHERE student_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, newPassword);
            statement.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            statement.setInt(3, studentId);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Password updated for student: {}", studentId);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Error updating password", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public boolean assignToRoom(int studentId, int blockId, int roomId) throws SQLException {
        String sql = "UPDATE students SET block_id = ?, room_id = ?, updated_at = ? WHERE student_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, blockId);
            statement.setInt(2, roomId);
            statement.setTimestamp(3, Timestamp.valueOf(java.time.LocalDateTime.now()));
            statement.setInt(4, studentId);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Student assigned to room: studentId={}, blockId={}, roomId={}", studentId, blockId, roomId);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Error assigning student to room", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    private Student mapResultSetToStudent(ResultSet resultSet) throws SQLException {
        Student student = new Student();
        
        student.setStudentId(resultSet.getInt("student_id"));
        student.setStudentNumber(resultSet.getString("student_number"));
        student.setUsername(resultSet.getString("username"));
        student.setPassword(resultSet.getString("password"));
        student.setFullName(resultSet.getString("full_name"));
        student.setEmail(resultSet.getString("email"));
        student.setPhone(resultSet.getString("phone"));
        
        int blockId = resultSet.getInt("block_id");
        if (!resultSet.wasNull()) {
            student.setBlockId(blockId);
        }
        
        int roomId = resultSet.getInt("room_id");
        if (!resultSet.wasNull()) {
            student.setRoomId(roomId);
        }
        
        student.setCourse(resultSet.getString("course"));
        
        int yearOfStudy = resultSet.getInt("year_of_study");
        if (!resultSet.wasNull()) {
            student.setYearOfStudy(yearOfStudy);
        }
        
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            student.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = resultSet.getTimestamp("updated_at");
        if (updatedAt != null) {
            student.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        Timestamp lastLogin = resultSet.getTimestamp("last_login");
        if (lastLogin != null) {
            student.setLastLogin(lastLogin.toLocalDateTime());
        }
        
        student.setActive(resultSet.getBoolean("is_active"));
        
        // Display fields
        student.setBlockName(resultSet.getString("block_name"));
        student.setRoomNumber(resultSet.getString("room_number"));
        
        return student;
    }

    private List<Student> executeQuery(String sql) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            List<Student> students = new ArrayList<>();
            while (resultSet.next()) {
                students.add(mapResultSetToStudent(resultSet));
            }
            
            return students;
            
        } catch (SQLException e) {
            logger.error("Error executing query", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    private void closeResources(ResultSet resultSet, PreparedStatement statement, Connection connection) {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) DatabaseConnection.releaseConnection(connection);
        } catch (SQLException e) {
            logger.error("Error closing resources", e);
        }
    }
}
