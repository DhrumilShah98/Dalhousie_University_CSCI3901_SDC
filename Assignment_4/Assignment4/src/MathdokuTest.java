import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class MathdokuTest {
    public static void main(String[] args) {
        // Path to the input file
        File file1 = new File("D:\\DALHOUSIE TOTAL\\Study_Material\\Term_1\\CSCI_3901_SDC\\Assignments\\Assignment 4\\Assignment4\\src", "input.txt");

        try {
            BufferedReader br = new BufferedReader(new FileReader(file1));
            Mathdoku mathdoku = new Mathdoku();
            System.out.println("Load: " + mathdoku.loadPuzzle(br));
            System.out.println("Validate: " + mathdoku.validate());
            System.out.println("Solve: " + mathdoku.solve());
            System.out.println("Choices: " + mathdoku.choices());
            System.out.println("Print:\n" + mathdoku.print());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}