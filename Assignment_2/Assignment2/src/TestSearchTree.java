/**
 * Class to test the SearchTree class
 *
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @see SearchTree
 * @see SearchTreeInterface
 * @since 2021-01-31
 */
public class TestSearchTree {
    /**
     * AddTreeTestCases class contains test cases to add in the tree
     */
    private static class AddTreeTestCases {
        public void addNullString() {
            System.out.println("\n<========== ADD NULL STRING ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("bst.add(null): " + bst.add(null));
        }

        public void addEmptyString() {
            System.out.println("\n<========== ADD EMPTY STRING ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("bst.add(\"\"): " + bst.add(""));
        }

        public void addStringWith1Char() {
            System.out.println("\n<========== ADD STRING WITH 1 CHAR ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("bst.add(\"A\"): " + bst.add("A"));
        }

        public void addLongString() {
            System.out.println("\n<========== ADD LONG STRING ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("bst.add(\"I am a long long string\"): " + bst.add("I am a long long string"));
        }

        public void addStringAlphabetically() {
            System.out.println("\n<========== ADD STRING ALPHABETICALLY ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("add(\"A\"): " + bst.add("A"));
            System.out.println("add(\"b\"): " + bst.add("b"));
            System.out.println("add(\"C\"): " + bst.add("C"));
            System.out.println("add(\"d\"): " + bst.add("d"));
            System.out.println("add(\"e\"): " + bst.add("e"));
            System.out.println("add(\"F\"): " + bst.add("F"));
            System.out.println("add(\"g\"): " + bst.add("g"));
        }

        public void addStringReverseAlphabetically() {
            System.out.println("\n<========== ADD STRING REVERSE ALPHABETICALLY ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("add(\"G\"): " + bst.add("G"));
            System.out.println("add(\"f\"): " + bst.add("f"));
            System.out.println("add(\"E\"): " + bst.add("E"));
            System.out.println("add(\"D\"): " + bst.add("D"));
            System.out.println("add(\"c\"): " + bst.add("c"));
            System.out.println("add(\"B\"): " + bst.add("B"));
            System.out.println("add(\"a\"): " + bst.add("a"));
        }

        public void addBalancedTree() {
            System.out.println("\n<========== ADD BALANCED TREE ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("add(\"Egg\"): " + bst.add("Egg"));
            System.out.println("add(\"Carrot\"): " + bst.add("Carrot"));
            System.out.println("add(\"Apple\"): " + bst.add("Apple"));
            System.out.println("add(\"Date\"): " + bst.add("Date"));
            System.out.println("add(\"Lentil\"): " + bst.add("Lentil"));
            System.out.println("add(\"Fig\"): " + bst.add("Fig"));
            System.out.println("add(\"Yam\"): " + bst.add("Yam"));
        }

        // Test case category: Control Flow Case
        public void addExistingItemInTree() {
            System.out.println("\n<========== ADD EXISTING ITEM IN TREE ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("add(\"Egg\"): " + bst.add("Egg"));
            System.out.println("add(\"egg\"): " + bst.add("egg"));
            System.out.println("add(\"EGG\"): " + bst.add("EGG"));
            System.out.println("add(\"Egg\"): " + bst.add("Egg"));
        }
    }

    /**
     * FindTreeTestCases class contains test cases to find in the tree
     */
    private static class FindTreeTestCases {
        public void findNullString() {
            System.out.println("\n<========== FIND NULL STRING ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("bst.find(null): " + bst.find(null));
            System.out.println("bst.add(null): " + bst.add(null));
            System.out.println("bst.find(null): " + bst.find(null));

        }

        public void findEmptyString() {
            System.out.println("\n<========== FIND EMPTY STRING ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("bst.find(\"\"): " + bst.find(""));
            System.out.println("bst.add(\"\"): " + bst.add(""));
            System.out.println("bst.find(\"\"): " + bst.find(""));
        }

        public void findInEmptyTree() {
            System.out.println("\n<========== FIND IN EMPTY TREE ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("bst.find(\"Hello\"): " + bst.find("Hello"));
        }

        public void findStringWith1Char() {
            System.out.println("\n<========== FIND STRING WITH 1 CHAR ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("bst.add(\"A\"): " + bst.add("A"));
            System.out.println("bst.find(\"A\"): " + bst.find("A"));
            System.out.println("bst.find(\"a\"): " + bst.find("a"));
        }

        public void findLongString() {
            System.out.println("\n<========== FIND LONG STRING ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("bst.add(\"I am a long long string\"): " + bst.add("I am a long long string"));
            System.out.println("bst.find(\"I am a long long string\"): " + bst.find("I am a long long string"));
            System.out.println("bst.find(\"I AM A LONG LONG STRING\"): " + bst.find("I AM A LONG LONG STRING"));
            System.out.println("bst.find(\"i am a long long string\"): " + bst.find("i am a long long string"));
            System.out.println("bst.find(\" i am a long long string \"): " + bst.find(" i am a long long string "));
        }

        public void findStringInLeftOrderedTree() {
            System.out.println("\n<========== FIND STRING IN LEFT ORDERED TREE ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("add(\"G\"): " + bst.add("G"));
            System.out.println("add(\"f\"): " + bst.add("f"));
            System.out.println("add(\"E\"): " + bst.add("E"));
            System.out.println("add(\"D\"): " + bst.add("D"));
            System.out.println("add(\"c\"): " + bst.add("c"));
            System.out.println("add(\"B\"): " + bst.add("B"));
            System.out.println("add(\"a\"): " + bst.add("a"));

            System.out.println("find(\"G\"): " + bst.find("G"));
            System.out.println("find(\"E\"): " + bst.find("E"));
            System.out.println("find(\"B\"): " + bst.find("B"));
            System.out.println("find(\"a\"): " + bst.find("a"));
        }

        public void findStringInRightOrderedTree() {
            System.out.println("\n<========== FIND STRING IN RIGHT ORDERED TREE ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("add(\"A\"): " + bst.add("A"));
            System.out.println("add(\"b\"): " + bst.add("b"));
            System.out.println("add(\"C\"): " + bst.add("C"));
            System.out.println("add(\"d\"): " + bst.add("d"));
            System.out.println("add(\"e\"): " + bst.add("e"));
            System.out.println("add(\"F\"): " + bst.add("F"));
            System.out.println("add(\"g\"): " + bst.add("g"));

            System.out.println("find(\"A\"): " + bst.find("A"));
            System.out.println("find(\"C\"): " + bst.find("C"));
            System.out.println("find(\"F\"): " + bst.find("F"));
            System.out.println("find(\"g\"): " + bst.find("g"));
        }

        public void findItemInTree() {
            System.out.println("\n<========== FIND ITEM IN TREE ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("add(\"B\"): " + bst.add("B"));
            System.out.println("add(\"A\"): " + bst.add("A"));
            System.out.println("add(\"C\"): " + bst.add("C"));
            System.out.println("find(\"A\"): " + bst.find("A"));
            System.out.println("find(\"B\"): " + bst.find("B"));
            System.out.println("find(\"C\"): " + bst.find("C"));
        }

        public void findItemNotInTree() {
            System.out.println("\n<========== FIND ITEM NOT IN TREE ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("add(\"B\"): " + bst.add("B"));
            System.out.println("add(\"A\"): " + bst.add("A"));
            System.out.println("add(\"C\"): " + bst.add("C"));
            System.out.println("find(\"Hello\"): " + bst.find("Hello"));
            System.out.println("find(\"Not There\"): " + bst.find("Not There"));
        }

        public void findInTreeAddInTreeThenFindAgain() {
            System.out.println("\n<========== FIND IN TREE, ADD IN TREE AND FIND AGAIN ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("find(\"Hello\"): " + bst.find("Hello"));
            System.out.println("add(\"Hello\"): " + bst.add("Hello"));
            System.out.println("find(\"Hello\"): " + bst.find("Hello"));
        }

        public void findInBalancedTree() {
            System.out.println("\n<========== FIND IN BALANCED TREE ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("add(\"Egg\"): " + bst.add("Egg"));
            System.out.println("add(\"Carrot\"): " + bst.add("Carrot"));
            System.out.println("add(\"Apple\"): " + bst.add("Apple"));
            System.out.println("add(\"Date\"): " + bst.add("Date"));
            System.out.println("add(\"Lentil\"): " + bst.add("Lentil"));
            System.out.println("add(\"Fig\"): " + bst.add("Fig"));
            System.out.println("add(\"Yam\"): " + bst.add("Yam"));
            System.out.println("find(\"Egg\"): " + bst.find("Egg"));
            System.out.println("find(\"Egg\"): " + bst.find("Egg"));
            System.out.println("find(\"Carrot\"): " + bst.find("Carrot"));
            System.out.println("find(\"Lentil\"): " + bst.find("Lentil"));
            System.out.println("find(\"Fig\"): " + bst.find("Fig"));
            System.out.println("find(\"Carrot\"): " + bst.find("Carrot"));
            System.out.println("find(\"Carrot\"): " + bst.find("Carrot"));
            System.out.println("find(\"Carrot\"): " + bst.find("Carrot"));
        }
    }

    /**
     * ResetTreeTestCases class contains test cases to reset the tree
     */
    private static class ResetTreeTestCases {
        public void resetEmptyTree() {
            System.out.println("\n<========== RESET EMPTY TREE ==========>");
            SearchTree bst = new SearchTree();
            bst.reset();
        }

        public void resetTreeWith1Node() {
            System.out.println("\n<========== RESET TREE WITH 1 NODE ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("add(\"Egg\"): " + bst.add("Egg"));
            System.out.println("find(\"Egg\"): " + bst.find("Egg"));
            bst.reset();
        }

        public void resetBigTree() {
            System.out.println("\n<========== RESET BIG TREE ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("add(\"Egg\"): " + bst.add("Egg"));
            System.out.println("add(\"Carrot\"): " + bst.add("Carrot"));
            System.out.println("add(\"Apple\"): " + bst.add("Apple"));
            System.out.println("add(\"Date\"): " + bst.add("Date"));
            System.out.println("add(\"Lentil\"): " + bst.add("Lentil"));
            System.out.println("add(\"Fig\"): " + bst.add("Fig"));
            System.out.println("add(\"Yam\"): " + bst.add("Yam"));
            System.out.println("find(\"Egg\"): " + bst.find("Egg"));
            System.out.println("find(\"Egg\"): " + bst.find("Egg"));
            System.out.println("find(\"Carrot\"): " + bst.find("Carrot"));
            System.out.println("find(\"Lentil\"): " + bst.find("Lentil"));
            System.out.println("find(\"Fig\"): " + bst.find("Fig"));
            bst.reset();
        }

        public void resetTwiceInARow() {
            System.out.println("\n<========== RESET TWICE IN A ROW ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("add(\"Egg\"): " + bst.add("Egg"));
            System.out.println("add(\"Carrot\"): " + bst.add("Carrot"));
            System.out.println("add(\"Apple\"): " + bst.add("Apple"));
            System.out.println("add(\"Date\"): " + bst.add("Date"));
            System.out.println("add(\"Lentil\"): " + bst.add("Lentil"));
            System.out.println("add(\"Fig\"): " + bst.add("Fig"));
            System.out.println("add(\"Yam\"): " + bst.add("Yam"));
            System.out.println("find(\"Egg\"): " + bst.find("Egg"));
            System.out.println("find(\"Egg\"): " + bst.find("Egg"));
            System.out.println("find(\"Carrot\"): " + bst.find("Carrot"));
            System.out.println("find(\"Carrot\"): " + bst.find("Carrot"));
            System.out.println("find(\"Carrot\"): " + bst.find("Carrot"));
            bst.reset();
            bst.reset();
        }
    }

    /**
     * PrintTreeTestCases class contains test cases to print the tree
     */
    private static class PrintTreeTestCases {
        public void printEmptyTree() {
            System.out.println("\n<========== PRINT EMPTY TREE ==========>");
            SearchTree bst = new SearchTree();
            System.out.println(bst.printTree());
        }

        public void printTreeWith1Node() {
            System.out.println("\n<========== PRINT TREE WITH 1 NODE ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("add(\"Egg\"): " + bst.add("Egg"));
            System.out.println(bst.printTree());
        }

        public void printBigTree() {
            System.out.println("\n<========== PRINT BIG TREE ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("add(\"Egg\"): " + bst.add("Egg"));
            System.out.println("add(\"Carrot\"): " + bst.add("Carrot"));
            System.out.println("add(\"Apple\"): " + bst.add("Apple"));
            System.out.println("add(\"Date\"): " + bst.add("Date"));
            System.out.println("add(\"Lentil\"): " + bst.add("Lentil"));
            System.out.println("add(\"Fig\"): " + bst.add("Fig"));
            System.out.println("add(\"Yam\"): " + bst.add("Yam"));
            System.out.println(bst.printTree());
        }

        public void printLeftLinkedListTree() {
            System.out.println("\n<========== PRINT LEFT LINKED LIST TREE ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("add(\"G\"): " + bst.add("G"));
            System.out.println("add(\"f\"): " + bst.add("f"));
            System.out.println("add(\"E\"): " + bst.add("E"));
            System.out.println("add(\"D\"): " + bst.add("D"));
            System.out.println("add(\"c\"): " + bst.add("c"));
            System.out.println("add(\"B\"): " + bst.add("B"));
            System.out.println("add(\"a\"): " + bst.add("a"));
            System.out.println(bst.printTree());
        }

        public void printRightLinkedListTree() {
            System.out.println("\n<========== PRINT RIGHT LINKED LIST TREE ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("add(\"A\"): " + bst.add("A"));
            System.out.println("add(\"b\"): " + bst.add("b"));
            System.out.println("add(\"C\"): " + bst.add("C"));
            System.out.println("add(\"d\"): " + bst.add("d"));
            System.out.println("add(\"e\"): " + bst.add("e"));
            System.out.println("add(\"F\"): " + bst.add("F"));
            System.out.println("add(\"g\"): " + bst.add("g"));
            System.out.println(bst.printTree());
        }

        public void printBalancedTree() {
            System.out.println("\n<========== PRINT BALANCED TREE ==========>");
            SearchTree bst = new SearchTree();
            System.out.println("add(\"Egg\"): " + bst.add("Egg"));
            System.out.println("add(\"Carrot\"): " + bst.add("Carrot"));
            System.out.println("add(\"Apple\"): " + bst.add("Apple"));
            System.out.println("add(\"Date\"): " + bst.add("Date"));
            System.out.println("add(\"Lentil\"): " + bst.add("Lentil"));
            System.out.println("add(\"Fig\"): " + bst.add("Fig"));
            System.out.println("add(\"Yam\"): " + bst.add("Yam"));
            System.out.println(bst.printTree());
        }
    }

    /**
     * SearchTreeTest uses assertion to test the SearchTree
     */
    private static class SearchTreeTest {
        // Method implements the tree from figure 2
        public void baseTest() {
            SearchTree bst = new SearchTree();

            // Add everything from figure 2
            assert (bst.add("Egg"));
            assert (bst.add("Carrot"));
            assert (bst.add("Lentil"));
            assert (bst.add("Apple"));
            assert (bst.add("Date"));
            assert (bst.add("Fig"));
            assert (bst.add("Yam"));

            assert (1 == bst.find("Egg"));
            assert (1 == bst.find("Egg"));
            assert (2 == bst.find("Carrot"));
            assert (2 == bst.find("Lentil"));
            assert (3 == bst.find("Fig"));

            // Initial tree
            assert (bst.printTree().equals("Apple 3\nCarrot 2\nDate 3\nEgg 1\nFig 3\nLentil 2\nYam 3\n"));

            // After finding carrot twice
            assert (2 == bst.find("Carrot"));
            assert (2 == bst.find("Carrot"));
            assert (bst.printTree().equals("Apple 2\nCarrot 1\nDate 3\nEgg 2\nFig 4\nLentil 3\nYam 4\n"));
        }

        // Test output of an empty tree
        public void emptyTest() {
            SearchTree bst = new SearchTree();

            assert (bst.printTree().equals(""));
        }
    }

    /**
     * Main method. Starting point
     *
     * @param args string array
     */
    public static void main(String[] args) {

        // Add In Tree Test Cases
        AddTreeTestCases addTreeTestCases = new AddTreeTestCases();
        addTreeTestCases.addNullString();
        addTreeTestCases.addEmptyString();
        addTreeTestCases.addStringWith1Char();
        addTreeTestCases.addLongString();
        addTreeTestCases.addStringAlphabetically();
        addTreeTestCases.addStringReverseAlphabetically();
        addTreeTestCases.addBalancedTree();
        addTreeTestCases.addExistingItemInTree();

        // Find In Tree Test Cases
        FindTreeTestCases findTreeTestCases = new FindTreeTestCases();
        findTreeTestCases.findNullString();
        findTreeTestCases.findEmptyString();
        findTreeTestCases.findInEmptyTree();
        findTreeTestCases.findStringWith1Char();
        findTreeTestCases.findLongString();
        findTreeTestCases.findStringInLeftOrderedTree();
        findTreeTestCases.findStringInRightOrderedTree();
        findTreeTestCases.findItemInTree();
        findTreeTestCases.findItemNotInTree();
        findTreeTestCases.findInTreeAddInTreeThenFindAgain();
        findTreeTestCases.findInBalancedTree();

        // Reset Tree Test Cases
        ResetTreeTestCases resetTreeTestCases = new ResetTreeTestCases();
        resetTreeTestCases.resetEmptyTree();
        resetTreeTestCases.resetTreeWith1Node();
        resetTreeTestCases.resetBigTree();
        resetTreeTestCases.resetTwiceInARow();

        // Print Tree Test Cases
        PrintTreeTestCases printTreeTestCases = new PrintTreeTestCases();
        printTreeTestCases.printEmptyTree();
        printTreeTestCases.printTreeWith1Node();
        printTreeTestCases.printBigTree();
        printTreeTestCases.printLeftLinkedListTree();
        printTreeTestCases.printRightLinkedListTree();
        printTreeTestCases.printBalancedTree();

        // SearchTreeTest
        SearchTreeTest stt = new SearchTreeTest();
        stt.emptyTest();
        stt.baseTest();

        // Assert Tree Test
        SearchTree bst = new SearchTree();
        bst.add("E");
        bst.add("C");
        bst.add("L");
        bst.add("A");
        bst.add("D");
        bst.add("F");
        bst.add("Y");
        bst.find("Y");
        assert (bst.printTree().equals("A 3\nC 2\nD 3\nE 1\nF 4\nL 3\nY 2\n"));
        bst.find("L");
        bst.find("L");
        assert (bst.printTree().equals("A 3\nC 2\nD 3\nE 1\nF 3\nL 2\nY 3\n"));
        System.out.println(bst.printTree());


        // Make Tree Linked List
        SearchTree bstL = new SearchTree();
        bstL.add("E");
        bstL.add("C");
        bstL.add("L");
        bstL.add("A");
        bstL.add("D");
        bstL.add("F");
        bstL.add("Y");
        bstL.find("Y");
        bstL.find("Y");
        bstL.find("D");
        assert (bstL.printTree().equals("A 5\nC 4\nD 3\nE 2\nF 4\nL 3\nY 1\n"));
        bstL.find("L");
        bstL.find("F");
        assert (bstL.printTree().equals("A 7\nC 6\nD 5\nE 4\nF 3\nL 2\nY 1\n"));
        bstL.reset();
        bstL.find("C");
        assert (bstL.printTree().equals("A 6\nC 5\nD 6\nE 4\nF 3\nL 2\nY 1\n"));
    }
}