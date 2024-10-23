package service;

import model.SymbolTable;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SymbolTableGenerationServiceTest {

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

    private final SymbolTableGenerationService symbolTableGenerationService
            = new SymbolTableGenerationService();

    @Test
    void generateFromOccurrencyMapWithoutEOF() {
        SymbolTable symbolTable = symbolTableGenerationService
                .createFromOccurrencyMap(exampleOccurrencyMap)
                .withoutConsideringEOF();
        assertEquals(symbolTable.getTotalNumberOfDifferentSymbols(), 4);
        assertEquals(symbolTable.getTotalNumberOfOccurrencies(), 7);
    }

    @Test
    void generateFromOccurrencyMapWithEOF() {
        SymbolTable symbolTable = symbolTableGenerationService
                .createFromOccurrencyMap(exampleOccurrencyMap)
                .consideringEOF();
        assertEquals(symbolTable.getTotalNumberOfDifferentSymbols(), 5);
        assertEquals(symbolTable.getTotalNumberOfOccurrencies(), 8);
    }
}
