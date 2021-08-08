import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test suite")
public class SearchTreeUnitTestsFull {

    public static final String LONG_STRING = "Thelongstringinstrumentisamusicalinstrumentin" +
            "whichthestringisofsuchalengththatthefundamentaltransversewaveisbelow" +
            "whatapersoncanhearasatone";

    @Nested
    @DisplayName("Ability to search the tree")
    class SearchTests {
        @Nested
        @DisplayName("Input Validation")
        class Input {
            @Test
            @DisplayName("Find null string in tree")
            void nullStringFindTest() {
                SearchTree bst = new SearchTree();
                assertEquals(0, bst.find(null));
            }
        }

        @Nested
        @DisplayName("Boundary cases")
        class Boundaries {
            @Test
            @DisplayName("Find empty string in tree")
            void emptyStringFindTest() {
                SearchTree bst = new SearchTree();
                assertEquals(0, bst.find(""));
            }

            @Test
            @DisplayName("Find a string of 1 character")
            void singleCharacterFindTest() {
                SearchTree bst = new SearchTree();
                bst.add("a");
                assertEquals(1, bst.find("a"));
            }

            @Test
            @DisplayName("Find a long string")
            void longStringFindTest() {
                SearchTree bst = new SearchTree();
                bst.add(LONG_STRING);
                assertEquals(1, bst.find(LONG_STRING));
            }
        }

        @Nested
        @DisplayName("Control flow")
        class CF {
            @Test
            @DisplayName("Find in an empty tree")
            void findInEmptyTreeTest() {
                SearchTree bst = new SearchTree();
                assertTrue(bst.add("Jam"));
                assertTrue(bst.add("Leap"));
                assertFalse(bst.add("Leap"));
            }

            @Test
            @DisplayName("Find in a tree with 1 node")
            void findInTreeWithOneNodeTest() {
                SearchTree bst = new SearchTree();
                assertTrue(bst.add("Jam"));
                assertEquals(1, bst.find("Jam"));
            }

            @Test
            @DisplayName("Find the smallest string in the tree")
            void findSmallestStringInTreeTest() {
                SearchTree bst = new SearchTree();
                bst.add("Jam");
                bst.add("A");
                bst.add("Bat");
                bst.add("Jeep");
                assertEquals(2, bst.find("A"));
            }

            @Test
            @DisplayName("Find the largest string in the tree")
            void findLargestStringInTreeTest() {
                SearchTree bst = new SearchTree();
                bst.add("Jam");
                bst.add("A");
                bst.add("Bat");
                bst.add("Zee");
                assertEquals(2, bst.find("Zee"));
            }

            @Test
            @DisplayName("Find a middle string")
            void findMiddleStringInTreeTest() {
                SearchTree bst = new SearchTree();
                bst.add("Jam");
                bst.add("A");
                bst.add("Bat");
                bst.add("Zee");
                bst.add("Read");
                assertEquals(1, bst.find("Jam"));
            }

            @Test
            @DisplayName("Find Left child then left child")
            void findLeftLeftChildInTreeTest() {
                SearchTree bst = new SearchTree();
                bst.add("Jam");
                bst.add("Cat");
                bst.add("Bat");
                bst.add("Zee");
                bst.add("Read");
                assertEquals(3, bst.find("Bat"));
            }

            @Test
            @DisplayName("Find Left child then right child")
            void findLeftRightChildInTreeTest() {
                SearchTree bst = new SearchTree();
                bst.add("Jam");
                bst.add("Cat");
                bst.add("Bat");
                bst.add("Zee");
                bst.add("Read");
                bst.add("Dad");
                assertEquals(3, bst.find("Dad"));
            }

            @Test
            @DisplayName("Find Right child then left child")
            void findRightLeftChildInTreeTest() {
                SearchTree bst = new SearchTree();
                bst.add("Jam");
                bst.add("Cat");
                bst.add("Bat");
                bst.add("Read");
                bst.add("Zee");
                bst.add("Pear");
                assertEquals(3, bst.find("Pear"));
            }

