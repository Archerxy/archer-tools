package com.archer.tools.algorithm.signature;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

import com.archer.math.Curve;
import com.archer.math.EcPoint;
import com.archer.math.MathLib;
import com.archer.tools.algorithm.hash.SM3;

public class Signature {
	
	PopularCurve popularCurve;
	
	Curve curve;
	
	public Signature(PopularCurve curve) {
		this.popularCurve = curve;
	}
	
	public Signature(Curve curve) {
		this.curve = curve;
	}
	
	public byte[] getPublicKey(byte[] privateKey) {
		EcPoint pub;
		int byteLen;
		if(curve != null) {
			byteLen = curve.P.length;
			pub = EcPoint.mulCurve(privateKey, curve);
		} else {
			byteLen = popularCurve.P.length;
			pub = EcPoint.mul(privateKey, popularCurve.id);
		}
		byte[] pk = new byte[byteLen << 1];

		System.arraycopy(pub.x, 0, pk, byteLen - pub.x.length, pub.x.length);
		System.arraycopy(pub.y, 0, pk, pk.length - pub.y.length, pub.y.length);
		return pk;
	}
	
	public byte[] sign(byte[] privateKey, byte[] hash) {
		Random rand = new SecureRandom();
		EcPoint kp;
		byte[] k, n, r, s = null;
		int byteLen;
		if(curve != null) {
			byteLen = curve.P.length;
			k = new byte[byteLen];
			rand.nextBytes(k);
			n = curve.N;
			kp = EcPoint.mulCurve(k, curve);
			r = kp.x;
		} else {
			byteLen = popularCurve.P.length;
			k = new byte[byteLen];
			rand.nextBytes(k);
			n = popularCurve.N;

			if(popularCurve == PopularCurve.sm2p256v1) {
				EcPoint rs = smCalRS(privateKey, hash);
				r = rs.x;
				s = rs.y;
			} else {
				kp = EcPoint.mul(k, popularCurve.id);
				r = kp.x;
			}
		}
		if(popularCurve == null || popularCurve != PopularCurve.sm2p256v1) {
			s = MathLib.mulInvm(MathLib.add(hash, MathLib.mul(privateKey, r)), k, n);
			int cmp = MathLib.cmp(MathLib.mul(s, 2), n);
			if(cmp >= 0) {
				s = MathLib.sub(n, s);
			}
		}
		byte[] result = new byte[byteLen << 1];
		System.arraycopy(r, 0, result, byteLen - r.length, r.length);
		System.arraycopy(s, 0, result, result.length - s.length, s.length);
		return result;
	}
	
	public byte[] signWithV(byte[] privateKey, byte[] hash) {
		Random rand = new SecureRandom();
		EcPoint kp;
		byte[] k, n, r, s = null;
		int byteLen;
		if(curve != null) {
			byteLen = curve.P.length;
			k = new byte[byteLen];
			rand.nextBytes(k);
			n = curve.N;
			kp = EcPoint.mulCurve(k, curve);
			r = kp.x;
		} else {
			byteLen = popularCurve.P.length;
			k = new byte[byteLen];
			rand.nextBytes(k);
			n = popularCurve.N;
			kp = EcPoint.mul(k, popularCurve.id);
			r = kp.x;
		}
		s = MathLib.mulInvm(MathLib.add(hash, MathLib.mul(privateKey, r)), k, n);
		int v = s[s.length - 1] & 1, cmp = MathLib.cmp(MathLib.mul(s, 2), n);
		if(cmp >= 0) {
			s = MathLib.sub(n, s);
			v ^= 1;
		}
		byte[] result = new byte[byteLen << 1 + 1];
		System.arraycopy(r, 0, result, byteLen - r.length, r.length);
		System.arraycopy(s, 0, result, result.length - s.length, s.length);
		result[result.length - 1] = (byte) v;
		return result;
	}
	
	
	public boolean verify(byte[] publicKey, byte[] hash, byte[] sig) {
		int len = sig.length >> 1, pubLen = publicKey.length >> 1;
		byte[] r = new byte[len], s = new byte[len];
		System.arraycopy(sig, 0, r, 0, len);
		System.arraycopy(sig, len, s, 0, len);
		EcPoint pub = new EcPoint();
		pub.x = new byte[pubLen];
		pub.y = new byte[pubLen];
		System.arraycopy(publicKey, 0, pub.x, 0, pubLen);
		System.arraycopy(publicKey, pubLen, pub.y, 0, pubLen);
		byte[] p;
		EcPoint b0, b1;
		if(curve != null) {
			b0 = EcPoint.mulCurve(MathLib.mulInvm(hash, s, curve.N), curve);
			b1 = EcPoint.mulCurvePoint(MathLib.mulInvm(r, s, curve.N), curve, pub);
			p = curve.P;
		} else {
			if(popularCurve == PopularCurve.sm2p256v1) {
				b0 = EcPoint.mul(s, popularCurve.id);
				b1 = EcPoint.mulPoint(MathLib.mod(MathLib.add(r, s), popularCurve.N), popularCurve.id, pub);
			} else {
				b0 = EcPoint.mul(MathLib.mulInvm(hash, s, popularCurve.N), popularCurve.id);
				b1 = EcPoint.mulPoint(MathLib.mulInvm(r, s, popularCurve.N), popularCurve.id, pub);
			}
			p = popularCurve.P;
		}
		EcPoint result = EcPoint.add(b0, b1, p);
		if(popularCurve != null && popularCurve == PopularCurve.sm2p256v1) {
			byte[] e = getSm2Za(pub.x, pub.y, hash);
			return MathLib.cmp(MathLib.mod(MathLib.add(result.x, e), popularCurve.N), removePrefixZero(r)) == 0;
		}
		return MathLib.cmp(MathLib.mod(result.x, p), removePrefixZero(r)) == 0;
	}
	
	
	byte[] removePrefixZero(byte[] in) {
		int off = 0;
		while(in[off] == 0) {
			off++;
		}
		if(off == 0) {
			return in;
		}
		return Arrays.copyOfRange(in, off, in.length);
	}
	
