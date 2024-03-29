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

import io.netty.buffer.ByteBuf;
import java.util.Iterator;

/**
 * A DataRef where the underlying storage is a byte[]
 *
 * @author lkroll
 */
public class ByteArrayRef implements Comparable<ByteArrayRef>, DataRef {
    
    public final int begin;
    public final int length;
    private final byte[] backingArray;

    @Override
    public void copyTo(ByteBuf buffer) {
        buffer.writeBytes(backingArray, begin, length);
    }

    @Override
    public Iterable<DataRef> split(long numberOfChunks, int chunkSize) {
        return new BARIterator(chunkSize);
    }
    
    /**
     * Produce a new ref by wrapping the provided bytes
     *
     * @param backingArray the underlying data for the new ref
     * @return A new ref with the given data
     */
    public static ByteArrayRef wrap(byte[] backingArray) {
        return new ByteArrayRef(0, backingArray.length, backingArray);
    }
    
    public ByteArrayRef(int begin, int length, byte[] backingArray) {
        this.begin = begin;
        this.length = length;
        this.backingArray = backingArray;
    }

    /**
     * Returns the complete backing array (no copy)
     *
     * @return the underlying array
     */
    public byte[] getBackingArray() {
        return backingArray;
    }

    @Override
    public byte[] dereference() {
        if (length == 0) {
            return null;
        }
        return dereference(0, length);
    }
    
    @Override
    public byte dereference(long i) {
        if (i > Integer.MAX_VALUE) {
            throw new IndexOutOfBoundsException("Bounds don't fit into an integer: " + i);
        }
        int ii = (int) i;
        if (ii >= length) {
            throw new IndexOutOfBoundsException("Asked for index " + ii + " but length is only " + length);
        }
        return backingArray[begin + ii];
    }
    
    @Override
    public void assign(long i, byte val) {
        if (i > Integer.MAX_VALUE) {
            throw new IndexOutOfBoundsException("Bounds don't fit into an integer: " + i);
        }
        int ii = (int) i;
        if (ii >= length) {
            throw new IndexOutOfBoundsException("Asked for index " + ii + " but length is only " + length);
        }
        backingArray[begin + ii] = val;
    }

    /* NOTE: This code just asks to be abused such that it circumvents correct 
     (storage layer dependent) multi version usage.
     Hence having is not such a great idea after all.

     public ByteArrayRef replaceWith(byte[] newData) {
     if (newData == null) {
     delete();
     }
     int newLength = backingArray.length + (newData.length - this.length);
     byte[] newBack = new byte[newLength];
     System.arraycopy(backingArray, 0, newBack, 0, begin);
     System.arraycopy(newData, 0, newBack, begin, newData.length);
     System.arraycopy(backingArray, begin + length, newBack, begin + newData.length, newLength - (begin + length));
     return new ByteArrayRef(begin, newData.length, newBack);
     }

     public ByteArrayRef append(byte[] newData) {
     int newLength = backingArray.length + newData.length;
     byte[] newBack = new byte[newLength];
     System.arraycopy(backingArray, 0, newBack, 0, begin + length);
     System.arraycopy(newData, 0, newBack, begin + length, newData.length);
     System.arraycopy(backingArray, begin + length, newBack, begin + length + newData.length, newLength - (begin + length));
     return new ByteArrayRef(begin, length + newData.length, newBack);
     }
    
     public ByteArrayRef delete() {
     int newLength = backingArray.length - this.length;
     if (newLength <= 4) {
     return new ByteArrayRef(0, 0, null); // completely deleted
     }
     byte[] newBack = new byte[newLength];
     System.arraycopy(backingArray, 0, newBack, 0, begin);
     System.arraycopy(backingArray, begin + length, newBack, begin, newLength - (begin + length));
     return new ByteArrayRef(begin, 0, newBack);
     }
   
     */

    @Override
    public void copyTo(byte[] target, int offset) {
        if (backingArray == null) {
            return; // ignore
        }
        System.arraycopy(backingArray, begin, target, offset, length);
    }
    
