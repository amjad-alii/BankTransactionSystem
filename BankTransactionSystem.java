import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:mysql://localhost:3306/us_bank";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Ali@1234";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found: " + e.getMessage());
            return;
        }

        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            System.out.println("Connection established successfully");
            con.setAutoCommit(false); // Start transaction

            Scanner sc = new Scanner(System.in);
            
            // Input sender and receiver account details
            System.out.print("Enter sender's account number: ");
            int senderAcc = sc.nextInt();

            System.out.print("Enter receiver's account number: ");
            int receiverAcc = sc.nextInt();

            System.out.print("Enter amount to transfer: ");
            int amount = sc.nextInt();
            
            // Check if sender has sufficient balance BEFORE deducting
            if (!hasSufficientBalance(con, senderAcc, amount)) {
                System.out.println("Transaction failed: Insufficient balance.");
                return;
            }

            // Debit sender's account
            String debitQuery = "UPDATE account SET balance = balance - ? WHERE account_no = ?";
            try (PreparedStatement debitStmt = con.prepareStatement(debitQuery)) {
                debitStmt.setInt(1, amount);
                debitStmt.setInt(2, senderAcc);
                int debitRows = debitStmt.executeUpdate();
                
                // Credit receiver's account
                String creditQuery = "UPDATE account SET balance = balance + ? WHERE account_no = ?";
                try (PreparedStatement creditStmt = con.prepareStatement(creditQuery)) {
                    creditStmt.setInt(1, amount);
                    creditStmt.setInt(2, receiverAcc);
                    int creditRows = creditStmt.executeUpdate();

                    // If both debit and credit are successful, commit transaction
                    if (debitRows > 0 && creditRows > 0) {
                        con.commit();
                        System.out.println("Transaction successful! ₹" + amount + " transferred.");
                    } else {
                        con.rollback();
                        System.out.println("Transaction failed! Rolling back...");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    // Function to check if an account has sufficient balance
    private static boolean hasSufficientBalance(Connection con, int accountNo, int amount) {
        String query = "SELECT balance FROM account WHERE account_no = ?";
        try (PreparedStatement stt = con.prepareStatement(query)) {
            stt.setInt(1, accountNo);
            ResultSet rs = stt.executeQuery();
            if (rs.next()) {
                int currentBalance = rs.getInt("balance");
                return currentBalance >= amount;
            }
        } catch (SQLException e) {
            System.out.println("Error checking balance: " + e.getMessage());
        }
        return false;
    }
}
