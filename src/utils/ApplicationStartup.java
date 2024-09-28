package utils;



import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import controllers.HuffmanEncodingController;

@Configuration
@ComponentScan(basePackages = {"controllers", "services"})
public class ApplicationStartup {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationStartup.class);
        HuffmanEncodingController huffmanEncodingController = context.getBean(HuffmanEncodingController.class);
        CommandLineArguments commandLineArguments = new CommandLineArguments();
        commandLineArguments.parse(args);
        commandLineArguments.validate();
        switch (commandLineArguments.getTask()) {
            case ApplicationTask.HUFFMAN_COMPRESSION:
                huffmanEncodingController.compress(
                    commandLineArguments.getInputFilePath(), 
                    commandLineArguments.getOutputFilePath()
                );
                break;
            case ApplicationTask.HUFFMAN_DECOMPRESSION:
                huffmanEncodingController.decompress(
                    commandLineArguments.getInputFilePath(), 
                    commandLineArguments.getOutputFilePath()
                );
                break;
        }
        context.close();
    }
}