    @Override
    public int compareTo(ByteArrayRef that) {
        if (this.length != that.length) {
            return this.length - that.length;
        }
        for (int i = 0; i < this.length; i++) {
            byte thisB = this.backingArray[begin + i];
            byte thatB = that.backingArray[that.begin + i];
            if (thisB != thatB) {
                return thisB - thatB;
            }
        }
        return 0;
    }
    
    public int compareTo(byte[] that) {
        if (that == null) {
            return length;
        }
        if (this.length != that.length) {
            return this.length - that.length;
        }
        for (int i = 0; i < this.length; i++) {
            byte thisB = this.backingArray[begin + i];
            byte thatB = that[i];
            if (thisB != thatB) {
                return thisB - thatB;
            }
        }
        return 0;
    }
    
    @Override
    public boolean equals(Object that) {
        if (that instanceof ByteArrayRef) {
            return this.compareTo((ByteArrayRef) that) == 0;
        } else if (that instanceof byte[]) {
            return this.compareTo((byte[]) that) == 0;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        if (this.backingArray == null) {
            return 97 * hash; // + 0
        }
        for (int i = this.begin; i < (this.begin + this.length); i++) {
            hash = 97 * hash + backingArray[i];
        }
        return hash;
    }
    
    @Override
    public byte[] dereference(long start, long end) {
        if (start > Integer.MAX_VALUE || end > Integer.MAX_VALUE) {
            throw new IndexOutOfBoundsException("Bounds don't fit into an integer: " + start + ", " + end);
        }
        int starti = (int) start;
        int endi = (int) end;
        if ((starti >= length) || (starti < 0)) {
            throw new IndexOutOfBoundsException("Asked for start " + starti + " but length is only " + length);
        }
        if ((endi > length) || (endi < starti)) {
            throw new IndexOutOfBoundsException("Asked for end " + endi + " but length is only " + length + " and start is" + start);
        }
        int l = endi - starti;
        byte[] data = new byte[l];
        System.arraycopy(backingArray, begin + starti, data, 0, l);
        return data;
    }
    
    @Override
    public void assign(long start, byte[] newData) {
        long end = start + newData.length;
        if (start > Integer.MAX_VALUE || end > Integer.MAX_VALUE) {
            throw new IndexOutOfBoundsException("Bounds don't fit into an integer: " + start + ", " + end);
        }
        int starti = (int) start;
        int endi = (int) end;
        if ((start >= length) || (start < 0)) {
            throw new IndexOutOfBoundsException("Asked for start " + start + " but length is only " + length);
        }
        if ((endi > length)) {
            throw new IndexOutOfBoundsException("Asked for length " + newData.length + " but length is only " + length);
        }
        System.arraycopy(newData, 0, backingArray, (int)start, newData.length);
    }
    
    @Override
    public void assign(long start, DataRef newData) {
        assign(start, newData.dereference());
    }
    
    @Override
    public void copyTo(DataRef target, long offset) {
        target.assign(offset, backingArray);
    }

    @Override
    public long size() {
        return length;
    }

    @Override
    public void retain() {
        // Simply ignore...GC will do this for us
    }

    @Override
    public void release() {
        // Simply ignore...GC will do this for us
    }
    
    public static boolean rangeEquals(byte[] a, int startA, byte[] b, int startB, int length) {
        if ((a.length - startA < length) || (b.length - startB < length)) {
            return false;
        }
        for (int offset = 0; offset < length; offset++) {
            if (a[startA + offset] != b[startB + offset]) {
                return false;
            }
        }
        return true;
    }
    
    public class BARIterator implements Iterator<DataRef>, Iterable<DataRef> {

        public int pos = begin;
        private final int chunkSize;
        
        private BARIterator(int chunkSize) {
            this.chunkSize = chunkSize;
        }
        
        @Override
        public boolean hasNext() {
            return length > pos;
        }

        @Override
        public DataRef next() {
            int chunkLength = Math.min(chunkSize, length-pos);
            ByteArrayRef subarea = new ByteArrayRef(pos, chunkLength, backingArray);
            pos += chunkLength;
            return subarea;
        }

        @Override
        public Iterator<DataRef> iterator() {
            return this;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not yet implemented!");
        }
    }
}
