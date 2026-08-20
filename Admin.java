import java.util.Scanner;

public class Admin {
    private ComplaintService complaintService = new ComplaintService();

    public void showMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Admin Dashboard ---");
            System.out.println("1. View All Complaints");
            System.out.println("2. Resolve a Complaint");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            if (choice == 1) {
                complaintService.viewAllComplaints();
            } else if (choice == 2) {
                System.out.print("Enter Complaint ID to resolve: ");
                int id = scanner.nextInt();
                complaintService.resolveComplaint(id);
            } else if (choice == 3) {
                System.out.println("Logging out...");
                break;
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }
}