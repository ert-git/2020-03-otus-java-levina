package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import ru.otus.cachehw.HwListener.CacheAction;

/**
 * @author sergey created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
    // Надо реализовать эти методы

    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    public int size() {
        return cache.size();
    }
    public Set<K> keys() {
        return new HashSet<>(cache.keySet());
    }
    
    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        listeners.forEach(listener -> listener.notify(key, value, CacheAction.PUT));
    }

    @Override
    public void remove(K key) {
        V removed = cache.remove(key);
        listeners.forEach(listener -> listener.notify(key, removed, CacheAction.DELETE));
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }
}
