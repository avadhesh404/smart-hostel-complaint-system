public class Complaint {
    private int id;
    private String studentUsername;
    private String roomNumber;
    private String description;
    private String status;

    public Complaint(int id, String studentUsername, String roomNumber, String description, String status) {
        this.id = id;
        this.studentUsername = studentUsername;
        this.roomNumber = roomNumber;
        this.description = description;
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Room: %s | Status: %s | Description: %s (By: %s)", 
                id, roomNumber, status, description, studentUsername);
    }
}