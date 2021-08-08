import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * {@code SupplierInformation} class to get the suppliers summary information from the database.
 *
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @since 2021-03-21
 */
public class SupplierInformation {
    // EMPTY element.
    private static final String EMPTY = "null";

    // Database connection object.
    private final Connection connection;

    /**
     * Class constructor {@code SupplierInformation}.
     *
     * @param connection Database connection object.
     */
    public SupplierInformation(Connection connection) {
        this.connection = connection;
    }

    /**
     * Gets the suppliers list summary information for a particular time period.
     *
     * @param startDate Starting date for the period to summarize. (Format: YYYY-MM-DD)
     * @param endDate   Ending date for the period to summarize. (Format: YYYY-MM-DD)
     * @return suppliers list if exists otherwise {@code null}.
     */
    public Element getSupplierListElement(String startDate, String endDate) {

        // Query to extract suppliers information for a particular time period.
        final String supplierListQuery = "SELECT " +
                "s.CompanyName, " +
                "s.Address, s.City, s.Region, s.PostalCode, s.Country, " +
                "SUM(od.Quantity) AS NumProducts, " +
                "SUM(od.UnitPrice * od.Quantity) AS ProductValue " +
                "FROM " +
                "suppliers AS s, products AS p, orderdetails AS od, orders AS o " +
                "WHERE " +
                "s.SupplierID = p.SupplierID AND " +
                "p.ProductID = od.ProductID AND " +
                "o.OrderID = od.OrderID AND " +
                "o.OrderDate BETWEEN \"" + startDate + "\" AND \"" + endDate + "\" " +
                "GROUP BY " +
                "s.SupplierID " +
                "ORDER BY " +
                "s.CompanyName ASC;";

        try (final Statement statement = connection.createStatement();
             final ResultSet supplierListResultSet = statement.executeQuery(supplierListQuery)
        ) {

            // Create a new document.
            final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            // Create element <supplier_list>. (Root element)
            final Element supplierListEle = doc.createElement("supplier_list");

            // Iterate through the suppliers list result set.
            while (supplierListResultSet.next()) {

                // Create element <supplier_name> and add CompanyName if available otherwise add EMPTY.
                final Element supplierNameEle = doc.createElement("supplier_name");
                supplierNameEle.appendChild(doc.createTextNode(
                        (supplierListResultSet.getString("CompanyName") != null) ?
                                supplierListResultSet.getString("CompanyName") :
                                EMPTY));


                // Create element <street_address> and add Address if available otherwise add EMPTY.
                final Element streetAddressEle = doc.createElement("street_address");
                streetAddressEle.appendChild(doc.createTextNode(
                        (supplierListResultSet.getString("Address") != null) ?
                                supplierListResultSet.getString("Address").replace("\n", "") :
                                EMPTY));

                // Create element <city> and add City if available otherwise add EMPTY.
                final Element cityEle = doc.createElement("city");
                cityEle.appendChild(doc.createTextNode(
                        (supplierListResultSet.getString("City") != null) ?
                                supplierListResultSet.getString("City") :
                                EMPTY));

                // Create element <region> and add Region if available otherwise add EMPTY.
                final Element regionEle = doc.createElement("region");
                regionEle.appendChild(doc.createTextNode(
                        (supplierListResultSet.getString("Region") != null) ?
                                supplierListResultSet.getString("Region") :
                                EMPTY));

                // Create element <postal_code> and add PostalCode if available otherwise add EMPTY.
                final Element postalCodeEle = doc.createElement("postal_code");
                postalCodeEle.appendChild(doc.createTextNode(
                        (supplierListResultSet.getString("PostalCode") != null) ?
                                supplierListResultSet.getString("PostalCode") :
                                EMPTY));

                // Create element <country> and add Country if available otherwise add EMPTY.
                final Element countryEle = doc.createElement("country");
                countryEle.appendChild(doc.createTextNode(
                        (supplierListResultSet.getString("Country") != null) ?
                                supplierListResultSet.getString("Country") :
                                EMPTY));

                // Create element <num_products> and add NumProducts if available otherwise add EMPTY.
                final Element numProductsEle = doc.createElement("num_products");
                numProductsEle.appendChild(doc.createTextNode(
                        (supplierListResultSet.getString("NumProducts") != null) ?
                                supplierListResultSet.getString("NumProducts") :
                                EMPTY));

                // Create element <product_value> and add ProductValue if available otherwise add EMPTY.
                final Element productValueEle = doc.createElement("product_value");
                productValueEle.appendChild(doc.createTextNode(
                        (supplierListResultSet.getString("ProductValue") != null) ?
                                supplierListResultSet.getString("ProductValue") :
                                EMPTY));

                // Create element <address>. Add <street_address>, <city>, <region>, <postal_code> and <country> into <address>.
                final Element addressEle = doc.createElement("address");
                addressEle.appendChild(streetAddressEle);
                addressEle.appendChild(cityEle);
                addressEle.appendChild(regionEle);
                addressEle.appendChild(postalCodeEle);
                addressEle.appendChild(countryEle);

                // Create element <supplier>. Add <supplier_name>, <address>, <num_products> and <product_value> into <supplier>.
                final Element supplierEle = doc.createElement("supplier");
                supplierEle.appendChild(supplierNameEle);
                supplierEle.appendChild(addressEle);
                supplierEle.appendChild(numProductsEle);
                supplierEle.appendChild(productValueEle);

                // Add <supplier> into <supplier_list>.
                supplierListEle.appendChild(supplierEle);
            }

            // Return <supplier_list>.
            return supplierListEle;
        } catch (Exception e) {
            // Return null if error occurs.
            return null;
        }
    }
}