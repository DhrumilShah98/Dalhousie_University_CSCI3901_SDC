import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Mathdoku tests")
public class Testing {
    /* Test cases for Mathdoku */

    @Nested
    @DisplayName("Validation tests")
    class Validation {
        @Test
        @DisplayName("Bad input stream")
        public void badInput() {
            String test = "";
            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertFalse(solver.loadPuzzle(reader));
        }

        @Test
        @DisplayName("Non-square")
        public void notSquare() {
            String test = "3\n" +
                    "aabb\n" +
                    "abb\n" +
                    "ccc\n" +
                    "a 2 *\n" +
                    "b 3 +\n" +
                    "c 8 *";
            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertFalse(solver.loadPuzzle(reader) && solver.validate());
        }

        @Test
        @DisplayName("Missing group")
        public void missingGroup() {
            String test = "3\n" +
                    "aabb\n" +
                    "abb\n" +
                    "ccc\n" +
                    "a 2 *\n" +
                    "b 3 +";
            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertFalse(solver.loadPuzzle(reader) && solver.validate());
        }

        @Test
        @DisplayName("Disconnected 1")
        public void disconnected1() {
            String test = "3\n" +
                    "aab\n" +
                    "abb\n" +
                    "cca\n" +
                    "a 2 *\n" +
                    "b 3 +\n" +
                    "c 8 *";
            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertFalse(solver.loadPuzzle(reader) && solver.validate());
        }

        @Test
        @DisplayName("Disconnected 2")
        public void disconnected2() {
            String test = "3\n" +
                    "aab\n" +
                    "bbb\n" +
                    "caa\n" +
                    "a 2 *\n" +
                    "b 3 +\n" +
                    "c 8 *";
            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertFalse(solver.loadPuzzle(reader) && solver.validate());
        }

        @Test
        @DisplayName("Valid 1")
        public void validEq() {
            String test = "3\n" +
                    "aab\n" +
                    "abb\n" +
                    "acc\n" +
                    "a 2 *\n" +
                    "b 3 +\n" +
                    "c 8 =";
            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertFalse(solver.loadPuzzle(reader) && solver.validate());
        }

        @Test
        @DisplayName("Valid 2")
        public void validMinus() {
            String test = "3\n" +
                    "aab\n" +
                    "abb\n" +
                    "acc\n" +
                    "a 2 *\n" +
                    "b 3 -\n" +
                    "c 8 *";
            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertFalse(solver.loadPuzzle(reader) && solver.validate());
        }

        @Test
        @DisplayName("Valid 3")
        public void validPlus() {
            String test = "3\n" +
                    "aab\n" +
                    "abb\n" +
                    "aac\n" +
                    "a 2 *\n" +
                    "b 3 +\n" +
                    "c 8 +";
            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertFalse(solver.loadPuzzle(reader) && solver.validate());
        }
    }

    @Nested
    @DisplayName("Solving tests")
    class Solution {

        @Test
        @DisplayName("Trivial")
        public void trivial() {
            String test = "3\n" +
                    "abc\n" +
                    "def\n" +
                    "ghi\n" +
                    "a 1 =\n" +
                    "b 2 =\n" +
                    "c 3 =\n" +
                    "d 2 =\n" +
                    "e 3 =\n" +
                    "f 1 =\n" +
                    "g 3 =\n" +
                    "h 1 =\n" +
                    "i 2 =\n";
            String solution = "123\n231\n312";

            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertTrue(solver.loadPuzzle(reader));
            assertTrue(solver.validate());
            assertTrue(solver.solve());
            assertEquals(solution, solver.print().trim());
        }

        @Test
        @DisplayName("Plus")
        public void plus() {
            String test = "3\n" +
                    "abc\n" +
                    "def\n" +
                    "ghh\n" +
                    "a 1 =\n" +
                    "b 2 =\n" +
                    "c 3 =\n" +
                    "d 2 =\n" +
                    "e 3 =\n" +
                    "f 1 =\n" +
                    "g 3 =\n" +
                    "h 3 +\n";
            String solution = "123\n231\n312";

            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertTrue(solver.loadPuzzle(reader));
            assertTrue(solver.validate());
            assertTrue(solver.solve());
            assertEquals(solution, solver.print().trim());
        }

