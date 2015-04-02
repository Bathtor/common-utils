/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.larskroll.common;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Standard LRU cache with fixed capacity.
 * 
 * You can implement an {@link EvictionHandler} if you need custom cleanup code
 * when an item is evicted from the cache.
 * 
 * @author lkroll
 * @param <K> Key
 * @param <V> Value
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private final int cap;
    private final EvictionHandler<K, V> evicH;

    public LRUCache(int capacity, EvictionHandler<K, V> evictionHandler) {
        this.cap = capacity;
        this.evicH = evictionHandler;
    }
    
    public LRUCache(int capacity) {
        this(capacity, null);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        if (size() > cap) {
            if (evicH != null) {
                evicH.evicted(eldest);
            }
            return true;
        }
        return false;
    }
    
    public static interface EvictionHandler<K, V> {
        public void evicted(Map.Entry<K, V> entry);
    }
}
