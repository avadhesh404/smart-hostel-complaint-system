package com.hostel.complaint.dao.impl;

import com.hostel.complaint.dao.HostelStaffDAO;
import com.hostel.complaint.database.DatabaseConnection;
import com.hostel.complaint.model.HostelStaff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Hostel Staff DAO implementation
 * Provides database operations for hostel staff management
 */
public class HostelStaffDAOImpl implements HostelStaffDAO {
    private static final Logger logger = LoggerFactory.getLogger(HostelStaffDAOImpl.class);

    @Override
    public HostelStaff save(HostelStaff staff) throws SQLException {
        String sql = "INSERT INTO hostel_staff (employee_number, username, password, full_name, email, phone, " +
                     "role, specialization, block_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            statement.setString(1, staff.getEmployeeNumber());
            statement.setString(2, staff.getUsername());
            statement.setString(3, staff.getPassword());
            statement.setString(4, staff.getFullName());
            statement.setString(5, staff.getEmail());
            statement.setString(6, staff.getPhone());
            statement.setString(7, staff.getRole().name());
            statement.setString(8, staff.getSpecialization());
            
            if (staff.getBlockId() != null) {
                statement.setInt(9, staff.getBlockId());
            } else {
                statement.setNull(9, Types.INTEGER);
            }
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating staff failed, no rows affected.");
            }
            
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                staff.setStaffId(resultSet.getInt(1));
            }
            
            connection.commit();
            logger.info("Staff saved successfully: {}", staff.getEmployeeNumber());
            return staff;
            
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Error saving staff", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean update(HostelStaff staff) throws SQLException {
        String sql = "UPDATE hostel_staff SET employee_number = ?, username = ?, password = ?, full_name = ?, " +
                     "email = ?, phone = ?, role = ?, specialization = ?, block_id = ?, is_available = ?, " +
                     "updated_at = ? WHERE staff_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            statement.setString(1, staff.getEmployeeNumber());
            statement.setString(2, staff.getUsername());
            statement.setString(3, staff.getPassword());
            statement.setString(4, staff.getFullName());
            statement.setString(5, staff.getEmail());
            statement.setString(6, staff.getPhone());
            statement.setString(7, staff.getRole().name());
            statement.setString(8, staff.getSpecialization());
            
            if (staff.getBlockId() != null) {
                statement.setInt(9, staff.getBlockId());
            } else {
                statement.setNull(9, Types.INTEGER);
            }
            
            statement.setBoolean(10, staff.isAvailable());
            statement.setTimestamp(11, Timestamp.valueOf(java.time.LocalDateTime.now()));
            statement.setInt(12, staff.getStaffId());
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Staff updated successfully: {}", staff.getStaffId());
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Error updating staff", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM hostel_staff WHERE staff_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Staff deleted successfully: {}", id);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Error deleting staff", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public Optional<HostelStaff> findById(int id) throws SQLException {
        String sql = "SELECT s.*, hb.block_name FROM hostel_staff s " +
                     "LEFT JOIN hostel_blocks hb ON s.block_id = hb.block_id " +
                     "WHERE s.staff_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToStaff(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding staff by ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<HostelStaff> findAll() throws SQLException {
        String sql = "SELECT s.*, hb.block_name FROM hostel_staff s " +
                     "LEFT JOIN hostel_blocks hb ON s.block_id = hb.block_id " +
                     "ORDER BY s.full_name";
        
        return executeQuery(sql);
    }

    @Override
    public boolean exists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM hostel_staff WHERE staff_id = ?";
        
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
            logger.error("Error checking staff existence", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM hostel_staff";
        
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
            logger.error("Error counting staff", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public Optional<HostelStaff> findByEmployeeNumber(String employeeNumber) throws SQLException {
        String sql = "SELECT s.*, hb.block_name FROM hostel_staff s " +
                     "LEFT JOIN hostel_blocks hb ON s.block_id = hb.block_id " +
                     "WHERE s.employee_number = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, employeeNumber);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToStaff(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding staff by employee number", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public Optional<HostelStaff> findByUsername(String username) throws SQLException {
        String sql = "SELECT s.*, hb.block_name FROM hostel_staff s " +
                     "LEFT JOIN hostel_blocks hb ON s.block_id = hb.block_id " +
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
                return Optional.of(mapResultSetToStaff(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding staff by username", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public Optional<HostelStaff> findByEmail(String email) throws SQLException {
        String sql = "SELECT s.*, hb.block_name FROM hostel_staff s " +
                     "LEFT JOIN hostel_blocks hb ON s.block_id = hb.block_id " +
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
                return Optional.of(mapResultSetToStaff(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding staff by email", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<HostelStaff> findByRole(HostelStaff.StaffRole role) throws SQLException {
        String sql = "SELECT s.*, hb.block_name FROM hostel_staff s " +
                     "LEFT JOIN hostel_blocks hb ON s.block_id = hb.block_id " +
                     "WHERE s.role = ? ORDER BY s.full_name";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, role.name());
            
            resultSet = statement.executeQuery();
            
            List<HostelStaff> staffList = new ArrayList<>();
            while (resultSet.next()) {
                staffList.add(mapResultSetToStaff(resultSet));
            }
            
            return staffList;
            
        } catch (SQLException e) {
            logger.error("Error finding staff by role", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<HostelStaff> findByBlockId(int blockId) throws SQLException {
        String sql = "SELECT s.*, hb.block_name FROM hostel_staff s " +
                     "LEFT JOIN hostel_blocks hb ON s.block_id = hb.block_id " +
                     "WHERE s.block_id = ? ORDER BY s.full_name";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, blockId);
            
            resultSet = statement.executeQuery();
            
            List<HostelStaff> staffList = new ArrayList<>();
            while (resultSet.next()) {
                staffList.add(mapResultSetToStaff(resultSet));
            }
            
            return staffList;
            
        } catch (SQLException e) {
            logger.error("Error finding staff by block ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<HostelStaff> findAvailable() throws SQLException {
        String sql = "SELECT s.*, hb.block_name FROM hostel_staff s " +
                     "LEFT JOIN hostel_blocks hb ON s.block_id = hb.block_id " +
                     "WHERE s.is_available = true AND s.is_active = true ORDER BY s.full_name";
        
        return executeQuery(sql);
    }

    @Override
    public List<HostelStaff> findAvailableByRole(HostelStaff.StaffRole role) throws SQLException {
        String sql = "SELECT s.*, hb.block_name FROM hostel_staff s " +
                     "LEFT JOIN hostel_blocks hb ON s.block_id = hb.block_id " +
                     "WHERE s.role = ? AND s.is_available = true AND s.is_active = true ORDER BY s.full_name";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, role.name());
            
            resultSet = statement.executeQuery();
            
            List<HostelStaff> staffList = new ArrayList<>();
            while (resultSet.next()) {
                staffList.add(mapResultSetToStaff(resultSet));
            }
            
            return staffList;
            
        } catch (SQLException e) {
            logger.error("Error finding available staff by role", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<HostelStaff> searchByName(String name) throws SQLException {
        String sql = "SELECT s.*, hb.block_name FROM hostel_staff s " +
                     "LEFT JOIN hostel_blocks hb ON s.block_id = hb.block_id " +
                     "WHERE s.full_name LIKE ? ORDER BY s.full_name";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + name + "%");
            
            resultSet = statement.executeQuery();
            
            List<HostelStaff> staffList = new ArrayList<>();
            while (resultSet.next()) {
                staffList.add(mapResultSetToStaff(resultSet));
            }
            
            return staffList;
            
        } catch (SQLException e) {
            logger.error("Error searching staff by name", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean employeeNumberExists(String employeeNumber) throws SQLException {
        String sql = "SELECT COUNT(*) FROM hostel_staff WHERE employee_number = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, employeeNumber);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error checking employee number existence", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM hostel_staff WHERE username = ?";
        
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
        String sql = "SELECT COUNT(*) FROM hostel_staff WHERE email = ?";
        
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
    public boolean updateLastLogin(int staffId) throws SQLException {
        String sql = "UPDATE hostel_staff SET last_login = ? WHERE staff_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setTimestamp(1, Timestamp.valueOf(java.time.LocalDateTime.now()));
            statement.setInt(2, staffId);
            
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
    public boolean updatePassword(int staffId, String newPassword) throws SQLException {
        String sql = "UPDATE hostel_staff SET password = ?, updated_at = ? WHERE staff_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, newPassword);
            statement.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            statement.setInt(3, staffId);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Password updated for staff: {}", staffId);
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
    public boolean updateAvailability(int staffId, boolean isAvailable) throws SQLException {
        String sql = "UPDATE hostel_staff SET is_available = ?, updated_at = ? WHERE staff_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setBoolean(1, isAvailable);
            statement.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            statement.setInt(3, staffId);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Availability updated for staff: {}", staffId);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Error updating availability", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public boolean assignToBlock(int staffId, int blockId) throws SQLException {
        String sql = "UPDATE hostel_staff SET block_id = ?, updated_at = ? WHERE staff_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, blockId);
            statement.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            statement.setInt(3, staffId);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Staff assigned to block: staffId={}, blockId={}", staffId, blockId);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Error assigning staff to block", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    private HostelStaff mapResultSetToStaff(ResultSet resultSet) throws SQLException {
        HostelStaff staff = new HostelStaff();
        
        staff.setStaffId(resultSet.getInt("staff_id"));
        staff.setEmployeeNumber(resultSet.getString("employee_number"));
        staff.setUsername(resultSet.getString("username"));
        staff.setPassword(resultSet.getString("password"));
        staff.setFullName(resultSet.getString("full_name"));
        staff.setEmail(resultSet.getString("email"));
        staff.setPhone(resultSet.getString("phone"));
        staff.setRole(HostelStaff.StaffRole.valueOf(resultSet.getString("role")));
        staff.setSpecialization(resultSet.getString("specialization"));
        
        int blockId = resultSet.getInt("block_id");
        if (!resultSet.wasNull()) {
            staff.setBlockId(blockId);
        }
        
        staff.setAvailable(resultSet.getBoolean("is_available"));
        
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            staff.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = resultSet.getTimestamp("updated_at");
        if (updatedAt != null) {
            staff.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        Timestamp lastLogin = resultSet.getTimestamp("last_login");
        if (lastLogin != null) {
            staff.setLastLogin(lastLogin.toLocalDateTime());
        }
        
        staff.setActive(resultSet.getBoolean("is_active"));
        
        // Display fields
        staff.setBlockName(resultSet.getString("block_name"));
        
        return staff;
    }

    private List<HostelStaff> executeQuery(String sql) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            List<HostelStaff> staffList = new ArrayList<>();
            while (resultSet.next()) {
                staffList.add(mapResultSetToStaff(resultSet));
            }
            
            return staffList;
            
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
