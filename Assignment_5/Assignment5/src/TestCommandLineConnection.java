public class TestCommandLineConnection {
    public static void main(String[] args) {
        try {
            // Start date for the period to summarize.
            String startDate = args[0];

            // End date for the period to summarize.
            String endDate = args[1];

            // Name of the output file.
            String outputFile = args[2];

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