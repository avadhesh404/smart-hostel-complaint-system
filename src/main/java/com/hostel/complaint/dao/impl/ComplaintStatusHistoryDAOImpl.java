package com.hostel.complaint.dao.impl;

import com.hostel.complaint.dao.ComplaintStatusHistoryDAO;
import com.hostel.complaint.database.DatabaseConnection;
import com.hostel.complaint.model.Complaint;
import com.hostel.complaint.model.ComplaintStatusHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Complaint Status History DAO implementation
 */
public class ComplaintStatusHistoryDAOImpl implements ComplaintStatusHistoryDAO {
    private static final Logger logger = LoggerFactory.getLogger(ComplaintStatusHistoryDAOImpl.class);

    @Override
    public ComplaintStatusHistory save(ComplaintStatusHistory history) throws SQLException {
        String sql = "INSERT INTO complaint_status_history (complaint_id, old_status, new_status, " +
                     "changed_by, changed_by_role, notes) VALUES (?, ?, ?, ?, ?, ?)";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            statement.setInt(1, history.getComplaintId());
            
            if (history.getOldStatus() != null) {
                statement.setString(2, history.getOldStatus().name());
            } else {
                statement.setNull(2, Types.VARCHAR);
            }
            
            statement.setString(3, history.getNewStatus().name());
            statement.setString(4, history.getChangedBy());
            statement.setString(5, history.getChangedByRole().name());
            statement.setString(6, history.getNotes());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating status history failed, no rows affected.");
            }
            
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                history.setHistoryId(resultSet.getInt(1));
            }
            
            connection.commit();
            return history;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error saving status history", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean update(ComplaintStatusHistory history) throws SQLException {
        // Status history is immutable - no update needed
        return false;
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM complaint_status_history WHERE history_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error deleting status history", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public Optional<ComplaintStatusHistory> findById(int id) throws SQLException {
        String sql = "SELECT * FROM complaint_status_history WHERE history_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToHistory(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding status history by ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<ComplaintStatusHistory> findAll() throws SQLException {
        String sql = "SELECT * FROM complaint_status_history ORDER BY changed_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            List<ComplaintStatusHistory> historyList = new ArrayList<>();
            while (resultSet.next()) {
                historyList.add(mapResultSetToHistory(resultSet));
            }
            
            return historyList;
            
        } catch (SQLException e) {
            logger.error("Error finding all status history", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean exists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM complaint_status_history WHERE history_id = ?";
        
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
            logger.error("Error checking status history existence", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM complaint_status_history";
        
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
            logger.error("Error counting status history", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<ComplaintStatusHistory> findByComplaintId(int complaintId) throws SQLException {
        String sql = "SELECT * FROM complaint_status_history WHERE complaint_id = ? ORDER BY changed_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, complaintId);
            
            resultSet = statement.executeQuery();
            
            List<ComplaintStatusHistory> historyList = new ArrayList<>();
            while (resultSet.next()) {
                historyList.add(mapResultSetToHistory(resultSet));
            }
            
            return historyList;
            
        } catch (SQLException e) {
            logger.error("Error finding status history by complaint ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    private ComplaintStatusHistory mapResultSetToHistory(ResultSet resultSet) throws SQLException {
        ComplaintStatusHistory history = new ComplaintStatusHistory();
        
        history.setHistoryId(resultSet.getInt("history_id"));
        history.setComplaintId(resultSet.getInt("complaint_id"));
        
        String oldStatus = resultSet.getString("old_status");
        if (oldStatus != null) {
            history.setOldStatus(Complaint.ComplaintStatus.valueOf(oldStatus.replace(" ", "_")));
        }
        
        history.setNewStatus(Complaint.ComplaintStatus.valueOf(resultSet.getString("new_status").replace(" ", "_")));
        history.setChangedBy(resultSet.getString("changed_by"));
        history.setChangedByRole(ComplaintStatusHistory.ChangedByRole.valueOf(resultSet.getString("changed_by_role")));
        history.setNotes(resultSet.getString("notes"));
        
        Timestamp changedAt = resultSet.getTimestamp("changed_at");
        if (changedAt != null) {
            history.setChangedAt(changedAt.toLocalDateTime());
        }
        
        return history;
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
