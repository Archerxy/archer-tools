package com.archer.tools.algorithm.crypto;

import java.util.Random;

import com.archer.math.Curve;
import com.archer.math.EcPoint;
import com.archer.math.MathLib;
import com.archer.tools.algorithm.signature.PopularCurve;

public class Ecc {
	
	private static final int BASE_K = 30;
	
	private Curve curve;
	private Random rand = new Random();
	
	public Ecc(Curve curve) {
		this.curve = curve;
	}
	
	public EccKeyPair genKeyPair() {
		
		return null;
	}
	
	
	
	private EcPoint[] encrypt(byte[] m, EcPoint p) {
		byte[] mb = MathLib.mul(m, BASE_K);
		byte[][] xy = findxy(mb);
		if(xy[1] == null) {
			throw new IllegalArgumentException("input m can not be encoded.");
		}
		EcPoint mp = new EcPoint();
		mp.x = xy[0];
		mp.y = xy[1];
		byte[] r = new byte[curve.P.length];
		rand.nextBytes(r);
		EcPoint rG = EcPoint.mulCurve(r, curve);
		EcPoint rP = EcPoint.mulCurvePoint(r, curve, p);
		EcPoint m_rP = EcPoint.add(mp, rP, curve.P);
		
		return new EcPoint[] {rG, m_rP};
	}
	
	private byte[] decrypt(EcPoint rG, EcPoint m_rP, byte[] d) {
		EcPoint m = EcPoint.sub(m_rP, EcPoint.mulCurvePoint(d, curve, rG), curve.P);
		return MathLib.divui(m.x, BASE_K);
	}
	
	private byte[][] findxy(byte[] xsrc) {
		byte[] y = null, x = null;
		for(int i = 0; i < BASE_K; i++) {
			x = MathLib.add(xsrc, i);
			byte[] ysqr = MathLib.add(MathLib.add(MathLib.pow(x, 3), MathLib.mul(x, curve.A)), curve.B);
			ysqr = MathLib.mod(ysqr, curve.P);
			if((y = MathLib.sqrtif(ysqr)) != null) {
				break ;
			}
		}
		return new byte[][] {x, y};
	}
	
	public class EccKeyPair {
		byte[] publicKey;
		byte[] privateKey;
		
		protected EccKeyPair(byte[] publicKey, byte[] privateKey) {
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
		public void setPublicKey(byte[] publicKey) {
			this.publicKey = publicKey;
		}
		public void setPrivateKey(byte[] privateKey) {
			this.privateKey = privateKey;
		}
	}
	
	public static void main(String[] args) {
		String msg = "xuyihaoshuai";

		byte[] d = {29, -3, 74, 47, 123, 64, 41, 123, 67, -9, 89, 16, 84, 115, 18, -8, -41, -97, -57, 36, 103, 60, 115, -123, -5, -38, -97, 127, 32, -21, -25, 2};
		byte[] x = {36, 117, -87, 86, -21, 0, 78, 37, -128, -38, -1, -36, -74, -16, 60, -55, -46, 47, -29, -101, 95, 53, 113, 31, 0, 37, -46, 89, -70, -126, 10, -86};
		byte[] y = {44, -69, -127, -11, -19, 120, -83, 90, 46, 81, 15, -101, -16, -87, -106, -67, -33, -23, 18, 54, -67, 36, 99, 11, 59, -73, -96, 99, -98, 95, -115, -68};
		EcPoint p = new EcPoint();
		p.x = x;
		p.y = y;
		
		Ecc ecc = new Ecc(PopularCurve.secp256k1);
		EcPoint[] rm = ecc.encrypt(msg.getBytes(), p);
		byte[] ret = ecc.decrypt(rm[0],  rm[1], d);
		System.out.println(new String(ret));
		
		
		
	}
}
