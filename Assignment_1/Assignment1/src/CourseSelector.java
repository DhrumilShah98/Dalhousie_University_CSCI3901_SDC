import utils.FileUtils;
import utils.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * CourseSelector class collects the data on which courses students have taken
 * It allow users to make queries about the data
 * This class is used by CoursinatorV1
 *
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @see CoursinatorV1
 * @see FileUtils
 * @see Utils
 * @since 2021-01-23
 */
public class CourseSelector implements CourseSelectorInterface {

    /**
     * 2D array list that contains student indices and courses (i.e array list of array list)
     * The outer array list contains student indices and each index points to a array list of courses taken by that student
     * [1]->[c1][c2][c3]        // 3 courses taken by student 1
     * [2]->[c2][c4][c3][c5]    // 4 courses taken by student 2
     * [3]->[c1][c5][c6]        // 3 courses taken by student 3
     * [4]->[c7]                // 1 course  taken by student 4
     */
    private ArrayList<ArrayList<String>> studentsCoursesList = null;

    /**
     * Tree set(Sorted set) of all courses from studentsCoursesList
     * No repetition of courses
     * c1 c2 c3 c4 c5 c6 c7 ...
     */
    private TreeSet<String> allCoursesSet = null;

    /**
     * Method to read the content of file fileName if exists
     *
     * @param fileName input file name
     * @return number of lines read if file exists otherwise -1
     */
    @Override
    public int read(String fileName) {
        // Total number of lines read
        int numberOfLinesRead;

        // If file exists, read it and return number of lines read otherwise return -1
        if (FileUtils.fileExists(fileName)) {
            numberOfLinesRead = readInputFileData(fileName);
        } else {
            // File does not exists. Check your file name and try again
            numberOfLinesRead = -1;
        }

        // Return number of lines read
        return numberOfLinesRead;
    }

