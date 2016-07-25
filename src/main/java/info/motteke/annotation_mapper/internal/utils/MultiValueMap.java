package info.motteke.annotation_mapper.internal.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MultiValueMap<K, V> implements Map<K, List<V>> {

    private final Map<K, List<V>> base;

    public MultiValueMap() {
        this(new HashMap<K, List<V>>());
    }

    public MultiValueMap(Map<K, List<V>> base) {
        this.base = base;
    }

    @Override
    public int size() {
        return base.size();
    }

    @Override
    public boolean isEmpty() {
        return base.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return base.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return base.containsValue(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<V> get(Object key) {
        List<V> value = base.get(key);

        if (value == null) {
            value = new ArrayList<V>();
            base.put((K) key, value);
        }

        return value;
    }

    @Override
    public List<V> put(K key, List<V> value) {
        return base.get(key);
    }

    @Override
    public List<V> remove(Object key) {
        return base.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends List<V>> m) {
        base.putAll(m);
    }

    @Override
    public void clear() {
        base.clear();
    }

    @Override
    public Set<K> keySet() {
        return base.keySet();
    }

    @Override
    public Collection<List<V>> values() {
        return base.values();
    }

    @Override
    public Set<java.util.Map.Entry<K, List<V>>> entrySet() {
        return base.entrySet();
    }

    @Override
    public int hashCode() {
        return base.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return base.equals(obj);
    }

    @Override
    public String toString() {
        return base.toString();
    }
}
