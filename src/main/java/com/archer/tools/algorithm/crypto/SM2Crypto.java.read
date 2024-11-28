package com.archer.tools.algorithm.crypto;

import java.security.SecureRandom;
import java.util.Arrays;

import com.archer.math.EcPoint;
import com.archer.tools.algorithm.hash.SM3;

public class SM2Crypto {
	
	private static SecureRandom rand = new SecureRandom();
	private static final int digestSize = 32;
	
	public static SM2KeyPair genSM2KeyPair() {
		byte[] privateKey = new byte[digestSize];
		EcPoint pubPoint = EcPoint.mul(privateKey, EcPoint.CURVE_sm2p256v1);
		return new SM2KeyPair(privateKey, pubPoint.getEncoded());
	}
	
	public static byte[] getPublicKeyFromPrivateKey(byte[] privateKey) {
		return EcPoint.mul(privateKey, EcPoint.CURVE_sm2p256v1).getEncoded();
	}
	
	public static byte[] encrypt(byte[] publicKey, byte[] in, SM2CryptoMode mode) {	
		int inLen = in.length, pubOff = 0;
		if(publicKey.length == 65 && publicKey[0] == 4) {
			pubOff = 1;
		}
		EcPoint publicKeyPoint = 
				new EcPoint(Arrays.copyOfRange(publicKey, pubOff, pubOff + 32), Arrays.copyOfRange(publicKey, pubOff + 32, publicKey.length));
		
		byte[] c2 = new byte[inLen];
        System.arraycopy(in, 0, c2, 0, c2.length);
        
        byte[] k = nextK();
        byte[] c1 = EcPoint.mul(k, EcPoint.CURVE_sm2p256v1).getEncoded();
        EcPoint kPB = EcPoint.mulPoint(k, EcPoint.CURVE_sm2p256v1, publicKeyPoint);
        kdf(kPB, c2);

        byte[] kPBIn = new byte[kPB.getX().length + kPB.getY().length + inLen];
        System.arraycopy(kPB.getX(), 0, kPBIn, 0, kPB.getX().length);
        System.arraycopy(in, 0, kPBIn, kPB.getX().length, inLen);
        System.arraycopy(kPB.getY(), 0, kPBIn, kPB.getX().length + inLen, kPB.getY().length);
        byte[] c3 = SM3.hash(kPBIn);
        byte[] rv = new byte[c1.length + c2.length + c3.length];
        System.arraycopy(c1, 0, rv, 0, c1.length);
        switch (mode)
        {
        case C1C3C2:
            System.arraycopy(c3, 0, rv, c1.length, c3.length);
            System.arraycopy(c2, 0, rv, c1.length + c3.length, c2.length);
            break ;
        default:
            System.arraycopy(c2, 0, rv, c1.length, c2.length);
            System.arraycopy(c3, 0, rv, c1.length + c2.length, c3.length);
        }
        return rv;
	}
	
	public static byte[] decrypt(byte[] privateKey, byte[] in, SM2CryptoMode mode) {
		int inLen = in.length;
		
		byte[] c1 = new byte[digestSize * 2 + 1], c3 = new byte[digestSize], c2 = new byte[inLen - c1.length - c3.length];
        System.arraycopy(in, 0, c1, 0, c1.length);
        EcPoint c1P = EcPoint.decode(c1);
        c1P = EcPoint.mulPoint(privateKey, EcPoint.CURVE_sm2p256v1, c1P);
        
        if (mode == SM2CryptoMode.C1C3C2) {
            System.arraycopy(in, c1.length, c3, 0, c3.length);
            System.arraycopy(in, c1.length + c3.length, c2, 0, c2.length);
        } else {
            System.arraycopy(in, c1.length, c2, 0, c2.length);
            System.arraycopy(in, c1.length + c2.length, c3, 0, c3.length);
        }
        kdf(c1P, c2);

        byte[] c1PIn = new byte[c1P.getX().length + c1P.getY().length + c2.length];
        System.arraycopy(c1P.getX(), 0, c1PIn, 0, c1P.getX().length);
        System.arraycopy(c2, 0, c1PIn, c1P.getX().length, c2.length);
        System.arraycopy(c1P.getY(), 0, c1PIn, c1P.getX().length + c2.length, c1P.getY().length);
        byte[] c3Cal = SM3.hash(c1PIn);
        for(int i = 0; i < digestSize; i++) {
        	if(c3[i] != c3Cal[i]) {
                throw new IllegalArgumentException("invalid cipher text");
        	}
        }
        return c2;
	}
	
	private static byte[] nextK() {
        byte[] rv = new byte[32];
        rand.nextBytes(rv);
        if(rv[0] == 0) {
        	rv[0] = 1;
        }
        if(256 + rv[0] == 255) {
        	rv[0] = (byte) 254;
        }
        return rv;
	}
	
    private static void kdf(EcPoint c1, byte[] encData) {
        int prefix = c1.getX().length + c1.getY().length, off = 0, ct = 0;
        
        byte[] buf = null, in = new byte[prefix + 4];
        System.arraycopy(c1.getX(), 0, in, 0, c1.getX().length);
        System.arraycopy(c1.getY(), 0, in, c1.getX().length, c1.getY().length);
        
        while (off < encData.length) {
            ++ct;
            in[prefix] = (byte)(ct >>> 24);
            in[prefix+1] = (byte)(ct >>> 16);
            in[prefix+2] = (byte)(ct >>> 8);
            in[prefix+3] = (byte)(ct);
            buf = SM3.hash(in);

            int xorLen = Math.min(digestSize, encData.length - off);

            for (int i = 0; i != xorLen; i++) {
            	encData[off + i] ^= buf[i];
            }
            off += xorLen;
        }
    }
    
    public static enum SM2CryptoMode {
    	C1C2C3,
    	C1C3C2;
    }
    

	public static class SM2KeyPair {
		byte[] publicKey;
		byte[] privateKey;
		
		protected SM2KeyPair(byte[] publicKey, byte[] privateKey) {
			this.publicKey = publicKey;
			this.privateKey = privateKey;
		}
		
		public byte[] getPublicKey() {
			return publicKey;
		}
		public byte[] getPrivateKey() {
			return privateKey;
		}
	}
}
