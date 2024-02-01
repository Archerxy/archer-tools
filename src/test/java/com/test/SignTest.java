package com.test;

import java.math.BigInteger;
import java.util.Arrays;

import com.test.sig.EcCurve;
import com.test.sig.Ecdsa;
import com.test.sig.SM2;
import com.archer.tools.algorithm.signature.Signature;
import com.archer.tools.util.NumberUtil;

public class SignTest {
	
	public static void getBytes() {
		BigInteger P = new BigInteger("115792089237316195423570985008687907853269984665640564039457584007908834671663");
		BigInteger N = new BigInteger("115792089237316195423570985008687907852837564279074904382605163141518161494337");
		BigInteger A = new BigInteger("0");
		BigInteger B = new BigInteger("7");
		BigInteger Gx = new BigInteger("55066263022277343669578718895168534326250603453777594175500187360389116729240");
		BigInteger Gy = new BigInteger("32670510020758816978083085130507043184471273380659243275938904335757337482424");


//		BigInteger P = new BigInteger("115792089210356248756420345214020892766250353991924191454421193933289684991999");
//		BigInteger N = new BigInteger("115792089210356248756420345214020892766061623724957744567843809356293439045923");
//		BigInteger A = new BigInteger("115792089210356248756420345214020892766250353991924191454421193933289684991996");
//		BigInteger B = new BigInteger("18505919022281880113072981827955639221458448578012075254857346196103069175443");
//		BigInteger Gx = new BigInteger("22963146547237050559479531362550074578802567295341616970375194840604139615431");
//		BigInteger Gy = new BigInteger("85132369209828568825618990617112496413088388631904505083283536607588877201568");
		
		byte[] p = P.toByteArray(), n = N.toByteArray(), a = A.toByteArray();
		byte[] b = B.toByteArray(), gx = Gx.toByteArray(), gy = Gy.toByteArray();
//		int[] pi = new int[p.length], ni = new int[n.length];
//		int[] ai = new int[a.length], bi = new int[b.length];
//		int[] gxi = new int[gx.length], gyi = new int[gy.length];
		
		System.out.println(Arrays.toString(p));
		System.out.println(Arrays.toString(n));
		System.out.println(Arrays.toString(a));
		System.out.println(Arrays.toString(b));
		System.out.println(Arrays.toString(gx));
		System.out.println(Arrays.toString(gy));
//		
//		int i = 0, off = 0;
//		while(p[i] == 0) {
//			i++;
//		}
//		off = i;
//		for(; i < p.length; i++) {
//			pi[i] = p[i];
//			if(pi[i] < 0) {
//				pi[i] = 256 + pi[i];
//			}
//		}
//		pi = Arrays.copyOfRange(pi, off, pi.length);
//		
//		i = off = 0;
//		while(n[i] == 0) {
//			i++;
//		}
//		off = i;
//		for(; i < n.length; i++) {
//			ni[i] = n[i]; 
//			if(ni[i] < 0) {
//				ni[i] = 256 + ni[i];
//			}
//		}
//		ni = Arrays.copyOfRange(ni, off, ni.length);
//		
//
//		i = off = 0;
//		while(a[i] == 0) {
//			i++;
//		}
//		off = i;
//		for(; i < a.length; i++) {
//			ai[i] = a[i]; 
//			if(ai[i] < 0) {
//				ai[i] = 256 + ai[i];
//			}
//		}
//		ai = Arrays.copyOfRange(ai, off, ai.length);
//		
//
//		i = off = 0;
//		while(b[i] == 0) {
//			i++;
//		}
//		off = i;
//		for(; i < b.length; i++) {
//			bi[i] = b[i];
//			if(bi[i] < 0) {
//				bi[i] = 256 + bi[i];
//			} 
//		}
//		bi = Arrays.copyOfRange(bi, off, bi.length);
//		
//		
//
//		i = off = 0;
//		while(gx[i] == 0) {
//			i++;
//		}
//		off = i;
//		for(; i < gx.length; i++) {
//			gxi[i] = gx[i]; 
//			if(gxi[i] < 0) {
//				gxi[i] = 256 + gxi[i];
//			} 
//		}
//		gxi = Arrays.copyOfRange(gxi, off, gxi.length);
//
//
//		i = off = 0;
//		while(gy[i] == 0) {
//			i++;
//		}
//		off = i;
//		for(; i < gy.length; i++) {
//			gyi[i] = gy[i]; 
//			if(gyi[i] < 0) {
//				gyi[i] = 256 + gyi[i];
//			} 
//		}
//		gyi = Arrays.copyOfRange(gyi, off, gyi.length);
//
//		
//		System.out.println(Arrays.toString(pi));
//		System.out.println(Arrays.toString(ni));
//		System.out.println(Arrays.toString(ai));
//		System.out.println(Arrays.toString(bi));
//		System.out.println(Arrays.toString(gxi));
//		System.out.println(Arrays.toString(gyi));
//
//		System.out.println(pi.length);
//		System.out.println(ni.length);
//		System.out.println(ai.length);
//		System.out.println(bi.length);
//		System.out.println(gxi.length);
//		System.out.println(gyi.length);
	}
	
