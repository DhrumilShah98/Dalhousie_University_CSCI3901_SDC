import java.util.Set;

/**
 * {@code TestVertexCluster} class to test the {@code VertexCluster} class
 *
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @see VertexCluster
 * @see VertexClusterInterface
 * @since 2021-02-17
 */
public class TestVertexCluster {

    /**
     * {@code AddEdgeMethodTest} class contains test cases for method
     * addEdge(vertex1, vertex2, weight)
     */
    public static class AddEdgeMethodTest {

        // Input validation test case
        // {@code vertex1} and {@code vertex2} are {@code null}
        // {@code null} not allowed
        public void vertex1AndVertex2Null() {
            VertexCluster vx = new VertexCluster();
            assert !vx.addEdge(null, null, 3);
        }

        // Input validation test case
        // {@code vertex1} is empty and {@code vertex2} is {@code null}
        // EMPTY STRING not allowed
        public void vertex1EmptyAndVertex2Null() {
            VertexCluster vx = new VertexCluster();
            assert !vx.addEdge("", null, 3);
            assert !vx.addEdge(" ", null, 1);
            assert !vx.addEdge("  ", null, 2);
            assert !vx.addEdge("   ", null, 10);
        }

        // Input validation test case
        // {@code vertex1} is {@code null} and {@code vertex2} is empty
        // EMPTY STRING not allowed
        public void vertex1NullAndVertex2Empty() {
            VertexCluster vx = new VertexCluster();
            assert !vx.addEdge(null, "", 3);
            assert !vx.addEdge(null, " ", 1);
            assert !vx.addEdge(null, "  ", 2);
            assert !vx.addEdge(null, "   ", 10);
        }

        // Input validation test case
        // {@code vertex1} and {@code vertex2} are empty
        // EMPTY STRING not allowed
        public void vertex1AndVertex2Empty() {
            VertexCluster vx = new VertexCluster();
            assert !vx.addEdge("", "", 3);
            assert !vx.addEdge(" ", " ", 1);
            assert !vx.addEdge("   ", "  ", 2);
            assert !vx.addEdge("   ", "   ", 10);
        }

        // Input validation test case
        // Valid {@code vertex1} and {@code vertex2} but NEGATIVE {@code weight}
        // NEGATIVE WEIGHT not allowed
        public void negativeWeight() {
            VertexCluster vx = new VertexCluster();
            assert !vx.addEdge("P", "Q", -1);
            assert !vx.addEdge("M", "N", -2);
            assert !vx.addEdge("A", "B", -10);
            assert !vx.addEdge("A", "B", Integer.MIN_VALUE);
        }

        // Boundary value test case
        // Valid {@code vertex1} and {@code vertex2} with edge {@code weight} {@code 0}
        public void edgeWeight0() {
            VertexCluster vx = new VertexCluster();
            assert vx.addEdge("A", "B", 0);
        }

        // Boundary value test case
        // Valid {@code vertex1} and {@code vertex2} with edge {@code weight} {@code 1}
        public void edgeWeight1() {
            VertexCluster vx = new VertexCluster();
            vx.addEdge("A", "B", 1);
        }

        // Boundary value test case
        // Valid {@code vertex1} and {@code vertex2} with edge {@code weight} {@code Integer.MAX_INT}
        public void edgeWeightMaxVal() {
            VertexCluster vx = new VertexCluster();
            vx.addEdge("A", "B", Integer.MAX_VALUE);
        }

        // Boundary value test case
        // Valid {@code vertex1} and {@code vertex2} with only one character string
        public void oneCharStringVertex1And2() {
            VertexCluster vx = new VertexCluster();
            assert vx.addEdge("A", "B", 3);
        }

        // Boundary value test case
        // Valid {@code vertex1} and {@code vertex2} with a long string
        public void longStringVertex1AndVertex2() {
            VertexCluster vx = new VertexCluster();
            assert vx.addEdge("SDC Assignment of Student 031", "SDC Assignment of Student 032", 10);
        }

        // Control flow test case
        // {@code vertex1} is valid and {@code vertex2} is {@code null}
        // {@code vertex1} is not connected to the graph (i.e. No edge connected to vertex1)
        // {@code weight} is ignored
        public void vertex1ValidAndVertex2Null() {
            VertexCluster vx = new VertexCluster();
            assert vx.addEdge("A", null, 1);
            assert !vx.addEdge("A", null, 3);
            assert !vx.addEdge(null, "A", 10);
            assert !vx.addEdge("A", "A", -10);
        }

