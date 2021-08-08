import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test suites")
public class VertexClusterUnitTests {

    @Nested
    @DisplayName("Ability to create the graph from inputs")
    class AddTests {
        @Nested
        @DisplayName("Input Validation")
        class Input {
            @Test
            @DisplayName("Null string")
            public void nullTest() {
                VertexCluster test = new VertexCluster();

                assertFalse(test.addEdge(null, null, 1));
            }

            @Test
            @DisplayName("Empty string")
            public void emptyTest() {
                VertexCluster test = new VertexCluster();

                assertFalse(test.addEdge("A", "", 1));
            }

            @Test
            @DisplayName("Invalid weight")
            public void negativeTest() {
                VertexCluster test = new VertexCluster();

                assertFalse(test.addEdge("A", "B", -1));
            }

            @Test
            @DisplayName("Zero weight")
            public void zeroTest() {
                VertexCluster test = new VertexCluster();

                assertTrue(test.addEdge("A", "B", 0));
            }
        }

        @Nested
        @DisplayName("Boundary cases")
        class Boundaries {
            @Test
            @DisplayName("Weight 1")
            public void negativeTest() {
                VertexCluster test = new VertexCluster();

                assertTrue(test.addEdge("A", "B", 1));
            }

            @Test
            @DisplayName("Long vertex name")
            public void longNameTest() {
                VertexCluster test = new VertexCluster();

                assertTrue(test.addEdge("regrthiojdrthpuijsregophistioguhsroqIWNDKAFLNLSDGo" +
                        "idnrgfddddddrgwegterhrgedthdtyjfjfyujfyujaefaiouh" +
                        "gertoiuh34y78e5tjnkdfmgesrgtn", "B", 2));
            }

            @Test
            @DisplayName("Spaces test")
            public void spacesTest() {
                VertexCluster test = new VertexCluster();

                assertTrue(test.addEdge("Hello world", "B", 5));
            }
        }

        @Nested
        @DisplayName("Control flow")
        class CF {
            @Test
            @DisplayName("Normal operation")
            public void negativeTest() {
                VertexCluster test = new VertexCluster();

                assertTrue(test.addEdge("A", "B", 2));
                assertTrue(test.addEdge("C", "D", 17));
                assertTrue(test.addEdge("B", "E", 3));
                assertTrue(test.addEdge("E", "A", 5));
            }

            @Test
            @DisplayName("Add previously added edge")
            public void addTwiceTest() {
                VertexCluster test = new VertexCluster();

                test.addEdge("A", "B", 3);
                test.addEdge("A", "B", 5);

                Set<Set<String>> tmp = new HashSet(Arrays.asList(
                        new HashSet(Arrays.asList("A")),
                        new HashSet(Arrays.asList("B"))));

                assertEquals(tmp, test.clusterVertices((float)1));
            }

        }
    }

    @Nested
    @DisplayName("Ability to calculate and report clusters")
    class ClusterTests {
        @Nested
        @DisplayName("Input Validation")
        class Input {
            @Test
            @DisplayName("Negative tolerance")
            public void negativeTolTest() {
                VertexCluster test = new VertexCluster();

                test.addEdge("A", "B", 2);
                test.addEdge("C", "D", 5);

                assertEquals(null, test.clusterVertices((float)-1));
            }

            @Test
            @DisplayName("Division by 0")
            public void zeroDivTest() {
                VertexCluster test = new VertexCluster();

                test.addEdge("A", "B", 0);

                try {
                    test.clusterVertices((float)-1);
                } catch(Exception e) {
                    fail("Should not have thrown any exception");
                }
            }
        }

        @Nested
        @DisplayName("Boundary cases")
        class Boundaries {
            @Test
            @DisplayName("Empty graph")
            public void emptyTest() {
                VertexCluster test = new VertexCluster();
                Set<Set<String>> res;
                Set<Set<String>> empty = new HashSet();

                res = test.clusterVertices((float)1);

                assertTrue(res == null || res.equals(empty));
            }

            @Test
            @DisplayName("One edge")
            public void oneEdgeTestTest() {
                VertexCluster test = new VertexCluster();


                test.addEdge("A", "B", 2);

                Set<Set<String>> tmp = new HashSet(Arrays.asList(
                        new HashSet(Arrays.asList("A", "B"))));

                assertEquals(tmp, test.clusterVertices((float)3));
            }
        }

        @Nested
        @DisplayName("Control flow")
        class CF {

            @Test
            @DisplayName("Basic clustering")
            public void basicTest() {
                VertexCluster test = new VertexCluster();

                test.addEdge( "A", "B", 3);
                test.addEdge( "A", "C", 15);
                test.addEdge( "B", "D", 21);
                test.addEdge( "B", "E", 13);
                test.addEdge( "C", "D", 2);
                test.addEdge( "D", "E", 4);


                Set<Set<String>> tmp = new HashSet(Arrays.asList(
                        new HashSet(Arrays.asList("A", "B")),
                        new HashSet(Arrays.asList("C", "D", "E"))));

                assertEquals(tmp, test.clusterVertices((float)4));
            }

