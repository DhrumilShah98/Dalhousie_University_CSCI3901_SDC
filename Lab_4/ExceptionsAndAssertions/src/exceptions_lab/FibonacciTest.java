package exceptions_lab;

public class FibonacciTest {
    public static void main(String[] args) {
        // Fibonacci Sequence
        // fibo(n): 1 1 2 3 5 8 13 21 34 55 89 144 ...
        // n:       1 2 3 4 5 6  7  8  9 10 11  12 ...

        Fibonacci fibonacci = new Fibonacci();

        try {
            System.out.println("12th Fibonacci number is: " + fibonacci.fibo(12, 15)); // 144
        } catch (MaximumRecursionDepth e) {
            System.out.println(e.getMessage() + ": " + e.getDepth());
        }

        try {
            System.out.println("12th Fibonacci number is: " + fibonacci.fibo(12, 10)); // Exception
        } catch (MaximumRecursionDepth e) {
            System.out.println(e.getMessage() + ": " + e.getDepth());
        }

        try {
            System.out.println("12th Fibonacci number is: " + fibonacci.fibo(12, 11)); // Exception
        } catch (MaximumRecursionDepth e) {
            System.out.println(e.getMessage() + ": " + e.getDepth());
        }

        try {
            System.out.println("12th Fibonacci number is: " + fibonacci.fibo(12, 12)); // 144
        } catch (MaximumRecursionDepth e) {
            System.out.println(e.getMessage() + ": " + e.getDepth());
        }
    }
}