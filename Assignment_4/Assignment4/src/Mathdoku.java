import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;

/**
 * {@code Mathdoku} implements the {@code MathdokuInterface}.
 * It accepts a puzzle and ultimately solves it.
 *
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @see MathdokuInterface
 * @since 2021-03-07
 */
public class Mathdoku implements MathdokuInterface {
    // Size of this puzzle.
    private int size = 0;

    // Number of guesses this program had to make and later backtrack while solving the puzzle.
    private int choices = 0;

    // Total groups in this puzzle.
    private ArrayList<Character> groups = null;

    // Matrix where each cell is a character that represents the cell grouping to which the cell belongs.
    private char[][] charPlacementMatrix = null;

    // Final solution matrix. Solved puzzle.
    private int[][] solution = null;

    // Map with key as the character and value as {@code BoardGrouping} class.
    private HashMap<Character, BoardGrouping> charBoardGroupingMap = null;

    /**
     * Resets this puzzle
     */
    private void resetPuzzle() {
        this.size = 0;
        this.choices = 0;
        this.groups = null;
        this.charPlacementMatrix = null;
        this.solution = null;
        this.charBoardGroupingMap = null;
    }

    /**
     * Reinitialize the solution
     */
    private void reInitSolution() {
        solution = new int[size][size];
        choices = 0;
    }

    /**
     * Initialize this puzzle
     *
     * @param puzzleSize size of this puzzle
     */
    private void init(int puzzleSize) {
        // Initialize the size of this puzzle.
        size = puzzleSize;

        // Create this puzzle with size * size cells.
        charPlacementMatrix = new char[size][size];
        solution = new int[size][size];

        // Create a new charBoardGroupingMap map.
        charBoardGroupingMap = new HashMap<>();

        // Fill the solution matrix with 0s.
        for (int row = 0; row < size; ++row) {
            Arrays.fill(solution[row], 0);
        }

        // Initialize list of all groups.
        groups = new ArrayList<>();
    }

