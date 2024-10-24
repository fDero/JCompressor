package service;

import io.github.fdero.bits4j.core.BitList;
import io.github.fdero.bits4j.core.BitValue;
import io.github.fdero.bits4j.stream.BitReader;
import io.github.fdero.bits4j.stream.BitWriter;
import model.HuffmanTranslationTable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Service
public class HuffmanFileEncodingService {

    public void writeEncodedTextToOutputFile(InputStream inputStream, HuffmanTranslationTable translationTable, BitWriter bitWriter)
            throws IOException
    {
        int oneByteSymbolAsInteger;
        while ((oneByteSymbolAsInteger = inputStream.read()) != -1) {
            BitList encoding = translationTable.getHuffmanSequenceFromSymbol(oneByteSymbolAsInteger);
            for (BitValue bit : encoding) {
                bitWriter.write(bit);
            }
        }
        bitWriter.addPadding();
        bitWriter.flush();
    }

    public void readDecodedTextFromInputFile(OutputStream outputStream, HuffmanTranslationTable translationTable, BitReader bitReader)
            throws IOException
    {
        BitList bitsReadSoFar = new BitList();
        BitValue bitValue;
        while ((bitValue = bitReader.read()) != null) {
            bitsReadSoFar.add(bitValue);
            Integer symbol = translationTable.getSymbolFromHuffmanSequence(bitsReadSoFar);
            if (symbol == null) {
                continue;
            }
            if (symbol < 0 || symbol > 255) {
                break;
            }
            outputStream.write(symbol);
            bitsReadSoFar.clear();
        }
        outputStream.flush();
    }
}