    /**
     * Recommend method returns a list of numRec course recommendations given a list (taken) of courses already taken
     * separated by spaces. Only report courses if the recommendations are supported by at least support other students.
     *
     * @param taken   courses taken by current student
     * @param support courses taken by at least support number of students
     * @param numRec  number of courses to recommend
     * @return list of recommended courses if found otherwise null
     */
    @Override
    public ArrayList<String> recommend(String taken, int support, int numRec) {

        // Return null if support number of students is negative
        if (support < 0) {
            // Support for recommendations cannot be negative
            return null;
        }

        // Return null if numRec number of recommendations is negative or 0
        if (numRec <= 0) {
            // Number of recommendations cannot be negative or 0
            return null;
        }

        // Return false if studentsCoursesList or allCoursesSet is null or empty
        if (studentsCoursesList == null || allCoursesSet == null ||
                studentsCoursesList.isEmpty() || allCoursesSet.isEmpty()) {
            // Please read data from file first
            return null;
        }

        // Get string array of refined courses from initial courses taken string
        ArrayList<String> takenCoursesList = Utils.getRefinedStringCoursesList(taken);

        // Return false if takenCoursesList is null
        if (takenCoursesList == null) {
            // Enter valid courses
            return null;
        }

        // Array list of student indices who have taken all courses from takenCoursesList
        ArrayList<Integer> supportedStudentsIndices = new ArrayList<>();

        // Iterate through all the students and courses from studentsCoursesList
        // Select student index if that student has taken all courses from takenCoursesList
        for (int curStudentIndex = 0; curStudentIndex < studentsCoursesList.size(); ++curStudentIndex) {
            ArrayList<String> currentStudentCourses = studentsCoursesList.get(curStudentIndex);
            boolean allCoursesExists = true;
            for (String s : takenCoursesList) {
                if (!currentStudentCourses.contains(s)) {
                    allCoursesExists = false;
                    break;
                }
            }
            // Add only if student has taken all courses from takenCoursesList
            if (allCoursesExists) supportedStudentsIndices.add(curStudentIndex);
        }

        // Return null if no students found having taken courses
        if (supportedStudentsIndices.isEmpty()) {
            // No students have taken these courses. Not able to provide recommendations
            return null;
        }
        // Return null if number of supported students is less than support students or
        // Not enough students to make recommendation
        if (supportedStudentsIndices.size() < support) {
            // Very few students have taken these courses. Sorry!
            return null;
        }

        // Map of courses and number of students who have taken it
        // Key is course name and value is course frequency
        HashMap<String, Integer> recommendCoursesMap = new HashMap<>();

        // Iterate through all the supported students and fill recCoursesMap
        // Key is course name and value is freq of that course (Number of students who have taken it)
        for (Integer studentIndex : supportedStudentsIndices) {
            for (String studentCourse : studentsCoursesList.get(studentIndex)) {
                if (!takenCoursesList.contains(studentCourse)) {
                    if (recommendCoursesMap.containsKey(studentCourse)) {
                        // Update the frequency of added course
                        recommendCoursesMap.put(studentCourse, (recommendCoursesMap.get(studentCourse) + 1));
                    } else {
                        // Add course in map and set frequency to 1
                        recommendCoursesMap.put(studentCourse, 1);
                    }
                }
            }
        }

        // Return empty array list(i.e. []) if selected students have not taken any courses
        // other than courses in takenCoursesList. (No recommendations found)
        if (recommendCoursesMap.isEmpty()) {
            return new ArrayList<>();
        }

        // Sort the recCoursesMap in descending order based on freq of course
        // i.e Course at index 'i' have frequency greater than course at index 'i + 1'
        ArrayList<HashMap.Entry<String, Integer>> recommendCoursesListSorted = Utils.sortHashMapByValue(recommendCoursesMap);

        // Array list of all the recommended courses
        ArrayList<String> finalRecommendCourses = new ArrayList<>();

        // If courses to be recommended (numRec) is more than available recommended courses, display all
        if (numRec >= recommendCoursesListSorted.size()) {
            for (HashMap.Entry<String, Integer> course : recommendCoursesListSorted)
                finalRecommendCourses.add(course.getKey());
        } else {
            // numRec < total courses to be recommended
            int courseIndex;
            // Take first numRec number of courses from all recommended courses provided
            for (courseIndex = 0; courseIndex < numRec; ++courseIndex) {
                finalRecommendCourses.add(recommendCoursesListSorted.get(courseIndex).getKey());
            }

            // Take next courses only if the frequency of courses in all recommended courses is equal to last course in recCourses
            int prevCourseFreq = recommendCoursesListSorted.get(courseIndex - 1).getValue();
            for (courseIndex = numRec; courseIndex < recommendCoursesListSorted.size(); ++courseIndex) {
                if (prevCourseFreq == recommendCoursesListSorted.get(courseIndex).getValue()) {
                    finalRecommendCourses.add(recommendCoursesListSorted.get(courseIndex).getKey());
                }
            }
        }

        // Return array list of all the recommended courses
        return finalRecommendCourses;
    }

