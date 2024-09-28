package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import model.HuffmanTranslationTable;
import model.SymbolTable;
import service.HuffmanTranslationTableGenerationService;
import service.SymbolTableGenerationService;

@Controller
public class HuffmanEncodingController {

    private final SymbolTableGenerationService symbolTableGenerator;
    private final HuffmanTranslationTableGenerationService huffmanTranslationTableGenerator;

    @Autowired
    public HuffmanEncodingController(
        SymbolTableGenerationService symbolTableGenerationService,
        HuffmanTranslationTableGenerationService huffmanTranslationTableGenerationService
    ) {
        this.symbolTableGenerator = symbolTableGenerationService;
        this.huffmanTranslationTableGenerator = huffmanTranslationTableGenerationService;
    }

    public void compress(String inputFilePath, String outputFilePath)
        throws
            FileNotFoundException,
            IOException
    {
        System.out.println("Compressing file " + inputFilePath + " to " + outputFilePath);
        File inputFileHandle = new File(inputFilePath);
        InputStream inputFileStream = new FileInputStream(inputFileHandle);
        SymbolTable symbolTable = symbolTableGenerator.createFromInputStream(inputFileStream).withoutConsideringEOF();
        HuffmanTranslationTable huffmanTranslationTable = huffmanTranslationTableGenerator.generateTranslationTable(symbolTable);
        System.out.println(huffmanTranslationTable);
    }

    public void decompress(String inputFilePath, String outputFilePath) {
        System.out.println("Decompressing file " + inputFilePath + " to " + outputFilePath);
    }
}