	public static void signTest() {

		byte[] d = {29, -3, 74, 47, 123, 64, 41, 123, 67, -9, 89, 16, 84, 115, 18, -8, -41, -97, -57, 36, 103, 60, 115, -123, -5, -38, -97, 127, 32, -21, -25, 2};
		byte[] pk = {36, 117, -87, 86, -21, 0, 78, 37, -128, -38, -1, -36, -74, -16, 60, -55, -46, 47, -29, -101, 95, 53, 113, 31, 0, 37, -46, 89, -70, -126, 10, -86, 44, -69, -127, -11, -19, 120, -83, 90, 46, 81, 15, -101, -16, -87, -106, -67, -33, -23, 18, 54, -67, 36, 99, 11, 59, -73, -96, 99, -98, 95, -115, -68};
		
		Signature sig = new Signature(Signature.PopularCurve.secp256k1);
		Ecdsa ec = Ecdsa.from(EcCurve.SEC_P256_K1);
		
		byte[] rs = sig.sign(d, d);
		System.out.println("ec sig1 = "+Arrays.toString(rs));
		System.out.println("ec ver1 = "+sig.verify(pk, d, rs));
		System.out.println("ec ver2 = "+ec.verify(pk, d, rs));
		
		byte[] result2 = ec.sign(d, d);
		System.out.println("ec sig2 = "+Arrays.toString(result2));
		System.out.println("ec ver2 = "+ec.verify(pk, d, rs));
		System.out.println("ec ver1 = "+sig.verify(pk, d, rs));
	}
	
	public static  void sm2Test() {
		byte[] d = {29, -3, 74, 47, 123, 64, 41, 123, 67, -9, 89, 16, 84, 115, 18, -8, -41, -97, -57, 36, 103, 60, 115, -123, -5, -38, -97, 127, 32, -21, -25, 2};
		byte[] pk = {124, -111, 78, 61, -127, 10, -126, -115, 18, -118, 16, 64, 63, -12, 77, 32, 8, 95, -32, 73, 36, 98, 63, -81, -1, -112, -45, -87, -119, -31, -91, -5, -76, 120, -20, -101, -57, 45, -115, -110, -52, -50, 83, -74, -117, -113, -38, -51, -125, 18, -42, -84, 59, -33, -105, -3, 23, -8, 83, 51, 45, 74, -31, -105};
		
		
		Signature sig = new Signature(Signature.PopularCurve.sm2p256v1);
		SM2 sm2 = new SM2();
		
		
		byte[] rs = sig.sign(d, d);
		System.out.println("sm2 sig1 = "+Arrays.toString(rs));
		System.out.println("sm2 ver1 = "+sig.verify(pk, d, rs));
		System.out.println("sm2 ver2 = "+sm2.verify(pk, d, rs));
		
		byte[] sm2RS = sm2.sign(d, d);
		System.out.println("sm2 sig2 = "+Arrays.toString(sm2RS));
		System.out.println("sm2 ver2 = "+sm2.verify(pk, d, rs));
		System.out.println("sm2 ver1 = "+sig.verify(pk, d, rs));
	}
	
	public static void signCostTest() {

		byte[] d = {29, -3, 74, 47, 123, 64, 41, 123, 67, -9, 89, 16, 84, 115, 18, -8, -41, -97, -57, 36, 103, 60, 115, -123, -5, -38, -97, 127, 32, -21, -25, 2};
		byte[] pk = {36, 117, -87, 86, -21, 0, 78, 37, -128, -38, -1, -36, -74, -16, 60, -55, -46, 47, -29, -101, 95, 53, 113, 31, 0, 37, -46, 89, -70, -126, 10, -86, 44, -69, -127, -11, -19, 120, -83, 90, 46, 81, 15, -101, -16, -87, -106, -67, -33, -23, 18, 54, -67, 36, 99, 11, 59, -73, -96, 99, -98, 95, -115, -68};
		
		
		Ecdsa ec = Ecdsa.from(EcCurve.SEC_P256_K1);
		Signature sig = new Signature(Signature.PopularCurve.secp256k1);
		
		int total = 100;
		
		for(int i =0; i < total; i++) {
			byte[] rs = sig.sign(d, d);
			if(!sig.verify(pk, d, rs)) {
				System.out.println("rs = " + Arrays.toString(rs));
				System.out.println("sig sig failed.");
			}
			if(!ec.verify(pk, d, rs)) {
				System.out.println("sig ecc failed.");
			}
		}
		
		for(int i =0; i < total; i++) {
			byte[] rs = ec.sign(d, d);
			if(!ec.verify(pk, d, rs)) {
				System.out.println("ecc ecc failed.");
			}
			if(!sig.verify(pk, d, rs)) {
				System.out.println("ecc sig failed.");
			}
		}
		
//		long t0 = System.currentTimeMillis();
//		for(int i =0; i < total; i++) {
//			sm2.sign(d, d);
//		}
//		long t1 = System.currentTimeMillis();
//		System.out.println("cost1 = " + (t1 - t0));
//		long t2 = System.currentTimeMillis();
//		for(int i =0; i < total; i++) {
//			sig.sign(d, d);
//		}
//		long t3 = System.currentTimeMillis();
//		System.out.println("cost2 = " + (t3 - t2));
	}
	