        @Test
        @DisplayName("Times")
        public void times() {
            String test = "3\n" +
                    "abc\n" +
                    "def\n" +
                    "gff\n" +
                    "a 1 =\n" +
                    "b 2 =\n" +
                    "c 3 =\n" +
                    "d 2 =\n" +
                    "e 3 =\n" +
                    "f 2 *\n" +
                    "g 3 =\n";
            String solution = "123\n231\n312";

            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertTrue(solver.loadPuzzle(reader));
            assertTrue(solver.validate());
            assertTrue(solver.solve());
            assertEquals(solution, solver.print().trim());
        }

        @Test
        @DisplayName("Minus")
        public void minus() {
            String test = "3\n" +
                    "abc\n" +
                    "def\n" +
                    "ghh\n" +
                    "a 1 =\n" +
                    "b 2 =\n" +
                    "c 3 =\n" +
                    "d 2 =\n" +
                    "e 3 =\n" +
                    "f 1 =\n" +
                    "g 3 =\n" +
                    "h 1 -\n";
            String solution = "123\n231\n312";

            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertTrue(solver.loadPuzzle(reader));
            assertTrue(solver.validate());
            assertTrue(solver.solve());
            assertEquals(solution, solver.print().trim());
        }

        @Test
        @DisplayName("Divide")
        public void divide() {
            String test = "3\n" +
                    "abc\n" +
                    "def\n" +
                    "ghh\n" +
                    "a 1 =\n" +
                    "b 2 =\n" +
                    "c 3 =\n" +
                    "d 2 =\n" +
                    "e 3 =\n" +
                    "f 1 =\n" +
                    "g 3 =\n" +
                    "h 2 /\n";
            String solution = "123\n231\n312";

            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertTrue(solver.loadPuzzle(reader));
            assertTrue(solver.validate());
            assertTrue(solver.solve());
            assertEquals(solution, solver.print().trim());
        }

        @Test
        @DisplayName("No solution 1")
        public void unsolvable1() {
            String test = "3\n" +
                    "aaa\n" +
                    "def\n" +
                    "ghi\n" +
                    "a 4 *\n" +
                    "d 2 =\n" +
                    "e 3 =\n" +
                    "f 1 =\n" +
                    "g 3 =\n" +
                    "h 1 =\n" +
                    "i 2 =\n";

            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertTrue(solver.loadPuzzle(reader));
            assertTrue(solver.validate());
            assertFalse(solver.solve());
        }

        @Test
        @DisplayName("No solution 2")
        public void unsolvable2() {
            String test = "3\n" +
                    "abc\n" +
                    "def\n" +
                    "gff\n" +
                    "a 1 =\n" +
                    "b 2 =\n" +
                    "c 3 =\n" +
                    "d 2 =\n" +
                    "e 3 =\n" +
                    "f 6 *\n" +
                    "g 3 =\n";

            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertTrue(solver.loadPuzzle(reader));
            assertTrue(solver.validate());
            assertFalse(solver.solve());
        }

        @Test
        @DisplayName("PDF problem")
        public void pdf() {
            String test = "5\n" +
                    "abccd\n" +
                    "abcef\n" +
                    "ghhei\n" +
                    "jhkll\n" +
                    "jjkmm\n" +
                    "a 4 +\n" +
                    "b 2 /\n" +
                    "c 75 *\n" +
                    "d 2 =\n" +
                    "e 2 *\n" +
                    "f 4 =\n" +
                    "g 5 =\n" +
                    "h 60 *\n" +
                    "i 1 =\n" +
                    "j 8 *\n" +
                    "k 1 -\n" +
                    "l 1 -\n" +
                    "m 8 +\n";
            String solution = "14352\n32514\n53421\n25143\n41235";

            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertTrue(solver.loadPuzzle(reader));
            assertTrue(solver.validate());
            assertTrue(solver.solve());
            assertEquals(solution, solver.print().trim());
        }

        @Test
        @DisplayName("Easy problem")
        public void easy() {
            String test = "4\n" +
                    "aaab\n" +
                    "ccdb\n" +
                    "eedb\n" +
                    "efff\n" +
                    "a 8 +\n" +
                    "b 9 +\n" +
                    "c 4 +\n" +
                    "d 1 -\n" +
                    "e 24 *\n" +
                    "f 7 +\n";
            String solution = "4132\n1324\n2413\n3241";

            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertTrue(solver.loadPuzzle(reader));
            assertTrue(solver.validate());
            assertTrue(solver.solve());
            assertEquals(solution, solver.print().trim());
        }

