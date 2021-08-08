import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Properties;

/**
 * {@code Government} connects to the database and perform all the centralized operations.
 * It is built using Singleton Design Pattern and lazy initialization so only one instance of {@code Government} exists.
 * It stores the overall set of contacts.
 * It stores the overall set of test results.
 * It notify individuals who contact the database if they have been contacted with someone who has tested positive for COVID-19.
 * It reports the number of large gatherings on any particular date.
 *
 * @author Dhrumil Amish Shah (B00857606)
 * created on 2021-04-02
 * @version 1.0.0
 * @since 1.0.0
 */
public class Government {
    // instance holds this single government.
    private static Government instance;

    // governmentProperties holds the configuration properties of the database.
    private final Properties governmentProperties;

    /**
     * Constructs this {@code Government} with given configuration file {@code configFile}.
     * Configuration file contains {@code database}, {@code user} and {@code password}.
     * Each line in configuration file is formatted as key=value where key is either {@code database}, {@code user} or {@code password}.
     *
     * @param configFile configuration file that contains database configuration details.
     * @throws IllegalArgumentException if arguments passed are illegal or inappropriate.
     * @throws RuntimeException         if any error occurs while instantiating this mobile device.
     * @since 1.0.0
     */
    private Government(String configFile) {
        // Throw exception if configuration file name is invalid.
        if (configFile == null || configFile.isEmpty()) {
            throw new IllegalArgumentException("Invalid argument \"configFile\". - \"" + configFile + "\".");
        }

        // Load configuration file in governmentProperties.
        try (final InputStream inputStream = new FileInputStream(configFile)) {
            governmentProperties = new Properties();
            governmentProperties.load(inputStream);
        } catch (Exception e) {
            // Throw exception if error occurs while loading configuration file.
            throw new RuntimeException(e.getMessage());
        }

        // Throw exception if content in the configuration file is not proper.
        if (!governmentProperties.containsKey(GovernmentConstant.DATABASE_PATH_KEY)) {
            throw new RuntimeException("\"" + GovernmentConstant.DATABASE_PATH_KEY + "\" not found in configuration file.");
        }
        if (!governmentProperties.containsKey(GovernmentConstant.DATABASE_USER_KEY)) {
            throw new RuntimeException("\"" + GovernmentConstant.DATABASE_USER_KEY + "\" not found in configuration file.");
        }
        if (!governmentProperties.containsKey(GovernmentConstant.DATABASE_PASSWORD_KEY)) {
            throw new RuntimeException("\"" + GovernmentConstant.DATABASE_PASSWORD_KEY + "\" not found in configuration file.");
        }

        if (governmentProperties.getProperty(GovernmentConstant.DATABASE_PATH_KEY) == null ||
                governmentProperties.getProperty(GovernmentConstant.DATABASE_PATH_KEY).isEmpty()) {
            throw new RuntimeException("Invalid value for the key \"" + GovernmentConstant.DATABASE_PATH_KEY + "\".");
        }

        if (governmentProperties.getProperty(GovernmentConstant.DATABASE_USER_KEY) == null ||
                governmentProperties.getProperty(GovernmentConstant.DATABASE_USER_KEY).isEmpty()) {
            throw new RuntimeException("Invalid value for the key \"" + GovernmentConstant.DATABASE_USER_KEY + "\".");
        }

        if (governmentProperties.getProperty(GovernmentConstant.DATABASE_PASSWORD_KEY) == null) {
            throw new RuntimeException("Invalid value for the key \"" + GovernmentConstant.DATABASE_PASSWORD_KEY + "\".");
        }

        // Register JDBC driver to perform database operations.
        try {
            Class.forName(GovernmentConstant.JDBC_DRIVER).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            // Throw exception if error occurs.
            throw new RuntimeException(e.getMessage());
        }

        // Connect to the database to check the credentials and create tables if not created already.
        try (final Connection connection = DriverManager.getConnection(
                governmentProperties.getProperty(GovernmentConstant.DATABASE_PATH_KEY),
                governmentProperties.getProperty(GovernmentConstant.DATABASE_USER_KEY),
                governmentProperties.getProperty(GovernmentConstant.DATABASE_PASSWORD_KEY));
             final Statement statement = connection.createStatement()) {
            statement.addBatch(GovernmentDatabase.createTableMobileDevice());
            statement.addBatch(GovernmentDatabase.createTableTestOutcome());
            statement.addBatch(GovernmentDatabase.createTableMobileDeviceTestOutcome());
            statement.addBatch(GovernmentDatabase.createTableContact());
            statement.executeBatch();
        } catch (SQLException e) {
            // Throw exception if error occurs.
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Returns the {@code Government} instance.
     *
     * @param configFile configuration file that contains database configuration details.
     * @return {@code Government} instance.
     * @since 1.0.0
     */
    public static Government getInstance(String configFile) {
        if (instance == null) {
            instance = new Government(configFile);
        }
        return instance;
    }

    /**
     * Parses the XML string into an XML document and stores the respected values into appropriate lists.
     *
     * @param contactInfo                     XML string to be parsed.
     * @param initiatorPositiveTestHashesList stores the positive test hashes of the initiator.
     * @param contactPersonTwoList            contains alphanumeric strings for each device in contact with the initiator.
     * @param dateOfContactList               contains number of days since January 1, 2021 for each device in contact with the initiator.
     * @param durationOfContactList           contains number of minutes for each device in contact with the initiator.
     * @throws ParserConfigurationException if configuration error occurs.
     * @throws SAXException                 if parsing error occurs.
     * @throws IOException                  if any I/O error occurs.
     * @since 1.0.0
     */
    private void parseContactInfoXML(String contactInfo,
                                     LinkedList<String> initiatorPositiveTestHashesList,
                                     LinkedList<String> contactPersonTwoList,
                                     LinkedList<String> dateOfContactList,
                                     LinkedList<Integer> durationOfContactList) throws ParserConfigurationException, SAXException, IOException {
        try (final StringReader stringReader = new StringReader(contactInfo)) {
            // Parse the string and create XML document.
            final Document contactInfoXML = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(stringReader));

            contactInfoXML.getDocumentElement().normalize();

            // List of positive test hashes of the initiator.
            final NodeList testHashesNodeList = contactInfoXML.getElementsByTagName(GovernmentConstant.TEST_HASH);
            if (testHashesNodeList != null && testHashesNodeList.getLength() > 0) {
                for (int i = 0; i < testHashesNodeList.getLength(); ++i) {
                    initiatorPositiveTestHashesList.add(testHashesNodeList.item(i).getTextContent());
                }
            }

            // List of alphanumeric strings, number of days and duration for each device in contact with the initiator.
            final NodeList contactsNodeList = contactInfoXML.getElementsByTagName(GovernmentConstant.CONTACT);
            if (contactsNodeList != null && contactsNodeList.getLength() > 0) {
                for (int i = 0; i < contactsNodeList.getLength(); ++i) {
                    final Element contactEle = (Element) contactsNodeList.item(i);
                    contactPersonTwoList.add(contactEle.getElementsByTagName(GovernmentConstant.INDIVIDUAL).item(0).getTextContent());
                    dateOfContactList.add(contactEle.getElementsByTagName(GovernmentConstant.DATE).item(0).getTextContent());
                    durationOfContactList.add(Integer.parseInt(contactEle.getElementsByTagName(GovernmentConstant.DURATION).item(0).getTextContent()));
                }
            }
        }
    }

    /**
     * Called by {@code MobileDevice.synchronizeData()} to store the contact information into the database.
     * Caller is identified by the {@code initiator} which is the hash value of caller's device configuration properties.
     *
     * @param initiator   hash value of caller's device configuration properties.
     * @param contactInfo caller's contact information.
     * @return {@code true} if {@code initiator} has been near anyone diagnosed with COVID-19 in the 14 days otherwise {@code false}.
     * @throws IllegalArgumentException if arguments passed are illegal or inappropriate.
     * @throws RuntimeException         if any error occurs during execution of this method.
     * @since 1.0.0
     */
    public boolean mobileContact(String initiator, String contactInfo) {
        // Throw exception if initiator is invalid.
        if (initiator == null || initiator.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid argument \"initiator\". - \"" + initiator + "\".");
        }

        // Return false, initiator has not been near anyone diagnosed with COVID-19 in the 14 days.
        if (contactInfo == null || contactInfo.trim().isEmpty()) {
            return false;
        }

        final LinkedList<String> initiatorPositiveTestHashesList = new LinkedList<>();
        final LinkedList<String> contactPersonTwoList = new LinkedList<>();
        final LinkedList<String> dateOfContactList = new LinkedList<>();
        final LinkedList<Integer> durationOfContactList = new LinkedList<>();

        // Parse the XML string and store the values in appropriate list.
        try {
            parseContactInfoXML(contactInfo, initiatorPositiveTestHashesList, contactPersonTwoList, dateOfContactList, durationOfContactList);
        } catch (Exception e) {
            // Throw exception if any error occurs.
            throw new RuntimeException(e.getMessage());
        }

        // Query to insert initiator's hash and contacts hashes if any and not inserted already.
        final String insertMobileDeviceQuery = GovernmentDatabase.getInsertMobileDeviceQuery(initiator, contactPersonTwoList);

        // Query to insert initiator's positive test hashes if any.
        final String insertMobileDeviceTestResultQuery = (initiatorPositiveTestHashesList.size() > 0) ?
                GovernmentDatabase.getInsertMobileDeviceTestResultQuery(initiator, initiatorPositiveTestHashesList) :
                null;

        // Query to insert contacts made by the initiator if any.
        final String insertContactQuery = (contactPersonTwoList.size() > 0) ?
                GovernmentDatabase.getInsertContactQuery(initiator, contactPersonTwoList, dateOfContactList, durationOfContactList) :
                null;

        // Query to check if initiator has been near anyone diagnosed with COVID-19 in the last 14 days.
        final String testInitiatorCovidQuery = GovernmentDatabase.getSelectTestInitiatorQuery(initiator);

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        // List of all the COVID-19 contacts made by the initiator in the last 14 days.
        final LinkedList<Integer> contactColIds = new LinkedList<>();
        try {
            connection = DriverManager.getConnection(governmentProperties.getProperty(GovernmentConstant.DATABASE_PATH_KEY),
                    governmentProperties.getProperty(GovernmentConstant.DATABASE_USER_KEY),
                    governmentProperties.getProperty(GovernmentConstant.DATABASE_PASSWORD_KEY));

            statement = connection.createStatement();

            // Set auto commit to false before queries execution to ensure Atomicity.
            connection.setAutoCommit(false);

            // Prepare a batch of queries and execute them.
            statement.addBatch(insertMobileDeviceQuery);
            if (insertMobileDeviceTestResultQuery != null) {
                statement.addBatch(insertMobileDeviceTestResultQuery);
            }
            if (insertContactQuery != null) {
                statement.addBatch(insertContactQuery);
            }
            statement.executeBatch();

            resultSet = statement.executeQuery(testInitiatorCovidQuery);
            while (resultSet.next()) {
                contactColIds.add(resultSet.getInt(GovernmentDatabase.COLUMN_ID));
            }

            if (contactColIds.size() > 0) {
                // Query to update the notify field of contacts once reported.
                String updateColContactNotifiedQuery = GovernmentDatabase.updateContactNotifiedForContactQuery(contactColIds);
                statement.executeUpdate(updateColContactNotifiedQuery);
            }

            // Commit when all queries are executed successfully.
            connection.commit();

            // Set auto commit to true after execution.
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            // Throw exception if any error occurs.
            throw new RuntimeException(e.getMessage());
        } finally {
            // Close the resultSet instance.
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ignored) {
                }
            }

