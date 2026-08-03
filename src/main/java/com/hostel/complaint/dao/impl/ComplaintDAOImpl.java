package com.hostel.complaint.dao.impl;

import com.hostel.complaint.dao.ComplaintDAO;
import com.hostel.complaint.dao.ComplaintStatusHistoryDAO;
import com.hostel.complaint.database.DatabaseConnection;
import com.hostel.complaint.model.Complaint;
import com.hostel.complaint.model.ComplaintStatusHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Complaint DAO implementation
 * Provides database operations for complaint management
 */
public class ComplaintDAOImpl implements ComplaintDAO {
    private static final Logger logger = LoggerFactory.getLogger(ComplaintDAOImpl.class);
    
    private final ComplaintStatusHistoryDAO statusHistoryDAO;

    public ComplaintDAOImpl(ComplaintStatusHistoryDAO statusHistoryDAO) {
        this.statusHistoryDAO = statusHistoryDAO;
    }

    @Override
    public Complaint save(Complaint complaint) throws SQLException {
        String sql = "INSERT INTO complaints (complaint_number, student_id, category_id, title, description, " +
                     "block_id, room_id, priority, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            statement.setString(1, complaint.getComplaintNumber());
            statement.setInt(2, complaint.getStudentId());
            statement.setInt(3, complaint.getCategoryId());
            statement.setString(4, complaint.getTitle());
            statement.setString(5, complaint.getDescription());
            statement.setInt(6, complaint.getBlockId());
            statement.setInt(7, complaint.getRoomId());
            statement.setString(8, complaint.getPriority().name());
            statement.setString(9, complaint.getStatus().name());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating complaint failed, no rows affected.");
            }
            
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                complaint.setComplaintId(resultSet.getInt(1));
            }
            
            connection.commit();
            logger.info("Complaint saved successfully: {}", complaint.getComplaintNumber());
            return complaint;
            
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Error saving complaint", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean update(Complaint complaint) throws SQLException {
        String sql = "UPDATE complaints SET category_id = ?, assigned_staff_id = ?, title = ?, " +
                     "description = ?, priority = ?, status = ?, resolution_notes = ?, " +
                     "completion_image_path = ?, updated_at = ? WHERE complaint_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            statement.setInt(1, complaint.getCategoryId());
            if (complaint.getAssignedStaffId() != null) {
                statement.setInt(2, complaint.getAssignedStaffId());
            } else {
                statement.setNull(2, Types.INTEGER);
            }
            statement.setString(3, complaint.getTitle());
            statement.setString(4, complaint.getDescription());
            statement.setString(5, complaint.getPriority().name());
            statement.setString(6, complaint.getStatus().name());
            statement.setString(7, complaint.getResolutionNotes());
            statement.setString(8, complaint.getCompletionImagePath());
            statement.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(10, complaint.getComplaintId());
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Complaint updated successfully: {}", complaint.getComplaintId());
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Error updating complaint", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM complaints WHERE complaint_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Complaint deleted successfully: {}", id);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Error deleting complaint", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public Optional<Complaint> findById(int id) throws SQLException {
        String sql = "SELECT c.*, s.student_number, s.full_name as student_name, cat.category_name, " +
                     "hb.block_name, r.room_number, st.full_name as staff_name, st.role as staff_role " +
                     "FROM complaints c " +
                     "LEFT JOIN students s ON c.student_id = s.student_id " +
                     "LEFT JOIN categories cat ON c.category_id = cat.category_id " +
                     "LEFT JOIN hostel_blocks hb ON c.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON c.room_id = r.room_id " +
                     "LEFT JOIN hostel_staff st ON c.assigned_staff_id = st.staff_id " +
                     "WHERE c.complaint_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToComplaint(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding complaint by ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Complaint> findAll() throws SQLException {
        String sql = "SELECT c.*, s.student_number, s.full_name as student_name, cat.category_name, " +
                     "hb.block_name, r.room_number, st.full_name as staff_name, st.role as staff_role " +
                     "FROM complaints c " +
                     "LEFT JOIN students s ON c.student_id = s.student_id " +
                     "LEFT JOIN categories cat ON c.category_id = cat.category_id " +
                     "LEFT JOIN hostel_blocks hb ON c.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON c.room_id = r.room_id " +
                     "LEFT JOIN hostel_staff st ON c.assigned_staff_id = st.staff_id " +
                     "ORDER BY c.submitted_at DESC";
        
        return executeQuery(sql);
    }

    @Override
    public boolean exists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM complaints WHERE complaint_id = ?";
        
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
            logger.error("Error checking complaint existence", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM complaints";
        
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
            logger.error("Error counting complaints", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public Optional<Complaint> findByComplaintNumber(String complaintNumber) throws SQLException {
        String sql = "SELECT c.*, s.student_number, s.full_name as student_name, cat.category_name, " +
                     "hb.block_name, r.room_number, st.full_name as staff_name, st.role as staff_role " +
                     "FROM complaints c " +
                     "LEFT JOIN students s ON c.student_id = s.student_id " +
                     "LEFT JOIN categories cat ON c.category_id = cat.category_id " +
                     "LEFT JOIN hostel_blocks hb ON c.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON c.room_id = r.room_id " +
                     "LEFT JOIN hostel_staff st ON c.assigned_staff_id = st.staff_id " +
                     "WHERE c.complaint_number = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, complaintNumber);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToComplaint(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding complaint by number", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Complaint> findByStudentId(int studentId) throws SQLException {
        String sql = "SELECT c.*, s.student_number, s.full_name as student_name, cat.category_name, " +
                     "hb.block_name, r.room_number, st.full_name as staff_name, st.role as staff_role " +
                     "FROM complaints c " +
                     "LEFT JOIN students s ON c.student_id = s.student_id " +
                     "LEFT JOIN categories cat ON c.category_id = cat.category_id " +
                     "LEFT JOIN hostel_blocks hb ON c.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON c.room_id = r.room_id " +
                     "LEFT JOIN hostel_staff st ON c.assigned_staff_id = st.staff_id " +
                     "WHERE c.student_id = ? ORDER BY c.submitted_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, studentId);
            
            resultSet = statement.executeQuery();
            
            List<Complaint> complaints = new ArrayList<>();
            while (resultSet.next()) {
                complaints.add(mapResultSetToComplaint(resultSet));
            }
            
            return complaints;
            
        } catch (SQLException e) {
            logger.error("Error finding complaints by student ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Complaint> findByStaffId(int staffId) throws SQLException {
        String sql = "SELECT c.*, s.student_number, s.full_name as student_name, cat.category_name, " +
                     "hb.block_name, r.room_number, st.full_name as staff_name, st.role as staff_role " +
                     "FROM complaints c " +
                     "LEFT JOIN students s ON c.student_id = s.student_id " +
                     "LEFT JOIN categories cat ON c.category_id = cat.category_id " +
                     "LEFT JOIN hostel_blocks hb ON c.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON c.room_id = r.room_id " +
                     "LEFT JOIN hostel_staff st ON c.assigned_staff_id = st.staff_id " +
                     "WHERE c.assigned_staff_id = ? ORDER BY c.submitted_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, staffId);
            
            resultSet = statement.executeQuery();
            
            List<Complaint> complaints = new ArrayList<>();
            while (resultSet.next()) {
                complaints.add(mapResultSetToComplaint(resultSet));
            }
            
            return complaints;
            
        } catch (SQLException e) {
            logger.error("Error finding complaints by staff ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Complaint> findByStatus(Complaint.ComplaintStatus status) throws SQLException {
        String sql = "SELECT c.*, s.student_number, s.full_name as student_name, cat.category_name, " +
                     "hb.block_name, r.room_number, st.full_name as staff_name, st.role as staff_role " +
                     "FROM complaints c " +
                     "LEFT JOIN students s ON c.student_id = s.student_id " +
                     "LEFT JOIN categories cat ON c.category_id = cat.category_id " +
                     "LEFT JOIN hostel_blocks hb ON c.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON c.room_id = r.room_id " +
                     "LEFT JOIN hostel_staff st ON c.assigned_staff_id = st.staff_id " +
                     "WHERE c.status = ? ORDER BY c.submitted_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, status.name());
            
            resultSet = statement.executeQuery();
            
            List<Complaint> complaints = new ArrayList<>();
            while (resultSet.next()) {
                complaints.add(mapResultSetToComplaint(resultSet));
            }
            
            return complaints;
            
        } catch (SQLException e) {
            logger.error("Error finding complaints by status", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Complaint> findByPriority(Complaint.Priority priority) throws SQLException {
        String sql = "SELECT c.*, s.student_number, s.full_name as student_name, cat.category_name, " +
                     "hb.block_name, r.room_number, st.full_name as staff_name, st.role as staff_role " +
                     "FROM complaints c " +
                     "LEFT JOIN students s ON c.student_id = s.student_id " +
                     "LEFT JOIN categories cat ON c.category_id = cat.category_id " +
                     "LEFT JOIN hostel_blocks hb ON c.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON c.room_id = r.room_id " +
                     "LEFT JOIN hostel_staff st ON c.assigned_staff_id = st.staff_id " +
                     "WHERE c.priority = ? ORDER BY c.submitted_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, priority.name());
            
            resultSet = statement.executeQuery();
            
            List<Complaint> complaints = new ArrayList<>();
            while (resultSet.next()) {
                complaints.add(mapResultSetToComplaint(resultSet));
            }
            
            return complaints;
            
        } catch (SQLException e) {
            logger.error("Error finding complaints by priority", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Complaint> findByCategory(int categoryId) throws SQLException {
        String sql = "SELECT c.*, s.student_number, s.full_name as student_name, cat.category_name, " +
                     "hb.block_name, r.room_number, st.full_name as staff_name, st.role as staff_role " +
                     "FROM complaints c " +
                     "LEFT JOIN students s ON c.student_id = s.student_id " +
                     "LEFT JOIN categories cat ON c.category_id = cat.category_id " +
                     "LEFT JOIN hostel_blocks hb ON c.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON c.room_id = r.room_id " +
                     "LEFT JOIN hostel_staff st ON c.assigned_staff_id = st.staff_id " +
                     "WHERE c.category_id = ? ORDER BY c.submitted_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);
            
            resultSet = statement.executeQuery();
            
            List<Complaint> complaints = new ArrayList<>();
            while (resultSet.next()) {
                complaints.add(mapResultSetToComplaint(resultSet));
            }
            
            return complaints;
            
        } catch (SQLException e) {
            logger.error("Error finding complaints by category", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Complaint> findByBlockAndRoom(int blockId, int roomId) throws SQLException {
        String sql = "SELECT c.*, s.student_number, s.full_name as student_name, cat.category_name, " +
                     "hb.block_name, r.room_number, st.full_name as staff_name, st.role as staff_role " +
                     "FROM complaints c " +
                     "LEFT JOIN students s ON c.student_id = s.student_id " +
                     "LEFT JOIN categories cat ON c.category_id = cat.category_id " +
                     "LEFT JOIN hostel_blocks hb ON c.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON c.room_id = r.room_id " +
                     "LEFT JOIN hostel_staff st ON c.assigned_staff_id = st.staff_id " +
                     "WHERE c.block_id = ? AND c.room_id = ? ORDER BY c.submitted_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, blockId);
            statement.setInt(2, roomId);
            
            resultSet = statement.executeQuery();
            
            List<Complaint> complaints = new ArrayList<>();
            while (resultSet.next()) {
                complaints.add(mapResultSetToComplaint(resultSet));
            }
            
            return complaints;
            
        } catch (SQLException e) {
            logger.error("Error finding complaints by block and room", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Complaint> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        String sql = "SELECT c.*, s.student_number, s.full_name as student_name, cat.category_name, " +
                     "hb.block_name, r.room_number, st.full_name as staff_name, st.role as staff_role " +
                     "FROM complaints c " +
                     "LEFT JOIN students s ON c.student_id = s.student_id " +
                     "LEFT JOIN categories cat ON c.category_id = cat.category_id " +
                     "LEFT JOIN hostel_blocks hb ON c.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON c.room_id = r.room_id " +
                     "LEFT JOIN hostel_staff st ON c.assigned_staff_id = st.staff_id " +
                     "WHERE c.submitted_at BETWEEN ? AND ? ORDER BY c.submitted_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setTimestamp(1, Timestamp.valueOf(startDate));
            statement.setTimestamp(2, Timestamp.valueOf(endDate));
            
            resultSet = statement.executeQuery();
            
            List<Complaint> complaints = new ArrayList<>();
            while (resultSet.next()) {
                complaints.add(mapResultSetToComplaint(resultSet));
            }
            
            return complaints;
            
        } catch (SQLException e) {
            logger.error("Error finding complaints by date range", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Complaint> findWithFilters(Complaint.ComplaintStatus status, Complaint.Priority priority,
                                          Integer categoryId, Integer blockId, Integer roomId,
                                          LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT c.*, s.student_number, s.full_name as student_name, cat.category_name, " +
            "hb.block_name, r.room_number, st.full_name as staff_name, st.role as staff_role " +
            "FROM complaints c " +
            "LEFT JOIN students s ON c.student_id = s.student_id " +
            "LEFT JOIN categories cat ON c.category_id = cat.category_id " +
            "LEFT JOIN hostel_blocks hb ON c.block_id = hb.block_id " +
            "LEFT JOIN rooms r ON c.room_id = r.room_id " +
            "LEFT JOIN hostel_staff st ON c.assigned_staff_id = st.staff_id " +
            "WHERE 1=1 "
        );
        
        List<Object> parameters = new ArrayList<>();
        
        if (status != null) {
            sql.append("AND c.status = ? ");
            parameters.add(status.name());
        }
        
        if (priority != null) {
            sql.append("AND c.priority = ? ");
            parameters.add(priority.name());
        }
        
        if (categoryId != null) {
            sql.append("AND c.category_id = ? ");
            parameters.add(categoryId);
        }
        
        if (blockId != null) {
            sql.append("AND c.block_id = ? ");
            parameters.add(blockId);
        }
        
        if (roomId != null) {
            sql.append("AND c.room_id = ? ");
            parameters.add(roomId);
        }
        
        if (startDate != null && endDate != null) {
            sql.append("AND c.submitted_at BETWEEN ? AND ? ");
            parameters.add(Timestamp.valueOf(startDate));
            parameters.add(Timestamp.valueOf(endDate));
        }
        
        sql.append("ORDER BY c.submitted_at DESC");
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql.toString());
            
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            
            resultSet = statement.executeQuery();
            
            List<Complaint> complaints = new ArrayList<>();
            while (resultSet.next()) {
                complaints.add(mapResultSetToComplaint(resultSet));
            }
            
            return complaints;
            
        } catch (SQLException e) {
            logger.error("Error finding complaints with filters", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean updateStatus(int complaintId, Complaint.ComplaintStatus newStatus, String changedBy,
                               ComplaintStatusHistoryDAO.ChangedByRole changedByRole, String notes) throws SQLException {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            
            // Get current status
            Optional<Complaint> complaintOpt = findById(complaintId);
            if (!complaintOpt.isPresent()) {
                throw new SQLException("Complaint not found");
            }
            
            Complaint.ComplaintStatus oldStatus = complaintOpt.get().getStatus();
            
            // Update complaint status and timestamp
            String updateSql = "UPDATE complaints SET status = ?, ";
            LocalDateTime now = LocalDateTime.now();
            
            switch (newStatus) {
                case ASSIGNED:
                    updateSql += "assigned_at = ? ";
                    break;
                case IN_PROGRESS:
                    updateSql += "in_progress_at = ? ";
                    break;
                case RESOLVED:
                    updateSql += "resolved_at = ? ";
                    break;
                case CLOSED:
                    updateSql += "closed_at = ? ";
                    break;
                default:
                    updateSql += "updated_at = ? ";
            }
            
            updateSql += "WHERE complaint_id = ?";
            
            PreparedStatement updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setString(1, newStatus.name());
            updateStatement.setTimestamp(2, Timestamp.valueOf(now));
            updateStatement.setInt(3, complaintId);
            
            updateStatement.executeUpdate();
            
            // Create status history entry
            ComplaintStatusHistory history = new ComplaintStatusHistory(
                complaintId, oldStatus, newStatus, changedBy,
                ComplaintStatusHistory.ChangedByRole.valueOf(changedByRole.name()), notes
            );
            
            statusHistoryDAO.save(history);
            
            connection.commit();
            logger.info("Complaint status updated: {} -> {}", oldStatus, newStatus);
            return true;
            
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Error updating complaint status", e);
            throw e;
        } finally {
            if (connection != null) {
                DatabaseConnection.releaseConnection(connection);
            }
        }
    }

    @Override
    public boolean assignToStaff(int complaintId, int staffId) throws SQLException {
        String sql = "UPDATE complaints SET assigned_staff_id = ?, assigned_at = ?, status = 'Assigned' " +
                     "WHERE complaint_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            statement.setInt(1, staffId);
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(3, complaintId);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Complaint assigned to staff: complaintId={}, staffId={}", complaintId, staffId);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Error assigning complaint to staff", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public boolean updateResolution(int complaintId, String resolutionNotes, String completionImagePath) throws SQLException {
        String sql = "UPDATE complaints SET resolution_notes = ?, completion_image_path = ?, " +
                     "resolved_at = ?, status = 'Resolved' WHERE complaint_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            statement.setString(1, resolutionNotes);
            statement.setString(2, completionImagePath);
            statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(4, complaintId);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Complaint resolution updated: {}", complaintId);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error("Error updating complaint resolution", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public Map<String, Long> getStatistics() throws SQLException {
        String sql = "SELECT " +
                     "COUNT(*) as total, " +
                     "SUM(CASE WHEN status = 'Submitted' THEN 1 ELSE 0 END) as submitted, " +
                     "SUM(CASE WHEN status = 'Assigned' THEN 1 ELSE 0 END) as assigned, " +
                     "SUM(CASE WHEN status = 'In Progress' THEN 1 ELSE 0 END) as in_progress, " +
                     "SUM(CASE WHEN status = 'Resolved' THEN 1 ELSE 0 END) as resolved, " +
                     "SUM(CASE WHEN status = 'Closed' THEN 1 ELSE 0 END) as closed, " +
                     "SUM(CASE WHEN priority = 'Critical' THEN 1 ELSE 0 END) as critical " +
                     "FROM complaints";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            Map<String, Long> stats = new HashMap<>();
            if (resultSet.next()) {
                stats.put("total", resultSet.getLong("total"));
                stats.put("submitted", resultSet.getLong("submitted"));
                stats.put("assigned", resultSet.getLong("assigned"));
                stats.put("in_progress", resultSet.getLong("in_progress"));
                stats.put("resolved", resultSet.getLong("resolved"));
                stats.put("closed", resultSet.getLong("closed"));
                stats.put("critical", resultSet.getLong("critical"));
            }
            
            return stats;
            
        } catch (SQLException e) {
            logger.error("Error getting complaint statistics", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public double getAverageResolutionTime() throws SQLException {
        String sql = "SELECT AVG(TIMESTAMPDIFF(HOUR, submitted_at, resolved_at)) as avg_time " +
                     "FROM complaints WHERE resolved_at IS NOT NULL";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getDouble("avg_time");
            }
            
            return 0.0;
            
        } catch (SQLException e) {
            logger.error("Error getting average resolution time", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public String generateNextComplaintNumber() throws SQLException {
        String sql = "SELECT complaint_number FROM complaints " +
                     "WHERE complaint_number LIKE 'CMP%' " +
                     "ORDER BY complaint_number DESC LIMIT 1";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            int year = LocalDateTime.now().getYear();
            int sequence = 1;
            
            if (resultSet.next()) {
                String lastNumber = resultSet.getString("complaint_number");
                // Format: CMP2024001
                String yearPart = lastNumber.substring(3, 7);
                String seqPart = lastNumber.substring(7);
                
                if (Integer.parseInt(yearPart) == year) {
                    sequence = Integer.parseInt(seqPart) + 1;
                }
            }
            
            return String.format("CMP%04d%04d", year, sequence);
            
        } catch (SQLException e) {
            logger.error("Error generating complaint number", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Complaint> searchByStudentName(String studentName) throws SQLException {
        String sql = "SELECT c.*, s.student_number, s.full_name as student_name, cat.category_name, " +
                     "hb.block_name, r.room_number, st.full_name as staff_name, st.role as staff_role " +
                     "FROM complaints c " +
                     "LEFT JOIN students s ON c.student_id = s.student_id " +
                     "LEFT JOIN categories cat ON c.category_id = cat.category_id " +
                     "LEFT JOIN hostel_blocks hb ON c.block_id = hb.block_id " +
                     "LEFT JOIN rooms r ON c.room_id = r.room_id " +
                     "LEFT JOIN hostel_staff st ON c.assigned_staff_id = st.staff_id " +
                     "WHERE s.full_name LIKE ? ORDER BY c.submitted_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + studentName + "%");
            
            resultSet = statement.executeQuery();
            
            List<Complaint> complaints = new ArrayList<>();
            while (resultSet.next()) {
                complaints.add(mapResultSetToComplaint(resultSet));
            }
            
            return complaints;
            
        } catch (SQLException e) {
            logger.error("Error searching complaints by student name", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public Map<String, Long> getComplaintsByCategory() throws SQLException {
        String sql = "SELECT cat.category_name, COUNT(c.complaint_id) as count " +
                     "FROM categories cat " +
                     "LEFT JOIN complaints c ON cat.category_id = c.category_id " +
                     "GROUP BY cat.category_name " +
                     "ORDER BY count DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            Map<String, Long> result = new LinkedHashMap<>();
            while (resultSet.next()) {
                result.put(resultSet.getString("category_name"), resultSet.getLong("count"));
            }
            
            return result;
            
        } catch (SQLException e) {
            logger.error("Error getting complaints by category", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Map<String, Object>> getMonthlyTrends(int months) throws SQLException {
        String sql = "SELECT DATE_FORMAT(submitted_at, '%Y-%m') as month, COUNT(*) as count " +
                     "FROM complaints " +
                     "WHERE submitted_at >= DATE_SUB(NOW(), INTERVAL ? MONTH) " +
                     "GROUP BY DATE_FORMAT(submitted_at, '%Y-%m') " +
                     "ORDER BY month ASC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, months);
            
            resultSet = statement.executeQuery();
            
            List<Map<String, Object>> trends = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, Object> trend = new HashMap<>();
                trend.put("month", resultSet.getString("month"));
                trend.put("count", resultSet.getLong("count"));
                trends.add(trend);
            }
            
            return trends;
            
        } catch (SQLException e) {
            logger.error("Error getting monthly trends", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    private Complaint mapResultSetToComplaint(ResultSet resultSet) throws SQLException {
        Complaint complaint = new Complaint();
        
        complaint.setComplaintId(resultSet.getInt("complaint_id"));
        complaint.setComplaintNumber(resultSet.getString("complaint_number"));
        complaint.setStudentId(resultSet.getInt("student_id"));
        complaint.setCategoryId(resultSet.getInt("category_id"));
        
        int staffId = resultSet.getInt("assigned_staff_id");
        if (!resultSet.wasNull()) {
            complaint.setAssignedStaffId(staffId);
        }
        
        complaint.setTitle(resultSet.getString("title"));
        complaint.setDescription(resultSet.getString("description"));
        complaint.setBlockId(resultSet.getInt("block_id"));
        complaint.setRoomId(resultSet.getInt("room_id"));
        complaint.setPriority(Complaint.Priority.valueOf(resultSet.getString("priority")));
        complaint.setStatus(Complaint.ComplaintStatus.valueOf(resultSet.getString("status")));
        
        Timestamp submittedAt = resultSet.getTimestamp("submitted_at");
        if (submittedAt != null) {
            complaint.setSubmittedAt(submittedAt.toLocalDateTime());
        }
        
        Timestamp assignedAt = resultSet.getTimestamp("assigned_at");
        if (assignedAt != null) {
            complaint.setAssignedAt(assignedAt.toLocalDateTime());
        }
        
        Timestamp inProgressAt = resultSet.getTimestamp("in_progress_at");
        if (inProgressAt != null) {
            complaint.setInProgressAt(inProgressAt.toLocalDateTime());
        }
        
        Timestamp resolvedAt = resultSet.getTimestamp("resolved_at");
        if (resolvedAt != null) {
            complaint.setResolvedAt(resolvedAt.toLocalDateTime());
        }
        
        Timestamp closedAt = resultSet.getTimestamp("closed_at");
        if (closedAt != null) {
            complaint.setClosedAt(closedAt.toLocalDateTime());
        }
        
        complaint.setResolutionNotes(resultSet.getString("resolution_notes"));
        complaint.setCompletionImagePath(resultSet.getString("completion_image_path"));
        
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            complaint.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = resultSet.getTimestamp("updated_at");
        if (updatedAt != null) {
            complaint.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        // Display fields
        complaint.setStudentNumber(resultSet.getString("student_number"));
        complaint.setStudentName(resultSet.getString("student_name"));
        complaint.setCategoryName(resultSet.getString("category_name"));
        complaint.setBlockName(resultSet.getString("block_name"));
        complaint.setRoomNumber(resultSet.getString("room_number"));
        complaint.setStaffName(resultSet.getString("staff_name"));
        complaint.setStaffRole(resultSet.getString("staff_role"));
        
        return complaint;
    }

    private List<Complaint> executeQuery(String sql) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            List<Complaint> complaints = new ArrayList<>();
            while (resultSet.next()) {
                complaints.add(mapResultSetToComplaint(resultSet));
            }
            
            return complaints;
            
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
