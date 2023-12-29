package com.archer.tools.algorithm.signature;


import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

import com.archer.tools.algorithm.hash.SM3;
import com.archer.tools.util.NumberUtil;


public class SM2 {

    static final String USER_ID = "1234567812345678";

    /**
     * Parameters for y^2 = x^3 + a*x + b (mod = P).
     * Base point G(x,y).
     * */
    BigInteger P;
    BigInteger N;
    BigInteger A;
    BigInteger B;
    BigInteger Gx;
    BigInteger Gy;


    /**
     * Initialize several numbers in range 0~9.
     * */
    static final BigInteger[] NUM = {
            BigInteger.ZERO, BigInteger.ONE, new BigInteger("2"), new BigInteger("3"), new BigInteger("4"),
            new BigInteger("5"), new BigInteger("6"), new BigInteger("7"), new BigInteger("8"), new BigInteger("9")
    };

    public SM2() {

        P = new BigInteger("115792089210356248756420345214020892766250353991924191454421193933289684991999");
        N = new BigInteger("115792089210356248756420345214020892766061623724957744567843809356293439045923");
        A = new BigInteger("115792089210356248756420345214020892766250353991924191454421193933289684991996");
        B = new BigInteger("18505919022281880113072981827955639221458448578012075254857346196103069175443");
        Gx = new BigInteger("22963146547237050559479531362550074578802567295341616970375194840604139615431");
        Gy = new BigInteger("85132369209828568825618990617112496413088388631904505083283536607588877201568");
    }

