package model;

import java.util.Map;

public class HuffmanTranslationTable {

    private final Map<Byte, BitSequence> translationMap;

    public HuffmanTranslationTable(Map<Byte, BitSequence> translationMap) {
        this.translationMap = translationMap;
    }

    @Override
    public String toString() {
        return "HuffmanTranslationTable{" +
            "translationMap=" + translationMap +
            '}';
    }
}
