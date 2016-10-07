package se.code77.j_;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class JS {
    public interface JCollection<K, V> extends Iterable<K> {
        V get(K key);
        void set(K key, V value);
    }

    private static class JMapCollection<K, V, M extends Map<K,V>> implements JCollection<K, V> {
        protected final M mMap;

        public JMapCollection(M map) {
            mMap = map;
        }

        @Override
        public V get(K key) {
            return mMap.get(key);
        }

        @Override
        public void set(K key, V value) {
            mMap.put(key, value);
        }

        @Override
        public Iterator<K> iterator() {
            return mMap.keySet().iterator();
        }
    }

    private static class JDelegateCollection<K, V> implements JCollection<K, V> {
        private final JCollection<K, V> mDelegate;

        public JDelegateCollection(JCollection<K, V> delegate) {
            mDelegate = delegate;
        }

        @Override
        public final V get(K key) {
            return mDelegate.get(key);
        }

        @Override
        public final void set(K key, V value) {
            mDelegate.set(key, value);
        }

        @Override
        public final Iterator<K> iterator() {
            return mDelegate.iterator();
        }
    }

    public static class JObject<V> extends JDelegateCollection<String, V> {
        public JObject() {
            super(new JMapCollection<>(new LinkedHashMap<String, V>()));
        }

        public static <V> JObject from(Map<String, V> map) {
            JObject obj = new JObject();

            for (Map.Entry<String, V> entry : map.entrySet()) {
                obj.set(entry.getKey(), entry.getValue());
            }

            return obj;
        }

//        public JObject(Object obj) {
//            mDelegate = new JReflectiveCollection(obj);
//        }
    }

    public interface Function {
    }

    public interface IterateFunction<K, V> extends Function {
        void call(V value, K key, JCollection<K, V> collection);
    }

    public interface TransformFunction<K, V, TV> extends Function {
        TV call(V value, K key, JCollection<K, V> collection);
    }

    public static class JArray<V> extends JMapCollection<Integer, V, TreeMap<Integer, V>> {
        private int mLength;

        public JArray() {
            super(new TreeMap<Integer, V>());
        }

        public JArray(int length) {
            this();

            this.mLength = length;
        }

        public static <V> JArray of(V... elements) {
            JArray<V> arr = new JArray<>();

            for (int i = 0; i < elements.length; i++) {
                arr.set(i, elements[i]);
            }

            return arr;
        }

        public static <V> JArray from(Collection<V> collection) {
            JArray<V> arr = new JArray<>();
            Iterator<V> iter = collection.iterator();

            for (int i = 0; iter.hasNext(); i++) {
                arr.set(i, iter.next());
            }

            return arr;
        }

        @Override
        public void set(Integer key, V value) {
            super.set(key, value);

            if (key >= mLength) {
                mLength = key + 1;
            }
        }

        public int length() {
            return mLength;
        }

        public void push(V value) {
            set(length(), value);
        }

        public V pop() {
            if (mMap.isEmpty()) {
                return null;
            }

            mLength--;
            return mMap.remove(mMap.lastKey());
        }

        public JArray<V> concat(JArray<V>... others) {
            JArray<V> arr = new JArray<>();

            for (Integer index : this) {
                V value = get(index);

                arr.set(index, value);
            }

            for (JArray<V> other : others) {
                int offset = arr.length();

                for (Integer index : other) {
                    V value = other.get(index);

                    arr.set(index + offset, value);
                }
            }

            return arr;
        }

        public void forEach(IterateFunction<Integer, V> callback) {
            for (Integer index : this) {
                callback.call(get(index), index, this);
            }
        }

        public <TV> JArray<TV> map(TransformFunction<Integer, V, TV> callback) {
            JArray<TV> result = new JArray<>();

            for (Integer index : this) {
                result.set(index, callback.call(get(index), index, this));
            }

            return result;
        }

        public void splice(int start, int deleteCount, V... items) {
            if (start > length()) {
                start = length();
            } else if (start < 0) {
                start = length() + start;
            }

            if (deleteCount < 0 || deleteCount > mLength - start) {
                deleteCount = mLength - start;
            }

            int offset = items.length - deleteCount;
            Map<Integer, V> moved = new TreeMap<>();
            Iterator<Map.Entry<Integer, V>> iter = mMap.tailMap(start).entrySet().iterator();

            while (iter.hasNext()) {
                Map.Entry<Integer, V> entry = iter.next();
                Integer index = entry.getKey();
                V value = entry.getValue();

                if (index >= start + deleteCount) {
                    moved.put(index + offset, value);
                }

                iter.remove();
            }

            for (int i = 0; i < items.length; i++) {
                mMap.put(start + i, items[i]);
            }

            mMap.putAll(moved);
            mLength += offset;
        }

        public JArray<V> slice(int begin, int end) {
            JArray<V> result  = new JArray<>();

            for (Map.Entry<Integer, V> entry : mMap.subMap(begin, end).entrySet()) {
                result.set(entry.getKey() - begin, entry.getValue());
            }

            return result;
        }

        public V shift() {
            V value = get(0);

            splice(0, 1);

            return value;
        }

        public int unshift(V... values) {
            splice(0, 0, values);

            return mLength;
        }

        public int indexOf(V searchElement) {
            return indexOf(searchElement, 0);
        }

        public int indexOf(V searchElement, int fromIndex) {
            for (Map.Entry<Integer, V> entry : mMap.tailMap(fromIndex).entrySet()) {
                if (searchElement.equals(entry.getValue())) {
                    return entry.getKey();
                }
            }

            return -1;
        }

        public int lastIndexOf(V searchElement) {
            return lastIndexOf(searchElement, mLength - 1);
        }

        private int lastIndexOf(V searchElement, int fromIndex) {
            int key = -1;

            for (Map.Entry<Integer, V> entry : mMap.headMap(fromIndex + 1).entrySet()) {
                if (searchElement.equals(entry.getValue())) {
                    key = entry.getKey();
                }
            }

            return key;
        }

        public interface PredicateFunction<K, V> {
            boolean call(V value, K key, JCollection<K, V> collection);
        }

        public boolean every(PredicateFunction<Integer, V> callback) {
            for (Map.Entry<Integer, V> entry : mMap.entrySet()) {
                if (!callback.call(entry.getValue(), entry.getKey(), this)) {
                    return false;
                }
            }

            return true;
        }

        public JArray<V> filter(PredicateFunction<Integer, V> callback) {
            JArray<V> result = new JArray<>();

            for (Map.Entry<Integer, V> entry : mMap.entrySet()) {
                Integer key = entry.getKey();
                V value = entry.getValue();

                if (callback.call(value, key, this)) {
                    result.push(value);
                }
            }

            return result;
        }

        public boolean some(PredicateFunction<Integer, V> callback) {
            for (Map.Entry<Integer, V> entry : mMap.entrySet()) {
                if (callback.call(entry.getValue(), entry.getKey(), this)) {
                    return true;
                }
            }

            return false;
        }


    }
}
