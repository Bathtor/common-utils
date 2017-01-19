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

import java.util.Collection;

/**
 * Utility class with workarounds for functionality (maybe from Java8) that is
 * missing
 * in Java6
 *
 * @author lkroll
 */
public abstract class J6 {

    public static <T> T orDefault(T obj, T defaultValue) {
        if (obj == null) {
            return defaultValue;
        } else {
            return obj;
        }
    }

    public static long roundUp(long num, long divisor) {
        return (num + divisor - 1) / divisor;
    }

    public static int roundUp(int num, int divisor) {
        return (int) roundUp((long) num, (long) divisor);
    }

    public static <T> T randomElement(Collection<T> coll) {
        int num = (int) (Math.random() * coll.size());
        for (T t : coll) {
            if (--num < 0) {
                return t;
            }
        }
        throw new AssertionError();
    }
}
