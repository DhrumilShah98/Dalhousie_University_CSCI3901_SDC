import java.util.Objects;

/**
 * MyMapEntry<K, V> is a generic class that acts as a single object/entry/model in MyMap<K, V> class
 *
 * @param <K> key
 * @param <V> value
 * @author Dhrumil Amish Shah
 * @version 1.0.0
 * @see MyMap class
 * @see MyMapInterface class
 * @since 2021-01-14
 */
public class MyMapEntry<K, V> {
    /**
     * The key is used to identify each value uniquely
     */
    private K key;

    /**
     * The value is the associated value to the key
     */
    private V value;

    /**
     * Class constructor MyMapEntry with arguments key and value
     *
     * @param key   key
     * @param value value
     */
    public MyMapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Gets the key of this MyMapEntry
     *
     * @return this MyMapEntry's key
     */
    public K getKey() {
        return key;
    }

    /**
     * Sets the key of this MyMapEntry
     *
     * @param key this MyMapEntry's key
     */
    public void setKey(K key) {
        this.key = key;
    }

    /**
     * Gets the value of this MyMapEntry
     *
     * @return this MyMapEntry's value
     */
    public V getValue() {
        return value;
    }

    /**
     * Sets the value of this MyMapEntry
     *
     * @param value This MyMapEntry's value
     */
    public void setValue(V value) {
        this.value = value;
    }

    /**
     * Compare two keys
     *
     * @param o Second object
     * @return true if keys are same otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        MyMapEntry<?, ?> that = (MyMapEntry<?, ?>) o;
        return this.key.equals(that.key);
    }

    /**
     * Gets the hash code of the key
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}