import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * {@code MobileDevice} performs all functions that a mobile phone would typically do.
 * It store contacts with others and upload the contacts to the central database.
 * It reports when user have been tested positive from COVID-19 to the central database.
 * It reports back if the central database tells whether someone it has been around is diagnosed with COVID-19.
 *
 * @author Dhrumil Amish Shah (B00857606)
 * created on 2021-04-02
 * @version 1.0.0
 * @see Government
 * @since 1.0.0
 */
public class MobileDevice {
    // mobileDeviceProperties holds the configuration properties of this mobile device.
    private final Properties mobileDeviceProperties;

    // contactTracer holds the Government instance to perform typical database operations.
    private final Government contactTracer;

    // mobileDeviceHash holds the device configuration hash of this mobile device.
    private final String mobileDeviceHash;

    // mobileDeviceXMLFile holds the XML file name associated with this mobile device.
    private final String mobileDeviceXMLFile;

    /**
     * Constructs this {@code MobileDevice} with given configuration file {@code configFile}.
     * Configuration file contains this device network address - {@code address} and device name - {@code deviceName}.
     * Each line in configuration file is formatted as key=value where key is either {@code address} or {@code deviceName}.
     *
     * @param configFile    configuration file that contains this device configuration details.
     * @param contactTracer contactTracer is the government instance to perform typical database operations.
     * @throws IllegalArgumentException if arguments passed are illegal or inappropriate.
     * @throws RuntimeException         if any error occurs while instantiating this mobile device.
     * @since 1.0.0
     */
    public MobileDevice(String configFile, Government contactTracer) {
        // Throw exception if configuration file name is invalid.
        if (configFile == null || configFile.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid argument \"configFile\". - \"" + configFile + "\".");
        }

        // Throw exception if government instance is invalid.
        if (contactTracer == null) {
            throw new IllegalArgumentException("Invalid argument \"contactTracer\" - \"null\".");
        }

        // Load configuration file in mobileDeviceProperties.
        try (final InputStream inputStream = new FileInputStream(configFile)) {
            mobileDeviceProperties = new Properties();
            mobileDeviceProperties.load(inputStream);
        } catch (Exception e) {
            // Throw exception if error occurs while loading configuration file.
            throw new RuntimeException(e.getMessage());
        }

        // Throw exception if content in the configuration file is not proper.
        if (!mobileDeviceProperties.containsKey(MobileDeviceConstant.ADDRESS_KEY)) {
            throw new RuntimeException("\"" + MobileDeviceConstant.ADDRESS_KEY + "\" not found in configuration file.");
        }
        if (!mobileDeviceProperties.containsKey(MobileDeviceConstant.DEVICE_NAME_KEY)) {
            throw new RuntimeException("\"" + MobileDeviceConstant.DEVICE_NAME_KEY + "\" not found in configuration file.");
        }

        if (mobileDeviceProperties.getProperty(MobileDeviceConstant.ADDRESS_KEY) == null ||
                mobileDeviceProperties.getProperty(MobileDeviceConstant.ADDRESS_KEY).isEmpty()) {
            throw new RuntimeException("Invalid value for the key \"" + MobileDeviceConstant.ADDRESS_KEY + "\".");
        }
        if (mobileDeviceProperties.getProperty(MobileDeviceConstant.DEVICE_NAME_KEY) == null ||
                mobileDeviceProperties.getProperty(MobileDeviceConstant.DEVICE_NAME_KEY).isEmpty()) {
            throw new RuntimeException("Invalid value for the key \"" + MobileDeviceConstant.DEVICE_NAME_KEY + "\".");
        }

        try {
            // Store device hash of this mobile device in mobileDeviceHash.
            mobileDeviceHash = getMobileDeviceHash();

            // Store file name associated to this mobile device in mobileDeviceXMLFile.
            mobileDeviceXMLFile = mobileDeviceHash + ".xml";

            // Create an xml file for with this mobile device.
            createMobileDeviceXMLFile();
        } catch (Exception e) {
            // Throw exception if any error occurs.
            throw new RuntimeException(e.getMessage());
        }

