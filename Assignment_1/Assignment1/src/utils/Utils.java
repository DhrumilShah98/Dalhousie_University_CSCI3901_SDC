package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;

/**
 * Utils class contains all the common operations performed by the program
 *
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @since 2021-01-23
 */
public class Utils {
    /**
     * Method to check if course name is valid or not
     * Validation: not null, not empty, 8 characters, only alphanumeric string
     *
     * @param courseName course name
     * @return true if course name is valid otherwise false
     */
    public static boolean isCourseNameValid(String courseName) {
        // Return false if courseName is null
        if (courseName == null) {
            // Course name cannot be null
            return false;
        }

        // Trim courseName to remove leading and trailing spaces
        courseName = courseName.trim();

        // Return false if courseName is empty
        if (courseName.isEmpty()) {
            // Course name cannot be empty
            return false;
        }

        // Return false if courseName is not of 8 characters
        if (courseName.length() != 8) {
            // Course name can only be of 8 characters long
            return false;
        }

        // Return true if courseName is alphanumeric string otherwise false
        if (!courseName.matches("[0-9a-zA-Z]{8}")) {
            // Course name can only be alphanumeric string
            return false;
        }

        // Return true as course name is valid
        return true;
    }

    /**
     * Method to get refined courses array list ([c1, c2, c3, ...]) from courses string ("c1 c2 c3 ...")
     *
     * @param courses courses string separated by space character
     * @return refined courses array list otherwise null
     */
    public static ArrayList<String> getRefinedStringCoursesList(String courses) {
        // Return null if courses string is null
        if (courses == null) {
            // Courses string cannot be null
            return null;
        }

        // Return null if courses string is empty
        if (courses.trim().isEmpty()) {
            // Courses string cannot be empty
            return null;
        }

        // Initial courses array
        String[] coursesArr = courses.trim().split(" ");

        // Final courses list. Refined.
        // Maintain insertion order and does not allow duplicate values
        ArrayList<String> refinedCoursesList = new ArrayList<>();

        // Iterate through coursesArr and filter out wrong courses if any
        for (String currentCourse : coursesArr) {
            if (Utils.isCourseNameValid(currentCourse) && !refinedCoursesList.contains(currentCourse.toUpperCase())) {
                refinedCoursesList.add(currentCourse.toUpperCase());
            }
        }

        // Return refined courses array list
        return refinedCoursesList;
    }

    /**
     * Method to sort hash map by value and return array list with map entries.
     *
     * @param map hash map with keys and values
     * @return array list of map entries
     */
    public static ArrayList<HashMap.Entry<String, Integer>> sortHashMapByValue(HashMap<String, Integer> map) {
        // Create a list from elements of HashMap
        ArrayList<HashMap.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());

        // Sort the list
        // Swap when element at index 'i' is smaller than element at index 'i + 1'.
        list.sort(new Comparator<HashMap.Entry<String, Integer>>() {
            @Override
            public int compare(HashMap.Entry<String, Integer> o1, HashMap.Entry<String, Integer> o2) {
                if (o1.getValue() < o2.getValue()) return 1; // Swap
                else if (o1.getValue().equals(o2.getValue())) return 0;
                else return -1;
            }
        });

        // Return array list of map entries
        return list;
    }
}