        // Control flow test case
        // {@code vertex1} is {@code null} and {@code vertex2} is valid
        // {@code vertex2} is not connected to the graph (i.e. No edge connected to vertex1)
        // {@code weight} is ignored
        public void vertex1NullAndVertex2Valid() {
            VertexCluster vx = new VertexCluster();
            assert vx.addEdge(null, "A", 1);
            assert !vx.addEdge(null, "A", 3);
            assert !vx.addEdge("A", null, 10);
            assert !vx.addEdge("A", "A", -10);
        }

        // Control flow test case
        // {@code vertex1} and {@code vertex2} are equal
        // vertex is not connected to the graph (i.e. No edge connected to vertex)
        // {@code weight} is ignored
        public void vertex1AndVertex2Equal() {
            VertexCluster vx = new VertexCluster();
            assert vx.addEdge("A", "A", -10);
            assert !vx.addEdge(null, "A", 1);
            assert !vx.addEdge("A", null, 10);
        }

        // Control flow test case
        // Add a {@code null} graph. (A graph with n vertices and no edges)
        public void addANullGraph() {
            VertexCluster vx = new VertexCluster();
            assert vx.addEdge("A", null, 3);
            assert vx.addEdge("B", null, -1);
            assert vx.addEdge("P", null, 2);
            assert vx.addEdge(null, "Q", 12);
            assert vx.addEdge(null, "Y", -10);
            assert vx.addEdge(null, "Z", Integer.MAX_VALUE);
            assert vx.addEdge("M", "M", Integer.MIN_VALUE);
            assert vx.addEdge("N", "N", 20);
            assert vx.addEdge("O", "O", 4);
        }

        // Control flow test case
        // Add a connected graph with valid {@code vertex1} and {@code vertex2} and {@code weight} >= 0
        public void addAConnectedGraph() {
            VertexCluster vx = new VertexCluster();
            assert vx.addEdge("A", "B", 3);
            assert vx.addEdge("A", "H", 1);
            assert vx.addEdge("I", "H", 1);
            assert vx.addEdge("I", "B", 4);
            assert vx.addEdge("B", "C", 7);
            assert vx.addEdge("I", "C", 8);
            assert vx.addEdge("G", "H", 7);
            assert vx.addEdge("H", "J", 10);
            assert vx.addEdge("D", "I", 12);
            assert vx.addEdge("D", "C", 14);
            assert vx.addEdge("D", "E", 8);
            assert vx.addEdge("J", "F", 3);
            assert vx.addEdge("E", "F", 2);
            assert vx.addEdge("F", "G", 7);
            assert vx.addEdge("J", "D", 1);
            assert vx.addEdge("F", "K", 0);
            assert vx.addEdge("K", "E", 3);
        }

        // Control flow test case
        // Add separate vertices first and connect
        public void addSeparateVerticesAndConnect() {
            VertexCluster vx = new VertexCluster();
            assert vx.addEdge("A", null, 3);
            assert vx.addEdge(null, "B", 11);
            assert vx.addEdge("A", "B", 3);
        }

        // Control flow test case
        // Add duplicate edge
        public void addDuplicateEdge() {
            VertexCluster vx = new VertexCluster();
            assert vx.addEdge("A", "B", 3);
            assert !vx.addEdge("A", "B", 10);
            assert !vx.addEdge("A", null, 3);
            assert !vx.addEdge(null, "B", 3);
            assert !vx.addEdge("A", "A", 3);
            assert !vx.addEdge("B", "B", 3);
        }

        // Control flow test case
        // One vertex connected to only one vertex (Different vertex)
        public void vertexConnectedWithOneEdge() {
            VertexCluster vx = new VertexCluster();
            assert vx.addEdge("A", "B", 3);
            assert vx.addEdge("C", "D", 10);
            assert vx.addEdge("E", "F", 2);
            assert vx.addEdge("G", "H", 1);
            assert vx.addEdge("I", "J", 5);
            assert vx.addEdge("K", "L", 2);
        }

        // Control flow test case
        // Case sensitive vertices
        public void addCaseSensitiveVertices() {
            VertexCluster vx = new VertexCluster();
            assert vx.addEdge("A", "B", 3);
            assert vx.addEdge("a", "b", 10);
            assert vx.addEdge("A", "b", 2);
            assert vx.addEdge("B", "a", 1);
            assert vx.addEdge("P", "q", 5);
            assert vx.addEdge("P", "b", 2);
        }

