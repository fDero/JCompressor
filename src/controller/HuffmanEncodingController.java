package controller;

import java.io.*;

import io.github.fdero.bits4j.core.BitValue;
import io.github.fdero.bits4j.stream.BitWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import model.HuffmanTranslationTable;
import model.SymbolTable;
import service.HuffmanFileEncodingService;
import service.HuffmanTranslationTableGenerationService;
import service.SymbolTableManagementService;

@Controller
public class HuffmanEncodingController {

    private final SymbolTableManagementService symbolTableManagementService;
    private final HuffmanTranslationTableGenerationService huffmanTranslationTableGenerator;
    private final HuffmanFileEncodingService huffmanFileEncodingService;

    @Autowired
    public HuffmanEncodingController(
            SymbolTableManagementService symbolTableManagementService,
            HuffmanTranslationTableGenerationService huffmanTranslationTableGenerationService,
            HuffmanFileEncodingService huffmanFileEncodingService
    ) {
        this.symbolTableManagementService = symbolTableManagementService;
        this.huffmanTranslationTableGenerator = huffmanTranslationTableGenerationService;
        this.huffmanFileEncodingService = huffmanFileEncodingService;
    }

    public void compress(String inputFilePath, String outputFilePath) throws IOException {
        File inputFileHandle = new File(inputFilePath);
        SymbolTable symbolTable;
        try (InputStream inputFileStream = new FileInputStream(inputFileHandle)) {
            symbolTable = symbolTableManagementService.createFromInputStream(inputFileStream).withoutConsideringEOF();
        }
        HuffmanTranslationTable huffmanTranslationTable = huffmanTranslationTableGenerator.generateTranslationTable(symbolTable);
        File outputFileHandle = new File(outputFilePath);
        try (OutputStream outputFileStream = new FileOutputStream(outputFileHandle)) {
            BitWriter bitWriter = new BitWriter(outputFileStream);
            symbolTableManagementService.writeEncodedSymbolTableToOutputFile(symbolTable, bitWriter);
            try (InputStream inputFileStream = new FileInputStream(inputFileHandle)) {
                huffmanFileEncodingService.writeEncodedTextToOutputFile(inputFileStream, huffmanTranslationTable, bitWriter);
            }
        }
    }

    public void decompress(String inputFilePath, String outputFilePath) throws IOException {
        System.out.println("Dumping contents of the file:");
        File inputFileHandle = new File(inputFilePath);
        try (InputStream inputFileStream = new FileInputStream(inputFileHandle)) {
            int integerEncodedByte;
            while ((integerEncodedByte = inputFileStream.read()) != -1) {
                System.out.println(integerEncodedByte);
            }
        }
    }
}
