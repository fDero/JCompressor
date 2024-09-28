package util;



import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import controller.HuffmanEncodingController;

@Configuration
@ComponentScan(basePackages = {"controller", "service"})
public class ApplicationStartup {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationStartup.class)) {
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
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            System.exit(1);
        }
    }
}
