package model;

import lombok.Getter;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public class SymbolTable {

    private final Map<Integer, Long> occurrencyMap;

    @Getter
    private long totalNumberOfOccurrencies = 0L;

    @SuppressWarnings("unused")
    private final String uniqueTableId = UUID.randomUUID().toString();

    public SymbolTable(Map<Integer, Long> occurrencyMap) {
        this.occurrencyMap = occurrencyMap;
        occurrencyMap.forEach((symbol, occurrence) -> totalNumberOfOccurrencies += occurrence);
    }

    public long getOccurrency(int integerEncodedByte) {
        return occurrencyMap.getOrDefault(integerEncodedByte, 0L);
    }

    public double getFrequency(int integerEncodedByte) {
        return (totalNumberOfOccurrencies == 0)?
            0 : getOccurrency(integerEncodedByte) / (double) totalNumberOfOccurrencies;
    }

    public double getLowerCumulativeFrequency(int integerEncodedByte) {
        double cumulativeFrequency = 0;
        for (Map.Entry<Integer, Long> entry : occurrencyMap.entrySet()) {
            if (entry.getKey() == integerEncodedByte) {
                break;
            }
            cumulativeFrequency += getFrequency(entry.getKey());
        }
        return cumulativeFrequency;
    }

    public double getUpperCumulativeFrequency(int integerEncodedByte) {
        return getLowerCumulativeFrequency(integerEncodedByte) + getFrequency(integerEncodedByte);
    }

    public Stream<Integer> streamFromMostFrequent() {
        return occurrencyMap.entrySet().stream()
            .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
            .map(Map.Entry::getKey);
    }

    public Stream<Integer> streamFromLeastFrequent() {
        return occurrencyMap.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey);
    }

    public Stream<Long> streamOccurrenciesDesc() {
        return occurrencyMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .map(Map.Entry::getValue);
    }

    public Stream<Long> streamOccurrenciesAsc() {
        return occurrencyMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue);
    }

    public int getTotalNumberOfDifferentSymbols() {
        return occurrencyMap.entrySet().size();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof SymbolTable otherSymbolTable) {
            return occurrencyMap.equals(otherSymbolTable.occurrencyMap);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return occurrencyMap.hashCode();
    }
}
