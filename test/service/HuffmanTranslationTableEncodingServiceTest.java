package service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.PriorityQueue;

import org.junit.jupiter.api.Test;

import model.BitSequence;
import model.HuffmanTranslationTable;

class HuffmanTranslationTableEncodingServiceTest {

    private final HuffmanTranslationTableEncodingService huffmanTranslationTableEncodingService 
        = new HuffmanTranslationTableEncodingService();

    private byte characterA = (byte) 'a';
    private byte characterB = (byte) 'b';
    private byte characterC = (byte) 'c';
    private byte characterD = (byte) 'd';

    private final HuffmanTranslationTable translationTable = new HuffmanTranslationTable(Map.of(
        characterA, new BitSequence("0"), 
        characterB, new BitSequence("10"), 
        characterC, new BitSequence("110"), 
        characterD, new BitSequence("111")
    ));

    @Test
    void encodeTranslationTableTest() {
        BitSequence encodedTranslationTable = huffmanTranslationTableEncodingService.encodeTranslationTable(translationTable);
        System.out.println(encodedTranslationTable);
        int expectedBitSequenceLength = 32 + 32;
        expectedBitSequenceLength += 8 * translationTable.getTotalNumberOfSymbols();
        expectedBitSequenceLength += translationTable.getTotalNumberOfBitsForEncodedAlphabet();
        assertEquals(expectedBitSequenceLength, encodedTranslationTable.size());
    }
}
