package com.archer.tools.psi;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.archer.math.MathLib;
import com.archer.tools.algorithm.hash.SHA256;


public class RSAPsi {

	public static final int BITS = 512;
	private static final int RAND_LEN = 32;
	private static final int BYTES = BITS >> 3;
	
	/**
	 * 1. First step.
	 * We generate RSA public key and private key.
	 * Client holds both keys.
	 * Then, client send publicKey to server.
	 * 
	 * return byte[][] {publicKey,privateKey}
	 * */
	public static byte[][] genKeyPair() {
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
		
		byte[] eb = e.toByteArray(), db = d.toByteArray(), nb = n.toByteArray();
		int l = BYTES << 1;
		byte[] pub = new byte[l], pri = new byte[l];
		System.arraycopy(nb, 0, pub, BYTES - nb.length, nb.length);
		System.arraycopy(eb, 0, pub, l - eb.length, eb.length);
		System.arraycopy(nb, 0, pri, BYTES - nb.length, nb.length);
		System.arraycopy(db, 0, pri, l - db.length, db.length);
		return new byte[][] {pub, pri};
	}
	
	/**
	 * 2. Second step.
	 * Server gets the public key in step 1.(publicKey = {n, e})
	 * Then, for each element(x) in the server's set, we randomly generate r to calculate u = hash512(x) * (r ^ x), mod n.
	 * At last, we get to data set which are set-r(contains randomly generated r) and set-u(which are calculated with hash512(x) * (r ^ e), mod n)
	 * Send set-u to client.
	 * 
	 * return Pair.p0 = uSet, Pair.p1 = rSet
	 * */
	public static Pair server0(byte[] pk, List<byte[]> set) {
		byte[] n = Arrays.copyOfRange(pk, 0, BYTES);
		byte[] e = Arrays.copyOfRange(pk, BYTES, BYTES << 1);
		ArrayList<byte[]> uSet = new ArrayList<>(set.size());
		ArrayList<byte[]> rSet = new ArrayList<>(set.size());

		Random rand = new Random();
		byte[] ran = new byte[RAND_LEN];
		rand.nextBytes(ran);
		for(byte[] s: set) {
			byte[] h = SHA256.hash(s);
			byte[] r = new byte[RAND_LEN];
			System.arraycopy(ran, 0, r, 0, RAND_LEN);
			
			rSet.add(r);
			byte[] u = MathLib.mulm(h, MathLib.powm(r, e, n), n);
			uSet.add(u);
		}
		return new Pair(uSet, rSet);
	}

	/**
	 * 3. Third step.
	 * Client gets data set set-u in step 2. Also, Client still holds publicKey and privateKey {d, n}.
	 * First, the RSA algorithm shows that m ^ e = c mod n and c ^ d = m mod n.
	 * So, we get z = u ^ d = (hash512(x) * (r ^ e)) ^ d = r * (hash512(x) ^ d), mod n.
	 * Moreover, for each element(y) in the client's set, we calculate b = hash512(hash512(y) ^ d, mod n).
	 * At last, we send both data sets(set-z and set-b) to client.
	 * 
	 * return Pair.p0 = zSet, Pair.p1 = bSet
	 * */
	public static Pair client1(byte[] sk, List<byte[]> uSet, List<byte[]> set) {
		byte[] n = Arrays.copyOfRange(sk, 0, BYTES);
		byte[] d = Arrays.copyOfRange(sk, BYTES, BYTES << 1);
		ArrayList<byte[]> zSet = new ArrayList<byte[]>(uSet.size());
		ArrayList<byte[]> bSet = new ArrayList<>(set.size());
		for(byte[] u: uSet) {
			byte[] z = MathLib.powm(u, d, n);
			zSet.add(z);
		}
		for(byte[] s: set) {
			byte[] h = SHA256.hash(s);
			byte[] b = MathLib.powm(h, d, n);
			bSet.add(b);
		}
		return new Pair(zSet, bSet);
	}
	
	
	/**
	 * 4. Forth step.
	 * Server gets both data sets(set-z and set-b) in step 3.
	 * Then, for each element(z) in set-z, we calculate a = z / r = (r * (hash512(x) ^ d)) / r = hash512(x) ^ d, mod n.
	 * The element(b) in set-b is hash512(hash512(x) ^ d, mod n).
	 * At last, we just need to calculate hash512(a) = hash12(hash512(x) ^ d, mod n), and compare it to b.
	 * 
	 * return set-a.
	 * */
	public static ArrayList<byte[]> server2(byte[] pk, List<byte[]> zSet, List<byte[]> rSet) {
		byte[] n = Arrays.copyOfRange(pk, 0, BYTES);
		byte[][] rArr = new byte[rSet.size()][];
		int i = 0;
		for(byte[] r: rSet) {
			rArr[i++] = r;
		}
		ArrayList<byte[]> aSet = new ArrayList<>(zSet.size());
		i = 0;
		for(byte[] z: zSet) {
			byte[] a = MathLib.mulInvm(z, rArr[i++], n);
			aSet.add(a);
		}
		return aSet;
	}
	
	public static ArrayList<byte[]> compare(List<byte[]> aSet, List<byte[]> bSet) {
		ArrayList<byte[]> psiSet;
		if(aSet.size() < bSet.size()) {
			psiSet = new ArrayList<>(aSet.size());
			Intersection insec = new Intersection(aSet);
			for(byte[] b: bSet) {
				if(insec.contains(b)) {
					psiSet.add(b);
				}
			}
		} else {
			psiSet = new ArrayList<>(bSet.size());
			Intersection insec = new Intersection(bSet);
			for(byte[] a: aSet) {
				if(insec.contains(a)) {
					psiSet.add(a);
				}
			}
		}
		return psiSet;
	}
	
	/**
	 * Here, we simply use array(as a map) and bloom filter to get the inspection.
	 * */
	static class Intersection {
		
		private static final int P = 32;
		private static final int C = 23;
		
		N[] map;
		
		//512 bits input
		public Intersection(List<byte[]> set) {
			int m = set.size() / P + 1;
			map = new N[m];
			for(byte[] s: set) {
				long p = 0;
				for(int i = 0; i < C; i++) {
					long l = (long) s[i];
					if(l < 0) {
						l += 256;
					}
					p |= (l << ((C - i) << 3));
				}
				if(p < 0) {
					p = -p;
				}
				int r = (int) (p % m);
				if(map[r] == null) {
					map[r] = new N(s);
				} else {
					N cur = new N(s);
					map[r].next = cur;
					cur.last = map[r];
					map[r] = cur;
				}
			}
		}
		
		public boolean contains(byte[] bs) {
			long p = 0;
			for(int i = 0; i < C; i++) {
				long l = (long) bs[i];
				if(l < 0) {
					l += 256;
				}
				p |= (l << ((C - i) << 3));
			}
			if(p < 0) {
				p = -p;
			}
			int m = map.length;
			int r = (int) (p % m);
			N cur = map[r];
			while(cur != null) {
				byte[] d = cur.data;
				if(d.length == bs.length) {
					boolean ok = true;
					for(int i = 0; i < bs.length; i++) {
						if(d[i] != bs[i]) {
							ok = false;
							break; 
						}
					}
					if(ok) {
						return true;
					}
				}
				cur = cur.last;
			}
			return false;
		}
	}
	
	static class N {
		byte[] data;
		N last;
		N next;
		
		public N(byte[] data) {
			this.data = data;
		}
	}
}

