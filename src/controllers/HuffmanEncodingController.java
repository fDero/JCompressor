package controllers;

import org.springframework.stereotype.Controller;

@Controller
public class HuffmanEncodingController {
    
    public void compress(String inputFilePath, String outputFilePath) {
        System.out.println("Compressing file " + inputFilePath + " to " + outputFilePath);
    }

    public void decompress(String inputFilePath, String outputFilePath) {
        System.out.println("Decompressing file " + inputFilePath + " to " + outputFilePath);
    }
}
