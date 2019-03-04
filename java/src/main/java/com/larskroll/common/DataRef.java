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

import io.netty.buffer.ByteBuf;

/**
 * This things is getting damn ObjC-y with all the memory management.
 * But some things Java just can't be trusted to do by itself^^
 * 
 * @author lkroll
 */
public interface DataRef {
    
    /**
     * Think ARC in ObjC
     */
    public void retain();
    
    /**
     * Think ARC in ObjC
     */
    public void release();
    
    /**
     * Gives the size of the data pointed to.
     * 
     * @return 
     */
    public long size();
    
    /**
     * Extracts the referenced data into a new byte[]
     *
     * @return
     */
    public byte[] dereference();
    
    /**
     * Extracts a single byte from the data
     *
     * @param i position of the byte to extract
     * @return
     */
    public byte dereference(long i);
    
    /**
     * Extracts the range [start, end) of bytes from the data
     *
     * @param start position of the first byte to extract
     * @param end position of the first byte to exclude
     * @return 
     */
    public byte[] dereference(long start, long end);
    
    /**
     * Writes val to position i in the data.
     * 
     * @param i byte position to write to
     * @param val byte to write
     */
    public void assign(long i, byte val);
    
    /**
     * Copies newData to the referenced data starting at position start
     * @param start position to start from
     * @param newData data to write
     */
    public void assign(long start, byte[] newData);
    
    /**
     * Copies newData to the referenced data starting at position start
     * @param start position to start from
     * @param newData data to write
     */
    public void assign(long start, DataRef newData);
    
    /**
     * Copies the data referenced here into the target starting at offset
     *
     * @param target
     * @param offset
     */
    public void copyTo(DataRef target, long offset);
    
    /**
     * Copies the data referenced here into the target starting at offset
     *
     * @param target
     * @param offset
     */
    public void copyTo(byte[] target, int offset);
    
    /**
     * Move the whole content into the provided ByteBuf
     * 
     * @param buffer 
     */
    public void copyTo(ByteBuf buffer);
    
    /**
     * Returns an Iterable over DataRefs pointing to numberOfChunks subareas of this DataRef such that each subarea is no larger than chunkSize
     * 
     * Don't COPY if possible...
     * Also notice that all subareas should be exactly chunk_size except the last
     * 
     * And Don't forget: https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html#size--
     * Java is weird
     * 
     * @param numberOfChunks
     * @param chunkSize
     * @return 
     */
    public Iterable<DataRef> split(long numberOfChunks, int chunkSize);
}