    /**
     * Method to print a 2D array with one row and column for each course that has been taken by any student,
     * in the order in which they appear. Each cell contains number of students who have taken both the courses
     *
     * @param courses string of courses separated by space
     * @return true if no error encountered otherwise false
     */
    @Override
    public boolean showCommon(String courses) {

        // Return false if studentsCoursesList or allCoursesSet is null or empty
        if (studentsCoursesList == null || allCoursesSet == null ||
                studentsCoursesList.isEmpty() || allCoursesSet.isEmpty()) {
            // Please read data from file first
            return false;
        }

        // Get string array of refined courses from initial courses string
        ArrayList<String> coursesList = Utils.getRefinedStringCoursesList(courses);

        // Return false if coursesArr is null
        if (coursesList == null) {
            // Enter valid courses
            return false;
        }

        // Return false if number of courses less than or equal to 1
        if (coursesList.size() <= 1) {
            // Very few courses entered. Please enter at least 2 courses
            return false;
        }

        // Course matrix (2D array)
        int[][] coursesStudentsMatrix = getCoursesMatrix(coursesList.toArray(new String[0]));

        // Print courses matrix on screen
        StringBuilder sb = new StringBuilder();
        for (int rowIndex = 0; rowIndex < coursesStudentsMatrix.length; ++rowIndex) {
            sb.append(coursesList.get(rowIndex));
            sb.append("\t");
            for (int colIndex = 0; colIndex < coursesStudentsMatrix[rowIndex].length; ++colIndex) {
                sb.append(coursesStudentsMatrix[rowIndex][colIndex]);
                sb.append("\t");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());

        // Return true after matrix is printed on screen
        return true;
    }

    /**
     * Method to print a 2D array with one row and column for every course that has been taken by any student,
     * in order of course name. Each cell contains number of students who have taken both the courses
     *
     * @param fileName output file name
     * @return true if no error encountered otherwise false
     */
    @Override
    public boolean showCommonAll(String fileName) {

        // Return false if studentsCoursesList or allCoursesSet is null or empty
        if (studentsCoursesList == null || allCoursesSet == null ||
                studentsCoursesList.isEmpty() || allCoursesSet.isEmpty()) {
            // Please read data from file first
            return false;
        }

        // Return false if fileName is not valid
        if (!FileUtils.isFileNameValid(fileName)) return false;

        // Get output file path
        File filePath = new File(fileName.trim());

        // Create a new file if it does not exists
        if (!filePath.exists()) {
            try {
                // Create a new file
                boolean newFileCreated = filePath.createNewFile();

                // Return false if error occurred while creating a new file
                if (!newFileCreated && !filePath.isFile()) {
                    // Error while creating file fileName. Please try again
                    return false;
                }
            } catch (Exception e) {
                // Return false if error occurred while creating a new file
                // Something went wrong. File fileName not created
                return false;
            }
        }

        // Create string array from allCoursesSet
        final String[] allCoursesArr = allCoursesSet.toArray(new String[0]);

        // Course matrix (2D array)
        int[][] coursesStudentsMatrix = getCoursesMatrix(allCoursesArr);

        // Return true if content is written in file otherwise false
        return writeOutputFileData(filePath, allCoursesArr, coursesStudentsMatrix);
    }

    /**
     * Method to read the content of file fileName
     *
     * @param fileName input file name
     * @return number of line read if file exists otherwise -1
     */
    private int readInputFileData(String fileName) {
        // Get input file path.
        File filePath = new File(fileName.trim());

        // Total number of lines read
        int numberOfLinesRead;

        // Create a buffered reader object for fast reading
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {

            // Current line read from file
            String currentLine = bufferedReader.readLine();

            // Return -1 if file is empty
            if (currentLine == null) {
                // File cannot be empty
                return -1;
            }

            // Initialize numberOfLinesRead to 0
            numberOfLinesRead = 0;

            // Initialize studentsCoursesList list
            studentsCoursesList = new ArrayList<>();

            // Initialize allCourses set
            allCoursesSet = new TreeSet<>();

            // Read file, one line at a time
            while (currentLine != null) {
                // Continue to next line if current line found is empty line
                if (currentLine.trim().isEmpty()) {
                    // Go to the next line in file. Will return null when EOF(End Of File) is reached
                    currentLine = bufferedReader.readLine();

                    // Return -1 if EOF reached and file is empty or only one student exists in file
                    if (currentLine == null) {
                        if (numberOfLinesRead == 0) {
                            // File cannot be empty
                            resetInstanceVariables();
                            return -1;
                        }
                        if (numberOfLinesRead == 1) {
                            // Only one student data exist in file
                            // Add more students and courses to get courses recommendations
                            resetInstanceVariables();
                            return -1;
                        }
                    }

                    // Continue to next line
                    continue;
                }

                // Current student index
                int currentStudentIndex = numberOfLinesRead;

                // Current student all courses string array
                String[] currentStudentCourses = currentLine.split(" ");

                // Initialize list to add courses for the current student
                studentsCoursesList.add(new ArrayList<>());

                // Iterate through courses of the current student
                for (String course : currentStudentCourses) {
                    // If course name is valid and not duplicate, add it to the current student courses list
                    // Also, add it with all other courses in allCourses set if not added already
                    if (Utils.isCourseNameValid(course) && !studentsCoursesList.get(currentStudentIndex).contains(course.toUpperCase())) {
                        studentsCoursesList.get(currentStudentIndex).add(course.toUpperCase());
                        allCoursesSet.add(course.toUpperCase());
                    }
                }

                // Increment number of lines read
                numberOfLinesRead++;

                // Go to the next line in file. Will return null when EOF(End Of File) is reached
                currentLine = bufferedReader.readLine();

                // Return -1 if EOF reached and only one student exists in file
                if (currentLine == null && numberOfLinesRead == 1) {
                    // Only one student data exist in file. Add more students and courses to get courses recommendations
                    resetInstanceVariables();
                    return -1;
                }
            }
        } catch (Exception e) {
            // Set numberOfLinesRead to -1 if some exception occurred
            // Something went wrong while reading fileName. Please try again.
            numberOfLinesRead = -1;
        }

        // Return number of lines read
        return numberOfLinesRead;
    }

    /**
     * Method to write the content in file provided
     *
     * @param filePath              output file path
     * @param allCourses            all courses
     * @param coursesStudentsMatrix courses and students matrix
     * @return true if content is written in file otherwise false
     */
    private boolean writeOutputFileData(File filePath, String[] allCourses, int[][] coursesStudentsMatrix) {
        // Create a buffered writer object for fast writing
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {

            // Delete any content in file if exists
            bufferedWriter.write("");

            // Write the 2D matrix in file
            for (int rowIndex = 0; rowIndex < coursesStudentsMatrix.length; ++rowIndex) {
                StringBuilder sb = new StringBuilder();
                sb.append(allCourses[rowIndex]);
                sb.append("\t");
                for (int colIndex = 0; colIndex < coursesStudentsMatrix[rowIndex].length; ++colIndex) {
                    sb.append(coursesStudentsMatrix[rowIndex][colIndex]);
                    sb.append("\t");
                }
                bufferedWriter.append(sb.toString());
                bufferedWriter.newLine();
            }

            // Return true on success
            return true;
        } catch (Exception e) {
            // Return false if file is not found
            // Something went wrong while writing to file fileName. Please try again.
            return false;
        }
    }

    /**
     * Method to get the course matrix (2D array) of size m x m where m is the length of allCourses array
     * Each cell gives the number of students who have taken both courses
     * Same courses are not paired and will always contain 0
     *
     * @param allCourses courses array of size m
     * @return course matrix (2D array) of size m x m
     */
    private int[][] getCoursesMatrix(final String[] allCourses) {
        // Course matrix (2D array) of size m x m where m is the length of allCourses array
        // Each cell gives the number of students who have taken both courses
        // Same courses are not paired and will always contain 0
        final int[][] coursesStudentsMatrix = new int[allCourses.length][allCourses.length];

        // Fill all the cells with 0
        for (int[] currentRow : coursesStudentsMatrix) {
            Arrays.fill(currentRow, 0);
        }

        // Iterate through allCourses array [c1, c2, c3, c4, ...]
        // courseOneIndex contains the current course index (first course)
        for (int courseOneIndex = 0; courseOneIndex < allCourses.length; ++courseOneIndex) {
            // courseOneName contains the current course name (first course name)
            String courseOneName = allCourses[courseOneIndex]; // Current course name, course 1 name
            // Iterate through list of students
            // currentStudentCoursesList is list of courses taken by current student
            for (ArrayList<String> currentStudentCoursesList : studentsCoursesList) {
                // Check if current student has taken courseOneName course (first course taken by current student)
                if (currentStudentCoursesList.contains(courseOneName)) {
                    // Iterate through remaining allCourses array [c2, c3, c4, ...]
                    // courseTwoIndex contains the current course index (second course)
                    for (int courseTwoIndex = (courseOneIndex + 1); courseTwoIndex < allCourses.length; ++courseTwoIndex) {
                        // courseTwoName contains the current course name (second course name)
                        String courseTwoName = allCourses[courseTwoIndex];
                        // Check if current student has also taken courseTwoName course (second course taken by current student)
                        if (currentStudentCoursesList.contains(courseTwoName)) {
                            // Current student has taken both the courses
                            // Add 1 to both the pairs
                            coursesStudentsMatrix[courseOneIndex][courseTwoIndex] += 1;
                            coursesStudentsMatrix[courseTwoIndex][courseOneIndex] += 1;
                        }
                    }
                }
            }
        }

        // Return 2D array of size m x m
        return coursesStudentsMatrix;
    }

    /**
     * Method to reset all instance variables
     */
    private void resetInstanceVariables() {
        studentsCoursesList = null;
        allCoursesSet = null;
    }
}