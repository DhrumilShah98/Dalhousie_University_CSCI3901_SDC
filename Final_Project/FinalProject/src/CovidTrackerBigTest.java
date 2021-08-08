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

@DisplayName("CovidTracker big test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CovidTrackerBigTest {
    int numOfGatherings;
    private static Government government;
    private static MobileDevice m1, m2, m3, m4, m5, m6, m7, m8, m9, m10;

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
        m6 = new MobileDevice("user_6_config.properties", government);
        m7 = new MobileDevice("user_7_config.properties", government);
        m8 = new MobileDevice("user_8_config.properties", government);
        m9 = new MobileDevice("user_9_config.properties", government);
        m10 = new MobileDevice("user_10_config.properties", government);

        // Create empty XML files and truncate all tables before starting the test.
        // This step ensures that the same test can be run multiple times without any issues.
        Assertions.assertTrue(createEmptyXMLFiles(m1));
        Assertions.assertTrue(createEmptyXMLFiles(m2));
        Assertions.assertTrue(createEmptyXMLFiles(m3));
        Assertions.assertTrue(createEmptyXMLFiles(m4));
        Assertions.assertTrue(createEmptyXMLFiles(m5));
        Assertions.assertTrue(createEmptyXMLFiles(m6));
        Assertions.assertTrue(createEmptyXMLFiles(m7));
        Assertions.assertTrue(createEmptyXMLFiles(m8));
        Assertions.assertTrue(createEmptyXMLFiles(m9));
        Assertions.assertTrue(createEmptyXMLFiles(m10));

        Assertions.assertTrue(truncateTableHelperMethod());
    }

    @DisplayName("Record multiple contacts")
    @Order(1)
    @Test
    public void executeRecordContact() {
        Assertions.assertTrue(m1.recordContact(m2.getMobileDeviceHash(), 5, 10));
        Assertions.assertTrue(m1.recordContact(m3.getMobileDeviceHash(), 7, 7));
        Assertions.assertTrue(m1.recordContact(m4.getMobileDeviceHash(), 9, 76));
        Assertions.assertTrue(m3.recordContact(m4.getMobileDeviceHash(), 2, 20));
        Assertions.assertTrue(m2.recordContact(m3.getMobileDeviceHash(), 3, 20));
        Assertions.assertTrue(m3.recordContact(m2.getMobileDeviceHash(), 7, 20));
        Assertions.assertTrue(m3.recordContact(m4.getMobileDeviceHash(), 2, 20));
        Assertions.assertTrue(m3.recordContact(m8.getMobileDeviceHash(), 8, 20));
        Assertions.assertTrue(m7.recordContact(m8.getMobileDeviceHash(), 3, 20));
        Assertions.assertTrue(m3.recordContact(m8.getMobileDeviceHash(), 7, 20));
        Assertions.assertTrue(m2.recordContact(m8.getMobileDeviceHash(), 3, 20));
        Assertions.assertTrue(m2.recordContact(m9.getMobileDeviceHash(), 6, 20));
        Assertions.assertTrue(m3.recordContact(m9.getMobileDeviceHash(), 7, 20));
        Assertions.assertTrue(m5.recordContact(m8.getMobileDeviceHash(), 8, 10));
        Assertions.assertTrue(m3.recordContact(m6.getMobileDeviceHash(), 3, 10));
        Assertions.assertTrue(m4.recordContact(m8.getMobileDeviceHash(), 7, 10));
        Assertions.assertTrue(m4.recordContact(m6.getMobileDeviceHash(), 6, 10));
        Assertions.assertTrue(m7.recordContact(m6.getMobileDeviceHash(), 8, 10));
        Assertions.assertTrue(m9.recordContact(m6.getMobileDeviceHash(), 4, 10));
        Assertions.assertTrue(m9.recordContact(m5.getMobileDeviceHash(), 3, 10));
        Assertions.assertTrue(m9.recordContact(m3.getMobileDeviceHash(), 8, 30));
        Assertions.assertTrue(m1.recordContact(m9.getMobileDeviceHash(), 2, 30));
        Assertions.assertTrue(m8.recordContact(m5.getMobileDeviceHash(), 7, 30));
        Assertions.assertTrue(m8.recordContact(m3.getMobileDeviceHash(), 1, 10));
        Assertions.assertTrue(m8.recordContact(m4.getMobileDeviceHash(), 9, 10));
        Assertions.assertTrue(m8.recordContact(m9.getMobileDeviceHash(), 4, 30));
        Assertions.assertTrue(m1.recordContact(m7.getMobileDeviceHash(), 4, 20));
        Assertions.assertTrue(m2.recordContact(m6.getMobileDeviceHash(), 7, 10));
        Assertions.assertTrue(m2.recordContact(m9.getMobileDeviceHash(), 3, 30));
        Assertions.assertTrue(m7.recordContact(m3.getMobileDeviceHash(), 5, 20));
        Assertions.assertTrue(m6.recordContact(m5.getMobileDeviceHash(), 9, 20));
        Assertions.assertTrue(m8.recordContact(m6.getMobileDeviceHash(), 3, 30));
        Assertions.assertTrue(m3.recordContact(m2.getMobileDeviceHash(), 8, 10));
        Assertions.assertTrue(m4.recordContact(m3.getMobileDeviceHash(), 3, 30));
        Assertions.assertTrue(m7.recordContact(m5.getMobileDeviceHash(), 1, 20));
        Assertions.assertTrue(m8.recordContact(m4.getMobileDeviceHash(), 7, 10));
        Assertions.assertTrue(m4.recordContact(m3.getMobileDeviceHash(), 8, 30));
        Assertions.assertTrue(m3.recordContact(m2.getMobileDeviceHash(), 5, 20));
        Assertions.assertTrue(m2.recordContact(m4.getMobileDeviceHash(), 7, 10));
        Assertions.assertTrue(m8.recordContact(m3.getMobileDeviceHash(), 2, 30));
        Assertions.assertTrue(m9.recordContact(m3.getMobileDeviceHash(), 8, 20));
        Assertions.assertTrue(m10.recordContact(m1.getMobileDeviceHash(), 8, 30));
        Assertions.assertTrue(m5.recordContact(m6.getMobileDeviceHash(), 4, 10));
        Assertions.assertTrue(m2.recordContact(m10.getMobileDeviceHash(), 8, 30));
        Assertions.assertTrue(m7.recordContact(m10.getMobileDeviceHash(), 3, 20));
        Assertions.assertTrue(m2.recordContact(m7.getMobileDeviceHash(), 7, 10));
        Assertions.assertTrue(m7.recordContact(m3.getMobileDeviceHash(), 3, 30));
        Assertions.assertTrue(m4.recordContact(m8.getMobileDeviceHash(), 5, 10));
    }

    @DisplayName("Synchronize data after recording multiple contacts and before recording any positive tests")
    @Order(2)
    @Test
    public void synchronizeDataBeforePositiveTest() {
        Assertions.assertFalse(m1.synchronizeData());
        Assertions.assertFalse(m2.synchronizeData());
        Assertions.assertFalse(m3.synchronizeData());
        Assertions.assertFalse(m4.synchronizeData());
        Assertions.assertFalse(m5.synchronizeData());
        Assertions.assertFalse(m6.synchronizeData());
        Assertions.assertFalse(m7.synchronizeData());
        Assertions.assertFalse(m8.synchronizeData());
        Assertions.assertFalse(m9.synchronizeData());
        Assertions.assertFalse(m10.synchronizeData());
    }

    @DisplayName("Record multiple covid reports")
    @Order(3)
    @Test
    public void executeRecordTestResultAndPositiveTest() {
        Assertions.assertTrue(government.recordTestResult("covid1", 2, true));
        Assertions.assertTrue(government.recordTestResult("covid2", 6, true));
        Assertions.assertTrue(government.recordTestResult("covid3", 8, true));
        Assertions.assertTrue(government.recordTestResult("covid4", 10, true));
        Assertions.assertTrue(government.recordTestResult("covid5", 15, true));

        Assertions.assertTrue(m1.positiveTest("covid1"));
        Assertions.assertTrue(m6.positiveTest("covid2"));
        Assertions.assertTrue(m2.positiveTest("covid3"));
        Assertions.assertTrue(m8.positiveTest("covid4"));
        Assertions.assertTrue(m10.positiveTest("covid5"));

        Assertions.assertTrue(government.recordTestResult("covid6", 17, false));
        Assertions.assertTrue(government.recordTestResult("covid7", 13, false));
        Assertions.assertTrue(government.recordTestResult("covid8", 46, false));
        Assertions.assertTrue(government.recordTestResult("covid9", 3, false));
    }

    @DisplayName("Synchronize data after recording multiple covid reports, synchronizing multiple times.")
    @Order(4)
    @Test
    public void synchronizeDataMultipleTimes() {
        Assertions.assertFalse(m1.synchronizeData());
        Assertions.assertFalse(m2.synchronizeData());
        Assertions.assertTrue(m3.synchronizeData());
        Assertions.assertFalse(m4.synchronizeData());
        Assertions.assertFalse(m5.synchronizeData());
        Assertions.assertFalse(m6.synchronizeData());
        Assertions.assertTrue(m7.synchronizeData());
        Assertions.assertTrue(m8.synchronizeData());
        Assertions.assertTrue(m9.synchronizeData());
        Assertions.assertTrue(m10.synchronizeData());

        Assertions.assertTrue(m1.synchronizeData());
        Assertions.assertTrue(m2.synchronizeData());
        Assertions.assertTrue(m3.synchronizeData());
        Assertions.assertTrue(m4.synchronizeData());
        Assertions.assertTrue(m5.synchronizeData());
        Assertions.assertFalse(m6.synchronizeData());
        Assertions.assertTrue(m7.synchronizeData());
        Assertions.assertFalse(m8.synchronizeData());
        Assertions.assertFalse(m9.synchronizeData());
        Assertions.assertFalse(m10.synchronizeData());

        Assertions.assertFalse(m1.synchronizeData());
        Assertions.assertFalse(m2.synchronizeData());
        Assertions.assertFalse(m3.synchronizeData());
        Assertions.assertFalse(m4.synchronizeData());
        Assertions.assertFalse(m5.synchronizeData());
        Assertions.assertFalse(m6.synchronizeData());
        Assertions.assertFalse(m7.synchronizeData());
        Assertions.assertFalse(m8.synchronizeData());
        Assertions.assertFalse(m9.synchronizeData());
        Assertions.assertFalse(m10.synchronizeData());
    }

    @DisplayName("Find gatherings")
    @Order(5)
    @Test
    public void executeFindGatherings() {
        numOfGatherings = government.findGatherings(10, 2, 1, 0.1f);
        Assertions.assertEquals(0, numOfGatherings);
        numOfGatherings = government.findGatherings(10, 2, 1, 0.1f);
        Assertions.assertEquals(0, numOfGatherings);
        numOfGatherings = government.findGatherings(20, 2, 1, 0.1f);
        Assertions.assertEquals(0, numOfGatherings);
        numOfGatherings = government.findGatherings(3, 2, 1, 0.01f);
        Assertions.assertEquals(10, numOfGatherings);
        numOfGatherings = government.findGatherings(4, 2, 1, 0.01f);
        Assertions.assertEquals(4, numOfGatherings);
        numOfGatherings = government.findGatherings(4, 2, 1, 0.5f);
        Assertions.assertEquals(4, numOfGatherings);
        numOfGatherings = government.findGatherings(4, 2, 1, 1f);
        Assertions.assertEquals(4, numOfGatherings);
        numOfGatherings = government.findGatherings(3, 4, 1, 0.5f);
        Assertions.assertEquals(0, numOfGatherings);
        numOfGatherings = government.findGatherings(3, 2, 1, 0.01f);
        Assertions.assertEquals(10, numOfGatherings);
        numOfGatherings = government.findGatherings(4, 3, 1, 0.01f);
        Assertions.assertEquals(0, numOfGatherings);
    }

    @DisplayName("Record new multiple contacts and synchronize data")
    @Order(6)
    @Test
    public void executeRecordContactForNewEntries() {
        Assertions.assertTrue(m1.recordContact(m2.getMobileDeviceHash(), 10, 5));
        Assertions.assertTrue(m1.recordContact(m3.getMobileDeviceHash(), 10, 7));
        Assertions.assertTrue(m1.recordContact(m4.getMobileDeviceHash(), 10, 8));
        Assertions.assertTrue(m3.recordContact(m4.getMobileDeviceHash(), 10, 10));
        Assertions.assertTrue(m2.recordContact(m3.getMobileDeviceHash(), 10, 10));
        Assertions.assertTrue(m5.recordContact(m3.getMobileDeviceHash(), 10, 4));
        Assertions.assertTrue(m5.recordContact(m4.getMobileDeviceHash(), 10, 12));
        Assertions.assertTrue(m5.recordContact(m6.getMobileDeviceHash(), 10, 6));
        Assertions.assertTrue(m6.recordContact(m7.getMobileDeviceHash(), 10, 9));
        Assertions.assertTrue(m5.recordContact(m7.getMobileDeviceHash(), 10, 13));
        Assertions.assertTrue(m5.recordContact(m3.getMobileDeviceHash(), 10, 1));

        Assertions.assertTrue(m1.synchronizeData());
        Assertions.assertFalse(m2.synchronizeData());
        Assertions.assertFalse(m3.synchronizeData());
        Assertions.assertFalse(m4.synchronizeData());
        Assertions.assertTrue(m5.synchronizeData());
        Assertions.assertFalse(m6.synchronizeData());
        Assertions.assertFalse(m7.synchronizeData());
        Assertions.assertFalse(m8.synchronizeData());
        Assertions.assertFalse(m9.synchronizeData());
        Assertions.assertFalse(m10.synchronizeData());
    }

    @DisplayName("Find new gatherings after recording multiple new contacts and syncing data")
    @Order(7)
    @Test
    public void executeFindGatheringsForNewEntries() {
        numOfGatherings = government.findGatherings(10, 3, 4, 0.1f);
        Assertions.assertEquals(2, numOfGatherings);
        numOfGatherings = government.findGatherings(10, 4, 4, 0.1f);
        Assertions.assertEquals(1, numOfGatherings);
        numOfGatherings = government.findGatherings(10, 3, 5, 0.1f);
        Assertions.assertEquals(2, numOfGatherings);
        numOfGatherings = government.findGatherings(10, 3, 4, 1f);
        Assertions.assertEquals(1, numOfGatherings);
        numOfGatherings = government.findGatherings(10, 3, 5, 0.25f);
        Assertions.assertEquals(2, numOfGatherings);
        numOfGatherings = government.findGatherings(10, 4, 5, 0.1f);
        Assertions.assertEquals(1, numOfGatherings);
        numOfGatherings = government.findGatherings(10, 4, 4, 0.1f);
        Assertions.assertEquals(1, numOfGatherings);
        numOfGatherings = government.findGatherings(10, 4, 4, 0.1f);
        Assertions.assertEquals(1, numOfGatherings);
        numOfGatherings = government.findGatherings(10, 5, 5, 0.1f);
        Assertions.assertEquals(1, numOfGatherings);
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
        File file6 = new File(m6.getMobileDeviceHash() + ".xml");
        File file7 = new File(m7.getMobileDeviceHash() + ".xml");
        File file8 = new File(m8.getMobileDeviceHash() + ".xml");
        File file9 = new File(m9.getMobileDeviceHash() + ".xml");
        File file10 = new File(m10.getMobileDeviceHash() + ".xml");

        Assertions.assertTrue(file1.delete());
        Assertions.assertTrue(file2.delete());
        Assertions.assertTrue(file3.delete());
        Assertions.assertTrue(file4.delete());
        Assertions.assertTrue(file5.delete());
        Assertions.assertTrue(file6.delete());
        Assertions.assertTrue(file7.delete());
        Assertions.assertTrue(file8.delete());
        Assertions.assertTrue(file9.delete());
        Assertions.assertTrue(file10.delete());
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