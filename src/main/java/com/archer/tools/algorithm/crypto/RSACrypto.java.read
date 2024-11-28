package com.archer.tools.algorithm.crypto;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

import com.archer.math.MathLib;

public class RSACrypto {
	private static final int BLOCK_SIZE = 117;
	
	public static final int BITS = 1024;
	public static final int BYTES = BITS >> 3;
	
	/**
	 * @return publicKey + privateKey
	 * */
	public static RsaKeyPair genKeyPair() {
		BigInteger[] edn = genEDN();
		byte[] e = edn[0].toByteArray(), d = edn[1].toByteArray(), n = edn[2].toByteArray();
		int l = BYTES << 1;
		byte[] pub = new byte[l], pri = new byte[l];
		System.arraycopy(n, 0, pub, BYTES - n.length, n.length);
		System.arraycopy(e, 0, pub, l - e.length, e.length);
		System.arraycopy(n, 0, pri, BYTES - n.length, n.length);
		System.arraycopy(d, 0, pri, l - d.length, d.length);
		return new RsaKeyPair(pub, pri);
	}
	
	public static byte[] encrypt(byte[] pub, byte[] message) {
		byte[] n = Arrays.copyOfRange(pub, 0, BYTES), e = Arrays.copyOfRange(pub, BYTES, BYTES << 1);
		int off = BLOCK_SIZE, size = message.length / BLOCK_SIZE, index = BYTES;
		if(message.length % BLOCK_SIZE > 0) {
			size++;
		}
		byte[] cipher = new byte[size * BYTES];
		while(off <= message.length) {
			byte[] m = Arrays.copyOfRange(message, off - BLOCK_SIZE, off);
			byte[] c = encrypt(e, n, m);
			System.arraycopy(c, 0, cipher, index - c.length, c.length);
			off += BLOCK_SIZE;
			index += BYTES;
		}
		if(off > message.length) {
			int l = off - BLOCK_SIZE;
			byte[] m = Arrays.copyOfRange(message, l, message.length);
			byte[] c = encrypt(e, n, m);
			System.arraycopy(c, 0, cipher, index - c.length, c.length);
		}
		return cipher;
	}
	
	public static byte[] decrypt(byte[] pri, byte[] cipher) {
		byte[] n = Arrays.copyOfRange(pri, 0, BYTES), d = Arrays.copyOfRange(pri, BYTES, BYTES << 1);
		int off = 0, size = cipher.length / BYTES;
		byte[] message = new byte[size * BLOCK_SIZE];
		for(int index = 0; index < cipher.length; index += BYTES) {
			byte[] c = Arrays.copyOfRange(cipher, index, index + BYTES);
			byte[] m = decrypt(d, n, c);
			int i = 0;
			while(m[i] == 0) {
				i++;
			}
			System.arraycopy(m, i, message, off, m.length - i);
			off += m.length - i;
		}
		return Arrays.copyOfRange(message, 0, off);
	}
	
	
	private static BigInteger[] genEDN() {
		SecureRandom sr = new SecureRandom();
		BigInteger p1 = BigInteger.probablePrime((BITS >> 1) - 1, sr);
		BigInteger p2 = BigInteger.probablePrime((BITS >> 1) - 1, sr);
		BigInteger n = p1.multiply(p2);
		BigInteger fiN = p1.subtract(BigInteger.ONE).multiply(p2.subtract(BigInteger.ONE));
		BigInteger e = BigInteger.probablePrime(fiN.bitLength(), sr);
		while(e.compareTo(fiN) >= 0) {
			e = BigInteger.probablePrime(fiN.bitLength() - 1, sr);
		}
		BigInteger d = e.modInverse(fiN);
		return new BigInteger[] {e, d, n};
	}
	
	private static byte[] encrypt(byte[] e, byte[] n, byte[] message) {
		return MathLib.powm(message, e, n);
	}

	private static byte[] decrypt(byte[] d, byte[] n, byte[] cipher) {
		return MathLib.powm(cipher, d, n);
	}
	
	public static class RsaKeyPair {
		byte[] publicKey;
		byte[] privateKey;
		
		protected RsaKeyPair(byte[] publicKey, byte[] privateKey) {
			super();
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