    /**
     * Closes the stream of data
     *
     * @param stream stream data
     */
    private void closeStream(BufferedReader stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
        }
    }

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
    @Override
    public boolean loadPuzzle(BufferedReader stream) {
        // Return false if stream is null.
        if (stream == null) {
            return false;
        }

        // Section 1.
        // First line is the size of this puzzle. Read it.
        int puzzleSize;
        try {
            puzzleSize = Integer.parseInt(stream.readLine());
        } catch (Exception e) {
            // Return false if not able to read the size of this puzzle.
            closeStream(stream);
            return false;
        }

        // Return false if size is less than 2. Puzzle has to be at least of size 2 * 2 (i.e., 4 cells).
        if (puzzleSize < 2) {
            closeStream(stream);
            return false;
        }

        // Initialize this puzzle.
        init(puzzleSize);

        // Section 2.
        // Fill the charPlacementMatrix matrix with the character representing the grouping.
        String currentLine;
        for (int row = 0; row < puzzleSize; ++row) {
            try {
                currentLine = stream.readLine();
                if (currentLine.length() != puzzleSize) {
                    // Return false if the current line is not of a proper length.
                    resetPuzzle();
                    closeStream(stream);
                    return false;
                }
            } catch (Exception e) {
                // Return false if not able to read the current line characters.
                resetPuzzle();
                closeStream(stream);
                return false;
            }

            // Iterate through all the characters of the current line.
            for (int col = 0; col < puzzleSize; ++col) {
                char currentCellChar = currentLine.charAt(col);

                // Fill the current character in the charPlacementMatrix.
                charPlacementMatrix[row][col] = currentCellChar;

                // Add current character and its location(i.e., row and col) in charBoardGroupingMap.
                if (charBoardGroupingMap.containsKey(currentCellChar)) {
                    charBoardGroupingMap.get(currentCellChar).boardLocations.add(new BoardLocation(row, col));
                } else {
                    ArrayList<BoardLocation> boardLocations = new ArrayList<>();
                    boardLocations.add(new BoardLocation(row, col));
                    BoardGrouping boardGrouping = new BoardGrouping(-1, '\0', boardLocations);
                    charBoardGroupingMap.put(currentCellChar, boardGrouping);
                    groups.add(currentCellChar);
                }
            }
        }

        // Section 3.
        // Iterate through all the constraints for each cell grouping in this puzzle.
        while (true) {
            try {
                if ((currentLine = stream.readLine()) == null) {
                    break;
                }

                final String[] cellConstraints = currentLine.split(" ");

                if (cellConstraints.length != 3) {
                    // Return false if current line is not of a proper length.
                    resetPuzzle();
                    closeStream(stream);
                    return false;
                }

                final char currentChar = cellConstraints[0].charAt(0);
                final int currentCharVal = Integer.parseInt(String.valueOf(cellConstraints[1]));
                final char currentCharOperation = cellConstraints[2].charAt(0);

                BoardGrouping currentCharGrouping = charBoardGroupingMap.get(currentChar);
                if (currentCharGrouping != null && currentCharGrouping.value == -1 && currentCharGrouping.operation == '\0') {
                    currentCharGrouping.value = currentCharVal;
                    currentCharGrouping.operation = currentCharOperation;
                }
            } catch (Exception e) {
                // Return false if not able to read the constraints of the current line.
                resetPuzzle();
                closeStream(stream);
                return false;
            }
        }

        closeStream(stream);
        // Return true as puzzle is loaded successfully.
        return true;
    }

    /**
     * Checks every grouping is a connected set of cells.
     * Ensure that there are no gaps between cells in a grouping.
     *
     * @return {@code true} if grouping is valid otherwise {@code false}.
     */
    private boolean validateGrouping() {
        // Iterate through all the groups.
        for (Character curGroupChar : groups) {
            final BoardGrouping curBoardGrouping = charBoardGroupingMap.get(curGroupChar);
            final BoardLocation curCharFirstBoardLocation = curBoardGrouping.boardLocations.get(0);

            // visited matrix to keep track of neighbours visited.
            final boolean[][] visited = new boolean[size][size];
            for (int row = 0; row < size; ++row) {
                Arrays.fill(visited[row], false);
            }

            // Queue of same neighbours.
            final Queue<BoardLocation> sameNeighbours = new LinkedList<>();

            // Add current group in sameNeighbours and mark it visited and set totalEncounters to 1.
            sameNeighbours.add(new BoardLocation(curCharFirstBoardLocation.rowIndex, curCharFirstBoardLocation.colIndex));
            visited[curCharFirstBoardLocation.rowIndex][curCharFirstBoardLocation.colIndex] = true;
            int totalEncounters = 1;

            // Iterate until sameNeighbours is not empty.
            while (!sameNeighbours.isEmpty()) {
                BoardLocation boardLocation = sameNeighbours.peek();

                final int row = boardLocation.rowIndex;
                final int col = boardLocation.colIndex;

                sameNeighbours.poll();

                // Add same neighbors (top, right, bottom and left) of the current group in the list.
                if ((row - 1) >= 0 && charPlacementMatrix[row - 1][col] == curGroupChar && !visited[row - 1][col]) {
                    sameNeighbours.add(new BoardLocation(row - 1, col));
                    visited[row - 1][col] = true;
                    totalEncounters++;
                }

                if ((row + 1) < size && charPlacementMatrix[row + 1][col] == curGroupChar && !visited[row + 1][col]) {
                    sameNeighbours.add(new BoardLocation(row + 1, col));
                    visited[row + 1][col] = true;
                    totalEncounters++;
                }


                if ((col - 1) >= 0 && charPlacementMatrix[row][col - 1] == curGroupChar && !visited[row][col - 1]) {
                    sameNeighbours.add(new BoardLocation(row, col - 1));
                    visited[row][col - 1] = true;
                    totalEncounters++;
                }

                if ((col + 1) < size && charPlacementMatrix[row][col + 1] == curGroupChar && !visited[row][col + 1]) {
                    sameNeighbours.add(new BoardLocation(row, col + 1));
                    visited[row][col + 1] = true;
                    totalEncounters++;
                }
            }

            // Return false if total encountered neighbours is not equal to total entries in the group.
            if (totalEncounters != curBoardGrouping.boardLocations.size()) {
                return false;
            }
        }

        // Return true as the grouping is correct.
        return true;
    }

    /**
     * Determines whether the loaded puzzle is valid or not.
     * Performs the additional input validation to check whether the input loaded
     * by the {@code lodePuzzle()} method specifies a valid mathdoku puzzle.
     *
     * @return {@code true} if the puzzle is valid and can be solved otherwise {@code false}.
     */
    @Override
    public boolean validate() {
        // Return false is size is less than 2. Puzzle has to be at least of size 2 * 2 (i.e., 4 cells).
        if (size < 2) {
            return false;
        }

        // Return false if puzzle is not of size * size.
        if (charPlacementMatrix.length == size) {
            for (int row = 0; row < size; ++row) {
                if (charPlacementMatrix[row].length != size) {
                    return false;
                }
            }
        } else {
            return false;
        }

        // Iterate through all the groups.
        for (Character currentGroupCharacter : groups) {
            BoardGrouping currentBoardGrouping = charBoardGroupingMap.get(currentGroupCharacter);

            // Return false if no grouping exists for the current group character.
            if (currentBoardGrouping == null) {
                return false;
            }

            // Return false if no constraints exists for the current group character.
            if (currentBoardGrouping.value <= 0 || currentBoardGrouping.operation == '\0') {
                return false;
            }

            switch (currentBoardGrouping.operation) {
                case Operators.EQUALS -> {
                    // Return false if '=' operation has not exactly one cell.
                    if (currentBoardGrouping.boardLocations.size() != 1) {
                        return false;
                    }
                }
                case Operators.ADDITION, Operators.MULTIPLICATION -> {
                    // Return false if '+' or '*' has not at least two cells.
                    if (currentBoardGrouping.boardLocations.size() < 2) {
                        return false;
                    }
                }
                case Operators.SUBTRACTION, Operators.DIVISION -> {
                    // Return false if '-' or '/' has not exactly two cells.
                    if (currentBoardGrouping.boardLocations.size() != 2) {
                        return false;
                    }
                }
                default -> {
                    // Return false if operation is not valid.
                    return false;
                }
            }
        }


        // Validate the grouping in the puzzle.
        // Return false if the grouping is not valid otherwise true.
        return validateGrouping();
    }

    /**
     * Checks whether the {@code number} is unique in current {@code row} and {@code col} of this puzzle.
     *
     * @param number current number to be checked.
     * @param row    row index.
     * @param col    col index.
     * @return {@code true} if number is unique in the current row and col otherwise {@code false}.
     */
    private boolean isNumberUniqueInRowCol(int number, int row, int col) {
        // Iterate the current row.
        for (int i = 0; i < size; ++i) {
            if (solution[row][i] == number) {
                // Return false as the number is not unique in current row.
                return false;
            }
        }

        // Iterate the current col.
        for (int i = 0; i < size; ++i) {
            if (solution[i][col] == number) {
                // Return false as the number is not unique in current col.
                return false;
            }
        }

        // Return true as the number is unique in the current row and col.
        return true;
    }

    /**
     * Finds the solution of this puzzle.
     *
     * @param row row index
     * @param col col index
     * @return {@code true} if solution is found otherwise {@code false}.
     */
    private boolean solve(int row, int col) {
        // Return true if last cell is reached to avoid further backtracking.
        if (row == (size - 1) && col == size) {
            return true;
        }

        // Move to the next row if last col is reached in the current row.
        if (col == size) {
            row++;
            col = 0;
        }

        final char currentCharInCell = charPlacementMatrix[row][col];
        final BoardGrouping currentCharGrouping = charBoardGroupingMap.get(currentCharInCell);

        // If current group character is '=', continue to the next col.
        if (currentCharGrouping.operation == Operators.EQUALS) {
            return solve(row, col + 1);
        }

        // Check how many cells are left to be filled for the current group.
        int remainingCells = 0;
        for (int i = 0; i < currentCharGrouping.boardLocations.size(); ++i) {
            if (solution
                    [currentCharGrouping.boardLocations.get(i).rowIndex]
                    [currentCharGrouping.boardLocations.get(i).colIndex] == 0) {
                remainingCells++;
            }
        }

        if (remainingCells > 1) {
            // More than one cell is remaining to be filled in the current group.
            for (int num = 1; num <= size; ++num) {
                // Check if it safe to place the num is the current cell.
                if (isNumberUniqueInRowCol(num, row, col)) {
                    // Place the num in the current cell assuming the assigned num in the current position is correct.
                    solution[row][col] = num;

                    // Increment the choices counter.
                    choices++;

                    // Solve the next col.
                    if (solve(row, col + 1)) {
                        return true;
                    }
                }

                // If solution is not found, remove the assignment.
                solution[row][col] = 0;
            }

            // Return false to backtrack to the previous col.
            return false;
        } else {
            // Only one cell is left to be filled in the current group.
            switch (currentCharGrouping.operation) {
                // Add the last remaining value in the cell if possible and move to the next col
                // otherwise backtrack to the previous col.
                case Operators.ADDITION -> {
                    int currentGroupTotal = 0;
                    for (int i = 0; i < currentCharGrouping.boardLocations.size(); ++i) {
                        currentGroupTotal += solution
                                [currentCharGrouping.boardLocations.get(i).rowIndex]
                                [currentCharGrouping.boardLocations.get(i).colIndex];
                    }
                    int remainingVal = currentCharGrouping.value - currentGroupTotal;

                    if (remainingVal > 0 && remainingVal <= size) {
                        if (isNumberUniqueInRowCol(remainingVal, row, col)) {
                            solution[row][col] = remainingVal;
                            choices++;

                            if (solve(row, col + 1)) {
                                return true;
                            }
                        }
                    }

                    solution[row][col] = 0;
                    return false;
                }
                case Operators.SUBTRACTION -> {
                    int ans1 = solution[currentCharGrouping.boardLocations.get(0).rowIndex][currentCharGrouping.boardLocations.get(0).colIndex];
                    for (int ans2 = 1; ans2 <= size; ++ans2) {
                        if ((ans1 != ans2) &&
                                (Math.abs((ans2 - ans1)) == currentCharGrouping.value) &&
                                isNumberUniqueInRowCol(ans2, row, col)) {
                            solution[row][col] = ans2;
                            choices++;

                            if (solve(row, col + 1)) {
                                return true;
                            }
                        }
                        solution[row][col] = 0;
                    }
                    return false;
                }
                case Operators.MULTIPLICATION -> {
                    int currentGroupTotal = 1;
                    for (int i = 0; i < currentCharGrouping.boardLocations.size(); ++i) {
                        if (solution
                                [currentCharGrouping.boardLocations.get(i).rowIndex]
                                [currentCharGrouping.boardLocations.get(i).colIndex] != 0) {
                            currentGroupTotal *= solution
                                    [currentCharGrouping.boardLocations.get(i).rowIndex]
                                    [currentCharGrouping.boardLocations.get(i).colIndex];
                        }
                    }
                    int remainingVal = currentCharGrouping.value / currentGroupTotal;
                    if (remainingVal <= size && (remainingVal * currentGroupTotal == currentCharGrouping.value)) {
                        if (isNumberUniqueInRowCol(remainingVal, row, col)) {
                            solution[row][col] = remainingVal;
                            choices++;

                            if (solve(row, col + 1)) {
                                return true;
                            }
                        }
                    }

                    solution[row][col] = 0;
                    return false;
                }
                case Operators.DIVISION -> {
                    int ans1 = solution[currentCharGrouping.boardLocations.get(0).rowIndex][currentCharGrouping.boardLocations.get(0).colIndex];
                    for (int ans2 = 1; ans2 <= size; ++ans2) {
                        if (ans1 != ans2) {
                            if (ans1 > ans2) {
                                if (((ans1 / ans2) == currentCharGrouping.value) && ((ans2 * currentCharGrouping.value) == ans1) && isNumberUniqueInRowCol(ans2, row, col)) {
                                    solution[row][col] = ans2;
                                    choices++;

                                    if (solve(row, col + 1)) {
                                        return true;
                                    }
                                }
                            } else {
                                if (((ans2 / ans1) == currentCharGrouping.value) && ((ans1 * currentCharGrouping.value) == ans2) && isNumberUniqueInRowCol(ans2, row, col)) {
                                    solution[row][col] = ans2;
                                    choices++;

                                    if (solve(row, col + 1)) {
                                        return true;
                                    }
                                }
                            }
                            solution[row][col] = 0;
                        }
                    }
                    return false;
                }
                default -> {
                    return false;
                }
            }
        }
    }

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
    @Override
    public boolean solve() {

        // Reinitialize the solution.
        reInitSolution();

        // Return false if this puzzle is not valid.
        if (!validate()) {
            return false;
        }

        // If the grouping contains only one cell (i.e., '='), simply put the correct value in the cell.
        for (HashMap.Entry<Character, BoardGrouping> currentEntry : charBoardGroupingMap.entrySet()) {
            if (currentEntry.getValue().operation == Operators.EQUALS) {
                solution[currentEntry.getValue().boardLocations.get(0).rowIndex][currentEntry.getValue().boardLocations.get(0).colIndex]
                        = currentEntry.getValue().value;
            }
        }

        // Solve this puzzle.
        return solve(0, 0);
    }

    /**
     * Prints the current puzzle state.
     *
     * @return current puzzle state as {@code String} object.
     */
    @Override
    public String print() {
        // Return null if this puzzle is not ready.
        if (size <= 1 || charPlacementMatrix == null || solution == null) return null;

        // Print the current state of this puzzle.
        // Output each row listed from top-to-bottom with rows separated by \n.
        // No space between cols of the rows.
        // If cell has the value assigned from 1 to size, print that value for the cell otherwise the grouping character for the cell.
        StringBuilder outputSB = new StringBuilder();
        for (int rowIndex = 0; rowIndex < size; ++rowIndex) {
            for (int colIndex = 0; colIndex < size; ++colIndex) {
                if (solution[rowIndex][colIndex] != 0) {
                    outputSB.append(solution[rowIndex][colIndex]);
                } else {
                    outputSB.append(charPlacementMatrix[rowIndex][colIndex]);
                }
            }
            outputSB.append("\n");
        }

        // Return the current state of this puzzle.
        return outputSB.toString();
    }

    /**
     * Keeps track of the number of guesses the program had to make and later backtrack while solving this puzzle.
     *
     * @return number of guesses.
     */
    @Override
    public int choices() {
        return this.choices;
    }

    /**
     * {@code BoardGrouping} class used by {@code Mathdoku} class.
     * Hold the value, operation and all the board locations.
     *
     * @author Dhrumil Amish Shah
     * @version 1.0.0
     * @see Mathdoku
     * @since 2021-03-07
     */
    private static class BoardGrouping {
        // Value outcome for the grouping.
        private int value;

        // Operation of the grouping.
        private char operation;

        // All board locations of the character.
        private final ArrayList<BoardLocation> boardLocations;

        /**
         * Class constructor {@code BoardGrouping}.
         *
         * @param value          grouping value.
         * @param operation      grouping operation.
         * @param boardLocations all board locations of the character.
         */
        public BoardGrouping(int value, char operation, ArrayList<BoardLocation> boardLocations) {
            this.value = value;
            this.operation = operation;
            this.boardLocations = boardLocations;
        }
    }

    /**
     * {@code BoardLocation} class used by {@code BoardGrouping} class.
     * Location of each character on the board. (Character row and col index).
     *
     * @author Dhrumil Amish Shah
     * @version 1.0.0
     * @see Mathdoku
     * @since 2021-03-07
     */
    private static class BoardLocation {
        // Character row index.
        private final int rowIndex;

        // Character col index.
        private final int colIndex;

        /**
         * Class constructor {@code BoardLocation}.
         *
         * @param rowIndex character row index.
         * @param colIndex character col index.
         */
        public BoardLocation(int rowIndex, int colIndex) {
            this.rowIndex = rowIndex;
            this.colIndex = colIndex;
        }
    }

    /**
     * {@code Operators} class with all the allowed operators
     *
     * @author Dhrumil Amish Shah
     * @version 1.0.0
     * @see Mathdoku
     * @since 2021-03-07
     */
    private static class Operators {
        private static final char ADDITION = '+';
        private static final char SUBTRACTION = '-';
        private static final char MULTIPLICATION = '*';
        private static final char DIVISION = '/';
        private static final char EQUALS = '=';
    }
}