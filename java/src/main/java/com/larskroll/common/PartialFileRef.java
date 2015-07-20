/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.larskroll.common;

import io.netty.buffer.ByteBuf;
import java.util.Iterator;

/**
 *
 * @author lkroll
 */
public class PartialFileRef implements DataRef {

    private final RAFileRef data;
    private final long begin;
    private final long length;

    public PartialFileRef(long begin, long length, RAFileRef data) {
        this.data = data;
        this.begin = begin;
        this.length = length;
        data.retain();
    }
    
    public RAFileRef fileRef() {
        return data;
    }

    @Override
    public void retain() {
        data.retain();
    }

    @Override
    public void release() {
        data.release();
    }

    @Override
    public long size() {
        return length;
    }

    @Override
    public byte[] dereference() {
        return dereference(0, length);
    }

    @Override
    public byte dereference(long i) {
        return data.dereference(begin + i);
    }

    @Override
    public byte[] dereference(long start, long end) {
        return data.dereference(begin + start, begin + end);
    }

    @Override
    public void assign(long i, byte val) {
        data.assign(begin + i, val);
    }

    @Override
    public void assign(long start, byte[] newData) {
        long end = begin + start + newData.length;
        if (end > begin + length) {
            throw new IndexOutOfBoundsException("Asked for start " + start + " and size " + newData.length + " but length is only " + length);
        }
        data.assign(begin + start, newData);
    }

    @Override
    public void assign(long start, DataRef newData) {
        data.assign(begin + start, newData);
    }

    @Override
    public void copyTo(DataRef target, long offset) {
        data.copyTo(target, offset, begin, length);
    }

    @Override
    public void copyTo(byte[] target, int offset) {
        if (length > Integer.MAX_VALUE) {
            throw new IndexOutOfBoundsException("length doesn't fit into an integer: " + length);
        }
        data.copyTo(target, offset, begin, (int) length);
    }

    @Override
    public void copyTo(ByteBuf buffer) {
        if (length > Integer.MAX_VALUE) {
            throw new IndexOutOfBoundsException("length doesn't fit into an integer: " + length);
        }
        data.copyTo(buffer, begin, (int)length);
    }

    @Override
    public Iterable<DataRef> split(long numberOfChunks, int chunkSize) {
        return new PFRIterator(chunkSize);
    }
    
    public class PFRIterator implements Iterator<DataRef>, Iterable<DataRef> {

        public long pos = 0;
        private final int chunkSize;
        private final long length;

        private PFRIterator(int chunkSize) {
            this.chunkSize = chunkSize;
            this.length = size();
        }

        @Override
        public boolean hasNext() {
            return length > pos;
        }

        @Override
        public DataRef next() {
            int chunkLength = (int) Math.min((long) chunkSize, length - pos); // the smaller one must be int sized
            PartialFileRef subarea = new PartialFileRef(pos, chunkLength, data);
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
