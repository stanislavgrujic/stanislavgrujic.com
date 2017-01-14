package com.stanislavgrujic.blog.refactoring.pinblockformat1;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * This class represents a suggested refactoring of PIN block Format 1 encoding implementation found in
 * {@link PinBlockFormat1EncoderOriginal} class.
 */
public class PinBlockFormat1EncoderRefactored {

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final String FORMAT_1_CONTROL_CHAR = "1";

    private static final int MIN_PIN_LENGTH = 4;
    private static final int MAX_PIN_LENGTH = 12;

    private static final int MAX_PIN_BLOCK_LENGTH = 16; // characters

    private static final byte PIN_LENGTH_MASK = (byte) 15;
    private static final int HIGH_BITS_MASK = 0xF0;
    private static final int LOW_BITS_MASK = 0x0F;
    private static final int PIN_START_POSITION = 1;

    /**
     * Encode passed plain PIN to PIN block Format 1 (ISO-1) encoding, as defined per ISO-9564.
     */
    public static byte[] encode(String plainPin) {
        int pinLength = plainPin.length();

        if (pinLength < MIN_PIN_LENGTH || pinLength > MAX_PIN_LENGTH) {
            throw new InvalidPinLengthException();
        }

        // control field and PIN length
        StringBuilder encodedPinBlock = new StringBuilder();
        encodedPinBlock.append(FORMAT_1_CONTROL_CHAR);
        encodedPinBlock.append(convertToHex(pinLength));

        encodedPinBlock.append(plainPin);

        // fill the pin block until the end with random digits
        int digitsToFill = MAX_PIN_BLOCK_LENGTH - encodedPinBlock.length();
        IntStream.range(0, digitsToFill)
                .forEach(iteration -> encodedPinBlock.append(createRandomHexChar()));

        return convertToBcd(encodedPinBlock.toString());
    }

    private static String createRandomHexChar() {
        return convertToHex(RANDOM.nextInt(16));
    }

    private static String convertToHex(int number) {
        return Integer.toHexString(number);
    }

    private static byte[] convertToBcd(String value) {
        int length = value.length();

        int packedLength = isOdd(length) ? (length + 1) / 2 : length / 2;
        byte[] packed = new byte[packedLength];
        IntStream.range(0, packedLength)
                .forEach(position -> packed[position] = convertCharsToBcd(value, position));
        return packed;
    }

    private static byte convertCharsToBcd(String value, int position) {
        int highPosition = position * 2;
        int lowPosition = highPosition + 1;
        byte high = (byte) (Character.getNumericValue(value.charAt(highPosition)) * 16);
        byte low = (byte) (Character.getNumericValue(value.charAt(lowPosition)));
        return (byte) (high | low);
    }

    /**
     * Decode PIN encoded as PIN block Format 1 (ISO-1) to plain pin value.
     */
    public static String decode(byte[] encoded) {
        int pinLength = extractPinLength(encoded);
        int bytesContainingPin = isOdd(pinLength) ? pinLength / 2 + 1 : pinLength / 2;

        String pinValue = packedBcdToString(extractPin(encoded, bytesContainingPin));

        // cut off the fill digit of an odd length PIN
        pinValue = pinValue.substring(0, pinLength);
        return pinValue;
    }

    private static int extractPinLength(byte[] pinBlockEncoded) {
        return pinBlockEncoded[0] & PIN_LENGTH_MASK;
    }

    private static boolean isOdd(int pinLength) {
        return pinLength % 2 != 0;
    }

    private static byte[] extractPin(byte[] encoded, int bytesContainingPin) {
        return Arrays.copyOfRange(encoded, PIN_START_POSITION, PIN_START_POSITION + bytesContainingPin);
    }

    private static String packedBcdToString(byte[] packedBcd) {
        StringBuilder builder = new StringBuilder();
        for (byte singleByte : packedBcd) {
            builder.append((singleByte & HIGH_BITS_MASK) / 16);
            builder.append(singleByte & LOW_BITS_MASK);
        }
        return builder.toString();
    }
}
