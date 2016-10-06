package se.code77.j_;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class J_ {
    public static class Chain<A> {
        protected final A mWrapped;

        public Chain(A value) {
            this.mWrapped = value;
        }

        public A value() {
            return mWrapped;
        }
    }

//    public static class MapChain<K, V> extends Chain<Map<K, V>> {
//        public MapChain(Map<K, V> value) {
//            super(value);
//        }
//
//        public MapChain<K, V> each(MapEachFunction<K, V> func) {
//            return new MapChain<>(J_.each(mWrapped, func));
//        }
//    }

    public static class IterableChain<A, I extends Iterable<A>> extends Chain<I> {
        public IterableChain(I iterable) {
            super(iterable);
        }

        public IterableChain<A, I> each(IterableEachFunction<A> func) {
            return new IterableChain<>(J_.each(mWrapped, func));
        }

        public <B> CollectionChain<B, Collection<B>> map(TransformFunction<A, B> func) {
            return new CollectionChain<>(J_.map(mWrapped, func));
        }

        public <B, LB extends Collection<B>> CollectionChain<B, LB> map(TransformFunction<A, B> func, LB out) {
            return new CollectionChain<>(J_.map(mWrapped, func, out));
        }

        public <B> Chain<B> reduce(ReduceFunction<A, B> func, B memo) {
            return new Chain<>(J_.reduce(mWrapped, func, memo));
        }

        public Chain<A> reduce(ReduceFunction<A, A> func) {
            return new Chain<>(J_.reduce(mWrapped, func));
        }

        public <B> Chain<B> reduceRight(ReduceFunction<A, B> func, B memo) {
            return new Chain<>(J_.reduceRight(mWrapped, func, memo));
        }

        public Chain<A> reduceRight(ReduceFunction<A, A> func) {
            return new Chain<>(J_.reduceRight(mWrapped, func));
        }

        public Chain<A> find(PredicateFunction<A> func) {
            return new Chain<>(J_.find(mWrapped, func));
        }

        public IterableChain<A, Iterable<A>> filter(PredicateFunction<A> func) {
            return new IterableChain<>(J_.filter(mWrapped, func));
        }

        public IterableChain<A, Iterable<A>> where(Properties properties) {
            return new IterableChain<>(J_.where(mWrapped, properties));
        }

        public Chain<A> findWhere(Properties properties) {
            return new Chain<>(J_.findWhere(mWrapped, properties));
        }

        public IterableChain<A, Iterable<A>> reject(PredicateFunction<A> predicate) {
            return new IterableChain<>(J_.reject(mWrapped, predicate));
        }

        public Chain<Boolean> every(PredicateFunction<A> predicate) {
            return new Chain<>(J_.every(mWrapped, predicate));
        }

        public Chain<Boolean> some(PredicateFunction<A> predicate) {
            return new Chain<>(J_.some(mWrapped, predicate));
        }

        public Chain<Boolean> contains(A value) {
            return new Chain<>(J_.contains(mWrapped, value));
        }

        public <B> IterableChain<B, Iterable<B>> pluck(String propertyName) {
            return new IterableChain<>(J_.<B, A>pluck(mWrapped, propertyName));
        }

        public <N extends Number> Chain<N> max(TransformFunction<A, N> func) {
            return new Chain<>(J_.max(mWrapped, func));
        }

        public <N extends Number> Chain<N> min(TransformFunction<A, N> func) {
            return new Chain<>(J_.min(mWrapped, func));
        }

        public <N extends Number> IterableChain<A, Iterable<A>> sortBy(TransformFunction<A, N> func) {
            return new IterableChain<>(J_.sortBy(this.mWrapped, func));
        }

        public <K> PropertyMapChain<K, Collection<A>, PropertyMap<K, Collection<A>>> groupBy(TransformFunction<A, K> func) {
            return new PropertyMapChain<>(J_.groupBy(mWrapped, func));
        }

        public <K> PropertyMapChain<K, A, PropertyMap<K, A>> indexBy(TransformFunction<A, K> func) {
            return new PropertyMapChain<>(J_.indexBy(mWrapped, func));
        }

        public <K> PropertyMapChain<K, Integer, PropertyMap<K, Integer>> countBy(TransformFunction<A, K> func) {
            return new PropertyMapChain<>(J_.countBy(mWrapped, func));
        }

        public Chain<Integer> size() {
            return new Chain<>(J_.size(mWrapped));
        }

        public CollectionChain<Iterable<A>, Collection<Iterable<A>>> partition(PredicateFunction<A> func) {
            return new CollectionChain<>(J_.partition(mWrapped, func));
        }
    }

    public static class CollectionChain<A, C extends Collection<A>> extends IterableChain<A, C> {
        public CollectionChain(C collection) {
            super(collection);
        }

        public Chain<A> first() {
            return new Chain<>(J_.first(mWrapped));
        }

        public CollectionChain<A, Collection<A>> initial(int excludeCount) {
            return new CollectionChain<>(J_.initial(mWrapped, excludeCount));
        }

        // TODO wrap  collectiom methods
    }

    public static class PropertyMapChain<K, V, M extends PropertyMap<K, V>> extends IterableChain<K, M> {
        public PropertyMapChain(M map) {
            super(map);
        }

        public PropertyMapChain<K, V, M> each(MapEachFunction<K, V> func) {
            return new PropertyMapChain<>(J_.each(mWrapped, func));
        }
    }

    void test() {
        J_.chain(Arrays.asList("Henrik", "Anna")).map(new TransformFunction<String, Integer>() {
            @Override
            public Integer transform(String item, int index, Iterable<String> list) {
                return item.length();
            }
        }).filter(new PredicateFunction<Integer>() {
            @Override
            public boolean predicate(Integer item) {
                return item % 2 == 0;
            }
        }).reduce(new ReduceFunction<Integer, Integer>() {
            @Override
            public Integer reduce(Integer memo, Integer item, int index, Iterable<Integer> list) {
                return memo + item;
            }
        }, 0).value();
    }

    public static <A> CollectionChain<A, Collection<A>> chain(Collection<A> list) {
        return new CollectionChain<>(list);
    }

    public static <A> IterableChain<A, Iterable<A>> chain(Iterable<A> list) {
        return new IterableChain<>(list);
    }

    public interface Function {
    }

    public interface IterableEachFunction<A> extends Function {
        void run(A item, int index, Iterable<A> list);
    }

    public static <A, L extends Iterable<A>> L each(L list, IterableEachFunction<A> func) {
        Iterator<A> iter = list.iterator();

        for (int i = 0; iter.hasNext(); i++) {
            A item = iter.next();

            func.run(item, i, list);
        }

        return list;
    }

    public interface MapEachFunction<K, V> extends Function {
        void run(V value, K key, PropertyMap<K, V> map);
    }

    public static <K, V, M extends PropertyMap<K, V>> M each(final M map, final MapEachFunction<K, V> func) {
        each(map, new IterableEachFunction<K>() {
            @Override
            public void run(K item, int index, Iterable<K> list) {
                func.run(map.getProperty(item), item, map);
            }
        });

        return map;
    }

    public interface TransformFunction<A, B> extends Function {
        B transform(A item, int index, Iterable<A> list);
    }


    public static <A, B> Collection<B> map(Iterable<A> list, final TransformFunction<A, B> func) {
        return map(list, func, new ArrayList<B>());
    }

    public static <A, B, LB extends Collection<B>> LB map(Iterable<A> list, final TransformFunction<A, B> func, LB result) {
        Iterator<A> iter = list.iterator();

        for (int i = 0; iter.hasNext(); i++) {
            result.add(func.transform(iter.next(), i, list));
        }

        return result;
    }

    public interface ReduceFunction<A, B> extends Function {
        B reduce(B memo, A item, int index, Iterable<A> list);
    }

    private static <A> List<A> copy(Iterable<A> list) {
        List<A> result = new ArrayList<>();

        for (A item : list) {
            result.add(item);
        }

        return result;
    }

    public static <A> A reduce(Iterable<A> list, ReduceFunction<A, A> func) {
        List<A> tmp = copy(list);
        A memo = tmp.remove(0);

        return reduce(list, func, memo);
    }

    public static <A, B> B reduce(Iterable<A> list, ReduceFunction<A, B> func, B memo) {
        Iterator<A> iter = list.iterator();

        for (int i = 0; iter.hasNext(); i++) {
            A item = iter.next();

            memo = func.reduce(memo, item, i, list);
        }

        return memo;
    }

    public static <A> A reduceRight(Iterable<A> list, ReduceFunction<A, A> func) {
        List<A> tmp = new ArrayList<>();

        for (A item : list) {
            tmp.add(0, item);
        }

        A memo = tmp.remove(0);

        return reduce(list, func, memo);
    }

    public static <A, B> B reduceRight(Iterable<A> list, ReduceFunction<A, B> func, B memo) {
        List<A> tmp = new ArrayList<>();

        for (A item : list) {
            tmp.add(0, item);
        }

        return reduce(list, func, memo);
    }

    public interface PredicateFunction<A> {
        boolean predicate(A item);
    }

    public static <A> A find(Iterable<A> list, PredicateFunction<A> func) {
        for (A item : list) {
            if (func.predicate(item)) {
                return item;
            }
        }

        return null;
    }

    public static <A> Iterable<A> filter(Iterable<A> list, PredicateFunction<A> func) {
        Collection<A> result = new ArrayList<>();

        return filter(list, func, result);
    }

    public static <A, LA extends Collection<A>> LA filter(Iterable<A> list, PredicateFunction<A> func, LA result) {
        for (A item : list) {
            if (func.predicate(item)) {
                result.add(item);
            }
        }

        return result;
    }

    // TODO all the reflect Field stuff is probably unusable. Limit JS objects to Java Map<K, V>?
    public static class Properties extends HashMap<String, Object> {
        public <A> PredicateFunction<A> getPredicate() {
            return new PredicateFunction<A>() {
                @Override
                public boolean predicate(A item) {
                    for (Map.Entry<String, Object> entry : entrySet()) {
                        if (!getValue(item, entry.getKey()).equals(entry.getValue())) {
                            return false;
                        }
                    }

                    return true;
                }
            };
        }

        public static class Builder {
            private final Properties properties = new Properties();

            public Builder put(String name, Object value) {
                properties.put(name, value);

                return this;
            }

            public Properties build() {
                return properties;
            }
        }
    }

    public static <A> Iterable<A> where(Iterable<A> list, Properties properties) {
        return filter(list, properties.<A>getPredicate());
    }

    static {
        where(Arrays.asList(new HashMap<String, Integer>()), new Properties.Builder().put("name", "Henrik").put("age", 39).build());
    }

    public static <A> A findWhere(Iterable<A> list, Properties properties) {
        return find(list, properties.<A>getPredicate());
    }

    public static <A> Iterable<A> reject(Iterable<A> list, final PredicateFunction<A> func) {
        return filter(list, new PredicateFunction<A>() {
            @Override
            public boolean predicate(A item) {
                return !func.predicate(item);
            }
        });
    }

    public static <A> boolean every(Iterable<A> list, PredicateFunction<A> func) {
        for (A item : list) {
            if (!func.predicate(item)) {
                return false;
            }
        }

        return true;
    }

    public static <A> boolean some(Iterable<A> list, PredicateFunction<A> func) {
        for (A item : list) {
            if (func.predicate(item)) {
                return true;
            }
        }

        return false;
    }

    public static <A> boolean contains(Iterable<A> list, A value, int fromIndex) {
        Iterator<A> iter = list.iterator();

        for (int i = 0; iter.hasNext(); i++) {
            A item = iter.next();

            if (i >= fromIndex && item.equals(value)) {
                return true;
            }
        }

        return false;
    }

    public static <A> boolean contains(Iterable<A> list, A value) {
        return contains(list, value, 0);
    }

//    public static <A, B> Iterable<B> pluck(Iterable<A> list, String propertyName, Class<B> clazz) {
//        List<B> result = new ArrayList<>(list.size());
//
//        for (A item : list) {
//            B value = null;
//            try {
//                value = (B)item.getClass().getField(propertyName).get(item);
//            } catch (Exception e) {
//            }
//
//            result.add(value);
//        }
//
//        return result;
//    }

    private static <K> Object getValue(Object obj, K key) {
        if (obj instanceof Map) {
            return ((Map)obj).get(key);
        }

        return null;
    }

    public static <V, A> Iterable<V> pluck(Iterable<A> list, String keyName) {
        List<V> result = new ArrayList<>();

        for (A item : list) {
            result.add((V)getValue(item, keyName));
        }

        return result;
    }


    public static <A, N extends Number> N max(Iterable<A> list, final TransformFunction<A, N> func) {
        return reduce(list, new ReduceFunction<A, N>() {
            @Override
            public N reduce(N memo, A item, int index, Iterable<A> list) {
                N number = func.transform(item, index, list);
                
                return memo == null || number.longValue() > memo.longValue() ? number : memo;
            }
        }, null);
    }

    private static class IdentityTransformFunction<A> implements TransformFunction<A, A> {
        @Override
        public A transform(A item, int index, Iterable<A> list) {
            return item;
        }
    }

    public static <N extends Number> N max(Iterable<N> list) {
        return max(list, new IdentityTransformFunction<N>());
    }

    public static <A, N extends Number> N min(Iterable<A> list, final TransformFunction<A, N> func) {
        return reduce(list, new ReduceFunction<A, N>() {
            @Override
            public N reduce(N memo, A item, int index, Iterable<A> list) {
                N number = func.transform(item, index, list);
                
                return memo == null || number.longValue() < memo.longValue() ? number : memo;
            }
        }, null);
    }

    public static <N extends Number> N min(Iterable<N> list) {
        return min(list, new IdentityTransformFunction<N>());
    }

    private static class TransformedItem<A, B> {
        public final A item;
        public final B transform;

        public TransformedItem(A item, B transform) {
            this.item = item;
            this.transform = transform;
        }
    }

    private static <A, B, C extends Collection<TransformedItem<A, B>>> C transformMap(Iterable<A> list, final TransformFunction<A, B> func, C result) {
        return J_.map(list, new TransformFunction<A, TransformedItem<A, B>>() {
            @Override
            public TransformedItem<A, B> transform(A item, int index, Iterable<A> list) {
                return new TransformedItem<>(item, func.transform(item, index, list));
            }
        }, result);
    }

    public static <A, N extends Number> Iterable<A> sortBy(Iterable<A> list, final TransformFunction<A, N> func) {
        List<TransformedItem<A, N>> entries = new ArrayList<>();

        transformMap(list, func, entries);

        Collections.sort(entries, new Comparator<TransformedItem<A, N>>() {
            @Override
            public int compare(TransformedItem<A, N> lhs, TransformedItem<A, N> rhs) {
                return lhs.transform.intValue() - rhs.transform.intValue();
            }
        });

        return map(entries, new TransformFunction<TransformedItem<A, N>, A>() {
            @Override
            public A transform(TransformedItem<A, N> item, int index, Iterable<TransformedItem<A, N>> list) {
                return item.item;
            }
        });
    }

    public static <A, K> PropertyMap<K, Collection<A>> groupBy(Iterable<A> list, final TransformFunction<A, K> func) {
        final PropertyMap<K, Collection<A>> result = new MapPropertyMap<>();

        each(list, new IterableEachFunction<A>() {
            @Override
            public void run(A item, int index, Iterable<A> list) {
                K key = func.transform(item, index, list);
                Collection<A> values = result.getProperty(key);

                if (values == null) {
                    values = new ArrayList<>();
                    result.setProperty(key, values);
                }
                
                values.add(item);
            }
        });
        
        return result;
    }

    public static <A, K> PropertyMap<K, A> indexBy(Iterable<A> list, final TransformFunction<A, K> func) {
        final PropertyMap<K, A> result = new MapPropertyMap<>();

        each(list, new IterableEachFunction<A>() {
            @Override
            public void run(A item, int index, Iterable<A> list) {
                K key = func.transform(item, index, list);

                result.setProperty(key, item);
            }
        });

        return result;
    }

    public static <A, K> PropertyMap<K, Integer> countBy(Iterable<A> list, final TransformFunction<A, K> func) {
        final PropertyMap<K, Integer> result = new MapPropertyMap<>();

        each(list, new IterableEachFunction<A>() {
            @Override
            public void run(A item, int index, Iterable<A> list) {
                K key = func.transform(item, index, list);
                Integer value = result.getProperty(key);

                if (value == null) {
                    value = 0;
                }

                result.setProperty(key, value + 1);
            }
        });

        return result;
    }

    public static <A> int size(Iterable<A> list) {
        int size = 0;

        for (A item : list) {
            size++;
        }

        return size;
    }

    public static <A> Collection<Iterable<A>> partition(Iterable<A> list, PredicateFunction<A> func) {
        return Arrays.asList(filter(list, func), reject(list, func));
    }

    public static <A> A first(Collection<A> list) {
        for (A item : list) {
            return item;
        }

        return null;
    }

    public static <A> Collection<A> first(Collection<A> list, int count) {
        Collection<A> result = new ArrayList<>(count);
        Iterator<A> iter = list.iterator();

        for (int i = 0; i < count && iter.hasNext(); i++) {
            result.add(iter.next());
        }

        return result;
    }

    public static <A> Collection<A> initial(Collection<A> list, int excludeCount) {
        return first(list, list.size() - excludeCount);
    }

    public static <A> A last(Collection<A> list) {
        return first(last(list, 1));
    }

    public static <A> Collection<A> last(Collection<A> list, int count) {
        Collection<A> result = new ArrayList<>(count);
        Iterator<A> iter = list.iterator();

        for (int i = 0, size = list.size(); iter.hasNext(); i++) {
            if (i >= size - count) {
                result.add(iter.next());
            }
        }

        return result;
    }

    public static <A> Collection<A> rest(Collection<A> list, int excludeCount) {
        return last(list, list.size() - excludeCount);
    }

    public static <A> Collection<A> compact(Collection<A> list) {
        Collection<A> result = new ArrayList<>();

        for (A item : list) {
            if (!isFalsy(item)) {
                result.add(item);
            }
        }

        return result;
    }

    private static <A> boolean isFalsy(A item) {
        boolean falsy = false;

        if (item == null) {
            falsy = true;
        } else if (item instanceof Number) {
            falsy = ((Number)item).intValue() == 0;
        } else if (item instanceof Boolean) {
            falsy = ((Boolean)item);
        } else if (item instanceof CharSequence) {
            falsy = ((CharSequence)item).length() == 0;
        }
        return falsy;
    }

    public static <A, C extends Collection<A>> Collection<A> flatten(Collection<C> list) {
        Collection<A> result = new ArrayList<>(list.size());

        for (C item : list) {
            result.addAll(item);
        }

        return result;
    }

    public static Collection<Object> flatten(Collection<?> list, boolean shallow) {
        Collection<Object> result = new ArrayList<>(list.size());

        for (Object item : list) {
            if (item instanceof Collection) {
                Collection<?> subList = (Collection<?>) item;

                if (!shallow) {
                    subList = flatten(subList, false);
                }

                result.addAll(subList);
            } else {
                result.add(item);
            }
        }

        return result;
    }

    public static <A> Collection<A> without(Collection<A> list, A... values) {
        List<A> excludedValues = Arrays.asList(values);

        return difference(list, excludedValues);
    }

    public static <A> Collection<A> union(Collection<A>... lists) {
        Collection<A> result = new HashSet<>();

        for (Collection<A> list : lists) {
            for (A item : list) {
                result.add(item);
            }
        }

        return result;
    }

    public static <A> Collection<A> intersection(Collection<A>... lists) {
        Collection<A> result = new HashSet<>();

        if (lists.length > 0) {
            Collection<A> list = lists[0];

            for (A item : list) {
                if (!result.contains(item)) {
                    boolean intersects = true;

                    for (int j = 1; j < lists.length; j++) {
                        if (!lists[j].contains(item)) {
                            intersects = false;
                            break;
                        }
                    }

                    if (intersects) {
                        result.add(item);
                    }
                }
            }
        }

        return result;
    }

    public static <A> Collection<A> difference(Collection<A> list, Collection<A>... others) {
        Collection<A> result = new ArrayList<>();
        Collection<A> excludedValues = flatten(Arrays.asList(others));

        for (A item : list) {
            if (!excludedValues.contains(item)) {
                result.add(item);
            }
        }

        return result;
    }

    public static <A, B> Collection<A> uniq(Collection<A> list, boolean isSorted, final TransformFunction<A, B> func) {
        final Collection<A> result = new ArrayList<>();
        final Collection<B> transforms = new HashSet<>();

        each(list, new IterableEachFunction<A>() {
            @Override
            public void run(A item, int index, Iterable<A> list) {
                B transform = func.transform(item, index, list);

                if (transforms.add(transform)) {
                    result.add(item);
                }

            }
        });

        return result;
    }

    public static <A> Collection<A> uniq(Collection<A> list, boolean isSorted) {
        Collection<A> result = new HashSet<>();

        for (A item : list) {
            result.add(item);
        }

        return result;
    }

    public static Collection<Collection<Object>> zip(Collection<?>... lists) {
        return unzip(Arrays.asList(lists));
    }

    public static Collection<Collection<Object>> unzip(Collection<Collection<?>> lists) {
        Collection<Collection<Object>> result = new ArrayList<>();
        Collection<Iterator<?>> iters = new ArrayList<>();

        for (Collection<?> list : lists) {
            iters.add(list.iterator());
        }

        boolean empty = false;

        while (!empty) {
            Collection<Object> zipped = new ArrayList<>();
            empty = true;

            for (Iterator<?> iter : iters) {
                if (iter.hasNext()) {
                    zipped.add(iter.next());
                    empty = false;
                } else {
                    zipped.add(null);
                }
            }

            if (!empty) {
                result.add(zipped);
            }
        }

        return result;
    }

    public static <K, V> PropertyMap<K, V> object(Collection<K> keys, Collection<V> values) {
        PropertyMap<K, V> result = new MapPropertyMap<>();
        Iterator<V> valuesIter = values.iterator();

        for (K key : keys) {
            if (valuesIter.hasNext()) {
                V value = valuesIter.next();

                result.setProperty(key, value);
            }
        }

        return result;
    }

    public static <A> int indexOf(Collection<A> list, A value, boolean isSorted) {
        Iterator<A> iter = list.iterator();

        for (int i = 0; iter.hasNext(); i++) {
            if (iter.next().equals(value)) {
                return i;
            }
        }

        return -1;
    }

    public static <A> int lastIndexOf(Collection<A> list, A value, int fromIndex) {
        Iterator<A> iter = list.iterator();
        int index = -1;

        for (int i = 0; iter.hasNext() && i <= fromIndex; i++) {
            if (iter.next().equals(value)) {
                index = i;
            }
        }

        return index;
    }

    public static <A> int lastIndexOf(Collection<A> list, A value) {
        return lastIndexOf(list, value, list.size());
    }

    public static <A, B> int sortedIndex(Collection<A> list, A value, TransformFunction<A, B> func) {
        // TODO
        return -1;
    }

    public static <A> int sortedIndex(Collection<A> list, A value) {
        // TODO
        return -1;
    }

    public static <A> int findIndex(Collection<A> list, PredicateFunction<A> func) {
        // TODO
        return -1;
    }

    public static <A> int findLastIndex(Collection<A> list, PredicateFunction<A> func) {
        // TODO
        return -1;
    }

    public static Collection<Integer> range(int stop) {
        return range(0, stop, 1);
    }

    public static Collection<Integer> range(int start, int stop) {
        return range(start, stop, 1);
    }

    public static Collection<Integer> range(int start, int stop, int step) {
        Collection<Integer> result = new ArrayList<>();

        for (int i = start; i < stop; i += step) {
            result.add(i);
        }

        return result;
    }

    public static <K, V> Collection<K> keys(PropertyMap<K, V> object) {
        Collection<K> result = new ArrayList<>();

        for (K key : object) {
            result.add(key);
        }

        return result;
    }

    public static <K, V> Collection<V> values(PropertyMap<K, V> object) {
        Collection<V> result = new ArrayList<>();

        for (K key : object) {
            result.add(object.getProperty(key));
        }

        return result;
    }

    public interface MapTransformFunction<K, V, TV> extends Function {
        TV transform(V value, K key, PropertyMap<K, V> object);
    }

    public interface MapPredicateFunction<K, V> extends Function {
        boolean predicate(V value, K key, PropertyMap<K, V> object);
    }

    public static <K, V, TV> PropertyMap<K, TV> mapObject(PropertyMap<K, V> object, MapTransformFunction<K, V, TV> func) {
        PropertyMap<K, TV> result = new MapPropertyMap<>();

        for (K key : object) {
            V value = object.getProperty(key);
            TV transform = func.transform(value, key, object);

            result.setProperty(key, transform);
        }

        return result;
    }

    public static class Pair<K, V> extends AbstractCollection<Object> {
        private final List<Object> mList;

        public Pair(K key, V value) {
            List<Object> list = new ArrayList<>();
            list.add(key);
            list.add(value);

            mList = Collections.unmodifiableList(list);
        }

        public K key() {
            return (K)mList.get(0);
        }

        public V value() {
            return (V)mList.get(1);
        }

        @Override
        public Iterator<Object> iterator() {
            return mList.iterator();
        }

        @Override
        public int size() {
            return mList.size();
        }
    }

    public static <K, V> Collection<Pair<K, V>> pairs(PropertyMap<K, V> object) {
        Collection<Pair<K, V>> result = new ArrayList<>();

        for (K key : object) {
            result.add(new Pair<>(key, object.getProperty(key)));
        }

        return result;
    }

    public static <K, V> PropertyMap<V, K> invert(PropertyMap<K, V> object) {
        PropertyMap<V, K> result = new MapPropertyMap<>();

        for (K key : object) {
            result.setProperty(object.getProperty(key), key);
        }

        return result;
    }

    public static <K, V> K findKey(PropertyMap<K, V> object, PredicateFunction<V> func) {
        for (K key : object) {
            if (func.predicate(object.getProperty(key))) {
                return key;
            }
        }

        return null;
    }

    public static <K, V, M extends PropertyMap<K, V>> M extend(M destination, PropertyMap<K, V>... sources) {
        for (PropertyMap<K, V> source : sources) {
            for (K key : source) {
                destination.setProperty(key, source.getProperty(key));
            }
        }

        return destination;
    }

    public static <K, V> PropertyMap<K, V> pick(PropertyMap<K, V> object, K... keys) {
        PropertyMap<K, V> result = new MapPropertyMap<>();

        for (K key : keys) {
            V value = object.getProperty(key);

            result.setProperty(key, value);
        }

        return result;
    }

    public static <K, V> PropertyMap<K, V> pick(PropertyMap<K, V> object, MapPredicateFunction<K, V> func) {
        PropertyMap<K, V> result = new MapPropertyMap<>();

        for (K key : object) {
            V value = object.getProperty(key);

            if (func.predicate(value, key, object)) {
                result.setProperty(key, value);
            }
        }

        return result;
    }

    public static <K, V> PropertyMap<K, V> omit(PropertyMap<K, V> object, K... keys) {
        PropertyMap<K, V> result = new MapPropertyMap<>();
        List<K> blackList = Arrays.asList(keys);

        for (K key : object) {
            if (!blackList.contains(key)) {
                V value = object.getProperty(key);

                result.setProperty(key, value);
            }
        }

        return result;
    }

    public static <K, V> PropertyMap<K, V> omit(PropertyMap<K, V> object, final MapPredicateFunction<K, V> func) {
        return pick(object, new MapPredicateFunction<K, V>() {
            @Override
            public boolean predicate(V value, K key, PropertyMap<K, V> object) {
                return !func.predicate(value, key, object);
            }
        });
    }

    public static <K, V, M extends PropertyMap<K, V>> M defaults(M object, PropertyMap<K, V>... defaults) {
        for (PropertyMap<K, V> def : defaults) {
            for (K key : def) {
                if (object.getProperty(key) == null) {
                    object.setProperty(key, def.getProperty(key));
                }
            }
        }

        return object;
    }

    public static <K, V> PropertyMap<K, V> clone(PropertyMap<K, V> object) {
        PropertyMap<K, V> clone = new MapPropertyMap<>();

        for (K key : object) {
            clone.setProperty(key, object.getProperty(key));
        }

        return clone;
    }

    public interface TapFunction<A> extends Function {
        void tap(A item);
    }

    public static <A> A tap(A item, TapFunction<A> func) {
        func.tap(item);

        return item;
    }

    public static <K, V> boolean has(PropertyMap<K, V> object, K key) {
        return object.getProperty(key) != null;
    }

    public interface PropertyFunction<K, V> extends Function {
        V call(PropertyMap<K, V> object);
    }

    public static <K, V> PropertyFunction<K, V> property(final K key) {
        return new PropertyFunction<K, V>() {
            @Override
            public V call(PropertyMap<K, V> object) {
                return object.getProperty(key);
            }
        };
    }

    public interface PropertyOfFunction<K, V> extends Function {
        V call(K key);
    }

    public static <K, V> PropertyOfFunction<K, V> propertyOf(final PropertyMap<K, V> object) {
        return new PropertyOfFunction<K, V>() {
            @Override
            public V call(K key) {
                return object.getProperty(key);
            }
        };
    }

    public static <K, V> PredicateFunction<PropertyMap<K, V>> matcher(final PropertyMap<K, V> attrs) {
        return new PredicateFunction<PropertyMap<K, V>>() {
            @Override
            public boolean predicate(PropertyMap<K, V> object) {
                for (K key : attrs) {
                    if (!attrs.getProperty(key).equals(object.getProperty(key))) {
                        return false;
                    }
                }

                return true;
            }
        };
    }
}