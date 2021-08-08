import java.io.BufferedReader;

/**
 * {@code MathdokuInterface} provides an underlying structure for the {@code Mathdoku} class.
 *
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @see Mathdoku
 * @since 2021-03-07
 */
public interface MathdokuInterface {
    /**
     * Reads this puzzle from a given stream of data. The input stream is formatted in three sections.
     * Section 1: Integer n which gives the size of the puzzle. (n * n grid).
     * [First line]
     * <p>
     * Section 2: Puzzle square itself. n lines each with n characters (n * n total characters).
     * Each cell is a character that represents the cell grouping to which the cell belongs.
     * [Next n lines]
     * <p>
     * Section 3: Constraints for each cell grouping in the puzzle.
     * The lines will have 3 values separated with spaces.
     * First value is the character representing the grouping.
     * Second value is the operation outcome for the grouping.
     * Third value is the operator for the grouping.
     * [Remaining lines]
     *
     * @param stream stream of data.
     * @return {true} if puzzle is read successfully otherwise {@code false}.
     */
    boolean loadPuzzle(BufferedReader stream);

    /**
     * Determines whether the loaded puzzle is valid or not.
     * Performs the additional input validation to check whether the input loaded
     * by the {@code lodePuzzle()} method specifies a valid mathdoku puzzle.
     *
     * @return {@code true} if the puzzle is valid and can be solved otherwise {@code false}.
     */
    boolean validate();

    /**
     * Finds the solution of this puzzle.
     * <p>
     * Store the solution in an object so that it can be retrieved later.
     * <p>
     * Keeps track of the number of guesses program make in the process of solving the puzzle.
     * Store this in an object to be retrieved later by the {@code choices()} method.
     *
     * @return {@code true} if solution is found otherwise {@code false}.
     */
    boolean solve();

    /**
     * Prints the current puzzle state.
     *
     * @return current puzzle state as {@code String} object.
     */
    String print();

    /**
     * Keeps track of the number of guesses the program had to make and later backtrack while solving this puzzle.
     *
     * @return number of guesses.
     */
    int choices();
}