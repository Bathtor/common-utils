/*
 * This file is part of the CaracalDB distributed storage system.
 *
 * Copyright (C) 2009 Swedish Institute of Computer Science (SICS) 
 * Copyright (C) 2009 Royal Institute of Technology (KTH)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.larskroll.common;

import com.google.common.collect.Ordering;
import java.util.Comparator;

/**
 *
 * @author lkroll
 * @param <K>
 * @param <V>
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