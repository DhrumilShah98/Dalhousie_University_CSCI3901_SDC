/**
 * A silly interface to the course selector class
 */

import java.util.Scanner;
import java.util.ArrayList;

public class CoursinatorV1 {

    /**
     * An array of menu options
     */
    private static final String[] options = {
            "1: Enter data from file",
            "2: Recommend some courses",
            "3: Show conflicts",
            "4: Output all conflicts to file",
            "0: Exit"
    };

    /**
     * The main method
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        CourseSelector selector = new CourseSelector(); // Course selector
        Scanner scanner = new Scanner(System.in);       // For reading input
        int selection;                                  // Will hold the user's input selection

        System.out.println("I am the coursinator.");
        System.out.println("I will help you to coursinate your university.");

        do {
            System.out.println("\nPlease enter a selection:");
            for (int i = 0; i < options.length; i++) {
                System.out.println("\t" + options[i]);
            }

            // Check that the next input is an integer
            if (scanner.hasNextInt()) {
                // Get the next integer and clear the line
                selection = scanner.nextInt();
                scanner.nextLine();
            } else {
                // If the next input is not an integer, set to an invalid option number
                selection = -1;
                scanner.next();
            }

            // Main program loop
            switch (selection) {
                case 1: // Read
                    // Get the filename
                    System.out.println("\nPlease enter a filename:");
                    String inName = scanner.nextLine();

                    selector.read(inName);
                    break;

                case 2: // Recommend
                    // Get a list of courses
                    System.out.println("\nPlease enter a list of courses:");
                    String taken = scanner.nextLine();

                    // Get a support number
                    System.out.println("\nPlease enter a minimum support for recommendations:");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Ack, that wasn't right");
                        scanner.next();
                        break;
                    }
                    int support = scanner.nextInt();

                    // Get a number of recommendations
                    System.out.println("\nPlease enter a number of recommendations:");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Ack, that wasn't right");
                        scanner.next();
                        break;
                    }
                    int numRec = scanner.nextInt();

                    // Compute recommendations and print to screen
                    ArrayList<String> result = selector.recommend(taken, support, numRec);
                    System.out.println("\nRecommendations: " + result);

                    break;

                case 3: // Show Common
                    // Get a list of courses
                    System.out.println("\nPlease enter a list of courses:");
                    String courses = scanner.nextLine();

                    // Show the common pairs of courses and print an error message in case of an error
                    if (!selector.showCommon(courses)) {
                        System.out.println("Something went wrong");
                    }
                    break;

                case 4: // Show Common All
                    // Get a filename
                    System.out.println("\nPlease enter a filename:");
                    String outName = scanner.nextLine();

                    // Print the common course matrix to the file, printing an error message in case of failure
                    if (!selector.showCommonAll(outName)) {
                        System.out.println("Something went wrong");
                    }
                    break;

                case 0: // Exit
                    break;

                default: // Error state
                    System.out.println("\nError. Incorrect use. Maybe you need a refresher. Or a brain transplant");
            }

        } while (selection != 0);

        System.out.println("\nLater.");
    }
}