            @Test
            @DisplayName("Find Right child then right child")
            void findRightRightChildInTreeTest() {
                SearchTree bst = new SearchTree();
                bst.add("Jam");
                bst.add("Cat");
                bst.add("Bat");
                bst.add("Read");
                bst.add("Zee");
                bst.add("Pear");
                assertEquals(3, bst.find("Zee"));
            }

            @Test
            @DisplayName("Find an item that is in the tree")
            void findAnyItemInTreeTest() {
                SearchTree bst = new SearchTree();
                bst.add("Jam");
                bst.add("Cat");
                bst.add("Bat");
                bst.add("Read");
                bst.add("Zee");
                bst.add("Pear");
                bst.add("Dog");
                bst.add("Gym");
                assertEquals(4, bst.find("Gym"));
            }

            @Test
            @DisplayName("Find an item that is not in the tree")
            void findItemNotInTreeTest() {
                SearchTree bst = new SearchTree();
                bst.add("Jam");
                bst.add("Cat");
                bst.add("Bat");
                bst.add("Read");
                assertEquals(0, bst.find("Gym"));
            }

            @Test
            @DisplayName("Search after and before add item")
            void findMiscTest1() {
                SearchTree bst = new SearchTree();
                bst.add("Jam");
                bst.add("Cat");
                bst.add("Bat");
                bst.add("Read");
                assertEquals(0, bst.find("Goat"));
                bst.add("Goat");
                assertEquals(3, bst.find("Goat"));
            }
        }
    }

    @Nested
    @DisplayName("Ability to add to the tree")
    class AddTests {
        @Nested
        @DisplayName("Input Validation")
        class Input {
            @Test
            @DisplayName("Add null string to tree")
            void nullStringAddTest() {
                SearchTree bst = new SearchTree();
                assertFalse(bst.add(null));
            }
        }

        @Nested
        @DisplayName("Boundary cases")
        class Boundaries {
            @Test
            @DisplayName("Add a string of 1 character")
            void singleCharacterAddTest() {
                SearchTree bst = new SearchTree();
                assertTrue(bst.add("a"));
            }

            @Test
            @DisplayName("Add a long string")
            void longStringAddTest() {
                SearchTree bst = new SearchTree();
                assertTrue(bst.add(LONG_STRING));
            }
        }

        @Nested
        @DisplayName("Control flow")
        class CF {
            @Test
            @DisplayName("Add to an empty tree")
            void addToEmptyTreeTest() {
                SearchTree bst = new SearchTree();
                assertTrue(bst.add("Jam"));
            }

            @Test
            @DisplayName("Add to a tree with 1 node")
            void addToTreeWithOneNodeTest() {
                SearchTree bst = new SearchTree();
                bst.add("Jam");
                assertTrue(bst.add("Kite"));
            }

            @Test
            @DisplayName("Add strings in alphabetical order")
            void addToTreeInAlphabeticalOrderTest() {
                SearchTree bst = new SearchTree();
                assertTrue(bst.add("Adam"));
                assertTrue(bst.add("Beans"));
                assertTrue(bst.add("Cat"));
                assertTrue(bst.add("Dog"));
                assertTrue(bst.add("Eve"));
                assertTrue(bst.add("Fat"));
                assertTrue(bst.add("God"));
                assertTrue(bst.add("Heal"));
                assertTrue(bst.add("Idiom"));
                assertTrue(bst.add("Kite"));
            }

            @Test
            @DisplayName("Add strings in reverse alphabetical order")
            void addToTreeInReverseAlphabeticalOrderTest() {
                SearchTree bst = new SearchTree();
                assertTrue(bst.add("Kite"));
                assertTrue(bst.add("Idiom"));
                assertTrue(bst.add("Heal"));
                assertTrue(bst.add("God"));
                assertTrue(bst.add("Fat"));
                assertTrue(bst.add("Eve"));
                assertTrue(bst.add("Dog"));
                assertTrue(bst.add("Cat"));
                assertTrue(bst.add("Beans"));
                assertTrue(bst.add("Adam"));
            }

            @Test
            @DisplayName("Add a smallest string")
            void addSmallestStringTest() {
                SearchTree bst = new SearchTree();
                assertTrue(bst.add("a"));
            }

            @Test
            @DisplayName("Add a largest string")
            void addLargestStringTest() {
                String largestString = "ZZZZZ";
                SearchTree bst = new SearchTree();
                bst.add("ZZZ");
                assertTrue(bst.add(largestString));
            }

