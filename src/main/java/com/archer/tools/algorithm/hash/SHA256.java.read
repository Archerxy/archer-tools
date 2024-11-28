package com.archer.tools.algorithm.hash;

public class SHA256 {

    public static byte[] hash(byte[] data) {
        int h0 = 0x6a09e667;
        int h1 = 0xbb67ae85;
        int h2 = 0x3c6ef372;
        int h3 = 0xa54ff53a;
        int h4 = 0x510e527f;
        int h5 = 0x9b05688c;
        int h6 = 0x1f83d9ab;
        int h7 = 0x5be0cd19;
        
        int[] k = {0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
                0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
                0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
                0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
                0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
                0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
                0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
                0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2};
        
        int orig_len = data.length; 
        long orig_len_bits = orig_len * 8;

        byte[] with_one = new byte[orig_len+1];         
        System.arraycopy(data, 0, with_one, 0, orig_len);
        with_one[with_one.length - 1] = (byte) 0x80; 
        int new_length = with_one.length*8;

        while (new_length % 512 != 448) {       
            new_length += 8;
        }

        byte[] with_zeros = new byte[new_length/8];
        System.arraycopy(with_one, 0 , with_zeros, 0, with_one.length);

        byte[] output = new byte[with_zeros.length + 8];
        for (int i = 0; i < 8; i++) {
            output[output.length -1 - i] = (byte) ((orig_len_bits >>> (8 * i)) & 0xFF);
        } 
        System.arraycopy(with_zeros, 0 , output, 0, with_zeros.length);
        
        int size = output.length;
        int num_chunks = size * 8 /512;
        
        for (int i = 0; i < num_chunks; i++) {
            int[] w = new int[64];
            
            for (int j = 0; j < 16; j++) {
                w[j] =  ((output[i*512/8 + 4*j] << 24) & 0xFF000000) | ((output[i*512/8 + 4*j+1] << 16) & 0x00FF0000);
                w[j] |= ((output[i*512/8 + 4*j+2] << 8) & 0xFF00) | (output[i*512/8 + 4*j+3] & 0xFF);
            }
            for (int j = 16; j < 64; j++) {
                int s0 = right_rotate(w[j-15], 7) ^ right_rotate(w[j-15], 18) ^ (w[j-15] >>> 3);
                int s1 = right_rotate(w[j-2], 17) ^ right_rotate(w[j-2], 19) ^ (w[j-2] >>> 10);
                w[j] = w[j-16] + s0 + w[j-7] + s1;
            }
            
            int a = h0;
            int b = h1;
            int c = h2;
            int d = h3;
            int e = h4;
            int f = h5;
            int g = h6;
            int h = h7;
            
            for (int j = 0; j < 64; j++) {
                int S1 = right_rotate(e, 6) ^ right_rotate(e, 11) ^ right_rotate(e, 25);
                int ch = (e & f) ^ (~e & g);
                int temp1 = h + S1 + ch + k[j] + w[j];
                int S0 = right_rotate(a, 2) ^ right_rotate(a, 13) ^ right_rotate(a, 22);
                int maj = (a & b) ^ (a & c) ^ (b & c);
                int temp2 = S0 + maj;
                
                h = g;
                g = f;
                f = e;
                e = d + temp1;
                d = c;
                c = b;
                b = a;
                a = temp1 + temp2;
            }
                  
            h0 = h0 + a;
            h1 = h1 + b;
            h2 = h2 + c;
            h3 = h3 + d;
            h4 = h4 + e;
            h5 = h5 + f;
            h6 = h6 + g;
            h7 = h7 + h;          
        }
        
        byte[] hash = new byte[32];
        for (int j = 0; j < 4; j++) {
            hash[j] = (byte) ((h0 >>> 24-j*8) & 0xFF);
        }
        for (int j = 0; j < 4; j++) {
            hash[j+4] = (byte) ((h1 >>> 24-j*8) & 0xFF);
        }
        for (int j = 0; j < 4; j++) {
            hash[j+8] = (byte) ((h2 >>> 24-j*8) & 0xFF);
        }
        for (int j = 0; j < 4; j++) {
            hash[j+12] = (byte) ((h3 >>> 24-j*8) & 0xFF);
        }
        for (int j = 0; j < 4; j++) {
            hash[j+16] = (byte) ((h4 >>> 24-j*8) & 0xFF);
        }
        for (int j = 0; j < 4; j++) {
            hash[j+20] = (byte) ((h5 >>> 24-j*8) & 0xFF);
        }
        for (int j = 0; j < 4; j++) {
            hash[j+24] = (byte) ((h6 >>> 24-j*8) & 0xFF);
        }
        for (int j = 0; j < 4; j++) {
            hash[j+28] = (byte) ((h7 >>> 24-j*8) & 0xFF);
        }
        return hash;
    }
    
    private static int right_rotate(int n, int d) {
        return (n >>> d) | (n << (32 - d));
    }
}
