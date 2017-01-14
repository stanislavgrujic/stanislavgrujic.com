package com.stanislavgrujic.blog.refactoring.pinblockformat1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * Test for {@link PinBlockFormat1EncoderOriginal}.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class PinBlockFormat1EncoderOriginalTest {

    @Test
    public void shouldEncodeAndDecodePinAsPinBlockFormat1() {
        // given
        String pin = "1234";

        // when
        byte[] pinBlock = PinBlockFormat1EncoderOriginal.encodeAsPinBlockFormat4(pin);
        String decodedPin = PinBlockFormat1EncoderOriginal.decodePinBlockFormat4(pinBlock);

        // then
        assertEquals("1234", decodedPin);
    }

    // Tests bellow are verifying the incorrect behavior of the the encoder

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionOnOddLengthEncoding() {
        // given
        String pin = "1234678";

        // when
        byte[] pinBlock = PinBlockFormat1EncoderOriginal.encodeAsPinBlockFormat4(pin);
        String decodedPin = PinBlockFormat1EncoderOriginal.decodePinBlockFormat4(pinBlock);

        // then
        assertEquals("1234678", decodedPin);
    }

    @Test
    public void shouldExtractOnlyFirstFourPinDigitsOnPinLongerThan4() {
        // given
        String pin = "123456";

        // when
        byte[] pinBlock = PinBlockFormat1EncoderOriginal.encodeAsPinBlockFormat4(pin);
        String decodedPin = PinBlockFormat1EncoderOriginal.decodePinBlockFormat4(pinBlock);

        // then
        assertEquals("1234", decodedPin);
    }
}
