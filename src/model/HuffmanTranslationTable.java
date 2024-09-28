package model;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.ToString;

@ToString
public class HuffmanTranslationTable {

    private final Map<Byte, BitSequence> translationMap;
    @Getter private int totalNumberOfBitsForEncodedAlphabet = 0;
    @Getter private int maxNumberOfBitsForEncodedSymbol = 0;
    @Getter private int totalNumberOfSymbols = 0;

    public HuffmanTranslationTable(Map<Byte, BitSequence> translationMap) {
        this.translationMap = translationMap;
        translationMap.forEach((symbol, bitSequence) -> {
            totalNumberOfBitsForEncodedAlphabet += bitSequence.size();
            maxNumberOfBitsForEncodedSymbol = Math.max(maxNumberOfBitsForEncodedSymbol, bitSequence.size());
            totalNumberOfSymbols++;
        });
    }

    public Stream<BitSequence> streamEncodings() {
        return translationMap.entrySet().stream().sequential()
            .sorted(Map.Entry.<Byte, BitSequence>comparingByValue(Comparator.comparingInt(BitSequence::size)))
            .map(Map.Entry::getValue);
    }

    public Stream<Byte> streamSybols() {
        return translationMap.entrySet().stream().sequential()
            .sorted(Map.Entry.<Byte, BitSequence>comparingByValue(Comparator.comparingInt(BitSequence::size)))
            .map(Map.Entry::getKey);
    }
}
