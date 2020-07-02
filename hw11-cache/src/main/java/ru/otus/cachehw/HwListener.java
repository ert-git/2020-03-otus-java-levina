package ru.otus.cachehw;

/**
 * @author sergey created on 14.12.18.
 */
public interface HwListener<K, V> {
    public static enum CacheAction {
        PUT, DELETE
    }

    void notify(K key, V value, CacheAction action);
}
