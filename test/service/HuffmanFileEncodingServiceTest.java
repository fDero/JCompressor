package service;

import io.github.fdero.bits4j.core.BitList;
import io.github.fdero.bits4j.core.BitListConversions;
import io.github.fdero.bits4j.stream.BitListInputStream;
import io.github.fdero.bits4j.stream.BitListOutputStream;
import io.github.fdero.bits4j.stream.BitReader;
import io.github.fdero.bits4j.stream.BitWriter;
import model.HuffmanTranslationTable;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HuffmanFileEncodingServiceTest {

    private final int EOF = -1;

    private final Map<Integer, BitList> exampleTranslationMap = Map.of(
            (int)'a', BitListConversions.fromBinaryString("1"),
            (int)'b', BitListConversions.fromBinaryString("01"),
            (int)'c', BitListConversions.fromBinaryString("001"),
            EOF,      BitListConversions.fromBinaryString("000")
    );

    private final HuffmanTranslationTable translationTable
            = new HuffmanTranslationTable(exampleTranslationMap);

    private final HuffmanFileEncodingService huffmanFileEncodingService
            = new HuffmanFileEncodingService();


    @Test
    void encodeAndDecodeUnderStandardCircumstances() throws IOException {
        ByteArrayInputStream sourceStream = new ByteArrayInputStream(new byte[] {'a', 'b', 'c'});
        BitList encodedBitList = new BitList();
        try (OutputStream outputStream = new BitListOutputStream(encodedBitList)) {
            BitWriter bitWriter = new BitWriter(outputStream);
            huffmanFileEncodingService.writeEncodedTextToOutputFile(sourceStream, translationTable, bitWriter);
            bitWriter.addPadding();
            bitWriter.flush();
        }
        ByteArrayOutputStream decodedStream = new ByteArrayOutputStream();
        try (InputStream encodedStream = new BitListInputStream(encodedBitList)) {
            BitReader bitReader = new BitReader(encodedStream);
            huffmanFileEncodingService.readDecodedTextFromInputFile(decodedStream, translationTable, bitReader);
        }
        byte[] decodedBytes = decodedStream.toByteArray();
        assertEquals(3,decodedBytes.length);
        assertEquals((byte)'a', decodedBytes[0]);
        assertEquals((byte)'b', decodedBytes[1]);
        assertEquals((byte)'c', decodedBytes[2]);
    }

    @Test
    void encodeAndDecodeFromEmptySource() throws IOException {
        ByteArrayInputStream sourceStream = new ByteArrayInputStream(new byte[] {});
        BitList encodedBitList = new BitList();
        try (OutputStream outputStream = new BitListOutputStream(encodedBitList)) {
            BitWriter bitWriter = new BitWriter(outputStream);
            huffmanFileEncodingService.writeEncodedTextToOutputFile(sourceStream, translationTable, bitWriter);
            bitWriter.addPadding();
            bitWriter.flush();
        }
        ByteArrayOutputStream decodedStream = new ByteArrayOutputStream();
        try (InputStream encodedStream = new BitListInputStream(encodedBitList)) {
            BitReader bitReader = new BitReader(encodedStream);
            huffmanFileEncodingService.readDecodedTextFromInputFile(decodedStream, translationTable, bitReader);
        }
        byte[] decodedBytes = decodedStream.toByteArray();
        assertEquals(0, decodedBytes.length);
    }
}
