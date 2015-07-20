/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.larskroll.common;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Iterator;

/**
 *
 * @author lkroll
 */
public class RAFileRef implements DataRef {

    private final RandomAccessFile raf;
    private final File f;
    private long rc = 1;
    private boolean delete = false;

    public RAFileRef(File f, RandomAccessFile raf) {
        this.f = f;
        this.raf = raf;
    }

    public RandomAccessFile getRAF() {
        return this.raf;
    }

    public File getFile() {
        return this.f;
    }

    public void markForDeletion() {
        this.delete = true;
        //f.deleteOnExit(); // in case the VM quits 
    }

    @Override
    public byte[] dereference() {
        try {
            byte[] data = new byte[(int) raf.length()];
            raf.seek(0);
            raf.read(data);
            return data;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public byte dereference(long i) {
        try {
            if (i >= raf.length()) {
                throw new IndexOutOfBoundsException("Asked for index " + i + " but length is only " + raf.length());
            }
            raf.seek(i);
            return raf.readByte();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public byte[] dereference(long start, long end) {
        try {
            if ((start >= raf.length()) || (start < 0)) {
                throw new IndexOutOfBoundsException("Asked for start " + start + " but length is only " + raf.length());
            }
            if ((end > raf.length()) || (end < start)) {
                throw new IndexOutOfBoundsException("Asked for end " + end + " but length is only " + raf.length() + " and start is" + start);
            }
            long l = end - start;
            if (l > Integer.MAX_VALUE) {
                throw new IndexOutOfBoundsException("Range doesn't fit into an integer: " + l);
            }
            int li = (int) l;
            byte[] data = new byte[li];
            raf.seek(start);
            raf.read(data);
            return data;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void assign(long i, byte val) {
        try {
            if (i >= raf.length()) {
                throw new IndexOutOfBoundsException("Asked for index " + i + " but length is only " + raf.length());
            }
            raf.seek(i);
            raf.writeByte(val);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void assign(long start, byte[] newData) {
        try {
            if ((start >= raf.length()) || (start < 0)) {
                throw new IndexOutOfBoundsException("Asked for start " + start + " but length is only " + raf.length());
            }
            long end = start + newData.length;
            if ((end > raf.length())) {
                throw new IndexOutOfBoundsException("Asked for length " + newData.length + " but length is only " + raf.length());
            }
            raf.seek(start);
            raf.write(newData);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void assign(long start, DataRef newData) {
        if (newData instanceof RAFileRef) {
            try {
                RAFileRef src = (RAFileRef) newData;
                raf.seek(start);
                //src.raf.seek(0);
                FileChannel sink = raf.getChannel();
                FileChannel source = src.raf.getChannel();
                source.transferTo(0, src.raf.length(), sink);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            assign(start, newData.dereference());
        }
    }

    @Override
    public void copyTo(DataRef target, long offset) {

    }

    void copyTo(DataRef target, long offset, long start, long length) {
        if (target instanceof RAFileRef) {
            try {
                RAFileRef tgt = (RAFileRef) target;
                //tgt.raf.seek(offset);
                raf.seek(start);
                FileChannel sink = tgt.raf.getChannel();
                FileChannel source = raf.getChannel();
                source.transferTo(offset, length, sink);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            if (offset > Integer.MAX_VALUE) {
                throw new IndexOutOfBoundsException("offset doesn't fit into an integer: " + offset);
            }
            copyTo(target.dereference(), (int) offset);
        }
    }

    void copyTo(byte[] target, int offset, long start, int length) {
        try {
            if ((offset >= target.length) || (offset < 0)) {
                throw new IndexOutOfBoundsException("Asked for offset " + offset + " but length is only " + target.length);
            }
            raf.seek(start);
            raf.read(target, offset, length);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void copyTo(byte[] target, int offset) {

        long length = size();
        if (length > Integer.MAX_VALUE) {
            throw new IndexOutOfBoundsException("length doesn't fit into an integer: " + length);
        }
        copyTo(target, offset, 0, (int) length);
    }

    @Override
    public long size() {
        try {
            return raf.length();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void retain() {
        rc++;
    }

    @Override
    public void release() {
        rc--;
        if (rc == 0) {
            try {
                raf.close();
                if (delete) {
                    f.delete();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (rc < 0) {
            throw new IllegalStateException("Object was already deallocated: " + raf);
        }
    }
    
    public long rc() { // for debugging only!
        return rc;
    }

    @Override
    public void copyTo(io.netty.buffer.ByteBuf buffer) {
        long length = size();
        if (length > Integer.MAX_VALUE) {
            throw new IndexOutOfBoundsException("length doesn't fit into an integer: " + length);
        }
        copyTo(buffer, 0, (int) length);
    }

    void copyTo(io.netty.buffer.ByteBuf buffer, long start, int length) {
        try {
            int written = -1;
            int startx = buffer.writerIndex();
            /* Note: there's a weird bug here, causing the buffer to sometimes
            * read nothing, although there's enough to read.
            * As a workaround: Keep trying until we get the desired amount of data
            */
            while (written < length) { 
                raf.seek(start);
                buffer.writerIndex(startx);
                FileChannel source = raf.getChannel();
                written = buffer.writeBytes(source, length);
//            if (written != length) {
//                System.err.println("Read from " + raf + " of size " + raf.length() 
//                        + " starting at " + start + " leaving " + (raf.length()-start) 
//                        + " to read " + length);
//                throw new RuntimeException("Buffer didn't write required bytes!");
//            }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Iterable<DataRef> split(long numberOfChunks, int chunkSize) {
        return new RAFRIterator(chunkSize);
    }

    public class RAFRIterator implements Iterator<DataRef>, Iterable<DataRef> {

        public long pos = 0;
        private final int chunkSize;
        private final long length;

        private RAFRIterator(int chunkSize) {
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
            PartialFileRef subarea = new PartialFileRef(pos, chunkLength, RAFileRef.this);
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
