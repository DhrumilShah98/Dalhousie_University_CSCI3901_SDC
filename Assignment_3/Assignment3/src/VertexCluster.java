import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.TreeSet;

/**
 * {@code VertexCluster} builds an undirected, weighted graph using {@code vertices} and {@code edges}.
 * Each {@code Vertex} has a string name/identifier (like a document name).
 * The {@code Edge.weight} on each {@code Edge} is a similarity measure
 * between the {@code Edge.vertex1} and {@code Edge.vertex2}.
 * It identify sets(clusters) of {@code vertices} that are all similar.
 *
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @see Vertex
 * @see Edge
 * @see VertexClusterInterface
 * @since 2021-02-15
 */
public class VertexCluster implements VertexClusterInterface {

    /**
     * {@code NO_WEIGHT} is used when {@code Vertex} is not connected in the graph.
     * (i.e. Separate vertex).
     */
    private static final int NO_WEIGHT = -1;

    /**
     * {@code vertices} is an array list of all the {@code Vertex}.
     *
     * @see Vertex
     */
    private ArrayList<Vertex> vertices;

    /**
     * {@code edges} is a sorted set of all the {@code Edge} in the graph.
     * It is an edge list representation of the graph.
     *
     * @see Edge
     */
    private final TreeSet<Edge> edges;

    /**
     * {@code verticesMap} is a hash map of {@code Vertex.vertex}
     * and its index location in {@code vertices} array list.
     */
    private final HashMap<String, Integer> verticesMap;

    /**
     * Class constructor {@code VertexCluster}.
     */
    public VertexCluster() {
        vertices = new ArrayList<>();
        edges = new TreeSet<>();
        verticesMap = new HashMap<>();
    }

    /**
     * Adds {@code vertex} in the graph.
     * It is not connected to any other vertex in the graph.
     * (i.e. Separate vertex).
     *
     * @param vertex {@code vertex} to be added
     * @return {@code true} if added otherwise {@code false}
     */
    private boolean addSeparateVertex(final String vertex) {
        // Check if any {@code Edge} exists containing {@code vertex}
        boolean edgeExists = edges.stream().anyMatch(edge ->
                (vertex.equals(edge.vertex1) || vertex.equals(edge.vertex2)));

        // Return {@code false} if edge exists containing {@code vertex}
        if (edgeExists) {
            return false;
        }

        // Add {@code vertex} in {@code edges} tree set(sorted set)
        boolean edgeAdded = edges.add(new Edge(vertex, null, NO_WEIGHT));

        // Return {@code false} if {@code Edge} addition failed
        if (!edgeAdded) {
            return false;
        }

        // Add {@code vertex} in {@code verticesMap} hash map and {@code vertices} array list
        verticesMap.put(vertex, vertices.size());
        vertices.add(new Vertex(vertex, vertices.size()));

        // Return {@code true}, vertex and edge added
        return true;
    }

