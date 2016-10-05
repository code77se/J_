package se.code77.j_;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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

    public static class MapChain<K, V> extends Chain<Map<K, V>> {
        public MapChain(Map<K, V> value) {
            super(value);
        }

        public MapChain<K, V> each(MapEachFunction<K, V> func) {
            return new MapChain<>(J_.each(mWrapped, func));
        }
    }

    public static class CollectionChain<A> extends Chain<Collection<A>> {
        public CollectionChain(Collection<A> value) {
            super(value);
        }

        public CollectionChain<A> each(CollectionEachFunction<A> func) {
            return new CollectionChain<>(J_.each(mWrapped, func));
        }

        public <B> CollectionChain<B> map(TransformFunction<A, B> func) {
            return new CollectionChain<>(J_.map(mWrapped, func));
        }

        public <B, LB extends Collection<B>> CollectionChain<B> map(TransformFunction<A, B> func, LB out) {
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

        public CollectionChain<A> filter(PredicateFunction<A> func) {
            return new CollectionChain<>(J_.filter(mWrapped, func));
        }

        public CollectionChain<A> where(Properties properties) {
            return new CollectionChain<>(J_.where(mWrapped, properties));
        }

        public Chain<A> findWhere(Properties properties) {
            return new Chain<>(J_.findWhere(mWrapped, properties));
        }

        public CollectionChain<A> reject(PredicateFunction<A> predicate) {
            return new CollectionChain<>(J_.reject(mWrapped, predicate));
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

        public <B> CollectionChain<B> pluck(String propertyName) {
            return new CollectionChain<>(J_.<B, A>pluck(mWrapped, propertyName));
        }

        public <N extends Number> Chain<N> max(TransformFunction<A, N> func) {
            return new Chain<>(J_.max(mWrapped, func));
        }

        public <N extends Number> Chain<N> min(TransformFunction<A, N> func) {
            return new Chain<>(J_.min(mWrapped, func));
        }

        public <N extends Number> CollectionChain<A> sortBy(TransformFunction<A, N> func) {
            return new CollectionChain<>(J_.sortBy(this.mWrapped, func));
        }

        public <K> MapChain<K, Collection<A>> groupBy(TransformFunction<A, K> func) {
            return new MapChain<>(J_.groupBy(mWrapped, func));
        }

        public <K> MapChain<K, A> indexBy(TransformFunction<A, K> func) {
            return new MapChain<>(J_.indexBy(mWrapped, func));
        }

        public <K> MapChain<K, Integer> countBy(TransformFunction<A, K> func) {
            return new MapChain<>(J_.countBy(mWrapped, func));
        }

        public Chain<Integer> size() {
            return new Chain<>(mWrapped.size());
        }

        public CollectionChain<Collection<A>> partition(PredicateFunction<A> func) {
            return new CollectionChain<>(J_.partition(mWrapped, func));
        }

    }

    void test() {
        J_.chain(Arrays.asList("Henrik", "Anna")).map(new TransformFunction<String, Integer>() {
            @Override
            public Integer transform(String item, int index, Collection<String> list) {
                return item.length();
            }
        }).filter(new PredicateFunction<Integer>() {
            @Override
            public boolean predicate(Integer item) {
                return item % 2 == 0;
            }
        }).reduce(new ReduceFunction<Integer, Integer>() {
            @Override
            public Integer reduce(Integer memo, Integer item, int index, Collection<Integer> list) {
                return memo + item;
            }
        }, 0).value();
    }

    public static <A> CollectionChain<A> chain(Collection<A> list) {
        return new CollectionChain<>(list);
    }

    public interface Function {
    }

    public interface CollectionEachFunction<A> extends Function {
        void run(A item, int index, Collection<A> list);
    }

    public static <A, L extends Collection<A>> L each(L list, CollectionEachFunction<A> func) {
        Iterator<A> iter = list.iterator();

        for (int i = 0; iter.hasNext(); i++) {
            A item = iter.next();

            func.run(item, i, list);
        }

        return list;
    }

    public interface MapEachFunction<K, V> extends Function {
        void run(V value, K key, Map<K, V> map);
    }

    public static <K, V, M extends Map<K, V>> M each(final M map, final MapEachFunction<K, V> func) {
        each(map.entrySet(), new CollectionEachFunction<Map.Entry<K, V>>() {
            @Override
            public void run(Map.Entry<K, V> item, int index, Collection<Map.Entry<K, V>> list) {
                func.run(item.getValue(), item.getKey(), map);
            }
        });

        return map;
    }

    public interface TransformFunction<A, B> extends Function {
        B transform(A item, int index, Collection<A> list);
    }


    public static <A, B> Collection<B> map(Collection<A> list, final TransformFunction<A, B> func) {
        return map(list, func, new ArrayList<B>());
    }

    public static <A, B, LB extends Collection<B>> LB map(Collection<A> list, final TransformFunction<A, B> func, LB result) {
        Iterator<A> iter = list.iterator();

        for (int i = 0, size = list.size(); i < size; i++) {
            result.add(func.transform(iter.next(), i, list));
        }

        return result;
    }

    public interface ReduceFunction<A, B> extends Function {
        B reduce(B memo, A item, int index, Collection<A> list);
    }

    public static <A> A reduce(Collection<A> list, ReduceFunction<A, A> func) {
        List<A> tmp = new ArrayList<>(list);
        A memo = tmp.remove(0);

        return reduce(list, func, memo);
    }

    public static <A, B> B reduce(Collection<A> list, ReduceFunction<A, B> func, B memo) {
        Iterator<A> iter = list.iterator();

        for (int i = 0, size = list.size(); i < size; i++) {
            A item = iter.next();

            memo = func.reduce(memo, item, i, list);
        }

        return memo;
    }

    public static <A> A reduceRight(Collection<A> list, ReduceFunction<A, A> func) {
        List<A> tmp = new ArrayList<>(list.size());

        for (A item : list) {
            tmp.add(0, item);
        }

        A memo = tmp.remove(0);

        return reduce(list, func, memo);
    }

    public static <A, B> B reduceRight(Collection<A> list, ReduceFunction<A, B> func, B memo) {
        List<A> tmp = new ArrayList<>(list.size());

        for (A item : list) {
            tmp.add(0, item);
        }

        return reduce(list, func, memo);
    }

    public interface PredicateFunction<A> {
        boolean predicate(A item);
    }

    public static <A> A find(Collection<A> list, PredicateFunction<A> func) {
        for (A item : list) {
            if (func.predicate(item)) {
                return item;
            }
        }

        return null;
    }

    public static <A> Collection<A> filter(Collection<A> list, PredicateFunction<A> func) {
        Collection<A> result = new ArrayList<>();

        return filter(list, func, result);
    }

    public static <A, LA extends Collection<A>> LA filter(Collection<A> list, PredicateFunction<A> func, LA result) {
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

    public static <A> Collection<A> where(Collection<A> list, Properties properties) {
        return filter(list, properties.<A>getPredicate());
    }

    static {
        where(Arrays.asList(new HashMap<String, Integer>()), new Properties.Builder().put("name", "Henrik").put("age", 39).build());
    }

    public static <A> A findWhere(Collection<A> list, Properties properties) {
        return find(list, properties.<A>getPredicate());
    }

    public static <A> Collection<A> reject(Collection<A> list, final PredicateFunction<A> func) {
        return filter(list, new PredicateFunction<A>() {
            @Override
            public boolean predicate(A item) {
                return !func.predicate(item);
            }
        });
    }

    public static <A> boolean every(Collection<A> list, PredicateFunction<A> func) {
        for (A item : list) {
            if (!func.predicate(item)) {
                return false;
            }
        }

        return true;
    }

    public static <A> boolean some(Collection<A> list, PredicateFunction<A> func) {
        for (A item : list) {
            if (func.predicate(item)) {
                return true;
            }
        }

        return false;
    }

    public static <A> boolean contains(Collection<A> list, A value, int fromIndex) {
        Iterator<A> iter = list.iterator();

        for (int i = 0; iter.hasNext(); i++) {
            A item = iter.next();

            if (i >= fromIndex && item.equals(value)) {
                return true;
            }
        }

        return false;
    }

    public static <A> boolean contains(Collection<A> list, A value) {
        return contains(list, value, 0);
    }

//    public static <A, B> Collection<B> pluck(Collection<A> list, String propertyName, Class<B> clazz) {
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

    public static <V, A> Collection<V> pluck(Collection<A> list, String keyName) {
        List<V> result = new ArrayList<>(list.size());

        for (A item : list) {
            result.add((V)getValue(item, keyName));
        }

        return result;
    }


    public static <A, N extends Number> N max(Collection<A> list, final TransformFunction<A, N> func) {
        return reduce(list, new ReduceFunction<A, N>() {
            @Override
            public N reduce(N memo, A item, int index, Collection<A> list) {
                N number = func.transform(item, index, list);
                
                return memo == null || number.longValue() > memo.longValue() ? number : memo;
            }
        }, null);
    }

    private static class IdentityTransformFunction<A> implements TransformFunction<A, A> {
        @Override
        public A transform(A item, int index, Collection<A> list) {
            return item;
        }
    }

    public static <N extends Number> N max(Collection<N> list) {
        return max(list, new IdentityTransformFunction<N>());
    }

    public static <A, N extends Number> N min(Collection<A> list, final TransformFunction<A, N> func) {
        return reduce(list, new ReduceFunction<A, N>() {
            @Override
            public N reduce(N memo, A item, int index, Collection<A> list) {
                N number = func.transform(item, index, list);
                
                return memo == null || number.longValue() < memo.longValue() ? number : memo;
            }
        }, null);
    }

    public static <N extends Number> N min(Collection<N> list) {
        return min(list, new IdentityTransformFunction<N>());
    }

    private static class RankedItem<A, N extends Number> {
        public final A item;
        public final N rank;

        public RankedItem(A item, N rank) {
            this.item = item;
            this.rank = rank;
        }
    }

    public static <A, N extends Number> Collection<A> sortBy(Collection<A> list, final TransformFunction<A, N> func) {
        List<RankedItem<A, N>> entries = new ArrayList<>();

        map(list, new TransformFunction<A, RankedItem<A, N>>() {
            @Override
            public RankedItem<A, N> transform(A item, int index, Collection<A> list) {
                return new RankedItem(item, func.transform(item, index, list));
            }
        }, entries);

        Collections.sort(entries, new Comparator<RankedItem<A, N>>() {
            @Override
            public int compare(RankedItem<A, N> lhs, RankedItem<A, N> rhs) {
                return lhs.rank.intValue() - rhs.rank.intValue();
            }
        });

        return map(entries, new TransformFunction<RankedItem<A, N>, A>() {
            @Override
            public A transform(RankedItem<A, N> item, int index, Collection<RankedItem<A, N>> list) {
                return item.item;
            }
        });
    }

    public static <A, K> Map<K, Collection<A>> groupBy(Collection<A> list, final TransformFunction<A, K> func) {
        final Map<K, Collection<A>> result = new HashMap<>();

        each(list, new CollectionEachFunction<A>() {
            @Override
            public void run(A item, int index, Collection<A> list) {
                K key = func.transform(item, index, list);
                Collection<A> values = result.get(key);

                if (values == null) {
                    values = new ArrayList<>();
                    result.put(key, values);
                }
                
                values.add(item);
            }
        });
        
        return result;
    }

    public static <A, K> Map<K, A> indexBy(Collection<A> list, final TransformFunction<A, K> func) {
        final Map<K, A> result = new HashMap<>();

        each(list, new CollectionEachFunction<A>() {
            @Override
            public void run(A item, int index, Collection<A> list) {
                K key = func.transform(item, index, list);

                result.put(key, item);
            }
        });

        return result;
    }

    public static <A, K> Map<K, Integer> countBy(Collection<A> list, final TransformFunction<A, K> func) {
        final Map<K, Integer> result = new HashMap<>();

        each(list, new CollectionEachFunction<A>() {
            @Override
            public void run(A item, int index, Collection<A> list) {
                K key = func.transform(item, index, list);
                Integer value = result.get(key);

                if (value == null) {
                    value = 0;
                }

                result.put(key, value + 1);
            }
        });

        return result;
    }

    public static <A> int size(Collection<A> list) {
        return list.size();
    }

    public static <A> Collection<Collection<A>> partition(Collection<A> list, PredicateFunction<A> func) {
        return Arrays.asList(filter(list, func), reject(list, func));
    }

}