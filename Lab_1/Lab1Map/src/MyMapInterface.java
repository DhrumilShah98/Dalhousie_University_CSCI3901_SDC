/**
 * MyMapInterface<K, V> provides the underlying structure for the MyMap<K, V> class
 *
 * @param <K> key
 * @param <V> value
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @see MyMap class
 * @see MyMapEntry class
 * @since 2021-01-14
 */
public interface MyMapInterface<K, V> {

    /**
     * Gets the value associated with the key
     *
     * @param key key
     * @return value associated with the key otherwise null
     */
    public V get(K key);

    /**
     * Put/Add key-value pair
     *
     * @param key   key
     * @param value value
     * @return true if key-value pair is added otherwise false
     */
    public boolean put(K key, V value);
}