    /**
     * Record an edge between {@code vertex1} and {@code vertex2} with given {@code weight}.
     * If {@code vertex1} and {@code vertex2} are equal and not {@code null} or empty or
     * {@code vertex1} IS NOT {@code null} or empty and {@code vertex2} IS {@code null} or
     * {@code vertex1} IS {@code null} and {@code vertex2} IS NOT {@code null} or empty,
     * it implies that vertex is not connected to the graph. (i.e. vertex without an edge).
     *
     * @param vertex1 name of the {@code vertex1}
     * @param vertex2 name of the {@code vertex2}
     * @param weight  edge weight {@code weight}
     * @return {@code true} if edge added otherwise {@code false}
     */
    @Override
    public boolean addEdge(String vertex1, String vertex2, int weight) {

        //Return {@code false} if {@code vertex1} and {@code vertex2} are {@code null}
        if (vertex1 == null && vertex2 == null) {
            return false;
        }

        // Case: {@code vertex1} IS NOT {@code null} and {@code vertex2} IS {@code null}
        // Vertex is not connected to the graph. (i.e. Vertex without an edge)
        // Ignore {@code weight}
        else if (vertex1 != null && vertex2 == null) {
            // Return {@code false} if {@code vertex1} is empty
            if (vertex1.trim().isEmpty()) {
                return false;
            } else {
                // Return {@code true} if {@code vertex1} is added otherwise {@code false}
                return addSeparateVertex(vertex1.trim());
            }
        }

        // Case: {@code vertex1} IS {@code null} and {@code vertex2} IS NOT {@code null}
        // Vertex is not connected to the graph. (i.e. Vertex without an edge)
        // Ignore {@code weight}
        else if (vertex1 == null) {
            // Return {@code false} if {@code vertex2} is empty
            if (vertex2.trim().isEmpty()) {
                return false;
            } else {
                // Return {@code true} if {@code vertex2} is added otherwise {@code false}
                return addSeparateVertex(vertex2.trim());
            }
        }

        // Case: {@code vertex1} and {@code vertex2} are equal
        // Vertex is not connected to the graph. (i.e. Vertex without an edge)
        // Ignore {@code weight}
        else if (vertex1.equals(vertex2)) {
            // Return {@code false} if {@code vertex1} is empty
            if (vertex1.trim().isEmpty()) {
                return false;
            } else {
                // Return {@code true} if {@code vertex1} is added otherwise {@code false}
                return addSeparateVertex(vertex1.trim());
            }
        }

        // Case: {@code vertex1} IS NOT {@code null} and {@code vertex2} IS NOT {@code null}
        // Vertices are connected
        else {
            // Return {@code false} if {@code vertex1} or {@code vertex2} is empty
            if (vertex1.trim().isEmpty() || vertex2.trim().isEmpty()) {
                return false;
            }

            // Return {@code false} if {@code weight} is negative
            if (weight < 0) {
                return false;
            }

            // Swap if {@code vertex1} is greater than {@code vertex2}
            if (vertex1.compareToIgnoreCase(vertex2) > 0) {
                String temp = vertex1;
                vertex1 = vertex2;
                vertex2 = temp;
            }

            // Required final variables to use with the stream
            final String finalVertex1 = vertex1.trim();
            final String finalVertex2 = vertex2.trim();

            // Logic to check if an edge exists between {@code finalVertex1} and {@code finalVertex2}
            boolean edgeExists = edges.stream().anyMatch(edge ->
                    (finalVertex1.equals(edge.vertex1) && finalVertex2.equals(edge.vertex2)) ||
                            (finalVertex1.equals(edge.vertex2) && finalVertex2.equals(edge.vertex1))
            );

            // Return {@code false} if an edge exists between {@code finalVertex1} and {@code finalVertex2}
            if (edgeExists) {
                return false;
            }

            // Add a new edge in {@code edges}
            boolean isEdgeAdded = edges.add(new Edge(finalVertex1, finalVertex2, weight));

            // Return {@code false} if edge is not added in {@code edges}
            if (!isEdgeAdded) {
                return false;
            }

            // Add {@code finalVertex1} and {@code finalVertex2} in {@code vertices} list and {@code verticesMap} map
            if (!verticesMap.containsKey(finalVertex1)) {
                verticesMap.put(finalVertex1, vertices.size());
                vertices.add(new Vertex(finalVertex1, vertices.size()));
            }
            if (!verticesMap.containsKey(finalVertex2)) {
                verticesMap.put(finalVertex2, vertices.size());
                vertices.add(new Vertex(finalVertex2, vertices.size()));
            }

            // Remove if {@code finalVertex1} exists as a separate vertex in the graph
            edges.removeIf(edge -> (edge.vertex1.equals(finalVertex1) && edge.vertex2 == null && edge.weight == NO_WEIGHT));

            // Remove if {@code finalVertex2} exists as a separate vertex in the graph
            edges.removeIf(edge -> (edge.vertex1.equals(finalVertex2) && edge.vertex2 == null && edge.weight == NO_WEIGHT));

            // Return {@code true}, vertex and edge added
            return true;
        }
    }

