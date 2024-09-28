package model;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.cache.annotation.Cacheable;

public class SymbolTable {

    private final Map<Integer, Long> occurrencyMap;
    private long totalNumberOfSymbols = 0L;

    @SuppressWarnings("unused")
    private final String uniqueTableId = UUID.randomUUID().toString();

    public SymbolTable(Map<Integer, Long> occurrencyMap) {
        this.occurrencyMap = occurrencyMap;
        occurrencyMap.forEach((symbol, occurrence) -> totalNumberOfSymbols += occurrence);
    }

    public long getOccurrency(int integerEncodedByte) {
        return occurrencyMap.getOrDefault(integerEncodedByte, Long.valueOf(0));
    }

    public double getFrequency(int integerEncodedByte) {
        return (totalNumberOfSymbols == 0)?
            0 : getOccurrency(integerEncodedByte) / (double) totalNumberOfSymbols;
    }

    @Cacheable(value="cumulativeFrequencies", key="#integerEncodedByte + '-' + #uniqueTableId")
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
}
