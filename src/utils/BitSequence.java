package utils;

import java.util.BitSet;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class BitSequence {
    
    private BitSet bitSet = new BitSet();
    private int size = 0;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(bitSet.get(i) ? '1' : '0');
        }
        return sb.toString();
    }
}