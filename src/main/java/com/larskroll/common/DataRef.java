/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.larskroll.common;

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
}
