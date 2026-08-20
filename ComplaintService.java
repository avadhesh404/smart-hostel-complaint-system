import java.sql.*;

public class ComplaintService {

    public boolean registerUser(String username, String password, String role) {
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role.toUpperCase());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Registration failed. Username might already exist.");
            return false;
        }
    }

    public String loginUser(String username, String password) {
        String query = "SELECT role FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void fileComplaint(String student, String room, String desc) {
        String query = "INSERT INTO complaints (student_username, room_number, description) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, student);
            stmt.setString(2, room);
            stmt.setString(3, desc);
            stmt.executeUpdate();
            System.out.println("Complaint filed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewStudentComplaints(String student) {
        String query = "SELECT * FROM complaints WHERE student_username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, student);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println(new Complaint(rs.getInt("id"), rs.getString("student_username"),
                        rs.getString("room_number"), rs.getString("description"), rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewAllComplaints() {
        String query = "SELECT * FROM complaints";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println(new Complaint(rs.getInt("id"), rs.getString("student_username"),
                        rs.getString("room_number"), rs.getString("description"), rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resolveComplaint(int id) {
        String query = "UPDATE complaints SET status = 'RESOLVED' WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Complaint status updated to RESOLVED!");
            else System.out.println("Complaint ID not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}