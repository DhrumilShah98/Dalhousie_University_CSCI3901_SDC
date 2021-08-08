/**
 * SearchTreeInterface provides the underlying structure for the SearchTree class
 *
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @see SearchTree
 * @since 2021-01-31
 */
public interface SearchTreeInterface {
    /**
     * Add the key to this tree and set initial search count to {@code 0}
     *
     * @param key key to add
     * @return {@code true} if key is added otherwise {@code false}
     */
    boolean add(String key);

    /**
     * Search for key in this tree and if found, increment the search counter and
     * balance the tree for better search results in later queries. Move items that are
     * search frequently closer to the root of this tree so that they can be found more quickly in later searches
     *
     * @param key key to search
     * @return depth of the node in this tree otherwise {@code 0}
     * @implSpec The default implementation will consider root node at depth {@code 1}
     */
    int find(String key);

    /**
     * Reset all the search counts in this tree to {@code 0}
     */
    void reset();

    /**
     * Creates a string of this tree's content
     *
     * @return string representation of this tree's content. If any error occurs, return {@code null}
     */
    String printTree();
}