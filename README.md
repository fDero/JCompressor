# JCompressor

Simple file compressor and decompressor written in Java-21. </br>
This project makes use of my own library [bits4j](https://github.com/fDero/Bits4j).

### Building from source
To build the project locally, [Apache Maven](https://maven.apache.org/) and 
[Java-21](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/downloads-list.html) 
neeed to be installed on the host machine. To build the project, run the following command:
```bash
mvn package
```

### Performing Huffman compression
Assuming the build was successful, running the following command you will be able to compress the `<INPUT-FILE>`, generating
an `<OUTPUT-COMPRESSED-FILE>` using the [Huffman algorithm](https://en.wikipedia.org/wiki/Huffman_coding)
```
java -jar ./target/JCompressor-0.0.1-SNAPSHOT.jar --input <INPUT-FILE> --task HUFFMAN_COMPRESSION --output <OUTPUT-COMPRESSED-FILE>
```

### Performing Huffman decompression
Assuming the build was successful, running the following command you will be able to decompress an `<INPUT-COMPRESSED-FILE>`, generating
an `<OUTPUT-FILE>` using the [Huffman algorithm](https://en.wikipedia.org/wiki/Huffman_coding)
```
java -jar ./target/JCompressor-0.0.1-SNAPSHOT.jar --input <INPUT-COMPRESSED-FILE> --task HUFFMAN_DECOMPRESSION --output <OUTPUT-FILE>
```