package model;

import java.util.BitSet;

import lombok.AllArgsConstructor;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class BitSequence {

    private final BitSet bitSet;
    private int size = 0;

    public BitSequence() {
        bitSet = new BitSet();
        size = 0;
    }

    public BitSequence(String stringEncodedBitSequence) {
        this();
        for (char c : stringEncodedBitSequence.toCharArray()) {
            assert c == '0' || c == '1';
            push(c == '1');
        }
    }

    public BitSequence(int integerNumber32bits) {
        bitSet = BitSet.valueOf(new long[] { integerNumber32bits });
        size = 32;
    }

    public BitSequence(byte byteNumber8bits) {
        bitSet = BitSet.valueOf(new long[] { byteNumber8bits });
        size = 8;
    }

    public int toInt() {
        assert size == 32;
        return (int) bitSet.toLongArray()[0];
    }

    
    public byte toByte() {
        assert size == 8;
        return (byte) bitSet.toLongArray()[0];
    }

    public void appendAllBits(BitSequence other) {
        for (int i = 0; i < other.size; i++) {
            push(other.get(i));
        }
    }

    public BitSequence pushZero() {
        bitSet.clear(size);
        size++;
        return this;
    }

    public BitSequence pushOne() {
        bitSet.set(size);
        size++;
        return this;
    }

    public BitSequence push(boolean bit) {
        return (bit)? pushOne() : pushZero();
    }

    public BitSequence generateCopy() {
        return new BitSequence((BitSet) this.bitSet.clone(), this.size);
    }

    public int size() {
        return size;
    }

    public boolean get(int index) {
        return bitSet.get(index);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(bitSet.get(i) ? '1' : '0');
        }
        return sb.toString();
    }

    @Override
    public Object clone() {
        return generateCopy();
    }
}
