import java.util.ArrayList;

/**
 * CourseSelectorInterface provides the underlying structure for the CourseSelector class
 *
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @see CourseSelector
 * @since 2021-01-23
 */
public interface CourseSelectorInterface {

    /**
     * Method to read the content of file fileName if exists
     *
     * @param fileName input file name
     * @return number of lines read if file exists otherwise -1
     */
    int read(String fileName);

    /**
     * Recommend method returns a list of numRec course recommendations given a list (taken) of courses already taken
     * separated by spaces. Only report courses if the recommendations are supported by at least support other students.
     *
     * @param taken   courses taken by current student
     * @param support courses taken by at least support number of students
     * @param numRec  number of courses to recommend
     * @return list of recommended courses if found otherwise null
     */
    ArrayList<String> recommend(String taken, int support, int numRec);

    /**
     * Method to print a 2D array with one row and column for each course that has been taken by any student,
     * in the order in which they appear. Each cell contains number of students who have taken both the courses
     *
     * @param courses string of courses separated by space
     * @return true if no error encountered otherwise false
     */
    boolean showCommon(String courses);

    /**
     * Method to print a 2D array with one row and column for every course that has been taken by any student,
     * in order of course name. Each cell contains number of students who have taken both the courses
     *
     * @param fileName output file name
     * @return true if no error encountered otherwise false
     */
    boolean showCommonAll(String fileName);
}