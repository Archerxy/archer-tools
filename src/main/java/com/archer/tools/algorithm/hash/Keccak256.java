package com.archer.tools.algorithm.hash;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

import static java.lang.Math.min;
import static java.lang.System.arraycopy;
import static java.util.Arrays.fill;

public class Keccak256 {

    static BigInteger BIT_64 = new BigInteger("18446744073709551615");

    static BigInteger MOD = new BigInteger("18446744073709551616");

    public static byte[] hash(final byte[] message) {

        int d = 1088, rate = 0x01, outputLen = 256;

        int[] uState = new int[200];
        int[] uMessage = convertToUint(message);

        int rateInBytes = rate / 8;
        int blockSize = 0;
        int inputOffset = 0;

        while (inputOffset < uMessage.length) {
            blockSize = min(uMessage.length - inputOffset, rateInBytes);
            for (int i = 0; i < blockSize; i++) {
                uState[i] = uState[i] ^ uMessage[i + inputOffset];
            }

            inputOffset = inputOffset + blockSize;
            if (blockSize == rateInBytes) {
                doKeccakf(uState);
                blockSize = 0;
            }
        }

        uState[blockSize] = uState[blockSize] ^ d;
        if ((d & 0x80) != 0 && blockSize == (rateInBytes - 1)) {
            doKeccakf(uState);
        }

        uState[rateInBytes - 1] = uState[rateInBytes - 1] ^ 0x80;
        doKeccakf(uState);

        ByteArrayOutputStream byteResults = new ByteArrayOutputStream();
        int tOutputLen = outputLen / 8;
        while (tOutputLen > 0) {
            blockSize = min(tOutputLen, rateInBytes);
            for (int i = 0; i < blockSize; i++) {
                byteResults.write((byte) uState[i]);
            }

            tOutputLen -= blockSize;
            if (tOutputLen > 0) {
                doKeccakf(uState);
            }
        }

        return byteResults.toByteArray();
    }

    static void doKeccakf(final int[] uState) {
        BigInteger[][] lState = new BigInteger[5][5];

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int[] data = new int[8];
                arraycopy(uState, 8 * (i + 5 * j), data, 0, data.length);
                lState[i][j] = convertFromLittleEndianTo64(data);
            }
        }

        roundB(lState);

        fill(uState, 0);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int[] data = convertFrom64ToLittleEndian(lState[i][j]);
                arraycopy(data, 0, uState, 8 * (i + 5 * j), data.length);
            }
        }
    }

    static void roundB(final BigInteger[][] state) {
        int LFSRstate = 1;
        for (int round = 0; round < 24; round++) {
            BigInteger[] C = new BigInteger[5];
            BigInteger[] D = new BigInteger[5];

            for (int i = 0; i < 5; i++) {
                C[i] = state[i][0].xor(state[i][1]).xor(state[i][2]).xor(state[i][3]).xor(state[i][4]);
            }
            // 4^1, 0^2, 1^3, 2^4, 3^0
            for (int i = 0; i < 5; i++) {
                D[i] = C[(i + 4) % 5].xor(leftRotate64(C[(i + 1) % 5], 1));
            }

            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    state[i][j] = state[i][j].xor(D[i]);
                }
            }

            int x = 1, y = 0;
            BigInteger current = state[x][y];
            for (int i = 0; i < 24; i++) {
                int tX = x;
                x = y;
                y = (2 * tX + 3 * y) % 5;

                BigInteger shiftValue = current;
                current = state[x][y];

                state[x][y] = leftRotate64(shiftValue, (i + 1) * (i + 2) / 2);
            }

            for (int j = 0; j < 5; j++) {
                BigInteger[] t = new BigInteger[5];
                for (int i = 0; i < 5; i++) {
                    t[i] = state[i][j];
                }

                for (int i = 0; i < 5; i++) {
                    BigInteger invertVal = t[(i + 1) % 5].xor(BIT_64);
                    state[i][j] = t[i].xor(invertVal.and(t[(i + 2) % 5]));
                }
            }


            for (int i = 0; i < 7; i++) {
                LFSRstate = ((LFSRstate << 1) ^ ((LFSRstate >> 7) * 0x71)) % 256;
                int bitPosition = (1 << i) - 1;
                if ((LFSRstate & 2) != 0) {
                    state[0][0] = state[0][0].xor(new BigInteger("1").shiftLeft(bitPosition));
                }
            }
        }
    }

    private static int[] convertToUint(final byte[] data) {
        int[] converted = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            converted[i] = data[i] & 0xFF;
        }

        return converted;
    }

    private static BigInteger convertFromLittleEndianTo64(final int[] data) {
        BigInteger uLong = BigInteger.ZERO;
        for (int i = 0; i < 8; i++) {
            uLong = uLong.add(new BigInteger(Integer.toString(data[i])).shiftLeft(8 * i));
        }

        return uLong;
    }

    private static int[] convertFrom64ToLittleEndian(final BigInteger uLong) {
        int[] data = new int[8];
        BigInteger mod256 = new BigInteger("256");
        for (int i = 0; i < 8; i++) {
            data[i] = uLong.shiftRight((8 * i)).mod(mod256).intValue();
        }

        return data;
    }

    private static BigInteger leftRotate64(final BigInteger value, final int rotate) {
        BigInteger lp = value.shiftRight(64 - (rotate % 64));
        BigInteger rp = value.shiftLeft(rotate % 64);

        return lp.add(rp).mod(MOD);
    }
}
