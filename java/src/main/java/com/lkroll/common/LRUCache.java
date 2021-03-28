/* 
* Copyright 2019 Lars Kroll
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software and 
* associated documentation files (the "Software"), to deal in the Software without restriction, 
* including without limitation the rights to use, copy, modify, merge, publish, distribute, 
* sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is 
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all copies or 
* substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
* INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE 
* AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
* DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package com.lkroll.common;

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