        // Control flow test case
        // Non trimmed vertices
        public void addNonTrimmedVertices() {
            VertexCluster vx = new VertexCluster();
            assert vx.addEdge(" A ", " B ", 3);
            assert !vx.addEdge("A", "B", 1);
            assert !vx.addEdge("   A    ", "   B    ", 2);
            assert !vx.addEdge("   A   ", "   B   ", 10);
        }

        // Data flow test cases
        // Add edge in an empty list
        public void addEdgeInEmptyList() {
            VertexCluster vx = new VertexCluster();
            assert vx.addEdge("P", "Q", 3);
        }

        // Data flow test case
        // Add edge into a list that already has edges
        public void addEdgeInListWithData() {
            VertexCluster vx = new VertexCluster();
            assert vx.addEdge("P", "Q", 3);
            assert vx.addEdge("P", "R", 12);
            assert vx.addEdge("R", "Q", 6);
        }
    }

    /**
     * {@code ClusterVerticesMethodTest} class contains test cases for method
     * clusterVertices(tolerance)
     */
    public static class ClusterVerticesMethodTest {

        // Helper method to add edges in {@code VertexCluster} class
        private void addEdges(VertexCluster vx) {
            assert vx.addEdge("A", "B", 3);
            assert vx.addEdge("A", "H", 1);
            assert vx.addEdge("I", "H", 1);
            assert vx.addEdge("I", "B", 4);
            assert vx.addEdge("B", "C", 7);
            assert vx.addEdge("I", "C", 8);
            assert vx.addEdge("G", "H", 7);
            assert vx.addEdge("H", "J", 10);
            assert vx.addEdge("D", "I", 12);
            assert vx.addEdge("D", "C", 14);
            assert vx.addEdge("D", "E", 8);
            assert vx.addEdge("J", "F", 3);
            assert vx.addEdge("E", "F", 2);
            assert vx.addEdge("F", "G", 7);
            assert vx.addEdge("J", "D", 1);
        }

        // Helper method to compare output of clusterVertices() method with actual output
        private boolean testOutput(String[][] testClusters, Set<Set<String>> opClusters) {
            int index = 0;
            for (Set<String> cluster : opClusters) {
                String[] opClusterString = cluster.toArray(new String[0]);
                String[] testClusterString = testClusters[index];
                for (int i = 0; i < opClusterString.length; ++i) {
                    if (!opClusterString[i].equals(testClusterString[i])) {
                        return false;
                    }
                }
                index++;
            }
            return true;
        }

        // Input validation test case
        // Call clusterVertices() with negative {@code tolerance}
        public void clusterVerticesWithNegativeTolerance() {
            VertexCluster vx = new VertexCluster();
            assert vx.addEdge("A", "B", 3);
            assert vx.addEdge("A", "H", 1);
            assert vx.addEdge("I", "H", 1);
            assert vx.addEdge("I", "B", 4);
            assert vx.clusterVertices(-1) == null;
        }

        // Boundary value test case
        // Call clusterVertices() with 0 {@code tolerance}
        public void clusterVerticesWithTolerance0() {
            VertexCluster vx = new VertexCluster();
            addEdges(vx);
            final String[][] testClusters = {{"A"}, {"B"}, {"H"}, {"I"}, {"C"}, {"G"}, {"J"}, {"D"}, {"E"}, {"F"}};
            final Set<Set<String>> opClusters = vx.clusterVertices(0f);
            assert testOutput(testClusters, opClusters);
        }

        // Boundary value test case
        // Call clusterVertices() with 1 {@code tolerance}
        public void clusterVerticesWithTolerance1() {
            VertexCluster vx = new VertexCluster();
            addEdges(vx);
            final String[][] testClusters = {{"A", "H", "I"}, {"B"}, {"C"}, {"G"}, {"D", "J"}, {"E"}, {"F"}};
            final Set<Set<String>> opClusters = vx.clusterVertices(1f);
            assert testOutput(testClusters, opClusters);
        }