            @Test
            @DisplayName("Left child then left child")
            void addLeftLeftChildTest() {
                SearchTree bst = new SearchTree();
                assertTrue(bst.add("Jam"));
                assertTrue(bst.add("Bacon"));
                assertTrue(bst.add("Alloy"));
            }

            @Test
            @DisplayName("Left child then right child")
            void addLeftRightChildTest() {
                SearchTree bst = new SearchTree();
                assertTrue(bst.add("Jam"));
                assertTrue(bst.add("Bacon"));
                assertTrue(bst.add("Cat"));
            }

            @Test
            @DisplayName("Right child then left child")
            void addRightLeftChildTest() {
                SearchTree bst = new SearchTree();
                assertTrue(bst.add("Jam"));
                assertTrue(bst.add("Leap"));
                assertTrue(bst.add("Kite"));
            }

            @Test
            @DisplayName("Right child then right child")
            void addRightRightChildTest() {
                SearchTree bst = new SearchTree();
                assertTrue(bst.add("Jam"));
                assertTrue(bst.add("Leap"));
                assertTrue(bst.add("Night"));
            }

            @Test
            @DisplayName("Add an item that is already in the tree")
            void addExistingItemToTreeTest() {
                SearchTree bst = new SearchTree();
                assertTrue(bst.add("Jam"));
                assertTrue(bst.add("Leap"));
                assertFalse(bst.add("Leap"));
            }

        }
    }

    @Nested
    @DisplayName("Ability to rotate the tree")
    class RotateTests {
        @Test
        @DisplayName("Right rotation at depth 2")
        public void rightRotate() {
            SearchTree bst = new SearchTree();

            bst.add("Egg");
            bst.add("Carrot");
            bst.add("Lentil");
            bst.add("Apple");
            bst.add("Date");
            bst.add("Fig");
            bst.add("Yam");

            bst.find("Egg");
            bst.find("Egg");
            bst.find("Carrot");
            bst.find("Lentil");
            bst.find("Fig");

            // Initial tree
            String tree = "Apple 3\nCarrot 2\nDate 3\nEgg 1\nFig 3\nLentil 2\nYam 3\n";
            assertTrue(tree.equals(bst.printTree()));

            // After finding carrot twice
            bst.find("Carrot");
            bst.find("Carrot");
            tree = "Apple 2\nCarrot 1\nDate 3\nEgg 2\nFig 4\nLentil 3\nYam 4\n";
            assertTrue(tree.equals(bst.printTree()));
        }

        @Test
        @DisplayName("Left rotation at depth 2")
        public void leftRotate() {
            SearchTree bst = new SearchTree();

            bst.add("Egg");
            bst.add("Carrot");
            bst.add("Lentil");
            bst.add("Apple");
            bst.add("Date");
            bst.add("Fig");
            bst.add("Yam");

            bst.find("Egg");
            bst.find("Egg");
            bst.find("Carrot");
            bst.find("Lentil");
            bst.find("Fig");

            // Initial tree
            String tree = "Apple 3\nCarrot 2\nDate 3\nEgg 1\nFig 3\nLentil 2\nYam 3\n";
            assertTrue(tree.equals(bst.printTree()));

            // After finding lentil twice
            bst.find("Lentil");
            bst.find("Lentil");
            tree = "Apple 4\nCarrot 3\nDate 4\nEgg 2\nFig 3\nLentil 1\nYam 2\n";
            assertTrue(tree.equals(bst.printTree()));
        }

        @Test
        @DisplayName("Rotate a leaf")
        void rotateLeaf() {
            SearchTree bst = new SearchTree();

            bst.add("Egg");
            bst.add("Carrot");
            bst.add("Lentil");
            bst.add("Apple");
            bst.add("Date");
            bst.add("Fig");
            bst.add("Yam");

            bst.find("Egg");
            bst.find("Egg");
            bst.find("Carrot");
            bst.find("Lentil");
            bst.find("Fig");

            // Initial tree
            String tree = "Apple 3\nCarrot 2\nDate 3\nEgg 1\nFig 3\nLentil 2\nYam 3\n";
            assertTrue(tree.equals(bst.printTree()));

            bst.find("Apple");
            bst.find("Apple");

            tree = "Apple 2\nCarrot 3\nDate 4\nEgg 1\nFig 3\nLentil 2\nYam 3\n";
            assertTrue(tree.equals(bst.printTree()));
        }

