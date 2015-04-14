/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.larskroll.common;

import com.google.common.primitives.UnsignedBytes;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author lkroll
 */
public class BitBuffer {

    private static final int ZERO = 0;
    private static final int[] POS = {1, 2, 4, 8, 16, 32, 64, 128};

    private final ArrayList<Boolean> buffer = new ArrayList<Boolean>();

    private BitBuffer() {
    }

    public static BitBuffer create(Boolean... args) {
        BitBuffer b = new BitBuffer();
        b.buffer.addAll(Arrays.asList(args));
        return b;
    }

    public BitBuffer write(Boolean... args) {
        buffer.addAll(Arrays.asList(args));
        return this;
    }

    public byte[] finalise() {
        int numBytes = (int) Math.ceil(((double) buffer.size()) / 8.0);
        byte[] bytes = new byte[numBytes];
        for (int i = 0; i < numBytes; i++) {
            int b = ZERO;
            for (int j = 0; j < 8; j++) {
                int pos = i * 8 + j;
                if (buffer.size() > pos) {
                    if (buffer.get(pos)) {
                        b = b ^ POS[j];
                    }
                }
            }
            bytes[i] = UnsignedBytes.checkedCast(b);
        }
        return bytes;
    }

    public static boolean[] extract(int numValues, byte[] bytes) {
        assert (((int) Math.ceil(((double) numValues) / 8.0)) <= bytes.length);

        boolean[] output = new boolean[numValues];
        for (int i = 0; i < bytes.length; i++) {
            int b = bytes[i];
            for (int j = 0; j < 8; j++) {
                int pos = i * 8 + j;
                if (pos >= numValues) {
                    return output;
                }
                output[pos] = ((b & POS[j]) != 0);
            }
        }

        return output;
    }
}
