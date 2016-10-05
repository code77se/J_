package se.code77.j_;

import java.util.Iterator;
import java.util.Map;

public class MapPropertyMap<K, V> implements PropertyMap<K, V> {
    private final Map<K, V> mMap;

    public MapPropertyMap(Map<K, V> map) {
        mMap = map;
    }

    @Override
    public V getProperty(K key) {
        return mMap.get(key);
    }

    @Override
    public void setProperty(K key, V value) {
        mMap.put(key, value);
    }

    @Override
    public Iterator<K> iterator() {
        return mMap.keySet().iterator();
    }
}