        @Test
        @DisplayName("Rotate deep in the tree")
        void rotateDeep() {
            SearchTree bst = new SearchTree();

            bst.add("Egg");
            bst.add("Carrot");
            bst.add("Lentil");
            bst.add("Apple");
            bst.add("Date");
            bst.add("Fig");
            bst.add("Yam");
            bst.add("Ham");

            bst.find("Egg");
            bst.find("Egg");
            bst.find("Carrot");
            bst.find("Lentil");
            bst.find("Fig");
            bst.find("Carrot");
            bst.find("Carrot");

            String tree = "Apple 2\nCarrot 1\nDate 3\nEgg 2\nFig 4\nHam 5\nLentil 3\nYam 4\n";
            assertTrue(tree.equals(bst.printTree()));

            assertEquals(4, bst.find("Fig"));
            tree = "Apple 2\nCarrot 1\nDate 3\nEgg 2\nFig 3\nHam 5\nLentil 4\nYam 5\n";
            assertTrue(tree.equals(bst.printTree()));
        }

        @Test
        @DisplayName("Rotate up one level and stop")
        void rotateOnce() {
            SearchTree bst = new SearchTree();

            bst.add("Egg");
            bst.add("Carrot");
            bst.add("Lentil");
            bst.add("Apple");
            bst.add("Date");
            bst.add("Fig");
            bst.add("Yam");

            bst.find("Egg");
            bst.find("Lentil");
            bst.find("Fig");

            // Initial tree
            String tree = "Apple 3\nCarrot 2\nDate 3\nEgg 1\nFig 3\nLentil 2\nYam 3\n";
            assertTrue(tree.equals(bst.printTree()));

            assertEquals(3, bst.find("Fig"));
            tree = "Apple 3\nCarrot 2\nDate 3\nEgg 1\nFig 2\nLentil 3\nYam 4\n";
            assertTrue(tree.equals(bst.printTree()));

            assertEquals(2, bst.find("Fig"));
            tree = "Apple 4\nCarrot 3\nDate 4\nEgg 2\nFig 1\nLentil 2\nYam 3\n";
            assertTrue(tree.equals(bst.printTree()));
        }

        @Test
        @DisplayName("Rotate all the way up")
        void rotateUp() {
            SearchTree bst = new SearchTree();
            bst.add("Jam");
            bst.add("Cat");
            bst.add("Bat");
            bst.add("Adam");
            bst.add("Account");
            bst.find("Account");
            bst.find("Account");
            bst.find("Account");
            bst.find("Account");
            bst.find("Account");
            assertEquals(1, bst.find("Account"));
        }
    }

    @Nested
    @DisplayName("Ability to reset the tree")
    class ResetTests {
        @Nested
        @DisplayName("Input Validation")
        class Input {

        }

        @Nested
        @DisplayName("Boundary cases")
        class Boundaries {
            @Test
            @DisplayName("Reset an empty tree")
            void resetEmptyTreeTest() {
                SearchTree bst = new SearchTree();
                bst.reset();
                assertEquals(0, bst.find("a"));
            }

            @Test
            @DisplayName("Reset a tree with 1 node")
            void resetTreeWithOneNodeTest() {
                SearchTree bst = new SearchTree();
                bst.add("a");
                bst.reset();
                assertEquals(1, bst.find("a"));
            }

            @Test
            @DisplayName("Reset a big tree")
            void resetBigTreeTest() {
                SearchTree bst = new SearchTree();
                bst.add("abacus");
                bst.add("bat");
                bst.add("cat");
                bst.find("abacus");
                bst.find("abacus");
                bst.find("cat");
                bst.reset();
                bst.find("cat");
                assertEquals(1, bst.find("cat"));
            }
        }

