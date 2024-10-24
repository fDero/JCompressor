package util;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import lombok.Getter;

@Getter
public class CommandLineArguments {

    @Option(name = "-i", aliases = "--input", required = true, usage = "Input file path")
    private String inputFilePath;

    @Option(name = "-o", aliases = "--output", required = true, usage = "Output file path")
    private String outputFilePath;

    @Option(name = "-t", aliases = "--task", required = true, usage = "Policy explaining output file should be derived from the input file")
    private ApplicationTask task;

    public void parse(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }
    }

    public void validate() {
        if (!Paths.get(inputFilePath).toFile().exists()) {
            System.err.println("Input file does not exist");
            System.exit(1);
        }
        if (!Files.isReadable(Paths.get(this.inputFilePath))) {
            System.err.println("Input file is not readable");
            System.exit(1);
        }
        if (inputFilePath.equals(outputFilePath)) {
            System.err.println("Input and output file paths are the same");
            System.exit(1);
        }
    }
}
