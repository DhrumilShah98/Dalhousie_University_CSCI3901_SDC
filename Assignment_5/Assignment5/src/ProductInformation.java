import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * {@code ProductInformation} class to get the products summary information from the database.
 *
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @since 2021-03-21
 */
public class ProductInformation {
    // EMPTY element.
    private static final String EMPTY = "null";

    // Database connection object.
    private final Connection connection;

    /**
     * Class constructor {@code ProductInformation}.
     *
     * @param connection Database connection object.
     */
    public ProductInformation(Connection connection) {
        this.connection = connection;
    }

    /**
     * Gets the products list summary information for a particular time period.
     *
     * @param startDate Starting date for the period to summarize. (Format: YYYY-MM-DD)
     * @param endDate   Ending date for the period to summarize. (Format: YYYY-MM-DD)
     * @return products list if exists otherwise {@code null}.
     */
    public Element getProductListElement(String startDate, String endDate) {

        // Query to extract products information for a particular time period.
        final String productListQuery = "SELECT " +
                "c.CategoryName, p.ProductName, s.CompanyName, " +
                "SUM(od.Quantity) AS UnitsSold, " +
                "SUM(od.Quantity * od.UnitPrice) as SaleValue " +
                "FROM " +
                "orderdetails AS od, products AS p, suppliers AS s, categories AS c, orders AS o " +
                "WHERE " +
                "od.ProductID = p.ProductID AND " +
                "p.SupplierID = s.SupplierID AND " +
                "p.CategoryID = c.CategoryID AND " +
                "o.OrderID = od.OrderID AND " +
                "o.OrderDate BETWEEN \"" + startDate + "\" AND \"" + endDate + "\" " +
                "GROUP BY " +
                "od.ProductID " +
                "ORDER BY " +
                "c.CategoryName ASC, p.ProductName ASC, s.CompanyName ASC;";

        try (final Statement statement = connection.createStatement();
             final ResultSet productListResultSet = statement.executeQuery(productListQuery)
        ) {
            // Create a new document.
            final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            // Create element <product_list>. (Root element)
            final Element productListEle = doc.createElement("product_list");

            // Previous category name.
            String previousCategoryName;

            // Current category name.
            String currentCategoryName = null;

            // Category element.
            Element categoryEle = null;

            // Iterate through the products list result set.
            while (productListResultSet.next()) {

                // Create element <product_name> and add ProductName if available otherwise add EMPTY.
                final Element productNameEle = doc.createElement("product_name");
                productNameEle.appendChild(doc.createTextNode(
                        (productListResultSet.getString("ProductName") != null) ?
                                productListResultSet.getString("ProductName") :
                                EMPTY));

                // Create element <supplier_name> and add CompanyName if available otherwise add EMPTY.
                final Element supplierNameEle = doc.createElement("supplier_name");
                supplierNameEle.appendChild(doc.createTextNode(
                        (productListResultSet.getString("CompanyName") != null) ?
                                productListResultSet.getString("CompanyName") :
                                EMPTY));

                // Create element <units_sold> and add UnitsSold if available otherwise add EMPTY.
                final Element unitsSoldEle = doc.createElement("units_sold");
                unitsSoldEle.appendChild(doc.createTextNode(
                        (productListResultSet.getString("UnitsSold") != null) ?
                                productListResultSet.getString("UnitsSold") :
                                EMPTY));

                // Create element <sale_value> and add SaleValue if available otherwise add EMPTY.
                final Element saleValueEle = doc.createElement("sale_value");
                saleValueEle.appendChild(doc.createTextNode(
                        (productListResultSet.getString("SaleValue") != null) ?
                                productListResultSet.getString("SaleValue") :
                                EMPTY));

                // Create element <product>. Add <product_name>, <supplier_name>, <units_sold> and <sale_value> into <product>.
                final Element productEle = doc.createElement("product");
                productEle.appendChild(productNameEle);
                productEle.appendChild(supplierNameEle);
                productEle.appendChild(unitsSoldEle);
                productEle.appendChild(saleValueEle);

                // Update previous category name with current category name.
                previousCategoryName = currentCategoryName;

                // Current category name
                currentCategoryName = (productListResultSet.getString("CategoryName") != null) ?
                        productListResultSet.getString("CategoryName") : EMPTY;

                // Create category list
                if (previousCategoryName == null) {

                    // Create element <category_name> and add CategoryName if available otherwise add EMPTY.
                    final Element categoryNameEle = doc.createElement("category_name");
                    categoryNameEle.appendChild(doc.createTextNode(currentCategoryName));

                    // Create element <category>. Add <category_name> and <product> into <category>.
                    categoryEle = doc.createElement("category");
                    categoryEle.appendChild(categoryNameEle);
                    categoryEle.appendChild(productEle);

                } else if (previousCategoryName.equals(currentCategoryName)) {
                    //  Add <product> into <category>.
                    categoryEle.appendChild(productEle);
                } else {
                    // Add <category> into <product_list>.
                    productListEle.appendChild(categoryEle);

                    // Create element <category_name> and add CategoryName if available otherwise add EMPTY.
                    final Element categoryNameEle = doc.createElement("category_name");
                    categoryNameEle.appendChild(doc.createTextNode(currentCategoryName));

                    // Create element <category>. Add <category_name> and <product> into <category>.
                    categoryEle = doc.createElement("category");
                    categoryEle.appendChild(categoryNameEle);
                    categoryEle.appendChild(productEle);
                }
            }

            // Add last <category> into <product_list>.
            productListEle.appendChild(categoryEle);

            // Return <product_list>.
            return productListEle;
        } catch (Exception e) {
            // Return null if error occurs.
            return null;
        }
    }
}