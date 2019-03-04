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
package com.larskroll.common;

import com.google.common.collect.Ordering;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import org.javatuples.Pair;

/**
 *
 * @author sario
 * @param <K> key type
 * @param <V> value type
 */
public class TopKMap<K extends Comparable, V> {

    private final ArrayList<Pair<K, V>> list = new ArrayList<Pair<K, V>>();
    private final PairFirstComparator<K, V> comp;
    public final int k;

    public TopKMap(int k) {
        this.k = k;
        Comparator<K> mycomp = Ordering.natural().reverse();
        this.comp = new PairFirstComparator<K, V>(mycomp);
    }

    public TopKMap(int k, Comparator<K> comp) {
        this.k = k;
        this.comp = new PairFirstComparator<K, V>(Ordering.from(comp).reverse());
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean containsKey(K key) {
        for (Pair<K, V> p : list) {
            if (comp.comp.compare(p.getValue0(), key) == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean containsValue(V value) {
        for (Pair<K, V> p : list) {
            if (p.getValue1().equals(value)) {
                return true;
            }
        }
        return false;
    }

    public V get(K key) {
        for (Pair<K, V> p : list) {
            if (comp.comp.compare(p.getValue0(), key) == 0) {
                return p.getValue1();
            }
        }
        return null;
    }

    public V put(K key, V value) {
        list.add(new Pair(key, value));
        if (list.size() < k) {
            return value;
        } else {
            Collections.sort(list, comp);
            Pair<K, V> p = list.remove(k-1);
            return p.getValue1();
        }
    }

    public Entry<K, V> ceilingEntry() {
        if (list.isEmpty()) {
            return null;
        }
        Pair<K, V> p = list.get(0);
        return new AbstractMap.SimpleImmutableEntry<K, V>(p.getValue0(), p.getValue1());
    }

    public Entry<K, V> floorEntry() {
        if (list.isEmpty()) {
            return null;
        }
        Pair<K, V> p = list.get(list.size() - 1);
        return new AbstractMap.SimpleImmutableEntry<K, V>(p.getValue0(), p.getValue1());
    }

    public void clear() {
        list.clear();
    }

    public List<Entry<K, V>> entryList() {
        ArrayList<Entry<K, V>> newlist = new ArrayList<Entry<K, V>>(list.size());
        for (Pair<K, V> p : list) {
            newlist.add(new AbstractMap.SimpleImmutableEntry<K, V>(p.getValue0(), p.getValue1()));
        }
        return newlist;
    }
    
    public List<V> values() {
        ArrayList<V> newlist = new ArrayList<V>(list.size());
        for (Pair<K, V> p : list) {
            newlist.add(p.getValue1());
        }
        return newlist;
    }
    
    public List<K> keys() {
        ArrayList<K> newlist = new ArrayList<K>(list.size());
        for (Pair<K, V> p : list) {
            newlist.add(p.getValue0());
        }
        return newlist;
    }

    public static class PairFirstComparator<KEY extends Comparable, VALUE> implements Comparator<Pair<KEY, VALUE>> {

        public final Comparator<KEY> comp;

        public PairFirstComparator(Comparator<KEY> comp) {
            this.comp = comp;
        }

        @Override
        public int compare(Pair<KEY, VALUE> o1, Pair<KEY, VALUE> o2) {
            return comp.compare(o1.getValue0(), o2.getValue0());
        }

    }
}
