package se.code77.j_;

public interface PropertyMap<K, V> extends Iterable<K> {
    V getProperty(K key);
    void setProperty(K key, V value);
}
