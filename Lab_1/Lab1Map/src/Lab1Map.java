/**
 * Lab1Map class to test the functionality of MyMap<K, V> class
 *
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @see MyMap class
 * @see MyMapInterface class
 * @see MyMapEntry class
 * @since 2021-01-14
 */
public class Lab1Map {
    public static void main(String[] args) {

        // map object that will hold key-value pairs
        MyMap<Integer, String> map = new MyMap<>();

        // Test1: Print whether the map is empty or not. Since it is called before adding any elements, it will return true
        System.out.println("\n<========== TEST 1 ==========>");
        System.out.println("map is empty? " + map.isEmpty()); // true, map is empty

        // Test 2: Print initial size and capacity of the map. Intially size will be 0 and capacity will be 16
        System.out.println("\n<========== TEST 2 ==========>");
        System.out.println("Size: " + map.size()); // 0
        System.out.println("Capacity: " + map.capacity()); // 16

        // Test 3: Insert an element in map and then print whether the map is empty or not. Now it will return false
        System.out.println("\n<========== TEST 3 ==========>");
        System.out.println("map.put(8, \"Kotlin\"): " + map.put(8, "Kotlin")); // true, key is unique so inserted
        System.out.println("map is empty? " + map.isEmpty()); // false, map is not empty

        // Test 4: Insert multiple elements in the map with unique keys
        System.out.println("\n<========== TEST 4 ==========>");
        System.out.println("map.put(4, \"Java\"): " + map.put(4, "Java")); // true, key is unique so inserted
        System.out.println("map.put(5, \"C++\"): " + map.put(5, "C++")); // true, key is unique so inserted
        System.out.println("map.put(32, \"C Lang\"): " + map.put(32, "C Lang")); // true, key is unique so inserted

        // Test 5: Insert elements with key that exists in the map. put() will return false
        System.out.println("\n<========== TEST 5 ==========>");
        System.out.println("map.put(8, \"Android\"): " + map.put(8, "Android")); // false, key exists in list
        System.out.println("map.put(32, \"Haskel\"): " + map.put(32, "Haskel")); // false, key exists in list

        // Test 6: Print size and capacity of the map. Since 4 elements are inserted, size will be 4 but capacity will be 16
        // capacity will only increase when (size/capacity) > load_factor(0.75 here)
        System.out.println("\n<========== TEST 6 ==========>");
        System.out.println("Size: " + map.size()); // 4
        System.out.println("Capacity: " + map.capacity()); // 16

        // Test 7: Insert multiple elements in the map with unique keys
        // Same value is allowed but same key is not allowed
        System.out.println("\n<========== TEST 7 ==========>");
        System.out.println("map.put(1, \"Cobol\"): " + map.put(1, "Cobol")); // true, key is unique so inserted
        System.out.println("map.put(2, \"Pascal\"): " + map.put(2, "Pascal")); // true, key is unique so inserted
        System.out.println("map.put(3, \"Basic\"): " + map.put(3, "Basic")); // true, key is unique so inserted
        System.out.println("map.put(6, \"Fortran\"): " + map.put(6, "Fortran")); // true, key is unique so inserted
        System.out.println("map.put(3, \"Python\"): " + map.put(7, "Python")); // true, key is unique so inserted
        System.out.println("map.put(6, \"C Lang\"): " + map.put(9, "C Lang")); // true, key is unique so inserted even though value is same
        System.out.println("map.put(3, \"Haskel\"): " + map.put(10, "Haskel")); // true, key is unique so inserted
        System.out.println("map.put(6, \"Dart\"): " + map.put(11, "Dart")); // true, key is unique so inserted

        // Test 8: Print size and capacity of the map. Since 12 elements are inserted, size will be 12 but capacity will be 16
        // capacity will only increase when (size/capacity) > load_factor(0.75 here)
        // Still, the ratio size/capacity(0.75) = load_factor(0.75) so no increase in capacity
        System.out.println("\n<========== TEST 8 ==========>");
        System.out.println("Size: " + map.size()); // 12
        System.out.println("Capacity: " + map.capacity()); //16

        // Test 9: Print size and capacity of the map after insertion of one more element.
        // Since 13 elements are inserted, size will be 13 but capacity will be 32.
        // Due to addition of 13th element, capacity increased because size/capacity(0.8125) > load_factor(0.75 here)
        // Thus, capacity is now doubled (i.e from 16 to 32).
        System.out.println("\n<========== TEST 9 ==========>");
        System.out.println("map.put(12, \"JS\"): " + map.put(12, "JS")); // true, key is unique so inserted
        System.out.println("Size: " + map.size()); // 13
        System.out.println("Capacity: " + map.capacity()); //32

        // Test 10: Get value for the keys that exists
        System.out.println("\n<========== TEST 10 ==========>");
        System.out.println("map.get(3): " + map.get(3)); // Basic
        System.out.println("map.get(9): " + map.get(9)); // C Lang, different key but same value
        System.out.println("map.get(32): " + map.get(32)); // C Lang, different key but same value
        System.out.println("map.get(12): " + map.get(12)); // JS

        // Test 11: For keys that does not exists, return null
        System.out.println("\n<========== TEST 11 ==========>");
        System.out.println("map.get(94): " + map.get(94)); // null, key does not exists
        System.out.println("map.get(15): " + map.get(15)); // null, key does not exists

        // Test 12: Enter null key
        // Null keys are allowed
        System.out.println("\n<========== TEST 12 ==========>");
        System.out.println("map.put(null, \"null key allowed\"): " + map.put(null, "null_key_value")); // true, key is unique so inserted (null key is allowed)
        System.out.println("map.put(null, \"null value\"): " + map.put(null, "null_value")); // false, key exists in list

        // Test 13: Get value for the inserted null key
        System.out.println("\n<========== TEST 13 ==========>");
        System.out.println("map.get(null): " + map.get(null)); // "null_key_value"
    }
}