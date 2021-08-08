import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

/**
 * MyMap<K, V> is a Map implementation of MyMapInterface<K, V>
 *
 * @param <K> Key
 * @param <V> Value
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @see MyMapInterface class
 * @see MyMapEntry class
 * @since 2021-01-14
 */
public class MyMap<K, V> implements MyMapInterface<K, V> {

    /**
     * The size tells number of key-value pairs we have in MyMap<K, V> class
     */
    private Integer size;

    /**
     * The capacity is the number of cells/buckets in the map
     */
    private Integer capacity;
    /**
     * The DEFAULT_CAPACITY is the default capacity of MyMap<K, V> class
     */
    private static final int DEFAULT_CAPACITY = 16;
    /**
     * The LOAD_FACTOR decides when to increase number of cells/buckets in the map.
     */
    public static Double LOAD_FACTOR = 0.75;
    /**
     * The map that will contain all key-value pairs
     */
    private List<List<MyMapEntry<K, V>>> myMap;

    /**
     * Class constructor MyMap
     */
    public MyMap() {
        // Set initial size to 0, no elements present
        size = 0;

        // Set initial capacity equal to the default capacity
        capacity = DEFAULT_CAPACITY;

        // Create a new map object
        myMap = new ArrayList<>(capacity);

        // Assign all cells/buckets to 'null' initially
        for (int i = 0; i < capacity; ++i) {
            myMap.add(null);
        }
    }

    /**
     * Gets the size of the map
     *
     * @return size of the map
     */
    public int size() {
        return size;
    }

    /**
     * Gets the capacity of the map
     *
     * @return capacity of the map
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Checks if Map is empty of not. If Map has no elements, its empty
     *
     * @return true if Map is empty otherwise false
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Gets the hashed value of the key
     *
     * @param key key
     * @return hashed value, between [0, capacity)
     */
    private int getHash(K key) {
        if (key == null) return Objects.hash(key) % capacity;
        return key.hashCode() % capacity;
    }

    /**
     * Gets the value associated with the key
     *
     * @param key key
     * @return value associated with the key otherwise null
     */
    @Override
    public V get(K key) {
        // Get hashed value of the key, index in range [0, capacity)
        int keyHashed = getHash(key);

        // If list is null or empty, it indicates that key is not present, return null
        List<MyMapEntry<K, V>> currentMyMapEntryList = myMap.get(keyHashed);
        if (currentMyMapEntryList == null || currentMyMapEntryList.isEmpty()) {
            return null; // or we can also throw an Exception like NoSuchElementException("Key " + key.toString() + " does not exists.")
        }

        // Iterate through all the entries and compare the key with the given key and if it matches, return its value
        for (MyMapEntry<K, V> currentMyMapEntry : currentMyMapEntryList) {
            if (currentMyMapEntry.getKey() == key || currentMyMapEntry.getKey().equals(key))
                return currentMyMapEntry.getValue();
        }

        // If no key exists, return null
        return null; // or we can also throw an Exception like NoSuchElementException("Key " + key.toString() + " does not exists.")
    }

    /**
     * Put/Add key-value pair
     *
     * @param key   key
     * @param value value
     * @return true if key-value pair is added otherwise false
     */
    @Override
    public boolean put(K key, V value) {
        // Get hashed value of the key, index in range [0, capacity)
        int keyHashed = getHash(key);

        // Check if the key exists already and if it does, return false
        List<MyMapEntry<K, V>> currentMyMapEntryList = myMap.get(keyHashed);
        if (currentMyMapEntryList != null && !currentMyMapEntryList.isEmpty()) {
            for (MyMapEntry<K, V> currentMyMapEntry : currentMyMapEntryList) {
                if (currentMyMapEntry.getKey() == key || currentMyMapEntry.getKey().equals(key)) return false;
            }
        }

        // Key is unique, add it in the map and return true
        if (currentMyMapEntryList == null) {
            currentMyMapEntryList = new ArrayList<>();
            myMap.set(keyHashed, currentMyMapEntryList);
        }
        currentMyMapEntryList.add(new MyMapEntry<>(key, value));
        size++;
        ensureCapacity();
        return true;
    }

    /**
     * Method to make sure capacity is ensured
     * When size/capacity > load_factor, double the capacity and rehash all the entries
     */
    private void ensureCapacity() {
        if (((size / Double.valueOf(capacity)) > LOAD_FACTOR)) {
            // Double the capacity
            capacity = capacity * 2;

            //Set size to 0 before rehash
            size = 0;

            // Old map copy
            List<List<MyMapEntry<K, V>>> myOldMap = myMap;

            // Create a new map that will contain all key-value pairs
            List<List<MyMapEntry<K, V>>> myNewMap = new ArrayList<>(capacity);

            // Assign all cells/buckets to 'null' initially
            for (int i = 0; i < capacity; ++i) {
                myNewMap.add(null);
            }

            // Make original map point to the new map
            myMap = myNewMap;

            // Rehash all the keys
            for (List<MyMapEntry<K, V>> myOldMapEntryList : myOldMap) {
                if (myOldMapEntryList != null && !myOldMapEntryList.isEmpty()) {
                    for (MyMapEntry<K, V> currentMyMapEntry : myOldMapEntryList) {
                        put(currentMyMapEntry.getKey(), currentMyMapEntry.getValue());
                    }
                }
            }

            // Make temporary variables 'null' for garbage collection
            myOldMap = null;
            myNewMap = null;
        }
    }
}