import java.util.Scanner;

public class TestUserConnection {
    public static void main(String[] args) {
        try {
            // Scanner class to take user input
            Scanner scanner = new Scanner(System.in);

            // Start date for the period to summarize.
            System.out.println("Enter starting date for the period to summarize: (YYYY-MM-DD)");
            String startDate = scanner.nextLine();

            // End date for the period to summarize.
            System.out.println("Enter ending date for the period to summarize: (YYYY-MM-DD)");
            String endDate = scanner.nextLine();

            // Name of the output file.
            System.out.println("Enter name of the output file: (.xml file)");
            String outputFile = scanner.nextLine();

            // Summary Document object
            SummaryDocument summaryDocument = new SummaryDocument();

            System.out.println("Please wait. Report is being generated.");
            if (summaryDocument.reportData(startDate, endDate, outputFile)) {
                System.out.println("Report ready. Please check " + outputFile);
            } else {
                System.out.println("Error occurred!");
            }
        } catch (Exception e) {
            System.out.println("Report generation failed: " + e.getMessage());
        }
    }
}