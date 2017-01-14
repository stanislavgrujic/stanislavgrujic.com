package com.stanislavgrujic.blog.refactoring.pinblockformat1;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * Test class for {@link PinBlockFormat1EncoderRefactored}.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class PinBlockFormat1EncoderRefactoredTest {

    @Test
    public void shouldEncodeAndDecodePinAsPinBlockFormat1() {
        // given
        String pin = "1234";

        // when
        byte[] pinBlock = PinBlockFormat1EncoderRefactored.encode(pin);
        String decodedPin = PinBlockFormat1EncoderRefactored.decode(pinBlock);

        // then
        Assert.assertEquals("1234", decodedPin);
    }

    @Test
    public void shouldEncodeAndDecodeOddLengthPinAsPinBlockFormat1() {
        // given
        String pin = "12345";

        // when
        byte[] pinBlock = PinBlockFormat1EncoderRefactored.encode(pin);
        String decodedPin = PinBlockFormat1EncoderRefactored.decode(pinBlock);

        // then
        Assert.assertEquals("12345", decodedPin);
    }

    @Test
    public void shouldEncodeAndDecodePinLongerThan4DigitsAsPinBlockFormat1() {
        // given
        String pin = "123456789";

        // when
        byte[] pinBlock = PinBlockFormat1EncoderRefactored.encode(pin);
        String decodedPin = PinBlockFormat1EncoderRefactored.decode(pinBlock);

        // then
        Assert.assertEquals("123456789", decodedPin);
    }

    @Test(expected = InvalidPinLengthException.class)
    public void shouldThrowInvalidPinLengthExceptionOnShortPin() {
        // given
        String pin = "123";

        // when then
        byte[] pinBlock = PinBlockFormat1EncoderRefactored.encode(pin);
    }

    @Test(expected = InvalidPinLengthException.class)
    public void shouldThrowInvalidPinLengthExceptionOnLongPin() {
        // given
        String pin = "1234567890123";

        // when then
        byte[] pinBlock = PinBlockFormat1EncoderRefactored.encode(pin);
    }
}
