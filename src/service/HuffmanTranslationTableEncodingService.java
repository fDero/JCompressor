package service;

import org.springframework.stereotype.Service;

import model.BitSequence;
import model.HuffmanTranslationTable;

@Service
public class HuffmanTranslationTableEncodingService {
 
    public BitSequence encodeTranslationTable(HuffmanTranslationTable translationTable) {
        int totalNumberOfSymbols = translationTable.getTotalNumberOfSymbols();
        int totalNumberOfBitsForEncodedAlphabet = translationTable.getTotalNumberOfBitsForEncodedAlphabet();
        BitSequence totalNumberOfSymbolsAsBitSequence = new BitSequence(totalNumberOfSymbols);
        BitSequence totalNumberOfBitsForEncodedAlphabetAsBitSequence = new BitSequence(totalNumberOfBitsForEncodedAlphabet);
        BitSequence encodedTranslationTable = new BitSequence();
        encodedTranslationTable.appendAllBits(totalNumberOfSymbolsAsBitSequence);
        encodedTranslationTable.appendAllBits(totalNumberOfBitsForEncodedAlphabetAsBitSequence);
        translationTable.streamSybols().forEachOrdered(symbol -> encodedTranslationTable.appendAllBits(new BitSequence(symbol)));
        translationTable.streamEncodings().forEachOrdered(encodedTranslationTable::appendAllBits);
        return encodedTranslationTable;
    }
}
