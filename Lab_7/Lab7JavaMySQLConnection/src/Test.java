import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Test {
    public static void main(String[] args) {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        // Load a connection library between Java and the database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            System.out.println("Error connecting to jdbc");
        }

        try {
            // Connect to the Dal database
            connection = DriverManager.getConnection("jdbc:mysql://db.cs.dal.ca:3306/csci3901",
                    "dashah", "B00857606");

            // Create a statement
            statement = connection.createStatement();

            System.out.println("Northwind company overview\n");

            // Run a query
            resultSet = statement.executeQuery("select * from employees order by LastName;");

            // Print out the results
            System.out.println("Employees:");
            while (resultSet.next()) {
                String name = resultSet.getString("LastName") + ", " + resultSet.getString("FirstName");
                System.out.println("\t" + name + ": " + resultSet.getString(4));
            }
            resultSet.close();

            // Run another query
            resultSet = statement.executeQuery("select * from products order by ProductID;");

            // Print out the results
            System.out.println("\nProducts:");
            while (resultSet.next()) {
                System.out.printf("\t%2d - %s ($%.2f/unit)\n",
                        resultSet.getInt("ProductID"),
                        resultSet.getString("ProductName"),
                        resultSet.getDouble("UnitPrice"));
            }
            resultSet.close();

        } catch (SQLException e) {
            // handle any errors
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());

        } finally {

            if (resultSet != null) {
                try { resultSet.close(); } catch (SQLException sqlEx) { }
                resultSet = null;
            }

            if (statement != null) {
                try { statement.close(); } catch (SQLException sqlEx) { } // ignore
                statement = null;
            }

            if (connection != null) {
                try { connection.close(); } catch (SQLException sqlEx) { } // ignore
                connection = null;
            }
        }
    }

}