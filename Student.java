import java.util.Scanner;

public class Student {
    private String username;
    private ComplaintService complaintService = new ComplaintService();

    public Student(String username) {
        this.username = username;
    }

    public void showMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Student Dashboard ---");
            System.out.println("1. File a Complaint");
            System.out.println("2. View My Complaints");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            if (choice == 1) {
                System.out.print("Enter Room Number: ");
                String room = scanner.nextLine();
                System.out.print("Enter Complaint Description: ");
                String desc = scanner.nextLine();
                complaintService.fileComplaint(username, room, desc);
            } else if (choice == 2) {
                complaintService.viewStudentComplaints(username);
            } else if (choice == 3) {
                System.out.println("Logging out...");
                break;
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }
}