        @Nested
        @DisplayName("Control flow")
        class CF {
            @Test
            @DisplayName("Rotate after reset")
            void rotateReset() {
                SearchTree bst = new SearchTree();
                bst.add("Jam");
                bst.add("Cat");
                bst.add("Bat");
                bst.add("Adam");
                bst.add("Account");
                assertEquals(1, bst.find("Jam"));
                assertEquals(1, bst.find("Jam"));
                assertEquals(1, bst.find("Jam"));
                assertEquals(1, bst.find("Jam"));
                assertEquals(1, bst.find("Jam"));
                bst.reset();
                bst.find("Cat");
                assertEquals(1, bst.find("Cat"));
            }

            @Test
            @DisplayName("Call reset twice in a row")
            void findAfterCallingResetTwiceTest() {
                SearchTree bst = new SearchTree();
                bst.add("Jam");
                bst.add("Cat");
                bst.add("Bat");
                bst.add("Read");
                assertEquals(3, bst.find("Bat"));
                assertEquals(3, bst.find("Cat"));
                bst.reset();
                assertEquals(2, bst.find("Bat"));
                bst.reset();
                assertEquals(1, bst.find("Bat"));
            }
        }
    }

    @Nested
    @DisplayName("Ability to print the tree")
    class PrintTests {
        @Nested
        @DisplayName("Input Validation")
        class Input {
            @Test
            @DisplayName("Printing an empty tree")
            void emptyTest() {
                SearchTree bst = new SearchTree();

                String tree = "";
                assertTrue(tree.equals(bst.printTree()));
            }
        }

        @Nested
        @DisplayName("Boundary cases")
        class Boundaries {
            @Test
            @DisplayName("Print an empty tree")
            void emptyTreePrintTest() {
                SearchTree bst = new SearchTree();
                String expected = "";
                String actual = bst.printTree();
                assertEquals(expected.trim(), actual.trim());
            }

            @Test
            @DisplayName("Print a tree with 1 node")
            void printTreeWithOneNodeTest() {
                SearchTree bst = new SearchTree();
                bst.add("Jam");
                String expected = "Jam 1";
                String actual = bst.printTree();
                assertEquals(expected.trim(), actual.trim());
            }

            @Test
            @DisplayName("Print a big tree")
            void printBigTreeTest() {
                SearchTree bst = new SearchTree();
                bst.add("Jam");
                bst.add("Kite");
                bst.add("Week");
                bst.add("Cold");
                bst.add("Heat");
                bst.add("Summer");
                bst.add("Man");
                String expected = "Cold 2\n" +
                        "Heat 3\n" +
                        "Jam 1\n" +
                        "Kite 2\n" +
                        "Man 5\n" +
                        "Summer 4\n" +
                        "Week 3";
                String actual = bst.printTree();
                assertEquals(expected.trim(), actual.trim());
            }
        }

        @Nested
        @DisplayName("Control flow")
        class CF {
            @Test
            @DisplayName("Print tree that is a linked list")
            void PrintListTreeTest() {
                SearchTree bst = new SearchTree();
                bst.add("Bat");
                bst.add("Cat");
                bst.add("Dog");
                bst.add("Gym");
                bst.add("Jam");
                bst.add("Pear");
                bst.add("Read");
                bst.add("Zee");
                String expected = "Bat 1\n" +
                        "Cat 2\n" +
                        "Dog 3\n" +
                        "Gym 4\n" +
                        "Jam 5\n" +
                        "Pear 6\n" +
                        "Read 7\n" +
                        "Zee 8";
                String actual = bst.printTree();
                assertEquals(expected.trim(), actual.trim());
            }

            @Test
            @DisplayName("Print tree with both left and right children")
            void PrintCompleteTreeTest() {
                SearchTree bst = new SearchTree();
                bst.add("Jam");
                bst.add("Cat");
                bst.add("Bat");
                bst.add("Read");
                bst.add("Zee");
                bst.add("Pear");
                bst.add("Dog");
                bst.add("Gym");
                String expected = "Bat 3\n" +
                        "Cat 2\n" +
                        "Dog 3\n" +
                        "Gym 4\n" +
                        "Jam 1\n" +
                        "Pear 3\n" +
                        "Read 2\n" +
                        "Zee 3";
                String actual = bst.printTree();
                assertEquals(expected.trim(), actual.trim());
            }
        }
    }

}