	public static void costTest() {
		byte[] d = {29, -3, 74, 47, 123, 64, 41, 123, 67, -9, 89, 16, 84, 115, 18, -8, -41, -97, -57, 36, 103, 60, 115, -123, -5, -38, -97, 127, 32, -21, -25, 2};
		byte[] pk = {124, -111, 78, 61, -127, 10, -126, -115, 18, -118, 16, 64, 63, -12, 77, 32, 8, 95, -32, 73, 36, 98, 63, -81, -1, -112, -45, -87, -119, -31, -91, -5, -76, 120, -20, -101, -57, 45, -115, -110, -52, -50, 83, -74, -117, -113, -38, -51, -125, 18, -42, -84, 59, -33, -105, -3, 23, -8, 83, 51, 45, 74, -31, -105};
		
		
		SM2 sm2 = new SM2();
		Signature sig = new Signature(Signature.PopularCurve.sm2p256v1);
		
		int total = 10000;
		
		for(int i =0; i < total; i++) {
			byte[] rs = sig.sign(d, d);
			if(!sig.verify(pk, d, rs)) {
				System.out.println(i + " rs = " + NumberUtil.bytesToHexStr(rs));
				System.out.println("sig sig failed. i = " + i);
			}
			if(!sm2.verify(pk, d, rs)) {
				System.out.println("sig sm2 failed. i = " + i);
			}
		}
//		7c914e3d810a828d128a10403ff44d20085fe04924623fafff90d3a989e1a5fbb478ec9bc72d8d92ccce53b68b8fdacd8312d6ac3bdf97fd17f853332d4ae197
//		df2c683ecd0d3506b18b0071952aedf04874379b09ce628a89bd08cdf5ce666fc6dc2c2d2d8cbfb102add1c3d69a4fb929a49dfb1f4bf955c50e15c03c8911e1
//		3d5b499abed44e2cfb47f9fbd4ad1a3665337f75e87bd6f1efc172def832924c0bdf8e5ebef95cdf29068e6f9a032e0ca5bfe566f75a919513eb5a8870f5c47a
//		System.out.println(NumberUtil.bytesToHexStr(pk));
//		for(int i =0; i < total; i++) {
//			byte[] rs = sm2.sign(d, d);
//			if(!sm2.verify(pk, d, rs)) {
//				System.out.println(i + " rs = " + NumberUtil.bytesToHexStr(rs));
//				System.out.println(i + " sm2 sm2 failed.");
//			}
//			if(!sig.verify(pk, d, rs)) {
//				System.out.println(i + " sm2 sig failed.");
//			}
//		}
		
//		long t0 = System.currentTimeMillis();
//		for(int i =0; i < total; i++) {
//			sm2.sign(d, d);
//		}
//		long t1 = System.currentTimeMillis();
//		System.out.println("cost1 = " + (t1 - t0));
//		long t2 = System.currentTimeMillis();
//		for(int i =0; i < total; i++) {
//			sig.sign(d, d);
//		}
//		long t3 = System.currentTimeMillis();
//		System.out.println("cost2 = " + (t3 - t2));
	}
	
	static void printUBytes(byte[] a) {
		for(int i = 0; i < a.length; i++) {
			int ai = a[i] < 0 ? a[i] + 256 : a[i];
			System.out.print(ai+", ");
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
//		signTest();
//		sm2Test();
//		getBytes();
		costTest();
		signCostTest();
		

//		BigInteger P = new BigInteger("115792089210356248756420345214020892766250353991924191454421193933289684991999");
//		BigInteger N = new BigInteger("115792089210356248756420345214020892766061623724957744567843809356293439045923");
//		BigInteger A = new BigInteger("115792089210356248756420345214020892766250353991924191454421193933289684991996");
//		BigInteger B = new BigInteger("18505919022281880113072981827955639221458448578012075254857346196103069175443");
//		BigInteger Gx = new BigInteger("22963146547237050559479531362550074578802567295341616970375194840604139615431");
//		BigInteger Gy = new BigInteger("85132369209828568825618990617112496413088388631904505083283536607588877201568");
//		System.out.println(Arrays.toString(P.toByteArray()));
//		System.out.println(Arrays.toString(N.toByteArray()));
//		System.out.println(Arrays.toString(A.toByteArray()));
//		System.out.println(Arrays.toString(B.toByteArray()));
//		System.out.println(Arrays.toString(Gx.toByteArray()));
//		System.out.println(Arrays.toString(Gy.toByteArray()));
	}
}
