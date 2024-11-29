package com.archer.tools.algorithm.signature;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

import com.archer.math.Curve;
import com.archer.math.EcPoint;
import com.archer.math.MathLib;
import com.archer.tools.algorithm.hash.SM3;

public class Signature {
	
	private PopularCurve popularCurve;
	
	private Curve curve;

	private Random rand = new SecureRandom();
	
	public Signature(PopularCurve curve) {
		this.popularCurve = curve;
	}
	
	public Signature(Curve curve) {
		this.curve = curve;
	}
	
	public byte[] getPublicKey(byte[] privateKey) {
		int byteLen;
		if(curve != null) {
			byteLen = curve.P.length;
		} else {
			byteLen = popularCurve.P.length;
		}
		if(privateKey.length != byteLen) {
			throw new IllegalArgumentException("invalid privateKey");
		}
		
		EcPoint pub;
		if(curve != null) {
			pub = EcPoint.mulCurve(privateKey, curve);
		} else {
			pub = EcPoint.mul(privateKey, popularCurve.id);
		}
		byte[] pk = new byte[byteLen << 1];

		System.arraycopy(pub.getX(), 0, pk, byteLen - pub.getX().length, pub.getX().length);
		System.arraycopy(pub.getY(), 0, pk, pk.length - pub.getY().length, pub.getY().length);
		return pk;
	}
	
