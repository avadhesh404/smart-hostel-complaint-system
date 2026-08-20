import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ComplaintService authService = new ComplaintService();

        System.out.println("=== Welcome to Smart Hostel Complaint System ===");

        while (true) {
            System.out.println("\n1. Register Student\n2. Login\n3. Exit");
            System.out.print("Select an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); 

            if (option == 1) {
                System.out.print("Enter New Username: ");
                String user = scanner.nextLine();
                System.out.print("Enter New Password: ");
                String pass = scanner.nextLine();
                if (authService.registerUser(user, pass, "STUDENT")) {
                    System.out.println("Registration successful! You can now log in.");
                }
            } else if (option == 2) {
                System.out.print("Username: ");
                String user = scanner.nextLine();
                System.out.print("Password: ");
                String pass = scanner.nextLine();

                String role = authService.loginUser(user, pass);
                if (role != null) {
                    System.out.println("Login Successful! Role: " + role);
                    if (role.equals("ADMIN")) {
                        new Admin().showMenu(scanner);
                    } else {
                        new Student(user).showMenu(scanner);
                    }
                } else {
                    System.out.println("Invalid credentials.");
                }
            } else if (option == 3) {
                System.out.println("Thank you for using SmartHostel!");
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
        scanner.close();
    }
}