    /**
     * Creates a deep copy of {@code vertices} array list.
     *
     * @return {@code vertices} deep copy
     */
    private ArrayList<Vertex> verticesCopy() {
        final ArrayList<Vertex> newVertices = new ArrayList<>();
        if (vertices != null) {
            vertices.forEach(v -> newVertices.add(new Vertex(v.vertex, newVertices.size())));
        }
        return newVertices;
    }

    /**
     * Gets the cluster sets.
     *
     * @return set of clusters
     */
    private Set<Set<String>> getClusterSets() {
        // Main cluster
        Set<Set<String>> verticesCluster = new LinkedHashSet<>();

        int prevClusterNum = -1;
        Set<String> newCluster = null;

        // Iterate through all the {@code vertices} and group
        // same vertices together in a cluster
        for (Vertex vertex : vertices) {
            int currentClusterNum = vertex.cluster; // Current vertex cluster number
            String currentVertexName = vertex.vertex; // Current vertex name
            if (prevClusterNum == -1) {
                newCluster = new LinkedHashSet<>();
                newCluster.add(currentVertexName);
            } else if (currentClusterNum == prevClusterNum) {
                newCluster.add(currentVertexName);
            } else {
                verticesCluster.add(newCluster);
                newCluster = new LinkedHashSet<>();
                newCluster.add(currentVertexName);
            }
            prevClusterNum = currentClusterNum;
        }
        verticesCluster.add(newCluster);
        return verticesCluster;
    }

