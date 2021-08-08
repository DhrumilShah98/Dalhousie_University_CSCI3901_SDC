/**
 * An implementation of a linked list
 * @param <T> The type of objects to store
 */
class LinkedList<T> {

    /** The root of the list */
    private Node<T> root;

    /** Adds an item to the head of the list */
    public void add(T item) {
        root = new Node<T>(item, root);
    }

    // Solution: Method to add elements to the end of the list.
    // New method to add an item at the end of the list
    public void addLast(T item){
        if(root == null){
            root = new Node<T>(item, null);
        } else {
            Node<T> tmp = root;
            while (tmp.tail != null){
                tmp = tmp.tail;
            }
            tmp.tail = new Node<>(item, null);
        }
    }

    /** Removes the head of the list if non-empty, does nothing otherwise */
    public void remove() {
        if (root != null) {
            root = root.tail;
        }
    }

    /** Add an item to the head of the list */
    public void append(LinkedList<T> list) {
        if (root == null) {
            root = list.root;
        } else {
            Node<T> tmp = root;

            // Iterate through the list to find the end
            // Solution: Instead of tmp != null, do tmp.tail != null to reach the last element of the list.
            while (tmp.tail != null) {
                tmp = tmp.tail;
            }

            // Assign the end of the list
            // Solution: Instead of tmp = list.root, assign second list root to first list tail (tmp.tail = list.root)
            tmp.tail = list.root;
        }
    }

    /** Copies a linked list */
    public LinkedList<T> copy() {
        LinkedList<T> tmp = new LinkedList<T>();

        // Copies the entire list
        // tmp.root = this.root.copy(); // Solution: This line creates a shallow copy of list. Hence removed

        // Solution: New logic added to create a deep copy of list (Completely new list in memory)
        Node<T> tmpNode = this.root;
        while (tmpNode != null) {
            tmp.addLast(tmpNode.head);
            tmpNode = tmpNode.tail;
        }

        return tmp;
    }

    /** Prints the contents of a list */
    public void print() {
        Node<T> tmp = root;

        // Iterate through the list
        while (tmp != null) {
            System.out.print(tmp.head);
            tmp = tmp.tail;
        }

        // Print a new line
        System.out.println();
    }

    /**
     * Private internal node class storing the actual linked list
     * @param <T> The type of objects to store
     */
    private static class Node<T> {
        public T head;
        public Node<T> tail;

        // Singleton list constructor
        public Node(T head) {
            this.head = head;
            this.tail = null;
        }

        // Standard "cons" constructor
        public Node(T head, Node<T> tail) {
            this.head = head;
            this.tail = tail;
        }

        // Creates a copy of the node
        public Node<T> copy() {
            Node<T> tmp = new Node<T>(this.head);

            tmp.head = this.head;
            if (this.tail == null) {
                tmp.tail = null;
            } else {
                tmp.tail = this.tail;
            }

            return tmp;
        }

    }
}

/**
 * Main program
 */
public class LinkedListTest {

    public static void main(String[] args) {

        LinkedList<Integer> lst1 = new LinkedList<Integer>();

        // Populate a list
        for (int i = 0; i < 10; i++) {
            lst1.add(i);
        }
        LinkedList<Integer> lst2 = lst1.copy();
        lst2.append(lst1);

        // Test cases
        lst1.print(); // Should be 9876543210
        lst2.print(); // Should be 98765432109876543210

    }
}
