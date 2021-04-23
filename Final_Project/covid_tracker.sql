# CREATE AND USE DATABASE "covid_tracker". (FOR LOCAL USE UNCOMMENT BELOW TWO LINES)
CREATE DATABASE IF NOT EXISTS covid_tracker;
USE covid_tracker;

# USE DATABASE "CS_ID". (FOR REMOTE USE UNCOMMENT BELOW LINE)
# USE CS_ID;

# CREATES "mobile_device" TABLE.
CREATE TABLE IF NOT EXISTS mobile_device(
	_id INT PRIMARY KEY AUTO_INCREMENT,
    mobile_device_hash VARCHAR(128) UNIQUE NOT NULL
);

# CREATES "test_outcome" TABLE.
CREATE TABLE IF NOT EXISTS test_outcome(
	_id INT PRIMARY KEY AUTO_INCREMENT,
     test_hash VARCHAR(128) UNIQUE NOT NULL,
     test_date DATE NOT NULL,
     test_result BOOLEAN NOT NULL
);

# CREATES "mobile_device_test_outcome" TABLE.
CREATE TABLE IF NOT EXISTS mobile_device_test_outcome(
	_id INT PRIMARY KEY AUTO_INCREMENT,
    mobile_device_id INT NOT NULL,
    test_outcome_id INT NOT NULL,
    FOREIGN KEY(mobile_device_id) REFERENCES mobile_device(_id),
    FOREIGN KEY(test_outcome_id) REFERENCES test_outcome(_id),
    UNIQUE(mobile_device_id, test_outcome_id)
);

# CREATES "contact" TABLE.
CREATE TABLE IF NOT EXISTS contact(
_id INT PRIMARY KEY AUTO_INCREMENT,
    contact_date DATE NOT NULL,
    contact_duration INT NOT NULL,
	person_one_id INT NOT NULL,
    person_two_id INT NOT NULL,
    contact_notified BOOLEAN NOT NULL,
    FOREIGN KEY(person_one_id) REFERENCES mobile_device(_id),
    FOREIGN KEY(person_two_id) REFERENCES mobile_device(_id)
);

# DROP ALL TABLES
# DROP TABLE contact;
# DROP TABLE mobile_device_test_outcome;
# DROP TABLE test_outcome;
# DROP TABLE mobile_device;

# TRUNCATE ALL TABLES TO REMOVE ALL RECORDS
# TRUNCATE TABLE contact;
# TRUNCATE TABLE mobile_device_test_outcome;
# TRUNCATE TABLE test_outcome;
# TRUNCATE TABLE mobile_device;