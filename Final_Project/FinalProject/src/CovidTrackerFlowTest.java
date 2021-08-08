import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

@DisplayName("CovidTracker flow test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CovidTrackerFlowTest {
    private static Government government;
    private static MobileDevice m1, m2, m3, m4, m5;

    @DisplayName("Setup before flow execution")
    @BeforeAll
    @Test
    public static void setUp() {
        // ASSUMPTION: Content in configuration files are correct.
        government = Government.getInstance("government.properties");

        m1 = new MobileDevice("user_1_config.properties", government);
        m2 = new MobileDevice("user_2_config.properties", government);
        m3 = new MobileDevice("user_3_config.properties", government);
        m4 = new MobileDevice("user_4_config.properties", government);
        m5 = new MobileDevice("user_5_config.properties", government);

        // Create empty XML files and truncate all tables before starting the test.
        // This step ensures that the same test can be run multiple times without any issues.
        Assertions.assertTrue(createEmptyXMLFiles(m1));
        Assertions.assertTrue(createEmptyXMLFiles(m2));
        Assertions.assertTrue(createEmptyXMLFiles(m3));
        Assertions.assertTrue(createEmptyXMLFiles(m4));
        Assertions.assertTrue(createEmptyXMLFiles(m5));

        Assertions.assertTrue(truncateTableHelperMethod());
    }

    @DisplayName("Record multiple contacts")
    @Order(1)
    @Test
    public void executeRecordContact() {
        // Day 18 -> m1 m2 m3 m4 m5
        // Day 19 -> m2 m3 m4
        // Day 20 -> m3 m4

        // Day 18 -> m1 m2 m3 m4 m5
        Assertions.assertTrue(m1.recordContact(m2.getMobileDeviceHash(), 18, 30));
        Assertions.assertTrue(m1.recordContact(m3.getMobileDeviceHash(), 18, 35));
        Assertions.assertTrue(m1.recordContact(m4.getMobileDeviceHash(), 18, 20));
        Assertions.assertTrue(m1.recordContact(m5.getMobileDeviceHash(), 18, 16));

        Assertions.assertTrue(m2.recordContact(m1.getMobileDeviceHash(), 18, 30));
        Assertions.assertTrue(m2.recordContact(m3.getMobileDeviceHash(), 18, 17));
        Assertions.assertTrue(m2.recordContact(m4.getMobileDeviceHash(), 18, 14));
        Assertions.assertTrue(m2.recordContact(m5.getMobileDeviceHash(), 18, 14));

        Assertions.assertTrue(m3.recordContact(m1.getMobileDeviceHash(), 18, 35));
        Assertions.assertTrue(m3.recordContact(m2.getMobileDeviceHash(), 18, 17));
        Assertions.assertTrue(m3.recordContact(m4.getMobileDeviceHash(), 18, 17));
        Assertions.assertTrue(m3.recordContact(m5.getMobileDeviceHash(), 18, 19));

        Assertions.assertTrue(m4.recordContact(m1.getMobileDeviceHash(), 18, 20));
        Assertions.assertTrue(m4.recordContact(m2.getMobileDeviceHash(), 18, 14));
        Assertions.assertTrue(m4.recordContact(m3.getMobileDeviceHash(), 18, 17));
        Assertions.assertTrue(m4.recordContact(m5.getMobileDeviceHash(), 18, 20));

        Assertions.assertTrue(m5.recordContact(m1.getMobileDeviceHash(), 18, 16));
        Assertions.assertTrue(m5.recordContact(m2.getMobileDeviceHash(), 18, 14));
        Assertions.assertTrue(m5.recordContact(m3.getMobileDeviceHash(), 18, 19));
        Assertions.assertTrue(m5.recordContact(m4.getMobileDeviceHash(), 18, 20));

        // Day 19 -> m2 m3 m4
        Assertions.assertTrue(m2.recordContact(m3.getMobileDeviceHash(), 19, 11));
        Assertions.assertTrue(m2.recordContact(m4.getMobileDeviceHash(), 19, 17));

        Assertions.assertTrue(m3.recordContact(m2.getMobileDeviceHash(), 19, 11));
        Assertions.assertTrue(m3.recordContact(m4.getMobileDeviceHash(), 19, 13));

        Assertions.assertTrue(m4.recordContact(m2.getMobileDeviceHash(), 19, 17));
        Assertions.assertTrue(m4.recordContact(m3.getMobileDeviceHash(), 19, 13));

        // Day 20 -> m3 m4
        Assertions.assertTrue(m3.recordContact(m4.getMobileDeviceHash(), 20, 5));
        Assertions.assertTrue(m4.recordContact(m3.getMobileDeviceHash(), 20, 5));
    }

    @DisplayName("Record covid reports")
    @Order(2)
    @Test
    public void executeRecordTestResultAndPositiveTest() {
        //m1 -> Negative (On Day 18)
        //m2 -> Negative (On Day 18)
        //m3 -> Positive (On Day 19)
        //m4 -> Negative (On Day 19)
        //m5 -> Positive (On Day 20)

        final String m1CovidReportHash = "covidHash1";
        final String m2CovidReportHash = "covidHash2";
        final String m3CovidReportHash = "covidHash3";
        final String m4CovidReportHash = "covidHash4";
        final String m5CovidReportHash = "covidHash5";

        Assertions.assertTrue(government.recordTestResult(m1CovidReportHash, 18, false));
        Assertions.assertTrue(government.recordTestResult(m2CovidReportHash, 18, false));
        Assertions.assertTrue(government.recordTestResult(m3CovidReportHash, 19, true));
        Assertions.assertTrue(government.recordTestResult(m4CovidReportHash, 19, false));
        Assertions.assertTrue(government.recordTestResult(m5CovidReportHash, 20, true));

        Assertions.assertTrue(m3.positiveTest(m3CovidReportHash));
        Assertions.assertTrue(m5.positiveTest(m5CovidReportHash));
    }

    @DisplayName("Synchronize data")
    @Order(3)
    @Test
    public void executeSynchronizeData() {
        //m1 -> Negative (On Day 18)
        //m2 -> Negative (On Day 18)
        //m3 -> Positive (On Day 19)
        //m4 -> Negative (On Day 19)
        //m5 -> Positive (On Day 20)

        Assertions.assertFalse(m1.synchronizeData());
        Assertions.assertFalse(m2.synchronizeData());
        Assertions.assertFalse(m3.synchronizeData());
        Assertions.assertTrue(m4.synchronizeData());
        Assertions.assertTrue(m5.synchronizeData());

        Assertions.assertTrue(m1.synchronizeData());
        Assertions.assertTrue(m2.synchronizeData());
        Assertions.assertTrue(m3.synchronizeData());
        Assertions.assertTrue(m4.synchronizeData());
        Assertions.assertFalse(m5.synchronizeData());

        Assertions.assertFalse(m1.synchronizeData());
        Assertions.assertFalse(m2.synchronizeData());
        Assertions.assertFalse(m3.synchronizeData());
        Assertions.assertFalse(m4.synchronizeData());
        Assertions.assertFalse(m5.synchronizeData());
    }

    @DisplayName("Find gatherings")
    @Order(4)
    @Test
    public void executeFindGatherings() {
        // Day 18 -> m1 m2 m3 m4 m5
        // Day 19 -> m2 m3 m4
        // Day 20 -> m3 m4

        // Day 18
        Assertions.assertEquals(0, government.findGatherings(18, 6, 13, 0.5f));
        Assertions.assertEquals(1, government.findGatherings(18, 5, 13, 0.5f));
        Assertions.assertEquals(1, government.findGatherings(18, 4, 13, 0.5f));
        Assertions.assertEquals(1, government.findGatherings(18, 3, 13, 0.5f));
        Assertions.assertEquals(1, government.findGatherings(18, 2, 13, 0.5f));

        Assertions.assertEquals(0, government.findGatherings(18, 5, 36, 0.5f));
        Assertions.assertEquals(1, government.findGatherings(18, 5, 13, 0.5f));
        Assertions.assertEquals(1, government.findGatherings(18, 4, 14, 0.5f));

        Assertions.assertEquals(1, government.findGatherings(18, 5, 14, 1f));
        Assertions.assertEquals(1, government.findGatherings(18, 4, 14, 0f));

        // Day 19
        Assertions.assertEquals(0, government.findGatherings(19, 6, 13, 0.5f));
        Assertions.assertEquals(0, government.findGatherings(19, 5, 13, 0.5f));
        Assertions.assertEquals(0, government.findGatherings(19, 4, 13, 0.5f));

        Assertions.assertEquals(1, government.findGatherings(19, 3, 11, 0.5f));
        Assertions.assertEquals(2, government.findGatherings(19, 2, 13, 0.2f));
        Assertions.assertEquals(0, government.findGatherings(19, 2, 20, 0.5f));
    }

    @DisplayName("Clear after flow execution")
    @AfterAll
    @Test
    public static void clear() {
        // clear() makes sure that this test file can be run multiple times without any issues.
        Assertions.assertTrue(truncateTableHelperMethod());

        File file1 = new File(m1.getMobileDeviceHash() + ".xml");
        File file2 = new File(m2.getMobileDeviceHash() + ".xml");
        File file3 = new File(m3.getMobileDeviceHash() + ".xml");
        File file4 = new File(m4.getMobileDeviceHash() + ".xml");
        File file5 = new File(m5.getMobileDeviceHash() + ".xml");

        Assertions.assertTrue(file1.delete());
        Assertions.assertTrue(file2.delete());
        Assertions.assertTrue(file3.delete());
        Assertions.assertTrue(file4.delete());
        Assertions.assertTrue(file5.delete());
    }

    /**
     * Helper method to truncate the tables.
     *
     * @return true if method executed successfully.
     */
    private static boolean truncateTableHelperMethod() {
        final Properties governmentProperties;
        try (final InputStream inputStream = new FileInputStream("government.properties")) {
            governmentProperties = new Properties();
            governmentProperties.load(inputStream);
        } catch (Exception e) {
            return false;
        }

        try (final Connection connection = DriverManager.getConnection(
                governmentProperties.getProperty("database"),
                governmentProperties.getProperty("user"),
                governmentProperties.getProperty("password"));
             final Statement statement = connection.createStatement()) {
            statement.addBatch("TRUNCATE TABLE contact;");
            statement.addBatch("TRUNCATE TABLE mobile_device_test_outcome;");
            statement.addBatch("TRUNCATE TABLE test_outcome;");
            statement.addBatch("TRUNCATE TABLE mobile_device;");
            statement.executeBatch();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Helper method to create XML file associated with the mobile device.
     *
     * @param mobileDevice mobile device to which the file is associated.
     * @return true if method is executed successfully.
     */
    private static boolean createEmptyXMLFiles(MobileDevice mobileDevice) {
        try (final FileOutputStream fileOutputStream = new FileOutputStream(mobileDevice.getMobileDeviceHash() + ".xml")) {
            final Document xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            final Element mobileDeviceEle = xmlDoc.createElement("MobileDevice");
            final Element contactsListEle = xmlDoc.createElement("ContactsList");
            final Element testHashesListEle = xmlDoc.createElement("TestHashesList");

            mobileDeviceEle.appendChild(contactsListEle);
            mobileDeviceEle.appendChild(testHashesListEle);

            xmlDoc.appendChild(mobileDeviceEle);

            final Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.transform(new DOMSource(xmlDoc), new StreamResult(fileOutputStream));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}