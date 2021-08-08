package utils;

import java.io.File;

/**
 * FileUtils class contains all the necessary file related operations performed by the program
 *
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @since 2021-01-23
 */
public class FileUtils {

    /**
     * Empty constructor. Class cannot be instantiated
     */
    private FileUtils() {
        // Empty constructor. Class cannot be instantiated
    }

    /**
     * Method to check if file name is valid or not
     * Validation: not null, not empty, ends with .txt
     *
     * @param fileName file name
     * @return true if file name is valid otherwise false
     */
    public static boolean isFileNameValid(String fileName) {
        // Return false if fileName is null
        if (fileName == null) {
            // File name cannot be null
            return false;
        }

        // Return false if fileName is empty
        if (fileName.trim().isEmpty()) {
            // File name cannot be empty
            return false;
        }

        // Return false if fileName does not ends with .txt file extension
        if (!fileName.trim().endsWith(".txt")) {
            // Invalid file extension. File extension can only be .txt
            return false;
        }

        // Return true if file name is valid (i.e not null, not empty, and ends with .txt)
        return true;
    }

    /**
     * Method to check if file fileName exists or not
     *
     * @param fileName file name (Absolute file path is provided)
     * @return true if file exists otherwise false
     */
    public static boolean fileExists(String fileName) {

        // Return false if file name is not valid
        if (!isFileNameValid(fileName)) return false;

        // Get file path. File exists in current folder and fileName is absolute path
        File filePath = new File(fileName.trim());

        // Return false if file does not exist in current folder
        if (!filePath.exists() || !filePath.isFile()) {
            // File fileName does not exist
            return false;
        }

        // Return true as file exists in current folder
        return true;
    }
}