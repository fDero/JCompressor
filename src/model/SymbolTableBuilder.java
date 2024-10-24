package model;

import java.util.HashMap;
import java.util.Map;

public class SymbolTableBuilder {

    private final Map<Integer, Long> occurrencyMap = new HashMap<>();
    private boolean alreadyBuilt = false;

    public void putSymbol(int integerEncodedByte, long occurrency) {
        occurrencyMap.put(integerEncodedByte, occurrency);
    }

    public void addSymbol(int integerEncodedByte) {
        long frequency = occurrencyMap.getOrDefault(integerEncodedByte, 0L);
        occurrencyMap.put(integerEncodedByte, frequency + 1);
    }

    public SymbolTable consideringEOF() {
        assert !alreadyBuilt;
        assert !occurrencyMap.containsKey(-1);
        occurrencyMap.put(-1, 1L);
        alreadyBuilt = true;
        return new SymbolTable(occurrencyMap);
    }

    public SymbolTable withoutConsideringEOF() {
        assert !alreadyBuilt;
        assert !occurrencyMap.containsKey(-1);
        alreadyBuilt = true;
        return new SymbolTable(occurrencyMap);
    }
}