    /**
     * @param privKeyBytes private key content bytes.
     * @param hashBytes hash content bytes.
     *
     * @return signature string.
     * */
    public byte[] sign(byte[] privKeyBytes, byte[] hashBytes) {
        try {
            byte[] publicKeyBs = privateKeyToPublicKey(privKeyBytes);
            return sign(privKeyBytes, publicKeyBs, hashBytes);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public byte[] sign(byte[] privKeyBytes, byte[] publicKeyBytes, byte[] hashBytes) {
        try {
            BigInteger priv = new BigInteger(1, privKeyBytes);
            byte[] pubXBytes = new byte[32], pubYBytes = new byte[32];
            System.arraycopy(publicKeyBytes, 0, pubXBytes, 0, 32);
            System.arraycopy(publicKeyBytes, 32, pubYBytes, 0, 32);
            BigInteger pubX = new BigInteger(1, pubXBytes);
            BigInteger pubY = new BigInteger(1, pubYBytes);

            byte[] z = getZ(pubX, pubY);
            byte[] input = new byte[z.length + hashBytes.length];
            System.arraycopy(z, 0, input, 0, z.length);
            System.arraycopy(hashBytes, 0, input, z.length, hashBytes.length);
            byte[] ebs = SM3.hash(input);
            BigInteger e = new BigInteger(1, ebs);

            BigInteger r = BigInteger.ZERO, s = BigInteger.ZERO;
            BigInteger k;

            do {
                do {
					k = newK();
//                	k = new BigInteger(1, new byte[] {-71, -20, -8, 61, 50, 94, 108, -110, 28, 45, 126, 24, -88, 89, -98, 54, 111, 81, 56, -99, -30, 38, -24, 15, -10, 8, 63, -35, -51, -85, -4, 126});
					byte[] kpbs = privateKeyToPublicKey(NumberUtil.bigIntToBytes(k));
                    byte[] kPubXBytes = new byte[32];
                    System.arraycopy(kpbs, 0, kPubXBytes, 0, 32);
                    BigInteger kPubX = new BigInteger(1, kPubXBytes);

                    r = e.add(kPubX).mod(N);
                } while (r.equals(BigInteger.ZERO) || r.add(k).equals(this.N));

        		BigInteger da = priv.add(BigInteger.ONE).modInverse(this.N);
        		s = r.multiply(priv);
        		s = k.subtract(s).mod(this.N);
                s = da.multiply(s).mod(this.N);
            } while (s.equals(BigInteger.ZERO));

            byte[] signature = new byte[64];
            byte[] rBs = NumberUtil.bigIntToBytes(r), sBs = NumberUtil.bigIntToBytes(s);
            System.arraycopy(rBs, 0, signature, 32 - rBs.length, rBs.length);
            System.arraycopy(sBs, 0, signature, 64 - sBs.length, sBs.length);
            return signature;
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @param pubKeyBytes public key content bytes.
     * @param hashBytes hash content bytes.
     * @param sig signature content.
     *
     * @return if the hash content has been falsified, return false.
     * */
    public boolean verify(byte[] pubKeyBytes, byte[] hashBytes, byte[] sig) {
        try {
            if(sig.length != 64) {
                throw new RuntimeException("Invalid signature, "+sig);
            }
            if(pubKeyBytes.length != 64) {
                throw new RuntimeException("Invalid publickKey.");
            }

            BigInteger r = new BigInteger(1, Arrays.copyOfRange(sig, 0, 32));
            BigInteger s = new BigInteger(1, Arrays.copyOfRange(sig, 32, 64));
            if(r.mod(N).equals(NUM[0]) || s.mod(N).equals(NUM[0])) {
                return false;
            }

            byte[] xBytes = new byte[32], yBytes = new byte[32];
            System.arraycopy(pubKeyBytes, 0, xBytes, 0, 32);
            System.arraycopy(pubKeyBytes, 32, yBytes, 0, 32);
            BigInteger x = NumberUtil.bytesToBigInt(xBytes), y = NumberUtil.bytesToBigInt(yBytes);

            byte[] z = getZ(x, y);
            byte[] input = new byte[z.length + hashBytes.length];
            System.arraycopy(z, 0, input, 0, z.length);
            System.arraycopy(hashBytes, 0, input, z.length, hashBytes.length);
            byte[] ebs = SM3.hash(input);
            BigInteger e = new BigInteger(1, ebs);

            BigInteger[] p = fastMultiply(Gx,Gy,NUM[1],s);
            BigInteger pz = inv(p[2],P);
            BigInteger px = pz.pow(2).multiply(p[0]).mod(P), py = pz.pow(3).multiply(p[1]).mod(P);

            BigInteger[] q = fastMultiply(x,y,NUM[1], r.add(s).mod(N));
            BigInteger qz = inv(q[2],P);
            BigInteger qx = qz.pow(2).multiply(q[0]).mod(P), qy = qz.pow(3).multiply(q[1]).mod(P);

            BigInteger[] g = fastAdd(px, py, NUM[1], qx, qy, NUM[1]);
            BigInteger gz = inv(g[2],P);
            BigInteger gx = gz.pow(2).multiply(g[0]).mod(P);

            return gx.add(e).mod(N).equals(r);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * @param privKeyBytes private key content bytes.
     *
     * @return bytes calculate public key bytes from private key bytes.
     * */
    public byte[] privateKeyToPublicKey(byte[] privKeyBytes) {
        BigInteger priv = NumberUtil.bytesToBigInt(privKeyBytes);
        if(priv.compareTo(N) > 0) {
            throw new RuntimeException("Invalid private key.");
        }

        BigInteger[] p = fastMultiply(Gx,Gy,NUM[1],priv);
        BigInteger z = inv(p[2],P);
        BigInteger x = z.pow(2).multiply(p[0]).mod(P), y = z.pow(3).multiply(p[1]).mod(P);
        byte[] xBs = NumberUtil.bigIntToBytes(x), yBs = NumberUtil.bigIntToBytes(y);
        byte[] pubKeyBytes = new byte[64];
        System.arraycopy(xBs, 0, pubKeyBytes, 0, xBs.length);
        System.arraycopy(yBs, 0, pubKeyBytes, 32, yBs.length);
        return pubKeyBytes;
    }

    /**
     * @param hashBytes hash content bytes.
     * @param sig signature content.
     *
     * @return bytes calculate public key bytes from signature and hash.
     * */
    public byte[] recoverToPublicKey(byte[] hashBytes, String sig) {
        throw new RuntimeException("cannot recover public key with sm2.");
    }

    public BigInteger[] fastMultiply(BigInteger a0, BigInteger a1, BigInteger a2, BigInteger n) {
        if(a1.equals(NUM[0]) || n.equals(NUM[0])) {
            return new BigInteger[]{NUM[0],NUM[0],NUM[1]};
        }
        if(n.equals(NUM[1])) {
            return new BigInteger[]{a0,a1,a2};
        }
        if(n.signum() < 0 || n.compareTo(N) >= 0) {
            return fastMultiply(a0,a1,a2,n.mod(N));
        }

        BigInteger[] a = fastMultiply(a0, a1, a2, n.shiftRight(1));
        BigInteger[] p = fastDouble(a[0],a[1],a[2]);
        BigInteger isOdd = n.mod(NUM[2]);
        if(isOdd.equals(NUM[1])) {
            p = fastAdd(p[0],p[1],p[2],a0,a1,a2);
        }
        return p;
    }

    public BigInteger[] fastDouble(BigInteger a0, BigInteger a1, BigInteger a2) {
        BigInteger ysq = a1.pow(2).mod(P);
        BigInteger s = ysq.multiply(a0).multiply(NUM[4]).mod(P);
        BigInteger m = a0.pow(2).multiply(NUM[3]).add(a2.pow(4).multiply(A)).mod(P);

        BigInteger nx = m.pow(2).subtract(s.multiply(NUM[2])).mod(P);
        BigInteger ny = m.multiply(s.subtract(nx)).subtract(ysq.pow(2).multiply(NUM[8])).mod(P);
        BigInteger nz = a1.multiply(a2).multiply(NUM[2]).mod(P);
        return new BigInteger[] {nx, ny, nz};
    }

    public BigInteger[] fastAdd(
            BigInteger p0, BigInteger p1, BigInteger p2,
            BigInteger q0, BigInteger q1, BigInteger q2
    ) {
        BigInteger u1 = q2.pow(2).multiply(p0).mod(P);
        BigInteger u2 = p2.pow(2).multiply(q0).mod(P);
        BigInteger s1 = q2.pow(3).multiply(p1).mod(P);
        BigInteger s2 = p2.pow(3).multiply(q1).mod(P);
        if(u1.equals(u2)) {
            if(s1.equals(s2)) {
                return fastDouble(p0, p1, p2);
            }
            return new BigInteger[] {NUM[0], NUM[0], NUM[1]};
        }
        BigInteger h = u2.subtract(u1), r = s2.subtract(s1);
        BigInteger h2 = h.pow(2).mod(P);
        BigInteger h3 = h2.multiply(h).mod(P);
        BigInteger u1h2 = u1.multiply(h2).mod(P);

        BigInteger nx = r.pow(2).subtract(h3).subtract(u1h2.multiply(NUM[2])).mod(P);
        BigInteger ny = r.multiply(u1h2.subtract(nx)).subtract(s1.multiply(h3)).mod(P);
        BigInteger nz = h.multiply(p2).multiply(q2).mod(P);
        return new BigInteger[] {nx, ny, nz};
    }

    static BigInteger inv(BigInteger a, BigInteger b) {
        if(a.equals(NUM[0])) {
            return NUM[0];
        }
        BigInteger lm = NUM[1], hm = NUM[0];
        BigInteger low = a.mod(b), high = b;

        while(low.compareTo(NUM[1]) > 0) {
            BigInteger r = high.divide(low);
            BigInteger nm = hm.subtract(lm.multiply(r)), ne = high.mod(low);
            hm = lm;
            high = low;
            lm = nm;
            low = ne;
        }
        return lm.mod(b);
    }

    byte[] getZ(BigInteger pubX, BigInteger pubY) {
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
        System.arraycopy(get32Bytes(pubX), 0, input, base.length, 32);
        System.arraycopy(get32Bytes(pubY), 0, input, base.length + 32, 32);
        return SM3.hash(input);
    }

    byte[] get32Bytes(BigInteger n) {
        byte[] ret;
        if (n == null) {
            return null;
        }

        if (n.toByteArray().length == 33) {
            ret = new byte[32];
            System.arraycopy(n.toByteArray(), 1, ret, 0, 32);
        } else if (n.toByteArray().length == 32) {
            ret = n.toByteArray();
        } else {
            ret = new byte[32];
            for (int i = 0; i < 32 - n.toByteArray().length; i++) {
                ret[i] = 0;
            }
            System.arraycopy(n.toByteArray(), 0, ret,
                    32 - n.toByteArray().length, n.toByteArray().length);
        }
        return ret;
    }

    BigInteger newK() {
        int nBitLength = this.N.bitLength();
        int minWeight = nBitLength >>> 2;
        SecureRandom random = new SecureRandom();
        BigInteger two = BigInteger.valueOf(2);
        BigInteger k;
        for (;;) {
            k = new BigInteger(nBitLength, random);

            if (k.compareTo(two) < 0  || (k.compareTo(this.N) >= 0)) {
                continue;
            }

            if (getWeight(k) < minWeight) {
                continue;
            }

            break;
        }
        return k;
    }

    int getWeight(BigInteger k) {
        if (k.signum() == 0) {
            return 0;
        }

        BigInteger _3k = k.shiftLeft(1).add(k);
        BigInteger diff = _3k.xor(k);

        return diff.bitCount();
    }
}
