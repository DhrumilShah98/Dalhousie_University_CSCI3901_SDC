/**
 * The Ackermann function is a total, computable function which is not primitive recursive.
 * Specifically, this means the Ackermann function can only be programmed in a Turing-complete
 * language like Java.
 *
 * The function is recursive and is defined by the following equations:
 *   A(0, n)         = n + 1
 *   A(m + 1, 0)     = A(m, 1)
 *   A(m + 1, n + 1) = A(m, A(m + 1, n))
 */
public class Ackermann {

    /** Computes the Ackermann function */
    public static int ackermann(int m, int n) {
        if (m == 0) {
            return n + 1;
        } else if (n == 0) {
            return ackermann(m - 1, 1);
        } else {
            return ackermann(m - 1, ackermann(m, n - 1)); // Solution: ackermann(m, n - 1) instead of ackermann(m, n)
        }
    }

    public static void main(String[] args) {

        // Test cases
        System.out.println(ackermann(0, 0));  // Should be 1
        System.out.println(ackermann(0, 53)); // Should be 54
        System.out.println(ackermann(2, 13)); // Should be 29
        System.out.println(ackermann(2, 0));  // Should be 3
    }
}