            // Close the statement instance.
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignored) {
                }
            }

            // Close the connection instance.
            if (connection != null) {
                try {
                    if (!connection.getAutoCommit()) {
                        connection.rollback();
                    }
                    connection.close();
                } catch (SQLException ignored) {
                }
            }
        }

        // Return true if the initiator has been near anyone diagnosed with COVID-19 in the 14 days otherwise false.
        return contactColIds.size() > 0;
    }

    /**
     * Record in the database that a COVID-19 test, identified by the alphanumeric string {@code testHash},
     * had a collection taken up on {@code date} and {@code result} positive or negative.
     *
     * @param testHash alphanumeric string that identifies the positive COVID-19 test.
     * @param date     number of days since January 1, 2021.
     * @param result   positive or negative COVID-19 test.
     * @return {@code true} if report is recorded successfully.
     * @throws IllegalArgumentException if arguments passed are illegal or inappropriate.
     * @throws RuntimeException         if any error occurs during execution of this method.
     * @since 1.0.0
     */
    public boolean recordTestResult(String testHash, int date, boolean result) {
        // Throw exception if testHash is invalid.
        if (testHash == null || testHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid argument \"testHash\". - \"" + testHash + "\".");
        }

        // Throw exception if date is invalid.
        if (date < 0) {
            throw new IllegalArgumentException("Invalid argument \"date\" - " + date + ".");
        }

        final String testDate = LocalDate.of(2021, 1, 1)
                .plusDays(date)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Query to insert COVID report record.
        final String insertRecordQuery = GovernmentDatabase.getInsertTestResultQuery(testHash, testDate, result);

        try (final Connection connection = DriverManager.getConnection(
                governmentProperties.getProperty(GovernmentConstant.DATABASE_PATH_KEY),
                governmentProperties.getProperty(GovernmentConstant.DATABASE_USER_KEY),
                governmentProperties.getProperty(GovernmentConstant.DATABASE_PASSWORD_KEY));
             final Statement statement = connection.createStatement()) {

            statement.executeUpdate(insertRecordQuery);
            return true;
        } catch (SQLException e) {
            // Throw exception if any error occurs.
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Finds gatherings on {@code date}.
     * Gathering is considered if it contains at least {@code minSize} individuals and they have contacted one another for
     * at least {@code minTime} minutes. Gathering is further deemed large and worth reporting if
     * c/m is greater or equal to {@code density} where
     * c is pairs of individuals within a gathering and m = n * (n - 1)/2 where n is individuals in a gathering.
     *
     * @param date    date for which number of gatherings are to be reported.
     * @param minSize minimum number of individuals in a gathering.
     * @param minTime minimum time for which individuals contacted.
     * @param density gathering density
     * @return number of gatherings found.
     * @throws IllegalArgumentException if arguments passed are illegal or inappropriate.
     * @throws RuntimeException         if any error occurs during execution of this method.
     * @since 1.0.0
     */
    public int findGatherings(int date, int minSize, int minTime, float density) {
        // Throw exception if date is invalid.
        if (date < 0) {
            throw new IllegalArgumentException("Invalid argument \"date\" - " + date + ".");
        }

        // Throw minSize if date is invalid.
        if (minSize < 2) {
            throw new IllegalArgumentException("Invalid argument \"minSize\" - " + minSize + ".");
        }

        // Throw exception if minTime is invalid.
        if (minTime < 1) {
            throw new IllegalArgumentException("Invalid argument \"minTime\" - " + minTime + ".");
        }

        // Throw exception if density is invalid.
        if (density < 0 || density > 1) {
            throw new IllegalArgumentException("Invalid argument \"density\" - " + density + ".");
        }

        // Gathering date in YYYY-MM-DD format.
        final String gatheringDate = LocalDate.of(2021, 1, 1)
                .plusDays(date)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Query to find all the pairs on give date who contacted for at least minTime.
        final String gatheringQuery = GovernmentDatabase.getContactGatheringQuery(gatheringDate, minTime);

        // List of all the pairs.
        LinkedList<Pair> allContacts;

        // List of all the individuals.
        LinkedHashSet<Integer> allIndividuals;

        try (final Connection connection = DriverManager.getConnection(
                governmentProperties.getProperty(GovernmentConstant.DATABASE_PATH_KEY),
                governmentProperties.getProperty(GovernmentConstant.DATABASE_USER_KEY),
                governmentProperties.getProperty(GovernmentConstant.DATABASE_PASSWORD_KEY));
             final Statement statement = connection.createStatement();
             final ResultSet resultSet = statement.executeQuery(gatheringQuery)) {

            allContacts = new LinkedList<>();
            allIndividuals = new LinkedHashSet<>();

            while (resultSet.next()) {
                final int personOneId = resultSet.getInt(GovernmentDatabase.COLUMN_PERSON_ONE_ID);
                final int personTwoId = resultSet.getInt(GovernmentDatabase.COLUMN_PERSON_TWO_ID);

                // Remove duplicate pairs. (i.e., pairs with same individuals)
                boolean isSimilarPairPresent = false;
                for (Pair p : allContacts) {
                    if (p.personOneId == personTwoId && p.personTwoId == personOneId) {
                        isSimilarPairPresent = true;
                        break;
                    }
                }

                if (!isSimilarPairPresent) {
                    allContacts.add(new Pair(personOneId, personTwoId));
                }

                allIndividuals.add(personOneId);
                allIndividuals.add(personTwoId);
            }

        } catch (SQLException e) {
            // Throw exception if any error occurs.
            throw new RuntimeException(e.getMessage());
        }

        // List of all the gatherings.
        final LinkedList<Gathering> gatherings = new LinkedList<>();

        // Logic to find all the gatherings.
        for (int i = 0; i < allContacts.size(); ++i) {
            final Pair currentContact = allContacts.get(i);

            // Ignore the pair if it is grouped already. (i.e., already part of a gathering)
            if (currentContact.isInGathering) {
                continue;
            }

            final LinkedList<Pair> currentGatheringPairs = new LinkedList<>();
            final LinkedHashSet<Integer> currentGatheringIndividuals = new LinkedHashSet<>();
            final ArrayDeque<Pair> currentGatheringsQueue = new ArrayDeque<>();

            currentGatheringPairs.addLast(currentContact);
            currentGatheringsQueue.addLast(currentContact);
            currentGatheringIndividuals.add(currentContact.personOneId);
            currentGatheringIndividuals.add(currentContact.personTwoId);
            currentContact.isInGathering = true;

            Pair curGatheringPair;
            while ((curGatheringPair = currentGatheringsQueue.pollFirst()) != null) {
                for (Integer individual : allIndividuals) {
                    if (individual == curGatheringPair.personOneId || individual == curGatheringPair.personTwoId) {
                        continue;
                    }

                    int pair1Index = -1;
                    for (int j = i + 1; j < allContacts.size(); ++j) {
                        if ((curGatheringPair.personOneId == allContacts.get(j).personOneId && individual == allContacts.get(j).personTwoId) ||
                                curGatheringPair.personOneId == allContacts.get(j).personTwoId && individual == allContacts.get(j).personOneId) {
                            pair1Index = j;
                            break;
                        }
                    }

                    int pair2Index = -1;
                    for (int j = i + 1; j < allContacts.size(); ++j) {
                        if ((curGatheringPair.personTwoId == allContacts.get(j).personOneId && individual == allContacts.get(j).personTwoId) ||
                                curGatheringPair.personTwoId == allContacts.get(j).personTwoId && individual == allContacts.get(j).personOneId) {
                            pair2Index = j;
                            break;
                        }
                    }

                    // Add selected individuals and pairs in the gathering
                    if (pair1Index != -1 && pair2Index != -1) {
                        if (!allContacts.get(pair1Index).isInGathering && !allContacts.get(pair2Index).isInGathering) {
                            currentGatheringPairs.addLast(allContacts.get(pair1Index));
                            currentGatheringPairs.addLast(allContacts.get(pair2Index));
                            currentGatheringsQueue.addLast(allContacts.get(pair1Index));
                            currentGatheringsQueue.addLast(allContacts.get(pair2Index));
                            currentGatheringIndividuals.add(individual);
                            allContacts.get(pair1Index).isInGathering = true;
                            allContacts.get(pair2Index).isInGathering = true;
                        } else if (allContacts.get(pair1Index).isInGathering && !allContacts.get(pair2Index).isInGathering) {
                            currentGatheringPairs.addLast(allContacts.get(pair2Index));
                            currentGatheringsQueue.addLast(allContacts.get(pair2Index));
                            allContacts.get(pair2Index).isInGathering = true;
                        } else if (!allContacts.get(pair1Index).isInGathering && allContacts.get(pair2Index).isInGathering) {
                            currentGatheringPairs.addLast(allContacts.get(pair1Index));
                            currentGatheringsQueue.addLast(allContacts.get(pair1Index));
                            allContacts.get(pair1Index).isInGathering = true;
                        }
                    }
                }
            }

            // Add current gathering pairs and individuals in list of all the gatherings.
            gatherings.add(new Gathering(currentGatheringPairs, currentGatheringIndividuals));
        }

        int totalGatherings = 0;

        // Logic to find worthy gatherings from all the gatherings.
        for (Gathering gathering : gatherings) {
            if (gathering.individuals.size() >= minSize) {
                final int totalPairsInGathering = gathering.pairs.size();
                final int totalIndividualsInGathering = gathering.individuals.size();
                final int totalPossiblePairsInGathering = (totalIndividualsInGathering * (totalIndividualsInGathering - 1)) / 2;
                if (((float) totalPairsInGathering) / totalPossiblePairsInGathering >= density) {
                    totalGatherings = totalGatherings + 1;
                }
            }
        }

        // Return total gatherings found.
        return totalGatherings;
    }

    /**
     * {@code GovernmentDatabase} holds all the database related constants and queries used by {@code Government}.
     *
     * @author Dhrumil Amish Shah (B00857606)
     * created on 2021-04-02
     * @version 1.0.0
     * @see Government
     * @since 1.0.0
     */
    private static class GovernmentDatabase {
        private static final String TABLE_MOBILE_DEVICE = "mobile_device";
        private static final String TABLE_TEST_OUTCOME = "test_outcome";
        private static final String TABLE_MOBILE_DEVICE_TEST_OUTCOME = "mobile_device_test_outcome";
        private static final String TABLE_CONTACT = "contact";
        private static final String COLUMN_ID = "_id";
        private static final String COLUMN_MOBILE_DEVICE_HASH = "mobile_device_hash";
        private static final String COLUMN_TEST_HASH = "test_hash";
        private static final String COLUMN_TEST_DATE = "test_date";
        private static final String COLUMN_TEST_RESULT = "test_result";
        private static final String COLUMN_MOBILE_DEVICE_ID = "mobile_device_id";
        private static final String COLUMN_TEST_OUTCOME_ID = "test_outcome_id";
        private static final String COLUMN_CONTACT_DATE = "contact_date";
        private static final String COLUMN_CONTACT_DURATION = "contact_duration";
        private static final String COLUMN_PERSON_ONE_ID = "person_one_id";
        private static final String COLUMN_PERSON_TWO_ID = "person_two_id";
        private static final String COLUMN_CONTACT_NOTIFIED = "contact_notified";

        /**
         * Gets the query string to create {@value TABLE_MOBILE_DEVICE} table
         * with columns {@value COLUMN_ID} and {@value COLUMN_MOBILE_DEVICE_HASH}.
         *
         * @return query string to create {@value TABLE_MOBILE_DEVICE} table.
         */
        private static String createTableMobileDevice() {
            return "CREATE TABLE IF NOT EXISTS " + TABLE_MOBILE_DEVICE + "(" +
                    COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT," +
                    COLUMN_MOBILE_DEVICE_HASH + " VARCHAR(128) UNIQUE NOT NULL" +
                    ");";
        }

        /**
         * Gets the query string to create {@value TABLE_TEST_OUTCOME} table
         * with columns {@value COLUMN_ID}, {@value COLUMN_TEST_HASH}, {@value COLUMN_TEST_DATE} and {@value COLUMN_TEST_RESULT}.
         *
         * @return query string to create {@value TABLE_TEST_OUTCOME} table.
         */
        private static String createTableTestOutcome() {
            return "CREATE TABLE IF NOT EXISTS " + TABLE_TEST_OUTCOME + "(" +
                    COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT," +
                    COLUMN_TEST_HASH + " VARCHAR(128) UNIQUE NOT NULL," +
                    COLUMN_TEST_DATE + " DATE NOT NULL," +
                    COLUMN_TEST_RESULT + " BOOLEAN NOT NULL" +
                    ");";
        }

        /**
         * Gets the query string to create {@value TABLE_MOBILE_DEVICE_TEST_OUTCOME} table
         * with columns {@value COLUMN_ID}, {@value COLUMN_MOBILE_DEVICE_ID} and {@value COLUMN_TEST_OUTCOME_ID}
         * which connects {@value TABLE_MOBILE_DEVICE} and {@value TABLE_TEST_OUTCOME} tables.
         *
         * @return query string to create {@value TABLE_MOBILE_DEVICE_TEST_OUTCOME} table.
         */
        private static String createTableMobileDeviceTestOutcome() {
            return "CREATE TABLE IF NOT EXISTS " + TABLE_MOBILE_DEVICE_TEST_OUTCOME + "(\n" +
                    COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT," +
                    COLUMN_MOBILE_DEVICE_ID + " INT NOT NULL," +
                    COLUMN_TEST_OUTCOME_ID + " INT NOT NULL," +
                    "FOREIGN KEY(" + COLUMN_MOBILE_DEVICE_ID + ") REFERENCES " + TABLE_MOBILE_DEVICE + "(" + COLUMN_ID + ")," +
                    "FOREIGN KEY(" + COLUMN_TEST_OUTCOME_ID + ") REFERENCES " + TABLE_TEST_OUTCOME + "(" + COLUMN_ID + ")," +
                    "UNIQUE(" + COLUMN_MOBILE_DEVICE_ID + ", " + COLUMN_TEST_OUTCOME_ID + ")" +
                    ");";
        }

        /**
         * Gets the query string to create {@value TABLE_CONTACT} table
         * with columns {@value COLUMN_ID}, {@value COLUMN_CONTACT_DATE}, {@value COLUMN_CONTACT_DURATION},
         * {@value COLUMN_PERSON_ONE_ID}, {@value COLUMN_PERSON_TWO_ID} and {@value COLUMN_CONTACT_NOTIFIED}.
         *
         * @return query string to create {@value TABLE_CONTACT} table.
         */
        private static String createTableContact() {
            return "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACT + "(" +
                    COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT," +
                    COLUMN_CONTACT_DATE + " DATE NOT NULL," +
                    COLUMN_CONTACT_DURATION + " INT NOT NULL," +
                    COLUMN_PERSON_ONE_ID + " INT NOT NULL," +
                    COLUMN_PERSON_TWO_ID + " INT NOT NULL," +
                    COLUMN_CONTACT_NOTIFIED + " BOOLEAN NOT NULL," +
                    "FOREIGN KEY(" + COLUMN_PERSON_ONE_ID + ") REFERENCES " + TABLE_MOBILE_DEVICE + "(" + COLUMN_ID + ")," +
                    "FOREIGN KEY(" + COLUMN_PERSON_TWO_ID + ") REFERENCES " + TABLE_MOBILE_DEVICE + "(" + COLUMN_ID + ")" +
                    ");";
        }

        /**
         * Gets the query string to insert all mobile device configuration hashes in {@value TABLE_MOBILE_DEVICE} table.
         *
         * @param initiator            initiator's mobile device configuration hash.
         * @param contactPersonTwoList list of mobile device configuration hashes contacted by the initiator.
         * @return query string to insert all mobile device configuration hashes in {@value TABLE_MOBILE_DEVICE} table.
         */
        private static String getInsertMobileDeviceQuery(String initiator, LinkedList<String> contactPersonTwoList) {
            final StringBuilder insertMobileDeviceSB = new StringBuilder();

            insertMobileDeviceSB.append("INSERT IGNORE INTO ")
                    .append(TABLE_MOBILE_DEVICE)
                    .append("(").append(COLUMN_MOBILE_DEVICE_HASH)
                    .append(") VALUES ");

            insertMobileDeviceSB.append("(\"").append(initiator).append("\")");

            contactPersonTwoList.forEach(contactPerson -> insertMobileDeviceSB.append(", (\"").append(contactPerson).append("\")"));

            insertMobileDeviceSB.append(";");
            return insertMobileDeviceSB.toString();
        }

        /**
         * Gets the query string to insert all positive test hashes of the {@code initiator} in {@value TABLE_MOBILE_DEVICE_TEST_OUTCOME} table.
         *
         * @param initiator                       initiator's mobile device configuration hash.
         * @param initiatorPositiveTestHashesList list of positive test hashes of the initiator.
         * @return query string to insert all positive test hashes of the {@code initiator} in {@value TABLE_MOBILE_DEVICE_TEST_OUTCOME} table.
         */
        private static String getInsertMobileDeviceTestResultQuery(String initiator, LinkedList<String> initiatorPositiveTestHashesList) {
            final StringBuilder insertMobileDeviceTestResultSB = new StringBuilder();

            insertMobileDeviceTestResultSB.append("INSERT IGNORE INTO ")
                    .append(TABLE_MOBILE_DEVICE_TEST_OUTCOME)
                    .append("(").append(COLUMN_MOBILE_DEVICE_ID).append(", ")
                    .append(COLUMN_TEST_OUTCOME_ID)
                    .append(") VALUES ");

            initiatorPositiveTestHashesList.forEach(initiatorPositiveHash -> {
                insertMobileDeviceTestResultSB.append("(");
                insertMobileDeviceTestResultSB.append("(SELECT ").append(COLUMN_ID).append(" FROM ").append(TABLE_MOBILE_DEVICE).append(" WHERE ").append(COLUMN_MOBILE_DEVICE_HASH).append(" = \"").append(initiator).append("\")");
                insertMobileDeviceTestResultSB.append(", ");
                insertMobileDeviceTestResultSB.append("(SELECT ").append(COLUMN_ID).append(" FROM ").append(TABLE_TEST_OUTCOME).append(" WHERE ").append(COLUMN_TEST_HASH).append(" = \"").append(initiatorPositiveHash).append("\")");
                insertMobileDeviceTestResultSB.append("), ");
            });

            insertMobileDeviceTestResultSB.replace(insertMobileDeviceTestResultSB.length() - 2, insertMobileDeviceTestResultSB.length(), ";");
            return insertMobileDeviceTestResultSB.toString();
        }

        /**
         * Gets the query string to insert all the contacts made by the {@code initiator} in {@value COLUMN_CONTACT_DATE}.
         *
         * @param initiator             initiator's mobile device configuration hash.
         * @param contactPersonTwoList  list of mobile device configuration hashes contacted by the {@code initiator}.
         * @param dateOfContactList     list of contact dates by the {@code initiator}.
         * @param durationOfContactList list of contact duration by the {@code initiator}.
         * @return query string to insert all the contacts made by the {@code initiator} in {@value COLUMN_CONTACT_DATE}.
         */
        private static String getInsertContactQuery(String initiator, LinkedList<String> contactPersonTwoList, LinkedList<String> dateOfContactList, LinkedList<Integer> durationOfContactList) {
            final StringBuilder insertContactSB = new StringBuilder();

            insertContactSB.append("INSERT INTO ")
                    .append(TABLE_CONTACT)
                    .append("(").append(COLUMN_CONTACT_DATE).append(", ")
                    .append(COLUMN_CONTACT_DURATION).append(", ")
                    .append(COLUMN_PERSON_ONE_ID).append(", ")
                    .append(COLUMN_PERSON_TWO_ID).append(", ")
                    .append(COLUMN_CONTACT_NOTIFIED)
                    .append(") VALUES ");

            for (int i = 0; i < contactPersonTwoList.size(); ++i) {
                insertContactSB.append("(");
                insertContactSB.append("\"").append(dateOfContactList.get(i)).append("\", ");
                insertContactSB.append(durationOfContactList.get(i)).append(", ");
                insertContactSB.append("(SELECT ").append(COLUMN_ID).append(" FROM ").append(TABLE_MOBILE_DEVICE).append(" WHERE ").append(COLUMN_MOBILE_DEVICE_HASH).append(" = \"").append(initiator).append("\"), ");
                insertContactSB.append("(SELECT ").append(COLUMN_ID).append(" FROM ").append(TABLE_MOBILE_DEVICE).append(" WHERE ").append(COLUMN_MOBILE_DEVICE_HASH).append(" = \"").append(contactPersonTwoList.get(i)).append("\"), ");
                insertContactSB.append(false);
                insertContactSB.append("), ");
            }
            insertContactSB.replace(insertContactSB.length() - 2, insertContactSB.length(), ";");
            return insertContactSB.toString();
        }

        /**
         * Gets the query string to insert COVID-19 test result in {@value TABLE_TEST_OUTCOME} table.
         *
         * @param testHash report test hash.
         * @param testDate report test date.
         * @param result   report result. {@code true} for positive and {@code false} for negative.
         * @return query string to insert COVID-19 test result in {@value TABLE_TEST_OUTCOME} table.
         */
        private static String getInsertTestResultQuery(String testHash, String testDate, boolean result) {
            return "INSERT IGNORE INTO " +
                    TABLE_TEST_OUTCOME +
                    "(" + COLUMN_TEST_HASH + ", " + COLUMN_TEST_DATE + ", " + COLUMN_TEST_RESULT + ")" +
                    " VALUES" +
                    "(\"" + testHash + "\", \"" + testDate + "\", " + result + ");";
        }

        /**
         * Gets the query string to check whether {@code initiator} contacted any COVID-19 individuals.
         *
         * @param initiator initiator's configuration hash. (mobile device configuration hash)
         * @return query string to select the contact ids of contacted COVID-19 individuals.
         */
        private static String getSelectTestInitiatorQuery(String initiator) {
            return "SELECT " +
                    "c." + COLUMN_ID + " " +
                    "FROM " +
                    "" + TABLE_CONTACT + " AS c, " + TABLE_MOBILE_DEVICE_TEST_OUTCOME + " AS mdtr, " + TABLE_TEST_OUTCOME + " AS tr " +
                    "WHERE " +
                    "c." + COLUMN_PERSON_TWO_ID + " = mdtr." + COLUMN_MOBILE_DEVICE_ID + " AND " +
                    "mdtr." + COLUMN_TEST_OUTCOME_ID + " = tr." + COLUMN_ID + " AND " +
                    "c." + COLUMN_PERSON_ONE_ID + " = (SELECT " + COLUMN_ID + " FROM " + TABLE_MOBILE_DEVICE + " WHERE " + COLUMN_MOBILE_DEVICE_HASH + " = \"" + initiator + "\") AND " +
                    "ABS(DATEDIFF(c." + COLUMN_CONTACT_DATE + ", tr." + COLUMN_TEST_DATE + ")) BETWEEN 0 AND 14 AND " +
                    "tr." + COLUMN_TEST_RESULT + " = true AND " +
                    "c." + COLUMN_CONTACT_NOTIFIED + " = false ;";
        }

        /**
         * Gets the query string to update notify column in {@value TABLE_CONTACT} table
         * for the contacts who are already considered once to report COVID-19.
         *
         * @param contactIds contacts whose notify flag is to be set true.
         * @return query string to update the notify column in {@value TABLE_CONTACT}.
         */
        private static String updateContactNotifiedForContactQuery(LinkedList<Integer> contactIds) {
            final StringBuilder sb = new StringBuilder();

            sb.append("UPDATE ").append(TABLE_CONTACT).append(" AS c ");
            sb.append("SET c.").append(COLUMN_CONTACT_NOTIFIED).append(" = true ");
            sb.append("WHERE c.").append(COLUMN_ID).append(" IN (");
            for (int i = 0; i < contactIds.size(); ++i) {
                if (i == (contactIds.size() - 1)) {
                    sb.append(contactIds.get(i)).append(");");
                } else {
                    sb.append(contactIds.get(i)).append(", ");
                }
            }
            return sb.toString();
        }

        /**
         * Gets the query string to fetch pairs for calculating gatherings from {@value TABLE_CONTACT} table.
         *
         * @param gatheringDate date of gathering.
         * @param minTime       minimum time of contact for each pair. (i.e., between individuals)
         * @return query string to fetch pairs for calculating gatherings from {@value TABLE_CONTACT} table.
         */
        private static String getContactGatheringQuery(String gatheringDate, int minTime) {
            return "SELECT c." + COLUMN_PERSON_ONE_ID + ", c." + COLUMN_PERSON_TWO_ID + " " +
                    "FROM " + TABLE_CONTACT + " AS c " +
                    "WHERE c." + COLUMN_CONTACT_DATE + " = \"" + gatheringDate + "\" " +
                    "GROUP BY c." + COLUMN_PERSON_ONE_ID + ", c." + COLUMN_PERSON_TWO_ID + " " +
                    "HAVING SUM(c." + COLUMN_CONTACT_DURATION + ") >= " + minTime + " " +
                    "ORDER BY c." + COLUMN_PERSON_ONE_ID + ", c." + COLUMN_PERSON_TWO_ID + ";";
        }
    }

    /**
     * {@code GovernmentConstant} holds all the constants used by {@code Government}.
     *
     * @author Dhrumil Amish Shah (B00857606)
     * created on 2021-04-02
     * @version 1.0.0
     * @see Government
     * @since 1.0.0
     */
    private static class GovernmentConstant {
        private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        private static final String DATABASE_PATH_KEY = "database";
        private static final String DATABASE_USER_KEY = "user";
        private static final String DATABASE_PASSWORD_KEY = "password";
        private static final String CONTACT = "Contact";
        private static final String INDIVIDUAL = "Individual";
        private static final String DATE = "Date";
        private static final String DURATION = "Duration";
        private static final String TEST_HASH = "TestHash";
    }

    /**
     * {@code Pair} holds two individuals as a pair.
     *
     * @author Dhrumil Amish Shah (B00857606)
     * created on 2021-04-02
     * @version 1.0.0
     * @see Gathering
     * @since 1.0.0
     */
    private static class Pair {
        // personOneId holds id of person one in this pair.
        final int personOneId;

        // personTwoId holds id of person two in this pair.
        final int personTwoId;

        // isInGathering holds boolean to indicate whether this pair is grouped in a gathering or not. (Initial value if false)
        boolean isInGathering;

        /**
         * Constructs this {@code Pair} with {@code personOneId} and {@code personTwoId}.
         *
         * @param personOneId id of person one in this pair.
         * @param personTwoId id of person two in this pair.
         */
        private Pair(int personOneId, int personTwoId) {
            this.personOneId = personOneId;
            this.personTwoId = personTwoId;
            this.isInGathering = false;
        }
    }

    /**
     * {@code Gathering} holds all pairs and individuals of a gathering.
     *
     * @author Dhrumil Amish Shah (B00857606)
     * created on 2021-04-02
     * @version 1.0.0
     * @see Pair
     * @since 1.0.0
     */
    private static class Gathering {
        // pairs holds all the pairs in this gathering.
        private final LinkedList<Pair> pairs;

        // individuals holds all the individuals in this gathering.
        private final LinkedHashSet<Integer> individuals;

        /**
         * Constructs this {@code Gathering} with {@code pairs} and {@code individual}.
         *
         * @param pairs       all pairs in this gathering.
         * @param individuals all individuals in this gathering.
         */
        private Gathering(LinkedList<Pair> pairs, LinkedHashSet<Integer> individuals) {
            this.pairs = pairs;
            this.individuals = individuals;
        }
    }
}