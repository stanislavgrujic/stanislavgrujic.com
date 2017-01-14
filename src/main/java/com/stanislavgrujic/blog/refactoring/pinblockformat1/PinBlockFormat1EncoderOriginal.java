package com.stanislavgrujic.blog.refactoring.pinblockformat1;

import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * This class represents an original implementation of PIN block format ISO-1 encoding, implemented per ISO-9564.
 */
public class PinBlockFormat1EncoderOriginal {

    public static byte[] encodeAsPinBlockFormat4(String plainPin) {
        byte[] pinBlock = new byte[8];
        pinBlock[0] = stringToPackedBcd("14")[0];
        byte[] bcdPin = stringToPackedBcd(plainPin);
        pinBlock[1] = bcdPin[0];
        pinBlock[2] = bcdPin[1];
        byte[] temporaryRndFill = stringToPackedBcd("7743438284");
        System.arraycopy(temporaryRndFill, 0, pinBlock, 3, 5);

        return pinBlock;
    }

    private static byte[] stringToPackedBcd(String pin) {
        int length = pin.length();
        if (length % 2 != 0) {
            throw new IllegalArgumentException("Given string length must be multiple of 2!");
        }
        byte[] packed = new byte[length / 2];
        for (int i = 0; i < packed.length; i++) {
            byte high = (byte) (Character.getNumericValue(pin.charAt(2 * i)) * 16);
            byte low = (byte) (Character.getNumericValue(pin.charAt(1 + 2 * i)));
            packed[i] = (byte) (high | low);
        }
        return packed;
    }

    public static String decodePinBlockFormat4(byte[] encoded) {
        byte[] pin = packedBcdToString(Arrays.copyOfRange(encoded, 1, 3), "").getBytes(UTF_8);
        return new String(pin, UTF_8);
    }

    private static String packedBcdToString(byte[] packedBcd, String separator) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < packedBcd.length; i++) {
            buf.append((packedBcd[i] & 0xf0) / 16);
            buf.append(separator);
            buf.append(packedBcd[i] & 0x0F);
            buf.append(separator);
        }
        return buf.toString();
    }
}
