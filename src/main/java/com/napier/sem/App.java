package com.napier.sem;

import java.sql.*;

public class App {
    // Hold the connection for this instance
    private Connection con = null;

    public static void main(String[] args) {
        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();

        a.disconnect();
    }
    public Employee getEmployee(int id) {
        try {
            Statement stmt = con.createStatement();
            String query =
                    "SELECT emp_no, first_name, last_name " +
                            "FROM employees " +
                            "WHERE emp_no = " + id;
            ResultSet rs = stmt.executeQuery(query);

            if (!rs.next()) return null;

            Employee e = new Employee();
            e.emp_no = rs.getInt("emp_no");
            e.first_name = rs.getString("first_name");
            e.last_name = rs.getString("last_name");
            return e;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public void connect() {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 100;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(30000);

                // Connect to database — NOTE: assign to the FIELD, not a local var
                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root",
                        "example"
                );

                System.out.println("Successfully connected");
                return; // success
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Disconnected");
            } catch (SQLException e) {
                System.out.println("Error closing connection to database");
            }
        }
    }
}