	EcPoint smCalRS(byte[] privateKey, byte[] hash) {
		int byteLen = popularCurve.P.length;
		Random rand = new SecureRandom();
		byte[] k = new byte[byteLen];
		rand.nextBytes(k);
		EcPoint pub = EcPoint.mul(privateKey, popularCurve.id);
		byte[] e = getSm2Za(pub.x, pub.y, hash);
		EcPoint kp = EcPoint.mul(k, popularCurve.id);
		kp.x = MathLib.mod(MathLib.add(kp.x, e), popularCurve.N);
		while(MathLib.cmp(MathLib.add(kp.x, e), popularCurve.N) == 0) {
			rand.nextBytes(k);
			kp = EcPoint.mul(k, popularCurve.id);
			kp.x = MathLib.mod(MathLib.add(kp.x, e), popularCurve.N);
		}
		byte[] s = MathLib.subm(k, MathLib.mul(kp.x, privateKey), popularCurve.N);
		s = MathLib.mulInvm(s, MathLib.add(privateKey, 1), popularCurve.N);
		kp.y = s;
		return kp;
	}
	
	/**
	 * userId = "1234567812345678"
	 * */
    byte[] getSm2Za(byte[] x, byte[] y, byte[] hash) {
        byte[] base = {0,-128,49,50,51,52,53,54,55,56,49,50,51,52,53,54,55,
                56,-1,-1,-1,-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
                -1,-1,-1,0,0,0,0,-1,-1,-1,-1,-1,-1,-1,-4,40,-23,-6,-98,
                -99,-97,94,52,77,90,-98,75,-49,101,9,-89,-13,-105,-119,
                -11,21,-85,-113,-110,-35,-68,-67,65,77,-108,14,-109,50,
                -60,-82,44,31,25,-127,25,95,-103,4,70,106,57,-55,-108,
                -113,-29,11,-65,-14,102,11,-31,113,90,69,-119,51,76,116,
                -57,-68,55,54,-94,-12,-10,119,-100,89,-67,-50,-29,107,
                105,33,83,-48,-87,-121,124,-58,42,71,64,2,-33,50,-27,33,
                57,-16,-96};

        byte[] input = new byte[210];
        System.arraycopy(base, 0, input, 0, base.length);
        System.arraycopy(x, 0, input, base.length + 32 - x.length, x.length);
        System.arraycopy(y, 0, input, base.length + 64 - y.length, y.length);
        byte[] z = SM3.hash(input);
        byte[] e = new byte[z.length + hash.length];
        System.arraycopy(z, 0, e, 0, z.length);
        System.arraycopy(hash, 0, e, z.length, hash.length);
        return SM3.hash(e);
    }
	
	public enum PopularCurve {
		/**
		 * famous curve
		 * */
		secp256k1(EcPoint.CURVE_secp256k1, 
				new byte[] {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -2, -1, -1, -4, 47},
				new byte[] {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -2, -70, -82, -36, -26, -81, 72, -96, 59, -65, -46, 94, -116, -48, 54, 65, 65}),
		sm2p256v1(EcPoint.CURVE_sm2p256v1, 
				new byte[] {-1, -1, -1, -2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1},
				new byte[] {-1, -1, -1, -2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 114, 3, -33, 107, 33, -58, 5, 43, 83, -69, -12, 9, 57, -43, 65, 35});

		private int id;
		private byte[] P;
		private byte[] N;
		
		PopularCurve(int id, byte[] P, byte[] N) {
			this.id = id;
			this.P = P;
			this.N = N;
		}
	}

}
