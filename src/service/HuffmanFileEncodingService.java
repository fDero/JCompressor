package service;

import io.github.fdero.bits4j.core.BitList;
import io.github.fdero.bits4j.core.BitValue;
import io.github.fdero.bits4j.stream.BitReader;
import io.github.fdero.bits4j.stream.BitWriter;
import model.HuffmanTranslationTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Service
public class HuffmanFileEncodingService {

    private static final Logger logger = LoggerFactory.getLogger(HuffmanFileEncodingService.class);

    public void writeEncodedTextToOutputFile(InputStream inputStream, HuffmanTranslationTable translationTable, BitWriter bitWriter)
            throws IOException
    {
        logger.info("translating contents of input-source-file to their encodings and writing the result to the output-compressed-file...");
        int oneByteSymbolAsInteger;
        while ((oneByteSymbolAsInteger = inputStream.read()) != -1) {
            BitList encoding = translationTable.getHuffmanSequenceFromSymbol(oneByteSymbolAsInteger);
            for (BitValue bit : encoding) {
                bitWriter.write(bit);
            }
        }
        for (BitValue bit : translationTable.getHuffmanSequenceForEOF()) {
            bitWriter.write(bit);
        }
        bitWriter.addPadding();
        bitWriter.flush();
    }

    public void readDecodedTextFromInputFile(OutputStream outputStream, HuffmanTranslationTable translationTable, BitReader bitReader)
            throws IOException
    {
        logger.info("translating contents of input-compressed-file to plain-text and writing the result to the output-decompressed-file...");
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
