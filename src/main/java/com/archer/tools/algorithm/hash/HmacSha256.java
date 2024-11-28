package com.archer.tools.algorithm.hash;


public class HmacSha256 {
    public static final int[] _5C = new int[256];

    public static final int[] _36 = new int[256];

    static {
        for(int i = 0; i < 256; ++i) {
            _5C[i] = i^0x5c;
            _36[i] = i^0x36;
        }
    }

    static byte[] translate(byte[] bs, int[] trans) {
        byte[] out = new byte[bs.length];
        for(int i = 0; i < bs.length; ++i) {
            int k = bs[i];
            if(k < 0) {
                k = k+256;
            }
            out[i] = (byte)trans[k];
        }
        return out;
    }

    /**
     * @param privKey private key content in bytes.
     * @param hash hash content in bytes.
     *
     * @return bytes get mystic hash bytes from private key and hash content.
     * */
    public static byte[] hmac(byte[] privKey, byte[] hash) {
        byte[] priv = new byte[64];

        System.arraycopy(privKey, 0, priv, 0, 32);

        byte[] innerBytes = SHA256.hash(concatBytes(translate(priv,_36), hash));
        return SHA256.hash(concatBytes(translate(priv,_5C), innerBytes));
    }

    public static byte[] concatBytes(byte[] ...bytes) {
        int l = 0;
        for(byte[] bs: bytes) {
            l += bs.length;
        }
        byte[] out = new byte[l];
        int s = 0;
        for(byte[] bs: bytes) {
            System.arraycopy(bs, 0, out, s, bs.length);
            s += bs.length;
        }
        return out;
    }
}


