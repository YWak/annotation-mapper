package info.motteke.annotation_mapper.internal.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MultiValueMap<K, V> implements Map<K, Collection<V>> {

    private final Map<K, Collection<V>> base;

    public MultiValueMap() {
        this(new HashMap<K, Collection<V>>());
    }

    public MultiValueMap(Map<K, Collection<V>> base) {
        this.base = base;
    }

    protected Collection<V> newCollection() {
        return new ArrayList<V>();
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
    public Collection<V> get(Object key) {
        Collection<V> value = base.get(key);

        if (value == null) {
            value = newCollection();
            base.put((K) key, value);
        }

        return value;
    }

    @Override
    public Collection<V> put(K key, Collection<V> value) {
        return base.get(key);
    }

    @Override
    public Collection<V> remove(Object key) {
        return base.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends Collection<V>> m) {
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
    public Collection<Collection<V>> values() {
        return base.values();
    }

    @Override
    public Set<java.util.Map.Entry<K, Collection<V>>> entrySet() {
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
