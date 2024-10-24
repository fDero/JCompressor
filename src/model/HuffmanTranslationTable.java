package model;

import java.util.HashMap;
import java.util.Map;

import io.github.fdero.bits4j.core.BitList;
import lombok.Getter;
import lombok.ToString;

@ToString
public class HuffmanTranslationTable {

    private final Map<Integer, BitList> translationMap;
    private final Map<BitList, Integer> reverseTranslationMap;
    @Getter private int totalNumberOfBitsForEncodedAlphabet = 0;
    @Getter private int maxNumberOfBitsForEncodedSymbol = 0;
    @Getter private int totalNumberOfSymbols = 0;

    public HuffmanTranslationTable(Map<Integer, BitList> translationMap) {
        this.translationMap = translationMap;
        this.reverseTranslationMap = new HashMap<>();
        translationMap.forEach((symbol, bitSequence) -> {
            reverseTranslationMap.put(bitSequence, symbol);
            totalNumberOfBitsForEncodedAlphabet += bitSequence.size();
            maxNumberOfBitsForEncodedSymbol = Math.max(maxNumberOfBitsForEncodedSymbol, bitSequence.size());
            totalNumberOfSymbols++;
        });
    }

    public BitList getHuffmanSequenceFromSymbol(Integer integerEncodedByte) {
        return translationMap.get(integerEncodedByte);
    }

    public Integer getSymbolFromHuffmanSequence(BitList huffmanSequence) {
        return reverseTranslationMap.get(huffmanSequence);
    }
}