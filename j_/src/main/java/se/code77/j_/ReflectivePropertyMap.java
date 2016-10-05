package se.code77.j_;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;

public class ReflectivePropertyMap<V> implements PropertyMap<String, V> {
    private final Object mObject;

    public ReflectivePropertyMap(Object o) {
        mObject = o;
    }

    @Override
    public V getProperty(String key) {
        try {
            return (V)mObject.getClass().getField(key).get(mObject);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setProperty(String key, V value) {
        try {
            mObject.getClass().getField(key).set(mObject, value);
        } catch (Exception e) {
        }
    }

    @Override
    public Iterator<String> iterator() {
        return J_.map(Arrays.asList(mObject.getClass().getFields()), new J_.TransformFunction<Field, String>() {
            @Override
            public String transform(Field item, int index, Iterable<Field> list) {
                return item.getName();
            }
        }).iterator();
    }
}