	public byte[] sign(byte[] privateKey, byte[] hash) {
		int byteLen;
		if(curve != null) {
			byteLen = curve.P.length;
		} else {
			byteLen = popularCurve.P.length;
		}
		if(privateKey.length != byteLen) {
			throw new IllegalArgumentException("invalid privateKey");
		}
		
		EcPoint kp;
		byte[] k, n, r, s = null;
		if(curve != null) {
			k = new byte[byteLen];
			rand.nextBytes(k);
			n = curve.N;
			kp = EcPoint.mulCurve(k, curve);
			r = kp.getX();
		} else {
			k = new byte[byteLen];
			rand.nextBytes(k);
			n = popularCurve.N;

			if(popularCurve == PopularCurve.sm2p256v1) {
				EcPoint rs = smCalRS(privateKey, hash);
				r = rs.getX();
				s = rs.getY();
			} else {
				kp = EcPoint.mul(k, popularCurve.id);
				r = kp.getX();
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
		int byteLen;
		if(curve != null) {
			byteLen = curve.P.length;
		} else {
			byteLen = popularCurve.P.length;
		}
		if(privateKey.length != byteLen) {
			throw new IllegalArgumentException("invalid privateKey");
		}
		
		EcPoint kp;
		byte[] k, n, r, s = null;
		k = new byte[byteLen];
		rand.nextBytes(k);
		if(curve != null) {
			n = curve.N;
			kp = EcPoint.mulCurve(k, curve);
		} else {
			n = popularCurve.N;
			kp = EcPoint.mul(k, popularCurve.id);
		}
		r = kp.getX();
		int odd = kp.getY()[kp.getY().length - 1];
		odd = ((odd < 0) ? (odd + 256) : odd) & 1;
		
		s = MathLib.mulInvm(MathLib.add(hash, MathLib.mul(privateKey, r)), k, n);
		if(MathLib.cmp(MathLib.mul(s, 2), n) > 0) {
			s = MathLib.sub(n, s);
			odd ^= 1;
		}
		byte[] result = new byte[(byteLen << 1) + 1];
		System.arraycopy(r, 0, result, byteLen - r.length, r.length);
		System.arraycopy(s, 0, result, result.length - s.length - 1, s.length);
		result[result.length - 1] = (byte)(odd + 27);
		return result;
	}
	
	
	public boolean verify(byte[] publicKey, byte[] hash, byte[] sig) {
		int byteLen;
		if(curve != null) {
			byteLen = curve.P.length;
		} else {
			byteLen = popularCurve.P.length;
		}
		
		if(publicKey.length != (byteLen << 1) && publicKey.length != (byteLen << 1) + 1) {
			throw new IllegalArgumentException("invalid publicKey");
		}
		if(sig.length == (byteLen << 1) + 1) {
			sig = Arrays.copyOfRange(sig, 0, (byteLen << 1));
		}
		if(sig.length != (byteLen << 1)) {
			throw new IllegalArgumentException("invalid sig");
		}
		int len = sig.length >> 1;
		byte[] r = new byte[len], s = new byte[len];
		System.arraycopy(sig, 0, r, 0, len);
		System.arraycopy(sig, len, s, 0, len);
		EcPoint pub = EcPoint.decode(publicKey);
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
			byte[] e = getSm2Za(pub.getX(), pub.getY(), hash);
			return MathLib.cmp(MathLib.mod(MathLib.add(result.getX(), e), popularCurve.N), removePrefixZero(r)) == 0;
		}
		return MathLib.cmp(MathLib.mod(result.getX(), p), removePrefixZero(r)) == 0;
	}
	
	public byte[] recoverToPublicKey(byte[] hash, byte[] sig) {
		if(sig.length != 65) {
			throw new IllegalArgumentException("Invalid signature data");
		}
		byte[] x = new byte[32], y, s = new byte[32];
		System.arraycopy(sig, 0, x, 0, 32);
		System.arraycopy(sig, 32, s, 0, 32);
		int v = sig[64] - 27;
		
		y = MathLib.pow(x, 3);
		byte[] t, p, n;
		if(popularCurve != null && popularCurve == PopularCurve.secp256k1) {
			// x ^ 3 + 7
			y = MathLib.add(y, new byte[] {7});
			MathLib.mod(y, PopularCurve.secp256k1.P);
			t = new byte[32];
			System.arraycopy(PopularCurve.secp256k1.P, 0, t, 0, 32);
			t[31] += 1;
			p = PopularCurve.secp256k1.P;
			n = PopularCurve.secp256k1.N;
		} else if(curve != null) {
			byte[] ax = MathLib.mul(x, curve.A);
			y = MathLib.add(y, ax);
			y = MathLib.add(y, curve.B);
			MathLib.mod(y, curve.P);
			t = MathLib.add(curve.P, new byte[] {1});
			p = curve.P;
			n = curve.N;
		} else {
			throw new IllegalArgumentException("Invalid curve type");
		}
		t = MathLib.divui(t, 4);
		y = MathLib.powm(y, t, p);
		int odd = y[y.length - 1];
		odd = ((odd < 0) ? (odd + 256) : odd) & 1;
		if((v ^ odd) == 1) {
			y = MathLib.sub(p, y);
		}

		EcPoint xy = new EcPoint();
		xy.setX(x);
		xy.setY(y);
		EcPoint sxy, mxy;
		if(popularCurve != null && popularCurve == PopularCurve.secp256k1) {
			sxy = EcPoint.mulPoint(s, PopularCurve.secp256k1.id, xy);
			mxy = EcPoint.mul(hash, PopularCurve.secp256k1.id);
		} else if(curve != null) {
			sxy = EcPoint.mulCurvePoint(s, curve, xy);
			mxy = EcPoint.mulCurve(hash, curve);
		} else {
			throw new IllegalArgumentException("Invalid curve type");
		}
		t = MathLib.inv(x, n);
		xy = EcPoint.sub(sxy, mxy, p);
		if(popularCurve != null && popularCurve == PopularCurve.secp256k1) {
			sxy = EcPoint.mulPoint(t, PopularCurve.secp256k1.id, xy);
		} else if(curve != null) {
			sxy = EcPoint.mulCurvePoint(t, curve, xy);
		} else {
			throw new IllegalArgumentException("Invalid curve type");
		}
		
		return sxy.getEncoded();
		
	}

    static byte[] getDL(int length) {
        if (length < 128) {
            return new byte[] {(byte) length};
        } else {
            byte[] stack = new byte[5];
            int pos = stack.length;
            do {
                stack[--pos] = (byte)length;
                length >>>= 8;
            } while (length != 0);
            stack[--pos] = (byte)(0x80 | (stack.length - pos));
            
            return Arrays.copyOfRange(stack, pos, stack.length);
        }
    }
    
    public byte[] toASN1Encoded(byte[] sig) {
		int byteLen;
		if(curve != null) {
			byteLen = curve.P.length;
		} else {
			byteLen = popularCurve.P.length;
		}
		if(sig == null || sig.length != (byteLen << 1)) {
			throw new IllegalArgumentException("invalid signature");
		}
		int idx = 0;
		byte[] r = new byte[0], s = new byte[0];
		for(int c = 0; c < 2; c++) {
			if(-128 <= sig[c * byteLen] && sig[c * byteLen] < 0) {
				if(c == 0) {
					r = new byte[byteLen + 1];
					System.arraycopy(sig, 0, r, 1, byteLen);
				} else {
					s = new byte[byteLen + 1];
					System.arraycopy(sig, byteLen, s, 1, byteLen);
				}
			} else {
				idx = c * byteLen;
				while(idx < ((c + 1) * byteLen) && sig[idx] == 0) {
					idx++;
				}
				if(idx >= ((c + 1) * byteLen)) {
					throw new IllegalArgumentException("invalid signature");
				}
				if(-128 <= sig[idx] && sig[idx] < 0) {
					if(c == 0) {
						r = new byte[byteLen - idx + 1];
						System.arraycopy(sig, idx, r, 1, byteLen - idx);
					} else {
						s = new byte[(2 * byteLen) - idx + 1];
						System.arraycopy(sig, idx, s, 1, (2 * byteLen) - idx);
					}
				} else {
					if(c == 0) {
						r = new byte[byteLen - idx];
						System.arraycopy(sig, idx, r, 0, byteLen - idx);
					} else {
						s = new byte[(2 * byteLen) - idx];
						System.arraycopy(sig, idx, s, 0, (2 * byteLen) - idx);
					}
				}
			}	
		}
    	byte[] rLenBs = getDL(r.length), sLenBs = getDL(s.length);
    	int totalLen = 1 + rLenBs.length + r.length + 1 + sLenBs.length + s.length;
    	byte[] totalLenBs = getDL(totalLen);
    	
    	int off = 0;
    	byte[] bs = new byte[1 + totalLenBs.length + totalLen];
    	
    	// 1byte
    	bs[off++] = 32 | 16;
    	
    	System.arraycopy(totalLenBs, 0, bs, off, totalLenBs.length);
    	off += totalLenBs.length;

    	//1byte
    	bs[off++] = 2;

    	//len bytes
    	System.arraycopy(rLenBs, 0, bs, off, rLenBs.length);
    	off += rLenBs.length;
    	
    	//r bytes
    	System.arraycopy(r, 0, bs, off, r.length);
    	off += r.length;
    	

    	//1byte
    	bs[off++] = 2;

    	//len bytes
    	System.arraycopy(sLenBs, 0, bs, off, sLenBs.length);
    	off += sLenBs.length;
    	
    	//r bytes
    	System.arraycopy(s, 0, bs, off, s.length);
    	off += s.length;
    	
    	return bs;
    }
    
    public byte[] decodeASN1(byte[] asn1Sig) {
		int byteLen;
		if(curve != null) {
			byteLen = curve.P.length;
		} else {
			byteLen = popularCurve.P.length;
		}
		
	
    	int off = 0;
    	if(asn1Sig[off++] != (32 | 16)) {
			throw new IllegalArgumentException("invalid asn1 signature");
    	}
    	int totalLenLen = 1, totalLen = 0;  
    	for(; totalLenLen < 4; totalLenLen++) {
    		for(int i = off; i < off + totalLenLen; i++) {
    			totalLen = (totalLen << 8) | asn1Sig[i];
    		}
    		if(totalLen == asn1Sig.length - 1 - totalLenLen) {
    			off += totalLenLen;
    			break;
    		}
    	}
    	if(totalLen == 0) {
			throw new IllegalArgumentException("invalid asn1 signature");
    	}
    	byte[] sig = new byte[byteLen << 1];
    	
    	int lenLen = 1;
    	if(byteLen >= (2 << 24)) {
    		lenLen = 4;
    	} else if(byteLen >= (2 << 16)) {
    		lenLen = 3;
    	} else if(byteLen >= (2 << 8)) {
    		lenLen = 2;
    	}
    	//r
    	if(asn1Sig[off++] != 2) {
			throw new IllegalArgumentException("invalid asn1 signature");
    	}
    	int rLen = 0;
    	for(int i = off; i < off + lenLen; i++) {
    		rLen = (rLen << 8) | asn1Sig[i];
		}
    	if(rLen == 0) {
			throw new IllegalArgumentException("invalid asn1 signature");
    	}
    	off += lenLen;
    	if(rLen > byteLen) {
        	System.arraycopy(asn1Sig, off + rLen - byteLen, sig, 0, byteLen);
    	} else {
        	System.arraycopy(asn1Sig, off, sig, byteLen - rLen, rLen);
    	}
    	off += rLen;
    	

    	//s
    	if(asn1Sig[off++] != 2) {
			throw new IllegalArgumentException("invalid asn1 signature");
    	}
    	int sLen = 0;
    	for(int i = off; i < off + lenLen; i++) {
    		sLen = (sLen << 8) | asn1Sig[i];
		}
    	if(sLen == 0) {
			throw new IllegalArgumentException("invalid asn1 signature");
    	}
    	off += lenLen;
    	if(sLen > byteLen) {
        	System.arraycopy(asn1Sig, off + sLen - byteLen, sig, byteLen, byteLen);
    	} else {
        	System.arraycopy(asn1Sig, off, sig, sig.length - sLen, sLen);
    	}
    	return sig;
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
		byte[] e = getSm2Za(pub.getX(), pub.getY(), hash);
		EcPoint kp = EcPoint.mul(k, popularCurve.id);
		kp.setX(MathLib.mod(MathLib.add(kp.getX(), e), popularCurve.N));
		while(MathLib.cmp(MathLib.add(kp.getX(), e), popularCurve.N) == 0) {
			rand.nextBytes(k);
			kp = EcPoint.mul(k, popularCurve.id);
			kp.setX(MathLib.mod(MathLib.add(kp.getX(), e), popularCurve.N));
		}
		byte[] s = MathLib.subm(k, MathLib.mul(kp.getX(), privateKey), popularCurve.N);
		s = MathLib.mulInvm(s, MathLib.add(privateKey, 1), popularCurve.N);
		kp.setY(s);
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
