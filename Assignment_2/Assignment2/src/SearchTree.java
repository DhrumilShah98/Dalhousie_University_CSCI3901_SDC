/**
 * SearchTree is a tree implementation of the SearchTreeInterface
 *
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @see SearchTreeInterface
 * @see Node
 * @since 2021-01-31
 */
public class SearchTree implements SearchTreeInterface {

    /**
     * Pointer/Reference to the root node of this tree
     */
    private Node rootNode;

    /**
     * Class constructor SearchTree
     */
    public SearchTree() {
        this.rootNode = null;
    }

    /**
     * Add the key to this tree and set initial search count to {@code 0}
     *
     * @param key key to add
     * @return {@code true} if key is added otherwise {@code false}
     */
    @Override
    public boolean add(String key) {
        // Return {@code false} if key is null or empty
        if (key == null || key.isEmpty()) return false;

        // If root node is null, create a new root node with the key and return {@code true}
        if (rootNode == null) {
            rootNode = new Node(key, null, null, null);
            return true; // Key successfully added
        }

        // Return {@code true} if key is added otherwise {@code false}
        return add(key, rootNode);
    }

    /**
     * Traverse this tree to add key if not added already
     *
     * @param key  key to add
     * @param node node in process
     * @return {@code true} if key is added otherwise {@code false}
     */
    private boolean add(String key, Node node) {
        // If key is equal to node.key, comparisonOp is {@code 0}
        // If key is less than node.key, comparisonOp is a negative number (lexicographically)
        // If key is greater than node.key, comparisonOp is a positive number (lexicographically)
        // Comparison IS NOT case sensitive
        int comparisonOp = key.toLowerCase().compareTo(node.getKey().toLowerCase());
        if (comparisonOp < 0) {
            // If current node's left child is not null, process it
            // Otherwise add key as current node's left child and return {@code true}
            if (node.getLeftChild() != null) {
                return add(key, node.getLeftChild());
            } else {
                Node newLeftNode = new Node(key, node, null, null);
                node.setLeftChild(newLeftNode);
                return true;
            }
        } else if (comparisonOp > 0) {
            // If current node's right child is not null, process it
            // Otherwise add key as current node's right child and return {@code true}
            if (node.getRightChild() != null) {
                return add(key, node.getRightChild());
            } else {
                Node newRightChild = new Node(key, node, null, null);
                node.setRightChild(newRightChild);
                return true;
            }
        } else {
            // Return {@code false} if key exists in this tree
            return false;
        }
    }

    /**
     * Search for key in this tree and if found, increment the search counter and
     * balance the tree for better search results in later queries. Move items that are
     * search frequently closer to the root of this tree so that they can be found more quickly in later searches
     *
     * @param key key to search
     * @return depth of the node in this tree otherwise {@code 0}
     * @implSpec The default implementation will consider root node at depth {@code 1}
     */
    @Override
    public int find(String key) {
        // Return false if key is null or empty
        if (key == null || key.isEmpty()) return 0;

        // Return null if tree is empty
        if (rootNode == null) return 0;

        // Return depth of the node in this tree otherwise {@code 0}
        return find(key, rootNode, 1);
    }

    /**
     * Traverse this tree and find the key
     *
     * @param key   key to search
     * @param node  node in process
     * @param depth depth of node
     * @return depth of the node in this tree otherwise {@code 0}
     */
    private int find(String key, Node node, int depth) {
        // If key is equal to node.key, comparisonOp is {@code 0}
        // If key is less than node.key, comparisonOp is a negative number (lexicographically)
        // If key is greater than node.key, comparisonOp is a positive number (lexicographically)
        // Comparison IS NOT case sensitive
        int comparisonOp = key.toLowerCase().compareTo(node.getKey().toLowerCase());
        if (comparisonOp < 0) {
            // If current node's left child is not null, process it
            // Otherwise return {@code 0}
            if (node.getLeftChild() != null) return find(key, node.getLeftChild(), depth + 1);
            else return 0;
        } else if (comparisonOp > 0) {
            // If current node's right child is not null, process it
            // Otherwise return {@code 0}
            if (node.getRightChild() != null) return find(key, node.getRightChild(), depth + 1);
            else return 0;
        } else {
            // Key found
            // Increment search count
            node.incrementSearchCount();

            // Reshape tree if possible
            reshapeTree(node, node.getParent());

            // Return depth if key exists in this tree
            return depth;
        }
    }

