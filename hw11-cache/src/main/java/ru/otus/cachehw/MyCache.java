package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import lombok.extern.slf4j.Slf4j;
import ru.otus.cachehw.HwListener.CacheAction;

/**
 * @author sergey created on 14.12.18.
 */
@Slf4j
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
        try {
            listeners.forEach(listener -> listener.notify(key, value, CacheAction.PUT));
        } catch (Exception e) {
            log.error("put: failed to notify about key={}, value={}", key, value);
        }
    }

    @Override
    public void remove(K key) {
        V removed = cache.remove(key);
        try {
            listeners.forEach(listener -> listener.notify(key, removed, CacheAction.DELETE));
        } catch (Exception e) {
            log.error("delete: failed to notify about key={}", key);
        }
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