        @Test
        @DisplayName("Hard problem")
        public void hard() {
            String test = "4\n" +
                    "aabb\n" +
                    "aacc\n" +
                    "ddee\n" +
                    "dffe\n" +
                    "a 32 *\n" +
                    "b 5 +\n" +
                    "c 4 +\n" +
                    "d 6 *\n" +
                    "e 9 +\n" +
                    "f 5 +\n";
            String solution = "4132\n2413\n3241\n1324";

            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertTrue(solver.loadPuzzle(reader));
            assertTrue(solver.validate());
            assertTrue(solver.solve());
            assertEquals(solution, solver.print().trim());
        }
    }

    @Nested
    @DisplayName("Efficiency tests")
    class Efficiency {

        @Test
        @DisplayName("Restricting on equals")
        public void restriction1() {
            String test = "3\n" +
                    "abc\n" +
                    "def\n" +
                    "ghi\n" +
                    "a 1 =\n" +
                    "b 2 =\n" +
                    "c 3 =\n" +
                    "d 2 =\n" +
                    "e 3 =\n" +
                    "f 1 =\n" +
                    "g 3 =\n" +
                    "h 1 =\n" +
                    "i 2 =\n";

            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertTrue(solver.loadPuzzle(reader));
            assertTrue(solver.validate());
            assertTrue(solver.solve());

            assertTrue(solver.choices() <= 9);
        }

        @Test
        @DisplayName("Restricting on rows")
        public void restriction2() {
            String test = "3\n" +
                    "abc\n" +
                    "def\n" +
                    "ggg\n" +
                    "a 1 =\n" +
                    "b 2 =\n" +
                    "c 3 =\n" +
                    "d 2 =\n" +
                    "e 3 =\n" +
                    "f 1 =\n" +
                    "g 6 +\n";

            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertTrue(solver.loadPuzzle(reader));
            assertTrue(solver.validate());
            assertTrue(solver.solve());

            assertTrue(solver.choices() <= 9);
        }

        @Test
        @DisplayName("Fewer choices as we go")
        public void restriction3() {
            String test = "3\n" +
                    "aaa\n" +
                    "aaa\n" +
                    "aaa\n" +
                    "a 18 +\n";

            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertTrue(solver.loadPuzzle(reader));
            assertTrue(solver.validate());
            assertTrue(solver.solve());

            assertTrue(solver.choices() <= 10);
        }

        @Test
        @DisplayName("Solvable")
        public void restriction4() {
            String test = "3\n" +
                    "aab\n" +
                    "aac\n" +
                    "def\n" +
                    "a 12 *\n" +
                    "b 3 =\n" +
                    "c 1 =\n" +
                    "d 1 =\n" +
                    "e 3 =\n" +
                    "f 2 =\n";

            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertTrue(solver.loadPuzzle(reader));
            assertTrue(solver.validate());
            assertTrue(solver.solve());

            assertTrue(solver.choices() <= 9);
        }

        @Test
        @DisplayName("Almost solvable")
        public void restriction5() {
            String test = "3\n" +
                    "aab\n" +
                    "aac\n" +
                    "def\n" +
                    "a 12 *\n" +
                    "b 1 =\n" +
                    "c 3 =\n" +
                    "d 1 =\n" +
                    "e 3 =\n" +
                    "f 2 =\n";

            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertTrue(solver.loadPuzzle(reader));
            assertTrue(solver.validate());
            assertTrue(solver.solve());

            assertTrue(solver.choices() <= 10);
        }

        @Test
        @DisplayName("Restricting on a group")
        public void restriction6() {
            String test = "3\n" +
                    "aab\n" +
                    "abb\n" +
                    "ccc\n" +
                    "a 6 +\n" +
                    "b 6 +\n" +
                    "c 6 +\n";

            Reader inputString = new StringReader(test);
            BufferedReader reader = new BufferedReader(inputString);
            Mathdoku solver = new Mathdoku();

            assertTrue(solver.loadPuzzle(reader));
            assertTrue(solver.validate());
            assertTrue(solver.solve());

            assertTrue(solver.choices() <= 9);
        }
    }
}
