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
