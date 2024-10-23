package service;

import io.github.fdero.bits4j.core.BitList;
import io.github.fdero.bits4j.core.BitListConversions;
import model.SymbolTable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SymbolTableEncodingServiceTest {

    private final int characterA = 'a';
    private final int characterB = 'b';
    private final int characterC = 'c';
    private final int characterD = 'd';

    private final SymbolTable exampleSymbolTable = new SymbolTable(Map.of(
            characterA, 3L,
            characterB, 2L,
            characterC, 1L,
            characterD, 1L
    ));

    private final SymbolTableEncodingService symbolTableEncodingService
            = new SymbolTableEncodingService();

    @Test
    void encodeSymbolTableOverallFormatTest() {
        BitList encodedSymbolTable = symbolTableEncodingService.encodeSymbolTable(exampleSymbolTable);
        int expectedBitSequenceLength = 32;
        expectedBitSequenceLength += 32 * exampleSymbolTable.getTotalNumberOfDifferentSymbols();
        expectedBitSequenceLength += 64 * exampleSymbolTable.getTotalNumberOfDifferentSymbols();
        System.out.println(encodedSymbolTable.toString());
        System.out.println(expectedBitSequenceLength);
        assertEquals(expectedBitSequenceLength, encodedSymbolTable.size());
    }

    @Test
    void encodeSymbolTablePreambleContentTest() {
        BitList encodedSymbolTable = symbolTableEncodingService.encodeSymbolTable(exampleSymbolTable);
        BitList first32bits = new BitList();
        first32bits.addAll(encodedSymbolTable.subList(0, 32));
        assertEquals(exampleSymbolTable.getTotalNumberOfDifferentSymbols(), BitListConversions.asInt(first32bits));
    }

    @Test
    void encodeSymbolTableAlphabetSectionContentTest() {
        BitList encodedSymbolTable = symbolTableEncodingService.encodeSymbolTable(exampleSymbolTable);
        int totalNumberOfSymbols = exampleSymbolTable.getTotalNumberOfDifferentSymbols();
        int bitOffset = 32;
        List<Integer> retrievedSymbols = new ArrayList<>();
        for (int i = 0; i < totalNumberOfSymbols; i++) {
            BitList symbolAsBitSequence = new BitList();
            symbolAsBitSequence.addAll(encodedSymbolTable.subList(bitOffset, bitOffset + 32));
            retrievedSymbols.add(BitListConversions.asInt(symbolAsBitSequence));
            bitOffset += 32;
        }
        Iterator<Integer> sourceSymbolsIterator = exampleSymbolTable.streamFromMostFrequent().iterator();
        Iterator<Integer> retrievedSymbolsIterator = retrievedSymbols.iterator();
        while (sourceSymbolsIterator.hasNext() && retrievedSymbolsIterator.hasNext()) {
            assertEquals(sourceSymbolsIterator.next().byteValue(), retrievedSymbolsIterator.next());
        }
        assertEquals(sourceSymbolsIterator.hasNext(), retrievedSymbolsIterator.hasNext());
    }

    @Test
    void encodeSymbolTableEncodedOccurrenciesSectionContentTest() {
        BitList encodedSymbolTable = symbolTableEncodingService.encodeSymbolTable(exampleSymbolTable);
        int totalNumberOfSymbols = exampleSymbolTable.getTotalNumberOfDifferentSymbols();
        int bitOffset = 32 + 32 * totalNumberOfSymbols;
        List<Long> retrievedOccurrencies = new ArrayList<>();
        for (int i = 0; i < totalNumberOfSymbols; i++) {
            BitList symbolAsBitSequence = new BitList();
            symbolAsBitSequence.addAll(encodedSymbolTable.subList(bitOffset, bitOffset + 64));
            retrievedOccurrencies.add(BitListConversions.asLong(symbolAsBitSequence));
            bitOffset += 64;
        }
        Iterator<Long> sourceOccurrenciesIterator = exampleSymbolTable.streamOccurrenciesDesc().iterator();
        Iterator<Long> retrievedSymbolsIterator = retrievedOccurrencies.iterator();
        while (sourceOccurrenciesIterator.hasNext() && retrievedSymbolsIterator.hasNext()) {
            assertEquals(sourceOccurrenciesIterator.next(), retrievedSymbolsIterator.next());
        }
        assertEquals(sourceOccurrenciesIterator.hasNext(), retrievedSymbolsIterator.hasNext());
    }

    @Test
    void decodeTest() {
        BitList encodedSymbolTable = symbolTableEncodingService.encodeSymbolTable(exampleSymbolTable);
        SymbolTable decodedSymbolTable = symbolTableEncodingService.decodeSymbolTable(encodedSymbolTable);
        assertEquals(exampleSymbolTable, decodedSymbolTable);
    }
}
