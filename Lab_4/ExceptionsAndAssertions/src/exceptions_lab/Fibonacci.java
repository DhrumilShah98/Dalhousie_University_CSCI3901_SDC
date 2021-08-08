package exceptions_lab;

/**
 * Fibonacci class to calculate nth Fibonacci number
 *
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @since 2021-02-04
 */
public class Fibonacci {
    /**
     * Calculate nth Fibonacci number
     *
     * @param n               number n
     * @param maxDepthAllowed max recursion depth allowed
     * @return nth fibonacci number
     * @throws MaximumRecursionDepth when current depth exceeds the max depth allowed
     */
    public int fibo(int n, int maxDepthAllowed) throws MaximumRecursionDepth {
        return fibonacci(n, maxDepthAllowed, 0);
    }

    /**
     * Calculate nth Fibonacci number
     *
     * @param n               number n
     * @param maxDepthAllowed max recursion depth allowed
     * @param currentDepth    current depth which increases with recursive calls
     * @return nth fibonacci number
     * @throws MaximumRecursionDepth when current depth exceeds the max depth allowed
     */
    private int fibonacci(int n, int maxDepthAllowed, int currentDepth) throws MaximumRecursionDepth {
        if (n < 0) {
            return -1;
        } else if (n == 0) {
            currentDepth++;
            if (currentDepth > maxDepthAllowed)
                throw new MaximumRecursionDepth("Maximum recursion depth exceeds from allowed depth " + maxDepthAllowed, currentDepth);
            return 0;
        } else if (n == 1) {
            currentDepth++;
            if (currentDepth > maxDepthAllowed)
                throw new MaximumRecursionDepth("Maximum recursion depth exceeds from allowed depth " + maxDepthAllowed, currentDepth);
            return 1;
        } else {
            currentDepth++;
            if (currentDepth > maxDepthAllowed)
                throw new MaximumRecursionDepth("Maximum recursion depth exceeds from allowed depth " + maxDepthAllowed, currentDepth);
            return fibonacci(n - 1, maxDepthAllowed, currentDepth) + fibonacci(n - 2, maxDepthAllowed, currentDepth);
        }
    }
}