            @Test
            @DisplayName("Don't use heaviest edge in cluster")
            public void heavyEdgeTest() {
                VertexCluster test = new VertexCluster();

                test.addEdge( "A", "B", 2);
                test.addEdge( "A", "C", 2);
                test.addEdge( "B", "C", 13);
                test.addEdge( "E", "F", 3);
                test.addEdge( "B", "E", 7);
                
                Set<Set<String>> tmp = new HashSet(Arrays.asList(
                        new HashSet(Arrays.asList("A", "B", "C")),
                        new HashSet(Arrays.asList("E", "F"))));

                assertEquals(tmp, test.clusterVertices((float)3));
            }

            @Test
            @DisplayName("Floating point numbers")
            public void floatTest() {
                VertexCluster test = new VertexCluster();

                test.addEdge( "A", "B", 3);
                test.addEdge( "A", "C", 10);
                test.addEdge( "C", "D", 3);


                Set<Set<String>> tmp = new HashSet(Arrays.asList(
                        new HashSet(Arrays.asList("A", "B")),
                        new HashSet(Arrays.asList("C", "D"))));

                assertEquals(tmp, test.clusterVertices((float)3));
            }

            @Test
            @DisplayName("Example from the pdf")
            public void pdfExample() {
                VertexCluster test = new VertexCluster();

                test.addEdge( "A", "B", 3);
                test.addEdge( "A", "H", 1);
                test.addEdge( "I", "H", 1);
                test.addEdge( "I", "B", 4);
                test.addEdge( "B", "C", 7);
                test.addEdge( "I", "C", 8);
                test.addEdge( "G", "H", 7);
                test.addEdge( "H", "J", 10);
                test.addEdge( "D", "I", 12);
                test.addEdge( "D", "C", 14);
                test.addEdge( "D", "E", 8);
                test.addEdge( "J", "F", 3);
                test.addEdge( "E", "F", 2);
                test.addEdge( "F", "G", 7);
                test.addEdge( "J", "D", 1);

                Set<Set<String>> tmp = new HashSet(Arrays.asList(
                        new HashSet(Arrays.asList("A", "B", "H", "I")),
                        new HashSet(Arrays.asList("C")),
                        new HashSet(Arrays.asList("D", "E", "F", "J")),
                        new HashSet(Arrays.asList("G"))));

                assertEquals(tmp, test.clusterVertices((float)3));
            }

            @Test
            @DisplayName("Single cluster")
            public void highTolTest() {
                VertexCluster test = new VertexCluster();

                test.addEdge( "A", "B", 3);
                test.addEdge( "A", "H", 1);
                test.addEdge( "I", "H", 1);
                test.addEdge( "I", "B", 4);
                test.addEdge( "B", "C", 7);
                test.addEdge( "I", "C", 8);
                test.addEdge( "G", "H", 7);
                test.addEdge( "H", "J", 10);
                test.addEdge( "D", "I", 12);
                test.addEdge( "D", "C", 14);
                test.addEdge( "D", "E", 8);
                test.addEdge( "J", "F", 3);
                test.addEdge( "E", "F", 2);
                test.addEdge( "F", "G", 7);
                test.addEdge( "J", "D", 1);

                Set<Set<String>> tmp = new HashSet(Arrays.asList(
                        new HashSet(Arrays.asList("A", "B", "H", "I", "C", "D", "E", "F", "J", "G"))));
                assertEquals(tmp, test.clusterVertices((float)100));
            }

            @Test
            @DisplayName("Trivial cluster")
            public void lowTolTest() {
                VertexCluster test = new VertexCluster();

                test.addEdge( "A", "B", 3);
                test.addEdge( "A", "H", 10);
                test.addEdge( "I", "H", 10);
                test.addEdge( "I", "B", 4);
                test.addEdge( "B", "C", 7);
                test.addEdge( "I", "C", 8);
                test.addEdge( "G", "H", 7);
                test.addEdge( "H", "J", 10);
                test.addEdge( "D", "I", 12);
                test.addEdge( "D", "C", 14);
                test.addEdge( "D", "E", 8);
                test.addEdge( "J", "F", 3);
                test.addEdge( "E", "F", 2);
                test.addEdge( "F", "G", 7);
                test.addEdge( "J", "D", 10);

                Set<Set<String>> tmp = new HashSet(Arrays.asList(
                        new HashSet(Arrays.asList("A")),
                        new HashSet(Arrays.asList("B")),
                        new HashSet(Arrays.asList("H")),
                        new HashSet(Arrays.asList("I")),
                        new HashSet(Arrays.asList("C")),
                        new HashSet(Arrays.asList("D")),
                        new HashSet(Arrays.asList("E")),
                        new HashSet(Arrays.asList("F")),
                        new HashSet(Arrays.asList("J")),
                        new HashSet(Arrays.asList("G"))));
                assertEquals(tmp, test.clusterVertices((float)1.1));
            }

        }
    }
}