    /**
     * Reshape this tree for better search results in later queries
     *
     * @param node   node in process
     * @param parent parent node of node in process
     */
    private void reshapeTree(Node node, Node parent) {
        // Return {@code false} if parent is null or search count of the current node is less or equal to
        // parent node search count
        if (parent == null || node.getSearchCount() <= parent.getSearchCount()) return;

        // If node.key is equal to parent.key, comparisonOp is {@code 0}
        // If node.key is less than parent.key, comparisonOp is a negative number (lexicographically)
        // If node.key is greater than parent.key, comparisonOp is a positive number (lexicographically)
        // Comparison IS NOT case sensitive
        int comparisonOp = node.getKey().toLowerCase().compareTo(parent.getKey().toLowerCase());

        // If comparisonOp is less than {@code 0}, perform RR (Right Rotation)
        // If comparisonOp is greater than {@code 0}, perform LR (Left Rotation)
        if (comparisonOp < 0) {
            parent.setLeftChild(node.getRightChild());
            if (node.getRightChild() != null) node.getRightChild().setParent(parent);
            node.setRightChild(parent);
            if (parent.getParent() != null) {
                node.setParent(parent.getParent());
                if (parent.getParent().getRightChild() != null && parent.getParent().getRightChild().getKey().equalsIgnoreCase(parent.getKey()))
                    parent.getParent().setRightChild(node); // parent node is right of it's parent node
                else
                    parent.getParent().setLeftChild(node); // parent node is left of it's parent node
            } else {
                rootNode = node;
                rootNode.setParent(null);
            }
            parent.setParent(node);
        } else if (comparisonOp > 0) {
            parent.setRightChild(node.getLeftChild());
            if (node.getLeftChild() != null) node.getLeftChild().setParent(parent);
            node.setLeftChild(parent);
            if (parent.getParent() != null) {
                node.setParent(parent.getParent());
                if (parent.getParent().getRightChild() != null && parent.getParent().getRightChild().getKey().equalsIgnoreCase(parent.getKey()))
                    parent.getParent().setRightChild(node); // parent node is right of it's parent node
                else
                    parent.getParent().setLeftChild(node); // parent node is left of it's parent node
            } else {
                rootNode = node;
                rootNode.setParent(null);
            }
            parent.setParent(node);
        }
    }

    /**
     * Reset all the search counts in this tree to {@code 0}
     */
    @Override
    public void reset() {
        reset(rootNode);
    }

    /**
     * Traverse this tree and reset search counter to {@code 0} for each node
     * Inorder Tree Traversal
     *
     * @param node node in process
     */
    private void reset(Node node) {
        if (node != null) {
            reset(node.getLeftChild());
            node.resetSearchCount();
            reset(node.getRightChild());
        }
    }

    /**
     * Creates a string of this tree's content
     * Root node is at depth {@code 1}
     *
     * @return string representation of this tree's content. If any error occurs, return {@code null}
     */
    @Override
    public String printTree() {
        StringBuilder treeBuilder = new StringBuilder();
        return traverseTreeInorder(treeBuilder, rootNode, 1).toString();
    }

    /**
     * Traverse this tree and create a string representation of this tree's content
     * Inorder Tree Traversal
     *
     * @param stringBuilder string builder to build string gradually
     * @param node          node in process
     * @param depth         depth of the node in process
     * @return string builder representation of this tree's content
     */
    private StringBuilder traverseTreeInorder(StringBuilder stringBuilder, Node node, int depth) {
        if (node != null) {
            traverseTreeInorder(stringBuilder, node.getLeftChild(), depth + 1);
            stringBuilder.append(node.getKey()).append(" ").append(depth).append("\n");
            traverseTreeInorder(stringBuilder, node.getRightChild(), depth + 1);
        }
        return stringBuilder;
    }

    /**
     * Node class acts as a single object/entry/model in the SearchTree class
     *
     * @author Dhrumil Amish Shah
     * @version 1.0.0
     * @see SearchTree
     * @see SearchTreeInterface
     * @since 2021-01-31
     */
    private static class Node {
        /**
         * Data/Key in this node
         */
        private final String key;

        /**
         * Counter to keep track of data/key searches
         * Default value is {@code 0}
         */
        private Long searchCount;

        /**
         * Pointer/Reference to this node's parent node
         */
        private Node parent;

        /**
         * Pointer/Reference to this node's left child node
         */
        private Node leftChild;

        /**
         * Pointer/Reference to this node's right child node
         */
        private Node rightChild;

        /**
         * Class constructor Node
         *
         * @param key        data
         * @param parent     reference to the parent node
         * @param leftChild  reference to the left child node
         * @param rightChild reference to the right child node
         */
        public Node(String key, Node parent, Node leftChild, Node rightChild) {
            this.key = key;
            this.searchCount = 0L; // Initial value is 0
            this.parent = parent;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        /**
         * Gets the data/key of this Node
         *
         * @return this Node's data/key
         */
        public String getKey() {
            return this.key;
        }

        /**
         * Gets the search count of this Node
         *
         * @return this Node's search count
         */
        public Long getSearchCount() {
            return this.searchCount;
        }

        /**
         * Gets the parent node pointer/reference of this Node
         *
         * @return this Node's parent pointer/reference node
         */
        public Node getParent() {
            return this.parent;
        }

        /**
         * Gets the left child node pointer/reference of this Node
         *
         * @return this Node's left child node pointer/reference
         */
        public Node getLeftChild() {
            return this.leftChild;
        }

        /**
         * Gets the right child node pointer/reference of this Node
         *
         * @return this Node's right child node pointer/reference
         */
        public Node getRightChild() {
            return this.rightChild;
        }

        /**
         * Sets the parent node pointer/reference of this Node
         *
         * @param parent this Node's parent pointer/reference node
         */
        public void setParent(Node parent) {
            this.parent = parent;
        }

        /**
         * Sets the left child node pointer/reference of this Node
         *
         * @param leftChild this Node's left child pointer/reference node
         */
        public void setLeftChild(Node leftChild) {
            this.leftChild = leftChild;
        }

        /**
         * Sets the right child node pointer/reference of this Node
         *
         * @param rightChild this Node's right child pointer/reference node
         */
        public void setRightChild(Node rightChild) {
            this.rightChild = rightChild;
        }

        /**
         * Increments the search counter of this Node
         */
        public void incrementSearchCount() {
            if (this.searchCount == Long.MAX_VALUE) return;
            this.searchCount++;
        }

        /**
         * Resets the search counter to {@code 0} of this Node
         */
        public void resetSearchCount() {
            this.searchCount = 0L;
        }
    }
}