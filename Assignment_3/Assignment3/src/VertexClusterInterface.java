import java.util.Set;

/**
 * {@code VertexClusterInterface} provides the underlying structure for the {@code VertexCluster} class.
 *
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @see VertexCluster
 * @since 2021-02-15
 */
public interface VertexClusterInterface {
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
    boolean addEdge(String vertex1, String vertex2, int weight);

    /**
     * Creates a set of clusters from the graph using given {@code tolerance} parameter.
     * {@code tolerance} decides whether to merge vertices or not.
     *
     * @param tolerance given {@code tolerance} value
     * @return set of clusters otherwise {@code null}
     */
    Set<Set<String>> clusterVertices(float tolerance);
}