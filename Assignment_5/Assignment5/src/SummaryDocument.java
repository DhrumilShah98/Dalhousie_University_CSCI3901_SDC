import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

/**
 * {@code SummaryDocument} class connects to the server, extracts the summary information from the database and creates an XML file.
 *
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @see CustomerInformation
 * @see ProductInformation
 * @see SupplierInformation
 * @since 2021-03-21
 */
public class SummaryDocument {

    // JDBC Driver.
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    // Database Path.
    private static final String DATABASE_PATH = "jdbc:mysql://db.cs.dal.ca:3306/csci3901";

    /**
     * Validate the dates.
     *
     * @param startDate Starting date for the period to summarize. (Format: YYYY-MM-DD)
     * @param endDate   Ending date for the period to summarize. (Format: YYYY-MM-DD)
     * @return {@code true} if valid otherwise {@code false}.
     */
    private boolean validateDates(String startDate, String endDate) {

        // Return false if start or end date is null.
        if (startDate == null || endDate == null) {
            return false;
        }

        startDate = startDate.trim();
        endDate = endDate.trim();

        // Check if date is of format YYYY-MM-DD.
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final Date startDateObj;
        final Date endDateObj;
        try {
            startDateObj = sdf.parse(startDate);
            endDateObj = sdf.parse(endDate);
        } catch (ParseException e) {
            // Return false if error occurs.
            return false;
        }

        // Ensure start date is not after end date.
        // Return true if valid otherwise false.
        return !startDateObj.after(endDateObj);
    }

    /**
     * Creates output file.
     *
     * @param outputFile Output file name.
     * @return {@code File} object if file is created otherwise {@code null}.
     */
    private File createOutputFile(String outputFile) {

        // Return null if output file name is null or is empty.
        if (outputFile == null || outputFile.trim().isEmpty()) {
            return null;
        }

        outputFile = outputFile.trim();

        // Return null if file extension is not ".xml".
        if (!outputFile.endsWith(".xml")) {
            return null;
        }

        // Create File object
        final File file = new File(outputFile);

        // Delete file if exists.
        if (file.exists()) {
            final boolean isFileDeleted = file.delete();
            if (!isFileDeleted) {
                // Return null if error occurs.
                return null;
            }
        }

        try {
            // Create new file.
            final boolean isNewFileCreated = file.createNewFile();
            if (!isNewFileCreated) {
                // Return null if error occurs.
                return null;
            }
        } catch (IOException e) {
            // Return null if error occurs.
            return null;
        }

        // Return File object.
        return file;
    }


    /**
     * Creates summary documentation over a period of time.
     *
     * @param startDate  Starting date for the period to summarize. (Format: YYYY-MM-DD)
     * @param endDate    Ending date for the period to summarize. (Format: YYYY-MM-DD)
     * @param connection Database connection object.
     * @return document created
     * @throws ParserConfigurationException if any error occurs while parsing.
     */
    private Document getSummaryDocument(String startDate, String endDate, Connection connection) throws ParserConfigurationException {
        // Get <customer_list>, <product_list> and <supplier_list> elements.
        final Element customerListElement = new CustomerInformation(connection).getCustomerListElement(startDate, endDate);
        final Element productListElement = new ProductInformation(connection).getProductListElement(startDate, endDate);
        final Element supplierListElement = new SupplierInformation(connection).getSupplierListElement(startDate, endDate);

        // Create a new document.
        final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        // Create element <period_summary>. (Root element)
        final Element periodSummaryEle = doc.createElement("period_summary");

        // Create <start_date> and <end_date> elements and add dates.
        final Element startDateElem = doc.createElement("start_date");
        final Element endDateElem = doc.createElement("end_date");
        startDateElem.appendChild(doc.createTextNode(startDate));
        endDateElem.appendChild(doc.createTextNode(endDate));

        // Add <start_date> and <end_date> elements into <period> element.
        final Element periodElem = doc.createElement("period");
        periodElem.appendChild(startDateElem);
        periodElem.appendChild(endDateElem);

        // Add <period> into <period_summary>.
        periodSummaryEle.appendChild(periodElem);

        // Add <customer_list> into <period_summary>.
        if (customerListElement != null) {
            doc.adoptNode(customerListElement);
            periodSummaryEle.appendChild(customerListElement);
        } else {
            periodSummaryEle.appendChild(doc.createElement("customer_list"));
        }

        // Add <product_list> into <period_summary>.
        if (productListElement != null) {
            doc.adoptNode(productListElement);
            periodSummaryEle.appendChild(productListElement);
        } else {
            periodSummaryEle.appendChild(doc.createElement("product_list"));
        }

        // Add <supplier_list> into <period_summary>.
        if (supplierListElement != null) {
            doc.adoptNode(supplierListElement);
            periodSummaryEle.appendChild(supplierListElement);
        } else {
            periodSummaryEle.appendChild(doc.createElement("supplier_list"));
        }

        // Add <period_summary> into the document.
        doc.appendChild(periodSummaryEle);
        return doc;
    }

    /**
     * Gets the data from the server and stores it in an output file.
     *
     * @param startDate  Starting date for the period to summarize. (Format: YYYY-MM-DD)
     * @param endDate    Ending date for the period to summarize. (Format: YYYY-MM-DD)
     * @param outputFile Output file name.
     * @return {@code true} if data is stored in output file otherwise {@code false}.
     */
    public boolean reportData(String startDate, String endDate, String outputFile) {

        // Return false if dates are not valid.
        if (!validateDates(startDate, endDate)) {
            return false;
        }

        // Return false if output file is not created.
        final File outputFileObj = createOutputFile(outputFile);
        if (outputFileObj == null) {
            return false;
        }

        startDate = startDate.trim();
        endDate = endDate.trim();

        // Check for the JDBC driver.
        try {
            Class.forName(JDBC_DRIVER).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            // Return false if error occurs.
            return false;
        }

        try (final Connection connection = DriverManager.getConnection(DATABASE_PATH, "CS_ID", "BANNER_ID")) {

            // Get summary documentation over a period of time.
            final Document doc = getSummaryDocument(startDate, endDate, connection);

            // Write in the output file.
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            tr.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(outputFileObj)));

        } catch (Exception e) {
            // Return false if error occurs.
            return false;
        }

        // Return true.
        return true;
    }
}