        // Store government instance in the contactTracer.
        this.contactTracer = contactTracer;
    }

    /**
     * Gets the hash string of this mobile device configuration properties.
     * It used SHA-256 cryptographic algorithm to hash the configuration properties.
     *
     * @return this mobile device configuration properties hash string.
     * @throws RuntimeException if any error occurs during execution of this method.
     * @since 1.0.0
     */
    public String getMobileDeviceHash() {
        if (mobileDeviceHash == null) {
            final String deviceConfig = mobileDeviceProperties.getProperty(MobileDeviceConstant.ADDRESS_KEY) + mobileDeviceProperties.getProperty(MobileDeviceConstant.DEVICE_NAME_KEY);
            try {
                final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

                // Return device hash.
                return String.format("%064x", new BigInteger(1, messageDigest.digest(deviceConfig.getBytes(StandardCharsets.UTF_8))));
            } catch (NoSuchAlgorithmException e) {
                // Throw exception if particular cryptographic algorithm requested is not available in the environment.
                throw new RuntimeException(e.getMessage());
            }
        }

        // Return device hash.
        return mobileDeviceHash;
    }

    /**
     * Stores the XML document into the XML file associated with this mobile device.
     *
     * @param xmlDoc XML document to be stored in file.
     * @throws TransformerException if any error occurs during the transformation process.
     * @throws IOException          if any I/O error occurs.
     * @since 1.0.0
     */
    private void saveXMLDocInXMLFile(Document xmlDoc) throws TransformerException, IOException {
        final Transformer tr = TransformerFactory.newInstance().newTransformer();
        tr.setOutputProperty(OutputKeys.METHOD, "xml");
        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        try (final FileOutputStream fileOutputStream = new FileOutputStream(mobileDeviceXMLFile)) {
            tr.transform(new DOMSource(xmlDoc), new StreamResult(fileOutputStream));
        }
    }

    /**
     * Creates an XML file associated with this mobile device if not exists already.
     * It stores the contacts made with other mobile devices and positive COVID-19 tests reported in this mobile device.
     *
     * @throws ParserConfigurationException if any error occurs while parsing.
     * @throws TransformerException         if any error occurs during the transformation process.
     * @throws IOException                  if any I/O error occurs.
     * @since 1.0.0
     */
    private void createMobileDeviceXMLFile() throws ParserConfigurationException, TransformerException, IOException {
        // File instance.
        final File file = new File(mobileDeviceXMLFile);

        // Return if file exists already.
        if (file.exists() && file.isFile()) {
            return;
        }

        // Create a new XML document.
        final Document xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        final Element mobileDeviceEle = xmlDoc.createElement(MobileDeviceConstant.MOBILE_DEVICE);
        final Element contactsListEle = xmlDoc.createElement(MobileDeviceConstant.CONTACTS_LIST);
        final Element testHashesListEle = xmlDoc.createElement(MobileDeviceConstant.TEST_HASHES_LIST);

        mobileDeviceEle.appendChild(contactsListEle);
        mobileDeviceEle.appendChild(testHashesListEle);

        xmlDoc.appendChild(mobileDeviceEle);

        // Store the XML document in the file associated with this mobile device.
        saveXMLDocInXMLFile(xmlDoc);
    }

    /**
     * Records contact when this mobile device detects another device in range.
     * Contacts are stored locally until time comes to send the bulk of contacts to the database during synchronization.
     *
     * @param individual alphanumeric string of the device in contact.
     * @param date       number of days since January 1, 2021.
     * @param duration   number of minutes for which devices contacted.
     * @return {@code true} if contact is added successfully in this mobile device.
     * @throws IllegalArgumentException if arguments passed are illegal or inappropriate.
     * @throws RuntimeException         if any error occurs during execution of this method.
     * @since 1.0.0
     */
    public boolean recordContact(String individual, int date, int duration) {
        // Throw exception if individual is invalid.
        if (individual == null || individual.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid argument \"individual\". - \"" + individual + "\".");
        }

        // Throw exception if date is invalid.
        if (date < 0) {
            throw new IllegalArgumentException("Invalid argument \"date\" - " + date + ".");
        }

        // Throw exception if duration is invalid.
        if (duration <= 0) {
            throw new IllegalArgumentException("Invalid argument \"duration\" - " + duration + ".");
        }

        // Return false if individual is same as this mobile device hash
        if (mobileDeviceHash.equals(individual)) {
            return false;
        }

        // Calculate date (YYYY-MM-DD) from number of days.
        final String onDate = LocalDate.of(2021, 1, 1)
                .plusDays(date)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


        // Read the XML file associated with this mobile device and parse it into a XML document.
        final Document xmlDoc;
        try {
            xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(mobileDeviceXMLFile);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        // Create a new contact, individual, date and duration element.
        final Element contactEle = xmlDoc.createElement(MobileDeviceConstant.CONTACT);

        final Element individualEle = xmlDoc.createElement(MobileDeviceConstant.INDIVIDUAL);
        final Element dateEle = xmlDoc.createElement(MobileDeviceConstant.DATE);
        final Element durationEle = xmlDoc.createElement(MobileDeviceConstant.DURATION);

        individualEle.appendChild(xmlDoc.createTextNode(individual));
        dateEle.appendChild(xmlDoc.createTextNode(onDate));
        durationEle.appendChild(xmlDoc.createTextNode(String.valueOf(duration)));

        // Append individual, date and duration to the contact element.
        contactEle.appendChild(individualEle);
        contactEle.appendChild(dateEle);
        contactEle.appendChild(durationEle);

        // Append the contact element to the existing XML document.
        final NodeList contactsNodeList = xmlDoc.getElementsByTagName(MobileDeviceConstant.CONTACTS_LIST);
        contactsNodeList.item(0).appendChild(contactEle);

        // Store the XML document in the file associated with this mobile device.
        try {
            saveXMLDocInXMLFile(xmlDoc);
        } catch (Exception e) {
            // Throw exception if any error occurs.
            throw new RuntimeException(e.getMessage());
        }

        // Return true, contact stored successfully.
        return true;
    }

    /**
     * Records positive test when the user tells this mobile device that they have tested positive for COVID-19.
     * Tests are stored locally until time comes to send them to the database during synchronization.
     *
     * @param testHash alphanumeric string that identifies the positive COVID-19 test.
     * @return {@code true} if test is added successfully to this mobile device otherwise {@code false}.
     * @throws IllegalArgumentException if arguments passed are illegal or inappropriate.
     * @throws RuntimeException         if any error occurs during execution of this method.
     * @since 1.0.0
     */
    public boolean positiveTest(String testHash) {
        // Throw exception if testHash is invalid.
        if (testHash == null || testHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid argument \"testHash\". - \"" + testHash + "\".");
        }

        // Read the XML file associated with this mobile device and parse it into a XML document.
        final Document xmlDoc;
        try {
            xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(mobileDeviceXMLFile);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        // Check if the testHash is unique.
        final NodeList existingTestHashesNodeList = xmlDoc.getElementsByTagName(MobileDeviceConstant.TEST_HASH);
        if (existingTestHashesNodeList != null && existingTestHashesNodeList.getLength() > 0) {
            for (int i = 0; i < existingTestHashesNodeList.getLength(); ++i) {
                if (testHash.equals(existingTestHashesNodeList.item(i).getTextContent())) {
                    // Return false, testHash is not unique
                    return false;
                }
            }
        }

        // Create a new test hash element.
        final Element testHashEle = xmlDoc.createElement(MobileDeviceConstant.TEST_HASH);
        testHashEle.appendChild(xmlDoc.createTextNode(testHash));

        // Append the test hash element to the existing XML document.
        final NodeList testHashesNodeList = xmlDoc.getElementsByTagName(MobileDeviceConstant.TEST_HASHES_LIST);
        testHashesNodeList.item(0).appendChild(testHashEle);

        // Store the XML document in the file associated with this mobile device.
        try {
            saveXMLDocInXMLFile(xmlDoc);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        // Return true, test hash stored successfully.
        return true;
    }

    /**
     * Synchronizes this mobile device data with the government database periodically.
     * All information is packaged in an XML string and sent to the government.
     *
     * @return {@code true} if this mobile device has been near anyone diagnosed with COVID-19 in the 14 days otherwise {@code false}.
     * @throws RuntimeException if any error occurs during execution of this method.
     * @since 1.0.0
     */
    public boolean synchronizeData() {
        try {
            // Read the XML into string.
            final String contactInfoXML = Files.readString(Path.of(mobileDeviceXMLFile), StandardCharsets.UTF_8);

            // Synchronize this mobile device data with the government database.
            final boolean covidContact = contactTracer.mobileContact(mobileDeviceHash, contactInfoXML);

            final boolean isFileDeleted = new File(mobileDeviceXMLFile).delete();
            if (isFileDeleted) {
                createMobileDeviceXMLFile();
            }

            // Return true if this mobile device has been near anyone diagnosed with COVID-19 in the 14 days otherwise false.
            return covidContact;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * {@code MobileDeviceConstant} holds all the constants used by {@code MobileDevice}.
     *
     * @author Dhrumil Amish Shah (B00857606)
     * created on 2021-04-02
     * @version 1.0.0
     * @see MobileDevice
     * @see Government
     * @since 1.0.0
     */
    private static class MobileDeviceConstant {
        private static final String ADDRESS_KEY = "address";
        private static final String DEVICE_NAME_KEY = "deviceName";
        private static final String MOBILE_DEVICE = "MobileDevice";
        private static final String CONTACTS_LIST = "ContactsList";
        private static final String TEST_HASHES_LIST = "TestHashesList";
        private static final String CONTACT = "Contact";
        private static final String INDIVIDUAL = "Individual";
        private static final String DATE = "Date";
        private static final String DURATION = "Duration";
        private static final String TEST_HASH = "TestHash";
    }
}