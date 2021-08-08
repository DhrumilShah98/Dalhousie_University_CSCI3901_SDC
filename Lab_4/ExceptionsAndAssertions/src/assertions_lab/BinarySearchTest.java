package assertions_lab;

public class BinarySearchTest {
    public static void main(String[] args) {
        int[] sortedArr = {2, 5, 8, 12, 16, 23, 38, 56, 72, 91};

        BinarySearch bs = new BinarySearch();
        // Pass
        System.out.println("\nIndex of element 2: " + bs.binarySearch(2, sortedArr));
        System.out.println("Index of element 5: " + bs.binarySearch(5, sortedArr));
        System.out.println("Index of element 8: " + bs.binarySearch(8, sortedArr));
        System.out.println("Index of element 12: " + bs.binarySearch(12, sortedArr));
        System.out.println("Index of element 16: " + bs.binarySearch(16, sortedArr));
        System.out.println("Index of element 23: " + bs.binarySearch(23, sortedArr));
        System.out.println("Index of element 38: " + bs.binarySearch(38, sortedArr));
        System.out.println("Index of element 56: " + bs.binarySearch(56, sortedArr));
        System.out.println("Index of element 72: " + bs.binarySearch(72, sortedArr));
        System.out.println("Index of element 91: " + bs.binarySearch(91, sortedArr));

        // Fail
        System.out.println("\nIndex of element 1: " + bs.binarySearch(1, sortedArr));
        System.out.println("Index of element 3: " + bs.binarySearch(3, sortedArr));
        System.out.println("Index of element 6: " + bs.binarySearch(6, sortedArr));
        System.out.println("Index of element 9: " + bs.binarySearch(9, sortedArr));
        System.out.println("Index of element 14: " + bs.binarySearch(14, sortedArr));
        System.out.println("Index of element 20: " + bs.binarySearch(20, sortedArr));
        System.out.println("Index of element 27: " + bs.binarySearch(27, sortedArr));
        System.out.println("Index of element 45: " + bs.binarySearch(45, sortedArr));
        System.out.println("Index of element 66: " + bs.binarySearch(66, sortedArr));
        System.out.println("Index of element 80: " + bs.binarySearch(80, sortedArr));
        System.out.println("Index of element 100: " + bs.binarySearch(100, sortedArr));
    }
}