    /**
     * Creates a set of clusters from the graph using given {@code tolerance} parameter.
     * {@code tolerance} decides whether to merge vertices or not.
     *
     * @param tolerance given {@code tolerance} value
     * @return set of clusters otherwise {@code null}
     */
    @Override
    public Set<Set<String>> clusterVertices(float tolerance) {

        // Return {@code null} if vertices is empty or edges is empty
        if (vertices.isEmpty() || edges.isEmpty()) {
            return null;
        }

        // Return {@code null} if {@code tolerance} is negative
        if (tolerance < 0) {
            return null;
        }

        // {@code clusterWeightMap} is a hash map with key as {@code Vertex.cluster} and value as heaviest edge
        // used to create the {@code Vertex.cluster}
        final HashMap<Integer, Float> clusterWeightMap = new HashMap<>();

        // {@code clusterVertexIndexMap} is a hash map with key as {@code Vertex.cluster} and
        // value as array list of all {@code Vertex} indices
        final HashMap<Integer, ArrayList<Integer>> clusterVertexIndexMap = new HashMap<>();

        // Iterate through all the edges
        for (Edge edge : edges) {
            // Add single vertex {@code edge.vertex1} in a separate cluster.
            // It is not connected to the graph
            if (edge.vertex2 == null && edge.weight == NO_WEIGHT) {
                final int vertex1Index = verticesMap.get(edge.vertex1); // First vertex index

                // Find logic
                final Vertex vertex1 = vertices.get(vertex1Index); // First vertex

                ArrayList<Integer> vIndices = new ArrayList<>();
                vIndices.add(vertex1Index);
                clusterVertexIndexMap.put(vertex1.cluster, vIndices); // Add {@code vertex1.cluster} as key and vertex index as arraylist
                clusterWeightMap.put(vertex1.cluster, 1f); // Add {@code vertex1.cluster} as key and {@code 1} as vertex weight
                // continue to the next edge
                continue;
            }

            final int vertex1Index = verticesMap.get(edge.vertex1); // First vertex index
            final int vertex2Index = verticesMap.get(edge.vertex2); // Second vertex index

            // Find logic
            final Vertex vertex1 = vertices.get(vertex1Index); // First vertex
            final Vertex vertex2 = vertices.get(vertex2Index); // Second vertex

            // Ensure that {@code vertex1} and {@code vertex2} are not in the same cluster
            if (vertex1.cluster != vertex2.cluster) {

                float vertex1Weight = clusterWeightMap.getOrDefault(vertex1.cluster, 1f); // First vertex weight
                float vertex2Weight = clusterWeightMap.getOrDefault(vertex2.cluster, 1f); // Second vertex weight

                // Calculate tolerance
                final float calculatedTolerance = (float) edge.weight / Math.min(vertex1Weight, vertex2Weight);

                if (calculatedTolerance <= tolerance) {
                    // Merge {@code vertex2.cluster} in {@code vertex1.cluster}
                    // Unify logic
                    if (clusterVertexIndexMap.containsKey(vertex1.cluster)) {
                        if (clusterVertexIndexMap.containsKey(vertex2.cluster)) {
                            // Take all vertices from cluster having vertex2 and merge them in cluster of vertex1
                            ArrayList<Integer> clusterVertex1Indices = clusterVertexIndexMap.get(vertex1.cluster);
                            int vertex2Cluster = vertex2.cluster;
                            for (Integer vIndex : clusterVertexIndexMap.get(vertex2Cluster)) {
                                vertices.get(vIndex).cluster = vertex1.cluster;
                                clusterVertex1Indices.add(vIndex);
                            }
                            clusterVertexIndexMap.put(vertex1.cluster, clusterVertex1Indices);
                            clusterVertexIndexMap.remove(vertex2Cluster);
                            clusterWeightMap.remove(vertex2Cluster);
                        } else {
                            // Add vertex2 in vertex1 cluster
                            vertex2.cluster = vertex1.cluster;
                            ArrayList<Integer> clusterVertexIndices = clusterVertexIndexMap.get(vertex1.cluster);
                            clusterVertexIndices.add(vertex2Index);
                            clusterVertexIndexMap.put(vertex1.cluster, clusterVertexIndices);
                        }
                    } else {
                        if (!clusterVertexIndexMap.containsKey(vertex2.cluster)) {
                            // Add vertex2 in vertex1 cluster
                            // Put cluster 1 in {@code clusterVertexIndexMap}
                            vertex2.cluster = vertex1.cluster;
                            ArrayList<Integer> clusterVertexIndices = new ArrayList<>();
                            clusterVertexIndices.add(vertex1Index);
                            clusterVertexIndices.add(vertex2Index);
                            clusterVertexIndexMap.put(vertex1.cluster, clusterVertexIndices);
                        } else {
                            // Take all vertices from cluster having vertex2 and merge them in cluster of vertex1.
                            // Put cluster 1 in {@code clusterVertexIndexMap}
                            ArrayList<Integer> clusterVertexIndices = new ArrayList<>();
                            clusterVertexIndices.add(vertex1Index);
                            int vertex2Cluster = vertex2.cluster;
                            for (Integer vIndex : clusterVertexIndexMap.get(vertex2.cluster)) {
                                vertices.get(vIndex).cluster = vertex1.cluster;
                                clusterVertexIndices.add(vIndex);
                            }
                            clusterVertexIndexMap.put(vertex1.cluster, clusterVertexIndices);
                            clusterVertexIndexMap.remove(vertex2Cluster);
                            clusterWeightMap.remove(vertex2Cluster);
                        }
                    }

                    // Update max weight in the current cluster
                    float maxWeight = Math.max(edge.weight, Math.max(vertex1Weight, vertex2Weight));
                    clusterWeightMap.put(vertex1.cluster, maxWeight); // Add/update weight of {@code vertex1.cluster}
                } else {
                    // When {@code calculatedTolerance} is greater than {@code tolerance}
                    // do no join the vertices but add in separate clusters if not already added

                    // If {@code vertex1.cluster} is a new cluster, add in
                    // {@code clusterWeightMap} and {@code clusterVertexIndexMap}
                    if (!clusterVertexIndexMap.containsKey(vertex1.cluster)) {
                        ArrayList<Integer> vIndices = new ArrayList<>();
                        vIndices.add(vertex1Index);
                        clusterVertexIndexMap.put(vertex1.cluster, vIndices); // Add {@code vertex1.cluster} as key and vertex index as arraylist
                        clusterWeightMap.put(vertex1.cluster, 1f); // Add {@code vertex1.cluster} as key and {@code 1} as vertex weight
                    }

                    // If {@code vertex2.cluster} is a new cluster, add in
                    // {@code clusterWeightMap} and {@code clusterVertexIndexMap}
                    if (!clusterWeightMap.containsKey(vertex2.cluster)) {
                        ArrayList<Integer> vIndices = new ArrayList<>();
                        vIndices.add(vertex2Index);
                        clusterVertexIndexMap.put(vertex2.cluster, vIndices); // Add {@code vertex2.cluster} as key and vertex index as arraylist
                        clusterWeightMap.put(vertex2.cluster, 1f); // Add {@code vertex2.cluster} as key and {@code 1} as vertex weight
                    }
                }
            }
        }

        // Logic to reset the {@code vertices} array list
        // Create a copy of {@code vertices} array list
        final ArrayList<Vertex> newVertices = verticesCopy();

        // Sort {@code vertices} using {@code Vertex.cluster} and {@code Vertex.vertex}
        Collections.sort(vertices);

        // Group the clusters in set
        Set<Set<String>> clusters = getClusterSets();

        // Retain the original {@code vertices} array
        vertices = newVertices;

        // Return clusters
        return clusters;
    }

