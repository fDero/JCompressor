package util;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import controller.HuffmanEncodingController;

import java.util.Arrays;

@Configuration
@ComponentScan(basePackages = {"controller", "service"})
public class ApplicationStartup {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationStartup.class)) {
            HuffmanEncodingController huffmanEncodingController = context.getBean(HuffmanEncodingController.class);
            CommandLineArguments commandLineArguments = new CommandLineArguments();
            commandLineArguments.parse(args);
            commandLineArguments.validate();
            logger.info("command line arguments parsed and validated");
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
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.exit(1);
        }
    }
}
