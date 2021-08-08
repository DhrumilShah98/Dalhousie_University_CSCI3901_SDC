import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class JavaConnection {
    public static void main(String[] args) {
        final String CSID = "CSID";
        final String BANNER_ID = "BANNER_ID";
        Scanner sc = new Scanner(System.in);
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        // Establish connection between Java program and the Database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.out.println("Error connecting to jdbc.");
        }

        // Connect to Dal Database
        try {
            connection = DriverManager.getConnection("jdbc:mysql://db.cs.dal.ca:3306/csci3901",
                    CSID, BANNER_ID);

            // Create a statement
            statement = connection.createStatement();

            while (true) {
                System.out.println("Please enter order number. Enter \"done\" to exit.");
                String orderNumber = sc.nextLine().trim();

				// Stop program execution on "done"
                if (orderNumber.equalsIgnoreCase("done")) {
                    System.out.println("Bye!");
                    break;
                }

                int orderNumberInt;
                try {
                    orderNumberInt = Integer.parseInt(orderNumber);
                } catch (Exception e) {
                    System.out.println("Please enter a valid order number.");
                    continue;
                }

				// Query to fetch the data
                final String query =
                        "Select\n" +
                                "orders.OrderID,\n" +
                                "orders.OrderDate,\n" +
                                "customers.ContactName,\n" +
                                "customers.Address,\n" +
                                "orderdetails.ProductID,\n" +
                                "orderdetails.UnitPrice,\n" +
                                "orderdetails.Quantity,\n" +
                                "orderdetails.UnitPrice * orderdetails.Quantity AS TotalCost\n" +
                                "from orders, customers, orderdetails where orders.OrderID\n" +
                                "= " + orderNumberInt + " and orderdetails.OrderID = " + orderNumberInt + " and orders.customerID = customers.CustomerID;";

                resultSet = statement.executeQuery(query);

				// Print the invoice on the screen
                if (resultSet.next()) {
                    final String orderID = resultSet.getString("OrderID");
                    final String orderDate = resultSet.getString("OrderDate");
                    final String contactName = resultSet.getString("ContactName");
                    final String address = resultSet.getString("Address");

                    final ArrayList<String> productCodes = new ArrayList<>();
                    final ArrayList<Double> unitPrices = new ArrayList<>();
                    final ArrayList<Integer> quantities = new ArrayList<>();
                    double totalCost = 0.0;

                    productCodes.add(resultSet.getString("ProductID"));
                    unitPrices.add(resultSet.getDouble("UnitPrice"));
                    quantities.add(resultSet.getInt("Quantity"));
                    totalCost += (unitPrices.get(unitPrices.size() - 1) * quantities.get(quantities.size() - 1));

                    while (resultSet.next()) {
                        productCodes.add(resultSet.getString("ProductID"));
                        unitPrices.add(resultSet.getDouble("UnitPrice"));
                        quantities.add(resultSet.getInt("Quantity"));
                        totalCost += (unitPrices.get(unitPrices.size() - 1) * quantities.get(quantities.size() - 1));
                    }


                    System.out.println("<========== Invoice ==========>");
                    System.out.println("Order Number: " + orderID);
                    System.out.println("Order Date: " + orderDate);
                    System.out.println("Name: " + contactName);
                    System.out.println("Address: " + address);

                    System.out.format("%12s%12s%12s%12s%n", "Product Code", "Quantity", "Unit Price", "Total");
                    for (int i = 0; i < productCodes.size(); ++i) {
                        System.out.format("%12s%12d%12.2f%12.2f%n", productCodes.get(i), quantities.get(i), unitPrices.get(i), (quantities.get(i) * unitPrices.get(i)));
                    }
                    System.out.println("\n");
                    System.out.println("Total Cost of the Order: " + totalCost);

                } else {
                    System.out.println("No Invoice Found");
                }

                resultSet.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                resultSet = null;
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                statement = null;
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                connection = null;
            }
        }
    }
}
