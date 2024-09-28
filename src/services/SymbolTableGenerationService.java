package services;

import org.springframework.stereotype.Service;

import model.SymbolTable;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.InputStream;

@Service
public final class SymbolTableGenerationService {
    
    public static class SymbolTableBuilder {

        private final Map<Integer, Long> occurrencyMap = new HashMap<>();
        private boolean alreadyBuilt = false;

        void putSymbol(int integerEncodedByte, long occurrency) {
            occurrencyMap.put(integerEncodedByte, occurrency);
        }

        void addSymbol(int integerEncodedByte) {
            long frequency = occurrencyMap.getOrDefault(integerEncodedByte, Long.valueOf(0));
            occurrencyMap.put(integerEncodedByte, frequency + 1);
        }

        public SymbolTable consideringEOF() {
            assert !alreadyBuilt;
            assert !occurrencyMap.containsKey(-1);
            occurrencyMap.put(-1, Long.valueOf(1));
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

    public SymbolTableBuilder createFromOccurrencyMap(Map<Integer, Long> occurrencyMap) {
        SymbolTableBuilder builder = new SymbolTableBuilder();
        occurrencyMap.forEach(builder::putSymbol);
        return builder;
    }

    public SymbolTableBuilder createFromInputStream(InputStream inputStream) throws IOException {
        SymbolTableBuilder builder = new SymbolTableBuilder();
        int integerEncodedByte = -1;
        while ((integerEncodedByte = inputStream.read()) != -1) {
            builder.addSymbol(integerEncodedByte);
        }
        return builder;
    }
}
