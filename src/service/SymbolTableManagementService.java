package service;

import controller.HuffmanEncodingController;
import io.github.fdero.bits4j.core.BitList;
import io.github.fdero.bits4j.core.BitListConversions;
import io.github.fdero.bits4j.core.BitValue;
import io.github.fdero.bits4j.stream.BitReader;
import io.github.fdero.bits4j.stream.BitWriter;
import model.SymbolTable;
import model.SymbolTableBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class SymbolTableManagementService {

    private static final Logger logger = LoggerFactory.getLogger(SymbolTableManagementService.class);

    public SymbolTableBuilder createFromOccurrencyMap(Map<Integer, Long> occurrencyMap) {
        logger.debug("generating the symbol-table from given occurrency-map...");
        SymbolTableBuilder builder = new SymbolTableBuilder();
        occurrencyMap.forEach(builder::putSymbol);
        return builder;
    }

    public SymbolTableBuilder createFromInputStream(InputStream inputStream) throws IOException {
        logger.info("generating the symbol-table from the source-input-file...");
        SymbolTableBuilder builder = new SymbolTableBuilder();
        int integerEncodedByte;
        while ((integerEncodedByte = inputStream.read()) != -1) {
            builder.addSymbol(integerEncodedByte);
        }
        return builder;
    }

    public void writeSymbolTableOnCompressedFile(SymbolTable symbolTable, BitWriter bitWriter)
            throws IOException
    {
        logger.info("writing the encoded-symbol-table on the output-compressed-file...");
        for (BitValue bit : encodeSymbolTable(symbolTable)) {
            bitWriter.write(bit);
        }
    }

    public BitList encodeSymbolTable(SymbolTable symbolTable) {
        logger.info("encoding the symbol-table in a raw sequence of bits...");
        BitList encodedSymbolTable = new BitList();
        encodedSymbolTable.addAll(BitListConversions.fromInt(symbolTable.getTotalNumberOfDifferentSymbols()));
        symbolTable.streamFromMostFrequent().forEachOrdered(plainTextSymbol -> encodedSymbolTable.addAll(BitListConversions.fromInt(plainTextSymbol)));
        symbolTable.streamOccurrenciesDesc().forEachOrdered(encodedSymbol -> encodedSymbolTable.addAll(BitListConversions.fromLong(encodedSymbol)));
        return encodedSymbolTable;
    }

    public SymbolTable readSymbolTableFromCompressedFile(BitReader bitReader) throws IOException {
        logger.info("reading the encoded-symbol-table from the input-compressed-file...");
        BitList first32bits = new BitList();
        for (int i = 0; i < 32; i++) {

            BitValue bit = bitReader.read();
            first32bits.add(bit);
        }
        int numberOfSymbols = BitListConversions.asInt(first32bits);
        Map<Integer, Long> occurrencyMap = new HashMap<>();
        List<Integer> symbols = new ArrayList<>();
        for (int i = 0; i < numberOfSymbols; i++) {
            BitList textAsBits = new BitList();
            for (int j = 0; j < 32; j++) {

                textAsBits.add(bitReader.read());
            }
            symbols.add(BitListConversions.asInt(textAsBits));
        }
        assert symbols.size() == numberOfSymbols;
        for (Integer symbol : symbols) {
            BitList occurrencyAsBits = new BitList();
            for (int j = 0; j < 64; j++) {

                occurrencyAsBits.add(bitReader.read());
            }
            long occurrency = BitListConversions.asLong(occurrencyAsBits);
            occurrencyMap.put(symbol, occurrency);
        }
        return new SymbolTable(occurrencyMap);
    }
}
