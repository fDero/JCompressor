package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

class SymbolTableTest {

    @Test
    void testGetOccurrency() {
        Map<Integer, Long> explicitSymbolMap = new HashMap<>();
        int characterA = (int) 'a';
        int characterB = (int) 'b';
        long occurrencyOfCharacterA = 5L;
        long occurrencyOfCharacterB = 5L;
        explicitSymbolMap.put(characterA, occurrencyOfCharacterA);
        explicitSymbolMap.put(characterB, occurrencyOfCharacterB);
        SymbolTable symbolTable = new SymbolTable(explicitSymbolMap);
        assertEquals(occurrencyOfCharacterA, symbolTable.getOccurrency(characterA));
        assertEquals(occurrencyOfCharacterB, symbolTable.getOccurrency(characterB));
    }

    @Test
    void testGetFrequency() {
        Map<Integer, Long> explicitSymbolMap = new HashMap<>();
        int characterA = (int) 'a';
        int characterB = (int) 'b';
        long occurrencyOfCharacterA = 2L;
        long occurrencyOfCharacterB = 2L;
        long totalNumberOfSymbols = occurrencyOfCharacterA + occurrencyOfCharacterB;
        explicitSymbolMap.put(characterA, occurrencyOfCharacterA);
        explicitSymbolMap.put(characterB, occurrencyOfCharacterB);
        SymbolTable symbolTable = new SymbolTable(explicitSymbolMap);
        double expectedFrequencyOfCharacterA = (double) occurrencyOfCharacterA / (double) totalNumberOfSymbols;
        double expectedFrequencyOfCharacterB = (double) occurrencyOfCharacterB / (double) totalNumberOfSymbols;
        assertEquals(expectedFrequencyOfCharacterA, symbolTable.getFrequency(characterA));
        assertEquals(expectedFrequencyOfCharacterB, symbolTable.getFrequency(characterB));
    }

    @Test
    void testGetUpperCumulativeFrequency() {
        Map<Integer, Long> explicitSymbolMap = new HashMap<>();
        int characterA = (int) 'a';
        int characterB = (int) 'b';
        long occurrencyOfCharacterA = 2L;
        long occurrencyOfCharacterB = 3L;
        explicitSymbolMap.put(characterA, occurrencyOfCharacterA);
        explicitSymbolMap.put(characterB, occurrencyOfCharacterB);
        SymbolTable symbolTable = new SymbolTable(explicitSymbolMap);
        double expectedCumulativeUpperFrequencyOfCharacterA = symbolTable.getFrequency(characterA);
        double expectedCumulativeUpperFrequencyOfCharacterB = symbolTable.getFrequency(characterA) + symbolTable.getFrequency(characterB);
        assertEquals(expectedCumulativeUpperFrequencyOfCharacterA, symbolTable.getUpperCumulativeFrequency(characterA));
        assertEquals(expectedCumulativeUpperFrequencyOfCharacterB, symbolTable.getUpperCumulativeFrequency(characterB));
    }

    
    @Test
    void testGetLowerCumulativeFrequency() {
        Map<Integer, Long> explicitSymbolMap = new HashMap<>();
        int characterA = (int) 'a';
        int characterB = (int) 'b';
        long occurrencyOfCharacterA = 2L;
        long occurrencyOfCharacterB = 3L;
        explicitSymbolMap.put(characterA, occurrencyOfCharacterA);
        explicitSymbolMap.put(characterB, occurrencyOfCharacterB);
        SymbolTable symbolTable = new SymbolTable(explicitSymbolMap);
        double expectedCumulativeLowerFrequencyOfCharacterA = 0;
        double expectedCumulativeLowerFrequencyOfCharacterB = symbolTable.getFrequency(characterA);
        assertEquals(expectedCumulativeLowerFrequencyOfCharacterA, symbolTable.getLowerCumulativeFrequency(characterA));
        assertEquals(expectedCumulativeLowerFrequencyOfCharacterB, symbolTable.getLowerCumulativeFrequency(characterB));
    }

    @Test
    void testGetOccurrencyOnEmptyTable() {
        Map<Integer, Long> explicitSymbolMap = new HashMap<>();
        int characterA = (int) 'a';
        int characterB = (int) 'b';
        long occurrencyOfCharacterA = 0L;
        long occurrencyOfCharacterB = 0L;
        explicitSymbolMap.put(characterA, occurrencyOfCharacterA);
        explicitSymbolMap.put(characterB, occurrencyOfCharacterB);
        SymbolTable symbolTable = new SymbolTable(explicitSymbolMap);
        assertEquals(occurrencyOfCharacterA, symbolTable.getOccurrency(characterA));
        assertEquals(occurrencyOfCharacterB, symbolTable.getOccurrency(characterB));
    }

    @Test
    void testGetFrequencyOnEmptyTable() {
        Map<Integer, Long> explicitSymbolMap = new HashMap<>();
        int characterA = (int) 'a';
        int characterB = (int) 'b';
        long occurrencyOfCharacterA = 0L;
        long occurrencyOfCharacterB = 0L;
        explicitSymbolMap.put(characterA, occurrencyOfCharacterA);
        explicitSymbolMap.put(characterB, occurrencyOfCharacterB);
        SymbolTable symbolTable = new SymbolTable(explicitSymbolMap);
        double expectedFrequencyOfCharacterA = 0D;
        double expectedFrequencyOfCharacterB = 0D;
        assertEquals(expectedFrequencyOfCharacterA, symbolTable.getFrequency(characterA));
        assertEquals(expectedFrequencyOfCharacterB, symbolTable.getFrequency(characterB));
    }

    @Test
    void testGetUpperCumulativeFrequencyOnEmptyTable() {
        Map<Integer, Long> explicitSymbolMap = new HashMap<>();
        int characterA = (int) 'a';
        int characterB = (int) 'b';
        long occurrencyOfCharacterA = 0L;
        long occurrencyOfCharacterB = 0L;
        explicitSymbolMap.put(characterA, occurrencyOfCharacterA);
        explicitSymbolMap.put(characterB, occurrencyOfCharacterB);
        SymbolTable symbolTable = new SymbolTable(explicitSymbolMap);
        double expectedCumulativeUpperFrequencyOfCharacterA = 0D;
        double expectedCumulativeUpperFrequencyOfCharacterB = 0D;
        assertEquals(expectedCumulativeUpperFrequencyOfCharacterA, symbolTable.getUpperCumulativeFrequency(characterA));
        assertEquals(expectedCumulativeUpperFrequencyOfCharacterB, symbolTable.getUpperCumulativeFrequency(characterB));
    }
    
    @Test
    void testGetLowerCumulativeFrequencyOnEmptyTable() {
        Map<Integer, Long> explicitSymbolMap = new HashMap<>();
        int characterA = (int) 'a';
        int characterB = (int) 'b';
        long occurrencyOfCharacterA = 0L;
        long occurrencyOfCharacterB = 0L;
        explicitSymbolMap.put(characterA, occurrencyOfCharacterA);
        explicitSymbolMap.put(characterB, occurrencyOfCharacterB);
        SymbolTable symbolTable = new SymbolTable(explicitSymbolMap);
        double expectedCumulativeLowerFrequencyOfCharacterA = 0D;
        double expectedCumulativeLowerFrequencyOfCharacterB = 0D;
        assertEquals(expectedCumulativeLowerFrequencyOfCharacterA, symbolTable.getLowerCumulativeFrequency(characterA));
        assertEquals(expectedCumulativeLowerFrequencyOfCharacterB, symbolTable.getLowerCumulativeFrequency(characterB));
    }
}