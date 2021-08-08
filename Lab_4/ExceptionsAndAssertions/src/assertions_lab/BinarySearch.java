package assertions_lab;

/**
 * BinarySearch class
 *
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @since 2021-02-04
 */
public class BinarySearch {
    // Loop invariants for binary search are as follow:
    // 1.) array has to be sorted in ascending order
    // 2.) start <= end, so we can terminate when start > end
    // 3.) start <= mid <= end

    /**
     * Gets the index of element if it exists in sortedArr
     *
     * @param element   element entered
     * @param sortedArr array in which element is to be found
     * @return index at which element exists otherwise {@code -1}
     */
    public int binarySearch(int element, int[] sortedArr) {
        int start = 0;
        int end = sortedArr.length - 1;

        // Loop preconditions
        assert (start == end || start < end);   // start <= end
        assert (isSorted(sortedArr)); // array is always sorted

        while (start <= end) {  // When start > end, terminate
            int mid = start + (end - start) / 2;

            // Loop invariants
            assert (0 == start || 0 < start); // 0 <= start
            assert (end == (sortedArr.length - 1) || end < (sortedArr.length - 1)); // end <= (sortedArr.length - 1)
            assert (start == mid || start < mid); // start <= mid
            assert (mid == end || mid < end); // mid <= end

            if (sortedArr[mid] == element) {
                return mid; // Return mid, element found
            } else if (sortedArr[mid] < element) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }

        // Loop postconditions
        assert (isSorted(sortedArr)); // makes sure array is not changed in the loop
        return -1; // Return -1, element not found
    }

    /**
     * Checks if array is sorted
     *
     * @param arr array of elements
     * @return {@code true} if array is sorted otherwise {@code false}
     */
    private boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; ++i) {
            if (arr[i - 1] > arr[i]) return false;
        }
        return true;
    }
}