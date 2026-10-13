/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package databaseconnectionoop_assignment;
        
        import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author DELL
 */
public class DatabaseConnectionOOP_Assignment {

    /**
     * @param args the command line arguments
     */
 
    // Database Configuration Credentials
    private static final String URL = "jdbc:mysql://localhost:3306/student_assignment_db";
    private static final String USER = "root"; // Default MySQL user
    private static final String PASSWORD = "qwertyuioP1234!@"; // Your MySQL password

    public static void main(String[] args) {
        Connection connection = null;

        try {
            // 1. Register JDBC Driver (Optional in newer JDBC versions, but good practice)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            System.out.println("Connecting to database...");
            
            // 2. Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            
            System.out.println("Database connection established successfully!");

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found. Include it in your libraries.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed! Check output console for errors.");
            e.printStackTrace();
        } finally {
            // 3. Close the connection to release resources
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    System.out.println("Connection closed safely.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
