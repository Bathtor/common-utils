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

import com.google.common.primitives.UnsignedBytes;
import com.google.common.primitives.UnsignedInts;
import java.util.Arrays;

/**
 * See also {se.sics.kompics.address.IdUtils}.
 * <p>
 * @author lkroll
 */
public abstract class ByteArrayFormatter {

    public static void toHexString(byte[] bytes, StringBuilder sb) {
        if (bytes == null) {
            sb.append("(null)");
            return;
        }

        for (int i = 0; i < bytes.length; i++) {
            String bStr = UnsignedBytes.toString(bytes[i], 16);
            if (bStr.length() == 1) {
                sb.append('0');
            }
            sb.append(bStr.toUpperCase());
        }

    }

    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }

    public static byte[] fromHexString(String str) {
        if (str.equals("(null)")) {
            return null;
        }
        if (str.equals("")) {
            return new byte[0];
        }
        String nospacestr = str.replaceAll("\\s", ""); // take away spaces
        if (UnsignedInts.remainder(nospacestr.length(), 2) != 0) {
            throw new NumberFormatException("String should contain only pairs of [0-F] (should be even)!");
        }
        int expectedLength = nospacestr.length() / 2;
        String[] byteBlocks = nospacestr.split("(?<=\\G.{2})"); // split in parts of length 2
        if (expectedLength != byteBlocks.length) {
            System.out.println("Blocks: " + Arrays.toString(byteBlocks));
            throw new NumberFormatException("String should contain only pairs of [0-F]!");
        }
        byte[] bytes = new byte[byteBlocks.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = UnsignedBytes.parseUnsignedByte(byteBlocks[i], 16);
        }
        return bytes;
    }

    public static void storeFormat(byte[] id, StringBuilder sb) {
        if (id == null) {
            return;
        }
        sb.append("0x");
        for (int i = 0; i < id.length; i++) {
            String bStr = UnsignedBytes.toString(id[i], 16);
            if (bStr.length() == 1) {
                sb.append('0');
            }
            sb.append(bStr.toUpperCase());
        }
    }

    public static String storeFormat(byte[] id) {
        StringBuilder sb = new StringBuilder();
        storeFormat(id, sb);
        return sb.toString();
    }

    public static byte[] parseStoreFormat(String str) {
        String[] bases = str.split("x");
        if ((bases.length == 2)
                && (bases[0].equals("0"))
                && (bases[1].length() > 0)
                && (bases[1].length() % 2 == 0)) {
            String parseStr = bases[1];
            byte[] res = new byte[parseStr.length() / 2];
            int i = 0;
            int j = 0;
            while (i < parseStr.length()) {
                String byteStr = parseStr.substring(i, i + 2);
                res[j] = UnsignedBytes.parseUnsignedByte(byteStr, 16);
                i += 2;
                j++;
            }
            return res;
        } else {
            throw new NumberFormatException("'" + str + "' can not be converted to id type byte[]");
        }
    }

    public static void printFormat(byte[] id, StringBuilder sb) {
        if (id == null) {
            return;
        }
        for (int i = 0; i < id.length; i++) {
            String bStr = UnsignedBytes.toString(id[i], 16);
            if (bStr.length() == 1) {
                sb.append('0');
            }
            sb.append(bStr.toUpperCase());
            if (i + 1 < id.length) { // No space at the end
                sb.append(' ');
            }
        }
    }

    public static String printFormat(byte[] id) {
        StringBuilder sb = new StringBuilder();
        printFormat(id, sb);
        return sb.toString();
    }
}
