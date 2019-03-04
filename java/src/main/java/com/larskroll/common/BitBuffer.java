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
