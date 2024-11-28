package com.archer.tools.algorithm.hash;

public class SHA1 {

	private static int bs = 64;
	
    public static byte[] hash(byte[] in) {
    	return computeHash(pad(in));
    }

    private static byte[][] pad(byte[] in) {
        int cb = in.length / bs;
        int res = in.length % bs;
        int eb = 0;
        if (res > 56)
            eb = 1;
        byte[][] data = new byte[cb + eb + 1][bs];
        for (int i = 0; i < cb; i++)
            for (int j = 0; j < bs; j++)
                data[i][j] = in[j + bs * i];
        for (int i = 0; i < res; i++)
            data[cb][i] = in[i + cb * bs];
        data[cb][res] = Byte.MIN_VALUE;
        int n = in.length * 8;
        byte[] size = {(byte) ((n >> 24) & 0xff), (byte) ((n >> 16) & 0xff), (byte) ((n >> 8) & 0xff), (byte) (n & 0xff)};
        for (int i = 0; i < size.length; i++)
            data[cb + eb][bs - 4 + i] = size[i];
        return data;
    }

    private static byte[] computeHash(byte[][] data) {
        int[] h = new int[5];
        h[0] = 0x67452301;
        h[1] = 0xEFCDAB89;
        h[2] = 0x98BADCFE;
        h[3] = 0x10325476;
        h[4] = 0xC3D2E1F0;
        int a, b, c, d, e, f = 0, k = 0, temp;
        for (int i = 0; i < data.length; i++) {
            int[] w = new int[80];
            for (int j = 0; j < 16; j++)
                w[j] = (((data[i][j * 4] & 0xff) << 8 | (data[i][4 * j + 1] & 0xff)) << 8 | (data[i][4 * j + 2] & 0xff)) << 8 | (data[i][4 * j + 3] & 0xff);
            for (int j = 16; j < 80; j++) {
                w[j] = w[j - 3] ^ w[j - 8] ^ w[j - 14] ^ w[j - 16];
                w[j] = leftCyclicShift(w[j],1);
            }

            a = h[0];
            b = h[1];
            c = h[2];
            d = h[3];
            e = h[4];

            for (int j = 0; j < 80; j++) {

                if ((j >= 0) && (j <= 19)) {
                    f = (b & c) | ((~b) & d);
                    k = 0x5A827999;
                }
                if ((j >= 20) && (j <= 39)) {
                    f = b ^ c ^ d;
                    k = 0x6ED9EBA1;
                }
                if ((j >= 40) && (j <= 59)) {
                    f = (b & c) | (b & d) | (c & d);
                    k = 0x8F1BBCDC;
                }
                if ((j >= 60) && (j <= 79)) {
                    f = b ^ c ^ d;
                    k = 0xCA62C1D6;
                }
                temp = leftCyclicShift(a,5) + f + e + k + w[j];
                e = d;
                d = c;
                c = leftCyclicShift(b,30);
                b = a;
                a = temp;
            }
            h[0] += a;
            h[1] += b;
            h[2] += c;
            h[3] += d;
            h[4] += e;
        }
        byte[] result = new byte[20];
        byte[] cache;
        for (int i = 0; i < 5; i++) {
            cache = new byte[] {(byte) ((h[i] >> 24) & 0xff), (byte) ((h[i] >> 16) & 0xff), (byte) ((h[i] >> 8) & 0xff), (byte) (h[i] & 0xff)};
            for (int j = 0; j < 4; j++)
                result[(i * 4) + j] = cache[j];
        }
        return result;
    }

    private static int leftCyclicShift(int x, int count) {
        int temp;
        for (int i = 0; i < count; i++) {
            temp = x >>> 31;
            x = x << 1 | temp;
        }
        return x;
    }
}
