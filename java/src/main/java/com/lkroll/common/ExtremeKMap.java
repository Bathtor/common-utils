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

import com.google.common.collect.Ordering;
import java.util.Comparator;

/**
 * A data structure to calculate TopK and BottomK at the same time
 *
 * @author lkroll
 * @param <K> key 
 * @param <V> value
 */
public class ExtremeKMap<K extends Comparable, V> {

    public final int k;

    private final TopKMap<K, V> tops;
    private final TopKMap<K, V> bottoms;

    public ExtremeKMap(int k) {
        Comparator<K> comparator = Ordering.natural(); // this is all mega ungly and unnecessary in java8 -.-
        Comparator<K> inverseComparator = Ordering.natural().reverse();
        this.k = k;
        tops = new TopKMap(k, comparator);
        bottoms = new TopKMap(k, inverseComparator);
    }

    public ExtremeKMap(int k, Comparator<K> inverseComparator) {
        Comparator<K> comparator = Ordering.natural();
        this.k = k;
        tops = new TopKMap(k, comparator);
        bottoms = new TopKMap(k, inverseComparator);
    }

    public ExtremeKMap(int k, Comparator<K> comparator, Comparator<K> inverseComparator) {
        this.k = k;
        tops = new TopKMap(k, comparator);
        bottoms = new TopKMap(k, inverseComparator);
    }

    public TopKMap<K, V> top() {
        return tops;
    }

    public TopKMap<K, V> bottom() {
        return bottoms;
    }

    public void put(K key, V value) {
        tops.put(key, value);
        bottoms.put(key, value);
    }
}