    /**
     * {@code Edge} is the model class for each edge between two {@code Vertex} in the graph.
     *
     * @author Dhrumil Amish Shah
     * @version 1.0.0
     * @see VertexCluster
     * @since 2021-02-15
     */
    private static class Edge implements Comparable<Edge> {
        /**
         * {@code vertex1} name of the first vertex/node.
         */
        private final String vertex1;

        /**
         * {@code vertex2} name of the second vertex/node.
         */
        private final String vertex2;

        /**
         * {@code weight} is the edge weight.
         */
        private final int weight;

        /**
         * Class constructor {@code Edge}.
         *
         * @param vertex1 name of the {@code vertex1}
         * @param vertex2 name of the {@code vertex2}
         * @param weight  edge weight
         */
        public Edge(String vertex1, String vertex2, int weight) {
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
            this.weight = weight;
        }

        /**
         * Sort edges by {@code weight}, {@code vertex1} and {@code vertex2}.
         *
         * @param edge compared edge
         * @return {@code 1} if to be swapped otherwise {@code -1}
         */
        @Override
        public int compareTo(Edge edge) {
            if (edge.weight == NO_WEIGHT) {
                return 1;
            } else if (this.weight - edge.weight > 0) {
                return 1;
            } else if (this.weight - edge.weight == 0) {
                if (this.vertex1.compareToIgnoreCase(edge.vertex1) > 0) {
                    return 1;
                } else if (this.vertex1.compareToIgnoreCase(edge.vertex1) == 0) {
                    if (this.vertex2.compareToIgnoreCase(edge.vertex2) > 0) {
                        return 1;
                    }
                }
            }
            return -1;
        }
    }

    /**
     * {@code Vertex} is the model class for each vertex in the graph.
     *
     * @author Dhrumil Amish Shah
     * @version 1.0.0
     * @see VertexCluster
     * @since 2021-02-15
     */
    private static class Vertex implements Comparable<Vertex> {
        /**
         * {@code vertex} name of the vertex/node.
         */
        private final String vertex;

        /**
         * {@code cluster} number in which the {@code vertex} belongs.
         * In other words, a group number
         */
        private int cluster;

        /**
         * Class constructor {@code Vertex}.
         *
         * @param vertex  name of the {@code vertex}
         * @param cluster cluster number in which the {@code vertex} belongs
         */
        public Vertex(String vertex, int cluster) {
            this.vertex = vertex;
            this.cluster = cluster;
        }

        /**
         * Sort vertices by {@code cluster} and {@code vertex}.
         *
         * @param vertex compared vertex
         * @return {@code 1} if to be swapped otherwise {@code -1}
         */
        @Override
        public int compareTo(Vertex vertex) {
            if (this.cluster > vertex.cluster) {
                return 1;
            } else if (this.cluster == vertex.cluster) {
                if (this.vertex.compareToIgnoreCase(vertex.vertex) > 0) {
                    return 1;
                }
            }
            return -1;
        }
    }
}