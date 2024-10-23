package model;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Stream;

import io.github.fdero.bits4j.core.BitList;
import lombok.Getter;
import lombok.ToString;

@ToString
public class HuffmanTranslationTable {

    private final Map<Integer, BitList> translationMap;
    @Getter private int totalNumberOfBitsForEncodedAlphabet = 0;
    @Getter private int maxNumberOfBitsForEncodedSymbol = 0;
    @Getter private int totalNumberOfSymbols = 0;

    public HuffmanTranslationTable(Map<Integer, BitList> translationMap) {
        this.translationMap = translationMap;
        translationMap.forEach((symbol, bitSequence) -> {
            totalNumberOfBitsForEncodedAlphabet += bitSequence.size();
            maxNumberOfBitsForEncodedSymbol = Math.max(maxNumberOfBitsForEncodedSymbol, bitSequence.size());
            totalNumberOfSymbols++;
        });
    }

    public Stream<BitList> streamEncodings() {
        return translationMap.entrySet().stream().sequential()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingInt(BitList::size)))
                .map(Map.Entry::getValue);
    }

    public Stream<Integer> streamSymbols() {
        return translationMap.entrySet().stream().sequential()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingInt(BitList::size)))
                .map(Map.Entry::getKey);
    }
}