        // Boundary value test case
        // Call clusterVertices() with Float.MAX_VALUE {@code tolerance}
        public void clusterVerticesWithToleranceMaxVal() {
            VertexCluster vx = new VertexCluster();
            addEdges(vx);
            final String[][] testClusters = {{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"}};
            final Set<Set<String>> opClusters = vx.clusterVertices(Float.MAX_VALUE);
            assert testOutput(testClusters, opClusters);
        }

        // Control flow test case
        // Call clusterVertices() on a graph with no edges
        public void clusterVerticesOnGraphWithNoEdges() {
            VertexCluster vx = new VertexCluster();
            vx.addEdge("A", "A", 3);
            vx.addEdge("B", null, 10);
            vx.addEdge(null, "C", 12);
            final String[][] testClusters = {{"A"}, {"B"}, {"C"}};
            final Set<Set<String>> opClusters1 = vx.clusterVertices(0);
            assert testOutput(testClusters, opClusters1);
            final Set<Set<String>> opClusters2 = vx.clusterVertices(1);
            assert testOutput(testClusters, opClusters2);
            final Set<Set<String>> opClusters3 = vx.clusterVertices(4);
            assert testOutput(testClusters, opClusters3);
            final Set<Set<String>> opClusters4 = vx.clusterVertices(Float.MAX_VALUE);
            assert testOutput(testClusters, opClusters4);
        }

        // Control flow test case
        // Call clusterVertices() on a graph where calculated tolerance > {@code tolerance)
        public void clusterVerticesOnGraphWithBigEdgeWeightAndSmallTolerance() {
            VertexCluster vx = new VertexCluster();
            vx.addEdge("A", "B", 30);
            vx.addEdge("C", "D", 100);
            vx.addEdge("E", "F", 120);
            final String[][] testClusters = {{"A"}, {"B"}, {"C"}, {"D"}, {"E"}, {"F"}};
            final Set<Set<String>> opClusters = vx.clusterVertices(2);
            assert testOutput(testClusters, opClusters);
        }

        // Data flow test case
        // Call clusterVertices() before addEdge()
        public void clusterVerticesBeforeAddEdge() {
            VertexCluster vx = new VertexCluster();
            assert vx.clusterVertices(3) == null;
        }

        // Data flow test case
        // Call clusterVertices() on same graph multiple times
        public void clusterVerticesOnSameGraphMultipleTimes() {
            VertexCluster vx = new VertexCluster();
            addEdges(vx);
            final String[][] testClusters = {{"A", "B", "H", "I"}, {"C"}, {"G"}, {"D", "E", "F", "J"}};
            final Set<Set<String>> opClusters1 = vx.clusterVertices(3f);
            final Set<Set<String>> opClusters2 = vx.clusterVertices(3f);
            final Set<Set<String>> opClusters3 = vx.clusterVertices(3f);
            assert testOutput(testClusters, opClusters1);
            assert testOutput(testClusters, opClusters2);
            assert testOutput(testClusters, opClusters3);
        }
    }

    /**
     * {@code Graph1Test} class contains the graph from PDF
     */
    public static class Graph1Test {
        VertexCluster vx = new VertexCluster();

        public Graph1Test() {
            assert vx.addEdge("A", "B", 3);
            assert vx.addEdge("A", "H", 1);
            assert vx.addEdge("I", "H", 1);
            assert vx.addEdge("I", "B", 4);
            assert vx.addEdge("B", "C", 7);
            assert vx.addEdge("I", "C", 8);
            assert vx.addEdge("G", "H", 7);
            assert vx.addEdge("H", "J", 10);
            assert vx.addEdge("D", "I", 12);
            assert vx.addEdge("D", "C", 14);
            assert vx.addEdge("D", "E", 8);
            assert vx.addEdge("J", "F", 3);
            assert vx.addEdge("E", "F", 2);
            assert vx.addEdge("F", "G", 7);
            assert vx.addEdge("J", "D", 1);
        }

        // Helper method to compare output of clusterVertices() method with actual output
        private boolean testOutput(String[][] testClusters, Set<Set<String>> opClusters) {
            int index = 0;
            for (Set<String> cluster : opClusters) {
                String[] opClusterString = cluster.toArray(new String[0]);
                String[] testClusterString = testClusters[index];
                for (int i = 0; i < opClusterString.length; ++i) {
                    if (!opClusterString[i].equals(testClusterString[i])) {
                        return false;
                    }
                }
                index++;
            }
            return true;
        }

        public void callClusterVerticesWithTolerance0() {
            final String[][] testClusters = {{"A"}, {"B"}, {"H"}, {"I"}, {"C"}, {"G"}, {"J"}, {"D"}, {"E"}, {"F"}};
            final Set<Set<String>> opClusters = vx.clusterVertices(0f);
            assert testOutput(testClusters, opClusters);
        }

        public void callClusterVerticesWithTolerance2() {
            final String[][] testClusters = {{"A", "H", "I"}, {"B"}, {"C"}, {"G"}, {"D", "J"}, {"E", "F"}};
            final Set<Set<String>> opClusters = vx.clusterVertices(2f);
            assert testOutput(testClusters, opClusters);
        }

        public void callClusterVerticesWithTolerance3() {
            final String[][] testClusters = {{"A", "B", "H", "I"}, {"C"}, {"G"}, {"D", "E", "F", "J"}};
            final Set<Set<String>> opClusters = vx.clusterVertices(3f);
            assert testOutput(testClusters, opClusters);
        }

        public void callClusterVerticesWithTolerance4() {
            final String[][] testClusters = {{"A", "B", "D", "E", "F", "H", "I", "J"}, {"C"}, {"G"}};
            final Set<Set<String>> opClusters = vx.clusterVertices(4f);
            assert testOutput(testClusters, opClusters);
        }

        public void callClusterVerticesWithTolerance7() {
            final String[][] testClusters = {{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"}};
            final Set<Set<String>> opClusters = vx.clusterVertices(7f);
            assert testOutput(testClusters, opClusters);
        }
    }

    public static void main(String[] args) {
        // <=====================================================>
        // <========================= 1 =========================>
        // <=====================================================>
        // addEdge(vertex1, vertex2, weight) test cases
        TestVertexCluster.AddEdgeMethodTest addEdgeMethodTest =
                new TestVertexCluster.AddEdgeMethodTest();

        // Input validation test cases
        addEdgeMethodTest.vertex1AndVertex2Null();
        addEdgeMethodTest.vertex1EmptyAndVertex2Null();
        addEdgeMethodTest.vertex1NullAndVertex2Empty();
        addEdgeMethodTest.vertex1AndVertex2Empty();
        addEdgeMethodTest.negativeWeight();

        // Boundary value test cases
        addEdgeMethodTest.edgeWeight0();
        addEdgeMethodTest.edgeWeight1();
        addEdgeMethodTest.edgeWeightMaxVal();
        addEdgeMethodTest.oneCharStringVertex1And2();
        addEdgeMethodTest.longStringVertex1AndVertex2();

        // Control flow test cases
        addEdgeMethodTest.vertex1ValidAndVertex2Null();
        addEdgeMethodTest.vertex1NullAndVertex2Valid();
        addEdgeMethodTest.vertex1AndVertex2Equal();
        addEdgeMethodTest.addANullGraph();
        addEdgeMethodTest.addAConnectedGraph();
        addEdgeMethodTest.addSeparateVerticesAndConnect();
        addEdgeMethodTest.addDuplicateEdge();
        addEdgeMethodTest.vertexConnectedWithOneEdge();
        addEdgeMethodTest.addCaseSensitiveVertices();
        addEdgeMethodTest.addNonTrimmedVertices();

        // Data flow test cases
        addEdgeMethodTest.addEdgeInEmptyList();
        addEdgeMethodTest.addEdgeInListWithData();


        // <=====================================================>
        // <========================= 2 =========================>
        // <=====================================================>
        // clusterVertices(tolerance) test cases
        TestVertexCluster.ClusterVerticesMethodTest clusterVerticesMethodTest =
                new TestVertexCluster.ClusterVerticesMethodTest();

        // Input validation test cases
        clusterVerticesMethodTest.clusterVerticesWithNegativeTolerance();

        // Boundary value test cases
        clusterVerticesMethodTest.clusterVerticesWithTolerance0();
        clusterVerticesMethodTest.clusterVerticesWithTolerance1();
        clusterVerticesMethodTest.clusterVerticesWithToleranceMaxVal();

        // Control flow test cases
        clusterVerticesMethodTest.clusterVerticesOnGraphWithNoEdges();
        clusterVerticesMethodTest.clusterVerticesOnGraphWithBigEdgeWeightAndSmallTolerance();
        // Some control flow test cases covered in Part 3 (Line number 531)

        // Data flow test cases
        clusterVerticesMethodTest.clusterVerticesBeforeAddEdge();
        clusterVerticesMethodTest.clusterVerticesOnSameGraphMultipleTimes();


        // <=====================================================>
        // <========================= 3 =========================>
        // <=====================================================>
        /* Test graph from PDF*/
        // Test Big: 1 (Graph 1) - From PDF
        TestVertexCluster.Graph1Test graph1Test = new TestVertexCluster.Graph1Test();
        graph1Test.callClusterVerticesWithTolerance0();
        graph1Test.callClusterVerticesWithTolerance2();
        graph1Test.callClusterVerticesWithTolerance3();
        graph1Test.callClusterVerticesWithTolerance4();
        graph1Test.callClusterVerticesWithTolerance7();


        // <=====================================================>
        // <========================= 4 =========================>
        // <=====================================================>
        // Test Big: 2 (Graph 2)
        VertexCluster v2 = new VertexCluster();
        assert v2.addEdge("M", "N", 35);
        assert v2.addEdge("K", "L", 2);
        assert v2.addEdge("C", "D", 2);
        assert v2.addEdge("C", "N", 67);
        assert v2.addEdge("O", "N", 1);
        assert v2.addEdge("L", "M", 18);
        assert v2.addEdge("M", "Q", 3);
        assert v2.addEdge("S", "R", 3);
        assert v2.addEdge("R", "Q", 2);
        assert v2.addEdge("G", "R", 3);
        assert v2.addEdge("G", "F", 4);
        assert v2.addEdge("B", "F", 4);
        assert v2.addEdge("H", "J", 4);
        assert v2.addEdge("H", "I", 22);
        assert v2.addEdge("A", "J", 13);
        assert v2.addEdge("G", "I", 2);
        assert v2.addEdge("L", "P", 3);
        assert v2.addEdge("B", "K", 23);
        assert v2.addEdge("P", "A", 2);
        assert v2.addEdge("C", "E", 1);
        assert v2.addEdge("U", "J", 21);
        assert v2.addEdge("T", "Q", 17);

        System.out.println(v2.clusterVertices(4.34f));
        System.out.println(v2.clusterVertices(4.33f));
        assert v2.addEdge("T", "V", 2);
        System.out.println(v2.clusterVertices(4.33f));

        // <=====================================================>
        // <========================= 5 =========================>
        // <=====================================================>
        // Test Big: 3 (Graph 3)
        VertexCluster v3 = new VertexCluster();
        assert v3.addEdge("A", "B", 3);
        assert v3.addEdge("B", "C", 2);
        assert v3.addEdge("C", "D", 7);
        assert v3.addEdge("D", "E", 2);
        assert v3.addEdge("E", "A", 5);
        assert v3.addEdge("J", "F", 5);
        assert v3.addEdge("F", "D", 2);
        assert v3.addEdge("H", "G", 2);
        assert v3.addEdge("F", "C", 4);
        assert v3.addEdge("D", "A", 3);
        assert v3.addEdge("I", "H", 3);
        assert v3.addEdge("J", "I", 6);
        assert v3.addEdge("G", "E", 6);
        assert v3.addEdge("G", "J", 5);

        System.out.println(v3.clusterVertices(4f));
        System.out.println(v3.clusterVertices(0f));

        // <=====================================================>
        // <========================= 6 =========================>
        // <=====================================================>
        // Test Big: 4 (Graph 4)
        VertexCluster vx2 = new VertexCluster();
        assert vx2.addEdge("a", "B", 12);
        assert vx2.addEdge("a", "c", 3);
        assert vx2.addEdge("c", "B", 4);
        assert vx2.addEdge("c", "d", 2);
        assert vx2.addEdge("c", "E", 3);
        assert vx2.addEdge("E", "B", 12);
        assert vx2.addEdge("B", "d", 4);
        assert vx2.addEdge("d", "f", 14);
        assert vx2.addEdge("f", "E", 4);
        System.out.println("Tol 0:               " + vx2.clusterVertices(0));
        System.out.println("Tol 1:               " + vx2.clusterVertices(1));
        System.out.println("Tol 2.333333f:       " + vx2.clusterVertices(2.333333f));
        System.out.println("Tol 3:               " + vx2.clusterVertices(3));
        System.out.println("Tol 10:              " + vx2.clusterVertices(10));
        System.out.println("Tol 20:              " + vx2.clusterVertices(20));
        System.out.println("Tol Float.MAX_VALUE: " + vx2.clusterVertices(Float.MAX_VALUE));
    }
}