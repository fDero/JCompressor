package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BitSequenceTest {
    
    @Test
    void integerConstructorTest() {
        int integerValue = 4;
        BitSequence bitSequence = new BitSequence(integerValue);
        assertEquals(32, bitSequence.size());
        assertEquals(integerValue, bitSequence.toInt());
    }

    @Test
    void byteConstructorTest() {
        byte byteValue = (byte) 'a';
        BitSequence bitSequence = new BitSequence(byteValue);
        assertEquals(8, bitSequence.size());
        assertEquals(byteValue, bitSequence.toByte());
    }

    @Test
    void stringConstructorTest() {
        BitSequence bitSequence = new BitSequence("101");
        assertEquals(3, bitSequence.size());
        assertEquals("101", bitSequence.toString());
    }

    @Test
    void stringConstructorAndAppendAllTest() {
        BitSequence bitSequence1 = new BitSequence("101");
        BitSequence bitSequence2 = new BitSequence("101");
        BitSequence bitSequence3 = new BitSequence();
        bitSequence3.appendAllBits(bitSequence1);
        bitSequence3.appendAllBits(bitSequence2);
        assertEquals(6, bitSequence3.size());
        assertEquals("101101", bitSequence3.toString());
    }
}