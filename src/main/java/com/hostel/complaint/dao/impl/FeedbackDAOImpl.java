package com.hostel.complaint.dao.impl;

import com.hostel.complaint.dao.FeedbackDAO;
import com.hostel.complaint.database.DatabaseConnection;
import com.hostel.complaint.model.Feedback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Feedback DAO implementation
 */
public class FeedbackDAOImpl implements FeedbackDAO {
    private static final Logger logger = LoggerFactory.getLogger(FeedbackDAOImpl.class);

    @Override
    public Feedback save(Feedback feedback) throws SQLException {
        String sql = "INSERT INTO feedback (complaint_id, student_id, rating, comments) VALUES (?, ?, ?, ?)";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            statement.setInt(1, feedback.getComplaintId());
            statement.setInt(2, feedback.getStudentId());
            
            if (feedback.getRating() != null) {
                statement.setInt(3, feedback.getRating());
            } else {
                statement.setNull(3, Types.INTEGER);
            }
            
            statement.setString(4, feedback.getComments());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating feedback failed, no rows affected.");
            }
            
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                feedback.setFeedbackId(resultSet.getInt(1));
            }
            
            connection.commit();
            logger.info("Feedback saved successfully: complaintId={}", feedback.getComplaintId());
            return feedback;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error saving feedback", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean update(Feedback feedback) throws SQLException {
        String sql = "UPDATE feedback SET rating = ?, comments = ? WHERE feedback_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            if (feedback.getRating() != null) {
                statement.setInt(1, feedback.getRating());
            } else {
                statement.setNull(1, Types.INTEGER);
            }
            
            statement.setString(2, feedback.getComments());
            statement.setInt(3, feedback.getFeedbackId());
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Feedback updated successfully: {}", feedback.getFeedbackId());
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error updating feedback", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM feedback WHERE feedback_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Feedback deleted successfully: {}", id);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error deleting feedback", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public Optional<Feedback> findById(int id) throws SQLException {
        String sql = "SELECT f.*, s.full_name as student_name, c.title as complaint_title " +
                     "FROM feedback f " +
                     "LEFT JOIN students s ON f.student_id = s.student_id " +
                     "LEFT JOIN complaints c ON f.complaint_id = c.complaint_id " +
                     "WHERE f.feedback_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToFeedback(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding feedback by ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Feedback> findAll() throws SQLException {
        String sql = "SELECT f.*, s.full_name as student_name, c.title as complaint_title " +
                     "FROM feedback f " +
                     "LEFT JOIN students s ON f.student_id = s.student_id " +
                     "LEFT JOIN complaints c ON f.complaint_id = c.complaint_id " +
                     "ORDER BY f.submitted_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            List<Feedback> feedbackList = new ArrayList<>();
            while (resultSet.next()) {
                feedbackList.add(mapResultSetToFeedback(resultSet));
            }
            
            return feedbackList;
            
        } catch (SQLException e) {
            logger.error("Error finding all feedback", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean exists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM feedback WHERE feedback_id = ?";
        
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
            logger.error("Error checking feedback existence", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM feedback";
        
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
            logger.error("Error counting feedback", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public Optional<Feedback> findByComplaintId(int complaintId) throws SQLException {
        String sql = "SELECT f.*, s.full_name as student_name, c.title as complaint_title " +
                     "FROM feedback f " +
                     "LEFT JOIN students s ON f.student_id = s.student_id " +
                     "LEFT JOIN complaints c ON f.complaint_id = c.complaint_id " +
                     "WHERE f.complaint_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, complaintId);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToFeedback(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding feedback by complaint ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Feedback> findByStudentId(int studentId) throws SQLException {
        String sql = "SELECT f.*, s.full_name as student_name, c.title as complaint_title " +
                     "FROM feedback f " +
                     "LEFT JOIN students s ON f.student_id = s.student_id " +
                     "LEFT JOIN complaints c ON f.complaint_id = c.complaint_id " +
                     "WHERE f.student_id = ? ORDER BY f.submitted_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, studentId);
            
            resultSet = statement.executeQuery();
            
            List<Feedback> feedbackList = new ArrayList<>();
            while (resultSet.next()) {
                feedbackList.add(mapResultSetToFeedback(resultSet));
            }
            
            return feedbackList;
            
        } catch (SQLException e) {
            logger.error("Error finding feedback by student ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean existsForComplaint(int complaintId, int studentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM feedback WHERE complaint_id = ? AND student_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, complaintId);
            statement.setInt(2, studentId);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error checking feedback existence for complaint", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public Double getAverageRatingForStaff(int staffId) throws SQLException {
        String sql = "SELECT AVG(f.rating) as avg_rating " +
                     "FROM feedback f " +
                     "JOIN complaints c ON f.complaint_id = c.complaint_id " +
                     "WHERE c.assigned_staff_id = ? AND f.rating IS NOT NULL";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, staffId);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getDouble("avg_rating");
            }
            
            return null;
            
        } catch (SQLException e) {
            logger.error("Error getting average rating for staff", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public Double getAverageRatingForCategory(int categoryId) throws SQLException {
        String sql = "SELECT AVG(f.rating) as avg_rating " +
                     "FROM feedback f " +
                     "JOIN complaints c ON f.complaint_id = c.complaint_id " +
                     "WHERE c.category_id = ? AND f.rating IS NOT NULL";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getDouble("avg_rating");
            }
            
            return null;
            
        } catch (SQLException e) {
            logger.error("Error getting average rating for category", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public Double getOverallAverageRating() throws SQLException {
        String sql = "SELECT AVG(rating) as avg_rating FROM feedback WHERE rating IS NOT NULL";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getDouble("avg_rating");
            }
            
            return null;
            
        } catch (SQLException e) {
            logger.error("Error getting overall average rating", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    private Feedback mapResultSetToFeedback(ResultSet resultSet) throws SQLException {
        Feedback feedback = new Feedback();
        
        feedback.setFeedbackId(resultSet.getInt("feedback_id"));
        feedback.setComplaintId(resultSet.getInt("complaint_id"));
        feedback.setStudentId(resultSet.getInt("student_id"));
        
        int rating = resultSet.getInt("rating");
        if (!resultSet.wasNull()) {
            feedback.setRating(rating);
        }
        
        feedback.setComments(resultSet.getString("comments"));
        
        Timestamp submittedAt = resultSet.getTimestamp("submitted_at");
        if (submittedAt != null) {
            feedback.setSubmittedAt(submittedAt.toLocalDateTime());
        }
        
        // Display fields
        feedback.setStudentName(resultSet.getString("student_name"));
        feedback.setComplaintTitle(resultSet.getString("complaint_title"));
        
        return feedback;
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
