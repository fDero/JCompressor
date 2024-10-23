package service;

import io.github.fdero.bits4j.core.BitList;
import io.github.fdero.bits4j.core.BitListConversions;
import model.SymbolTable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SymbolTableEncodingService {

    public BitList encodeSymbolTable(SymbolTable symbolTable) {
        BitList encodedSymbolTable = new BitList();
        encodedSymbolTable.addAll(BitListConversions.fromInt(symbolTable.getTotalNumberOfDifferentSymbols()));
        symbolTable.streamFromMostFrequent().forEachOrdered(plainTextSymbol -> encodedSymbolTable.addAll(BitListConversions.fromInt(plainTextSymbol)));
        symbolTable.streamOccurrenciesDesc().forEachOrdered(encodedSymbol -> encodedSymbolTable.addAll(BitListConversions.fromLong(encodedSymbol)));
        return encodedSymbolTable;
    }

    public SymbolTable decodeSymbolTable(BitList encodedSymbolTable) {
        Map<Integer, Long> occurrencyMap = new HashMap<>();
        BitList first32bits = new BitList();
        System.out.println(encodedSymbolTable.size());
        first32bits.addAll(encodedSymbolTable.subList(0, 32));
        int numberOfSymbols = BitListConversions.asInt(first32bits);
        int expectedSize = numberOfSymbols * 32 + numberOfSymbols * 64 + 32;
        assert expectedSize == encodedSymbolTable.size();
        List<Integer> symbols = new ArrayList<>();
        for (int i = 0; i < numberOfSymbols; i++) {
            BitList textAsBits = new BitList();
            textAsBits.addAll(encodedSymbolTable.subList(32 + i*32, 64 + i*32));
            symbols.add(BitListConversions.asInt(textAsBits));
        }
        int offset = 32 + numberOfSymbols*32;
        for (Integer symbol : symbols) {
            BitList occurrencyAsBits = new BitList();
            occurrencyAsBits.addAll(encodedSymbolTable.subList(offset, offset += 64));
            long occurrency = BitListConversions.asLong(occurrencyAsBits);
            occurrencyMap.put(symbol, occurrency);
        }
        return new SymbolTable(occurrencyMap);
    }
}
