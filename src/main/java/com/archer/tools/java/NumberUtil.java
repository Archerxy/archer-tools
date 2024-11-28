package com.archer.tools.java;

import java.math.BigInteger;
import java.util.Arrays;

public final class NumberUtil {
	
	private static final int DEFAULT_BYTE = 0x7f;
	
	private static final int[] hexToByteTable = new int[128];
	
	private static final char[] byteToHexTable = {
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };
	    
    static {
    	for(int i = 0; i < hexToByteTable.length; ++i)
    		hexToByteTable[i] = DEFAULT_BYTE; 
    	for(int i = 0; i < byteToHexTable.length; ++i)
    		hexToByteTable[byteToHexTable[i]] = i;
    }
	
	public static byte[] hexStrToBytes(String hexStr) {
		if(null == hexStr || hexStr.isEmpty())
			return new byte[0];
		String hex = hexStr.toLowerCase();
		if (hex.startsWith("0x"))
			hex = hex.substring(2);
		if(hex.length()%2 == 1)
			hex = "0"+hex;
		byte[] out = new byte[hex.length()>>1];
		for(int i = 0; i < hex.length(); i += 2) {
			char c1 = hex.charAt(i), c2 = hex.charAt(i+1);
			if(c1 < 0 || c1 > 128 || c2 < 0 || c2 > 128 || hexToByteTable[c1] == DEFAULT_BYTE || hexToByteTable[c2] == DEFAULT_BYTE)
				throw new RuntimeException("Invalid hex string, "+hexStr);
			out[i>>1] = (byte) ((hexToByteTable[c1]<<4)|hexToByteTable[c2]);
		}
		return out;
	}

	public static String bytesToHexStr(byte[] bs) {
		if(null == bs || bs.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for(byte b: bs) {
			int bi = b;
			if(bi < 0)
				bi = 256+bi;
			int b1 = (bi>>4), b2 = bi&0b1111;
			sb.append(byteToHexTable[b1]);
			sb.append(byteToHexTable[b2]);
		}
		return sb.toString();
	}

	public static BigInteger bytesToBigInt(byte[] bs) {
		if(null == bs || bs.length == 0)
			return BigInteger.ZERO;
		BigInteger num = BigInteger.ZERO;
		for(byte b: bs) {
			int n = b;
			if(n < 0)
				n += 256;
			num = num.shiftLeft(8).add(BigInteger.valueOf(n));
		}
		return num;
	}

	public static byte[] bigIntToBytes(BigInteger n) {
		byte[] bs = n.toByteArray();
		if(bs[0] == 0)
			return Arrays.copyOfRange(bs, 1, bs.length);
		return bs;
	}

	public static void intToBytes(int num, byte[] bytes, int off) {
		bytes[off++] = (byte) (0xff & (num >> 24));
		bytes[off++] = (byte) (0xff & (num >> 16));
		bytes[off++] = (byte) (0xff & (num >> 8));
		bytes[off] = (byte) (0xff & (num));
	}
	
	public static byte[] intToBytes(int num) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (0xff & (num >> 24));
		bytes[1] = (byte) (0xff & (num >> 16));
		bytes[2] = (byte) (0xff & (num >> 8));
		bytes[3] = (byte) (0xff & (num));
		return bytes;
	}
	
    public static int bytesToInt(byte[] bs, int off) {
        int n = bs[off] << 24;
        n |= (bs[++off] & 0xff) << 16;
        n |= (bs[++off] & 0xff) << 8;
        n |= (bs[++off] & 0xff);
        return n;
    }
    
	public static int bytesToInt(byte[] bytes) {
		return bytesToInt(bytes, 0);
	}

	public static byte[] longToBytes(long num) {
		byte[] bytes = new byte[8];
		for (int i = 0; i < 8; i++) {
			bytes[i] = (byte) (0xff & (num >> ((7 - i) * 8)));
		}

		return bytes;
	}
	
	public static byte[] intArrToBytes(int[] ints) {
        byte[] bs = new byte[ints.length * 4];
        for(int i = 0; i < ints.length; i++) {
        	bs[i*4] = (byte) (ints[i] >> 24);
        	bs[i*4 + 1] = (byte) ((ints[i] >> 16) & 0xff);
        	bs[i*4 + 1] = (byte) ((ints[i] >> 8) & 0xff);
        	bs[i*4 + 1] = (byte) (ints[i] & 0xff);
        }
        return bs;
	}
	
	public static int[] bytesToIntArr(byte[] bs) {
		int off = bs.length % 4, s = 0, len = bs.length >> 2;
		int[] ints;
		if(off == 0) {
			ints = new int[len];
		} else {
			ints = new int[len + 1];
			s = 1;
			for(int i = 0; i < off; i++) {
				int bi = bs[i];
				bi = bi < 0 ? bi + 256 : bi;
				ints[0] |= bi << ((off - i - 1) << 3);
			}
		}
		for(int i = s; i < ints.length; i++) {
			int pi = (i - s) << 2;
			int i0 = bs[pi + off], i1 = bs[pi + off + 1], i2 = bs[pi + off + 2], i3 = bs[pi + off + 3];
			i0 = i0 < 0 ? i0 + 256 : i0;
			i1 = i1 < 0 ? i1 + 256 : i1;
			i2 = i2 < 0 ? i2 + 256 : i2;
			i3 = i3 < 0 ? i3 + 256 : i3;
			ints[i] = (i0 << 24) | (i1 << 16) | (i2 << 8) | i3;
		}
		return ints;
	}
}

