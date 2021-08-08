import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * {@code CustomerInformation} class to get the customers summary information from the database.
 *
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @since 2021-03-21
 */
public class CustomerInformation {
    // EMPTY element.
    private static final String EMPTY = "null";

    // Database connection object.
    private final Connection connection;

    /**
     * Class constructor {@code CustomerInformation}.
     *
     * @param connection Database connection object.
     */
    public CustomerInformation(Connection connection) {
        this.connection = connection;
    }

    /**
     * Gets the customers list summary information for a particular time period.
     *
     * @param startDate Starting date for the period to summarize. (Format: YYYY-MM-DD)
     * @param endDate   Ending date for the period to summarize. (Format: YYYY-MM-DD)
     * @return customers list if exists otherwise {@code null}.
     */
    public Element getCustomerListElement(String startDate, String endDate) {

        // Query to extract customers information for a particular time period.
        final String customerListQuery = "SELECT " +
                "c.CompanyName, " +
                "c.Address, c.City, c.Region, c.PostalCode, c.Country, " +
                "COUNT(DISTINCT o.OrderID) AS NumOrders, " +
                "SUM(od.UnitPrice * od.Quantity) AS OrderValue " +
                "FROM " +
                "customers AS c, orders AS o, orderdetails AS od " +
                "WHERE " +
                "c.CustomerID = o.CustomerID AND " +
                "o.OrderID = od.OrderID AND " +
                "o.OrderDate BETWEEN \"" + startDate + "\" AND \"" + endDate + "\" " +
                "GROUP BY " +
                "o.CustomerID " +
                "ORDER BY " +
                "c.CompanyName ASC;";

        try (final Statement statement = connection.createStatement();
             final ResultSet customerListResultSet = statement.executeQuery(customerListQuery)
        ) {
            // Create a new document.
            final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            // Create element <customer_list>. (Root element)
            final Element customerListEle = doc.createElement("customer_list");

            // Iterate through the customers list result set.
            while (customerListResultSet.next()) {

                // Create element <customer_name> and add CompanyName if available otherwise add EMPTY.
                final Element customerNameEle = doc.createElement("customer_name");
                customerNameEle.appendChild(doc.createTextNode(
                        (customerListResultSet.getString("CompanyName") != null) ?
                                customerListResultSet.getString("CompanyName") :
                                EMPTY));

                // Create element <street_address> and add Address if available otherwise add EMPTY.
                final Element streetAddressEle = doc.createElement("street_address");
                streetAddressEle.appendChild(doc.createTextNode(
                        (customerListResultSet.getString("Address") != null) ?
                                customerListResultSet.getString("Address") :
                                EMPTY));

                // Create element <city> and add City if available otherwise add EMPTY.
                final Element cityEle = doc.createElement("city");
                cityEle.appendChild(doc.createTextNode(
                        (customerListResultSet.getString("City") != null) ?
                                customerListResultSet.getString("City") :
                                EMPTY));

                // Create element <region> and add Region if available otherwise add EMPTY.
                final Element regionEle = doc.createElement("region");
                regionEle.appendChild(doc.createTextNode(
                        (customerListResultSet.getString("Region") != null) ?
                                customerListResultSet.getString("Region") :
                                EMPTY));

                // Create element <postal_code> and add PostalCode if available otherwise add EMPTY.
                final Element postalCodeEle = doc.createElement("postal_code");
                postalCodeEle.appendChild(doc.createTextNode(
                        (customerListResultSet.getString("PostalCode") != null) ?
                                customerListResultSet.getString("PostalCode") :
                                EMPTY));

                // Create element <country> and add Country if available otherwise add EMPTY.
                final Element countryEle = doc.createElement("country");
                countryEle.appendChild(doc.createTextNode(
                        (customerListResultSet.getString("Country") != null) ?
                                customerListResultSet.getString("Country") :
                                EMPTY));

                // Create element <num_orders> and add NumOrders if available otherwise add EMPTY.
                final Element numOrdersEle = doc.createElement("num_orders");
                numOrdersEle.appendChild(doc.createTextNode(
                        (customerListResultSet.getString("NumOrders") != null) ?
                                customerListResultSet.getString("NumOrders") :
                                EMPTY));

                // Create element <order_value> and add OrderValue if available otherwise add EMPTY.
                final Element orderValueEle = doc.createElement("order_value");
                orderValueEle.appendChild(doc.createTextNode(
                        (customerListResultSet.getString("OrderValue") != null) ?
                                customerListResultSet.getString("OrderValue") :
                                EMPTY));


                // Create element <address>. Add <street_address>, <city>, <region>, <postal_code> and <country> into <address>.
                final Element addressEle = doc.createElement("address");
                addressEle.appendChild(streetAddressEle);
                addressEle.appendChild(cityEle);
                addressEle.appendChild(regionEle);
                addressEle.appendChild(postalCodeEle);
                addressEle.appendChild(countryEle);

                // Create element <customer>. Add <customer_name>, <address>, <num_orders> and <order_value> into <customer>.
                final Element customerEle = doc.createElement("customer");
                customerEle.appendChild(customerNameEle);
                customerEle.appendChild(addressEle);
                customerEle.appendChild(numOrdersEle);
                customerEle.appendChild(orderValueEle);

                // Add <customer> into <customer_list>.
                customerListEle.appendChild(customerEle);
            }

            // Return <customer_list>.
            return customerListEle;
        } catch (Exception e) {
            // Return null if error occurs.
            return null;
        }
    }
}