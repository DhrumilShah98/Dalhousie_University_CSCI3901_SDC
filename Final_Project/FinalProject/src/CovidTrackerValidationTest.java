import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;

@DisplayName("CovidTracker validation test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CovidTrackerValidationTest {

    @DisplayName("Validate Government.getInstance()")
    @Test
    @Order(1)
    public void validateGovernmentGetInstance() {
        // null value passed as argument configFile.
        Assertions.assertThrows(IllegalArgumentException.class, () -> Government.getInstance(null));

        // empty string passed as argument configFile.
        Assertions.assertThrows(IllegalArgumentException.class, () -> Government.getInstance(""));

        // file string passed as argument configFile that does not exists.
        Assertions.assertThrows(RuntimeException.class, () -> Government.getInstance("no_file.properties"));

        // file string passed as argument configFile with invalid syntax.
        Assertions.assertThrows(RuntimeException.class, () -> Government.getInstance("invalid_government.properties"));

        // wrong file string passed as argument configFile.
        Assertions.assertThrows(RuntimeException.class, () -> Government.getInstance("user_1_config.properties"));

        // valid file string passed as argument configFile.
        Assertions.assertNotNull(Government.getInstance("government.properties"));
    }

    @DisplayName("Validate Government.mobileContact()")
    @Test
    @Order(3)
    public void validateGovernmentMobileContact() {
        final Government government = Government.getInstance("government.properties");

        // null value passed as argument initiator.
        Assertions.assertThrows(IllegalArgumentException.class, () -> government.mobileContact(null, ""));

        // empty string passed as argument initiator.
        Assertions.assertThrows(IllegalArgumentException.class, () -> government.mobileContact("", ""));

        // null value passed as argument contactInfo indicates that initiator has not been near anyone diagnosed with COVID-19 in the last 14 days.
        Assertions.assertFalse(government.mobileContact("6c814daa4459d16a83308ae1a22024dc70f7e9972fce2f58e661ddbcb87d7121", null));

        // empty string passed as argument contactInfo indicates that initiator has not been near anyone diagnosed with COVID-19 in the last 14 days.
        Assertions.assertFalse(government.mobileContact("6c814daa4459d16a83308ae1a22024dc70f7e9972fce2f58e661ddbcb87d7121", ""));

        // invalid xml passed as argument to contactInfo.
        final String invalidXML = "<MobileDevice><ContactsList></ContactsList><TestHashesList></TestHashesList></MobileDevice";
        Assertions.assertThrows(RuntimeException.class, () -> government.mobileContact("6c814daa4459d16a83308ae1a22024dc70f7e9972fce2f58e661ddbcb87d7121", invalidXML));

        // empty xml passed as argument to contactInfo indicates that initiator has not been near anyone diagnosed with COVID-19 in the last 14 days.
        final String emptyXML = "<MobileDevice><ContactsList></ContactsList><TestHashesList></TestHashesList></MobileDevice>";
        Assertions.assertFalse(government.mobileContact("6c814daa4459d16a83308ae1a22024dc70f7e9972fce2f58e661ddbcb87d7121", emptyXML));
    }

    @DisplayName("Validate Government.recordTestResult()")
    @Test
    @Order(3)
    public void validateGovernmentRecordTestResult() {
        final Government government = Government.getInstance("government.properties");

        // null value passed as argument testHash.
        Assertions.assertThrows(IllegalArgumentException.class, () -> government.recordTestResult(null, 2, false));

        // empty string passed as argument testHash.
        Assertions.assertThrows(IllegalArgumentException.class, () -> government.recordTestResult("", 2, false));

        // negative value passed as argument date.
        Assertions.assertThrows(IllegalArgumentException.class, () -> government.recordTestResult("6c814daa4459d16a83308ae1a22024dc70f7e9772fce2f58e661ddbcb87d7122", -2, false));
    }

    @DisplayName("Validate Government.findGatherings()")
    @Test
    @Order(4)
    public void validateGovernmentFindGatherings() {
        final Government government = Government.getInstance("government.properties");

        // negative value passed as argument date.
        Assertions.assertThrows(IllegalArgumentException.class, () -> government.findGatherings(-2, 2, 30, 0.5f));

        // negative value passed as argument minSize.
        Assertions.assertThrows(IllegalArgumentException.class, () -> government.findGatherings(2, -2, 30, 0.5f));

        // zero value passed as argument minSize.
        Assertions.assertThrows(IllegalArgumentException.class, () -> government.findGatherings(2, 0, 30, 0.5f));

        // one value passed as argument minSize.
        Assertions.assertThrows(IllegalArgumentException.class, () -> government.findGatherings(2, 1, 30, 0.5f));

        // negative value passed as argument minTime.
        Assertions.assertThrows(IllegalArgumentException.class, () -> government.findGatherings(2, 1, -2, 0.5f));

        // zero value passed as argument minTime.
        Assertions.assertThrows(IllegalArgumentException.class, () -> government.findGatherings(2, 1, 0, 0.5f));

        // zero value passed as argument density.
        Assertions.assertThrows(IllegalArgumentException.class, () -> government.findGatherings(2, 1, 0, -0.5f));

        // greater than one value passed as argument density.
        Assertions.assertThrows(IllegalArgumentException.class, () -> government.findGatherings(2, 1, 0, 1.5f));
    }

    @DisplayName("Validate MobileDevice.MobileDevice()")
    @Test
    @Order(5)
    public void validateMobileDeviceConstructor() {
        final Government government = Government.getInstance("government.properties");

        // null value passed as argument configFile.
        Assertions.assertThrows(IllegalArgumentException.class, () -> new MobileDevice(null, government));

        // empty string passed as argument configFile.
        Assertions.assertThrows(IllegalArgumentException.class, () -> new MobileDevice("", government));

        // file string passed as argument configFile that does not exists.
        Assertions.assertThrows(RuntimeException.class, () -> new MobileDevice("no_user_config.properties", government));

        // file string passed as argument configFile with invalid syntax.
        Assertions.assertThrows(RuntimeException.class, () -> new MobileDevice("invalid_user_config.properties", government));

        // wrong file string passed as argument configFile.
        Assertions.assertThrows(RuntimeException.class, () -> new MobileDevice("government.properties", government));

        // null value passed as argument contactTracer.
        Assertions.assertThrows(RuntimeException.class, () -> new MobileDevice("government.properties", null));
    }

    @DisplayName("Validate MobileDevice.recordContact()")
    @Test
    @Order(6)
    public void validateMobileDeviceRecordContact() {
        final MobileDevice mobileDevice = new MobileDevice("user_12_config.properties", Government.getInstance("government.properties"));

        // null value passed as argument individual.
        Assertions.assertThrows(IllegalArgumentException.class, () -> mobileDevice.recordContact(null, 2, 30));

        // empty string value passed as argument individual.
        Assertions.assertThrows(IllegalArgumentException.class, () -> mobileDevice.recordContact("", 2, 30));

        // negative value passed as argument date.
        Assertions.assertThrows(IllegalArgumentException.class, () -> mobileDevice.recordContact("6c814daa4459d16a83308ae1a22024dc70f7e9972fce2f58e661ddbcb87d7134", -2, 30));

        // negative value passed as argument duration.
        Assertions.assertThrows(IllegalArgumentException.class, () -> mobileDevice.recordContact("6c814daa4459d16a83308ae1a22024dc70f7e9972fce2f58e661ddbcb87d7134", 2, -1));

        // zero value passed as argument date.
        Assertions.assertThrows(IllegalArgumentException.class, () -> mobileDevice.recordContact("6c814daa4459d16a83308ae1a22024dc70f7e9972fce2f58e661ddbcb87d7134", 2, 0));

        // same individual passed as argument individual.
        Assertions.assertFalse(mobileDevice.recordContact(mobileDevice.getMobileDeviceHash(), 3, 30));

        File file = new File(mobileDevice.getMobileDeviceHash() + ".xml");
        Assertions.assertTrue(file.delete());
    }

    @DisplayName("Validate MobileDevice.positiveTest()")
    @Test
    @Order(7)
    public void validateMobileDevicePositiveTest() {
        final MobileDevice mobileDevice = new MobileDevice("user_12_config.properties", Government.getInstance("government.properties"));

        // null value passed as argument testHash.
        Assertions.assertThrows(IllegalArgumentException.class, () -> mobileDevice.positiveTest(null));

        // empty string value passed as argument testHash.
        Assertions.assertThrows(IllegalArgumentException.class, () -> mobileDevice.positiveTest(""));

        File file = new File(mobileDevice.getMobileDeviceHash() + ".xml");
        Assertions.assertTrue(file.delete());
    }
}