package service;

import io.github.fdero.bits4j.core.BitList;
import io.github.fdero.bits4j.core.BitListConversions;
import io.github.fdero.bits4j.stream.BitListInputStream;
import io.github.fdero.bits4j.stream.BitReader;
import model.SymbolTable;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SymbolTableManagementServiceTest {

    private final int characterA = 'a';
    private final int characterB = 'b';
    private final int characterC = 'c';
    private final int characterD = 'd';

    private final Map<Integer, Long> exampleOccurrencyMap = Map.of(
            characterA, 3L,
            characterB, 2L,
            characterC, 1L,
            characterD, 1L
    );

    private final SymbolTable exampleSymbolTable = new SymbolTable(exampleOccurrencyMap);

    private final SymbolTableManagementService symbolTableManagementService
            = new SymbolTableManagementService();

    @Test
    void generateFromOccurrencyMapWithoutEOF() {
        SymbolTable symbolTable = symbolTableManagementService
                .createFromOccurrencyMap(exampleOccurrencyMap)
                .withoutConsideringEOF();
        assertEquals(symbolTable.getTotalNumberOfDifferentSymbols(), 4);
        assertEquals(symbolTable.getTotalNumberOfOccurrencies(), 7);
    }

    @Test
    void generateFromOccurrencyMapWithEOF() {
        SymbolTable symbolTable = symbolTableManagementService
                .createFromOccurrencyMap(exampleOccurrencyMap)
                .consideringEOF();
        assertEquals(symbolTable.getTotalNumberOfDifferentSymbols(), 5);
        assertEquals(symbolTable.getTotalNumberOfOccurrencies(), 8);
    }

    @Test
    void encodeSymbolTableOverallFormatTest() {
        BitList encodedSymbolTable = symbolTableManagementService.encodeSymbolTable(exampleSymbolTable);
        int expectedBitSequenceLength = 32;
        expectedBitSequenceLength += 32 * exampleSymbolTable.getTotalNumberOfDifferentSymbols();
        expectedBitSequenceLength += 64 * exampleSymbolTable.getTotalNumberOfDifferentSymbols();
        System.out.println(encodedSymbolTable.toString());
        System.out.println(expectedBitSequenceLength);
        assertEquals(expectedBitSequenceLength, encodedSymbolTable.size());
    }

    @Test
    void encodeSymbolTablePreambleContentTest() {
        BitList encodedSymbolTable = symbolTableManagementService.encodeSymbolTable(exampleSymbolTable);
        BitList first32bits = new BitList();
        first32bits.addAll(encodedSymbolTable.subList(0, 32));
        assertEquals(exampleSymbolTable.getTotalNumberOfDifferentSymbols(), BitListConversions.asInt(first32bits));
    }

    @Test
    void encodeSymbolTableAlphabetSectionContentTest() {
        BitList encodedSymbolTable = symbolTableManagementService.encodeSymbolTable(exampleSymbolTable);
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
        BitList encodedSymbolTable = symbolTableManagementService.encodeSymbolTable(exampleSymbolTable);
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
    void decodeTest() throws IOException {
        BitList encodedSymbolTable = symbolTableManagementService.encodeSymbolTable(exampleSymbolTable);
        BitReader bitReader = new BitReader(new BitListInputStream(encodedSymbolTable));
        SymbolTable decodedSymbolTable = symbolTableManagementService.readSymbolTableFromCompressedFile(bitReader);
        assertEquals(exampleSymbolTable, decodedSymbolTable);
    }
}
