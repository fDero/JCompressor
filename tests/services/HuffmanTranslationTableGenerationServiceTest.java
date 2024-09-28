package services;

import org.junit.jupiter.api.Test;

import model.BitSequence;
import model.SymbolTable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanTranslationTableGenerationServiceTest {
    
    private final HuffmanTranslationTableGenerationService huffmanTranslationTableGenerationService 
        = new HuffmanTranslationTableGenerationService();

    private int characterA = (int) 'a';
    private int characterB = (int) 'b';
    private int characterC = (int) 'c';
    private int characterD = (int) 'd';

    private final SymbolTable exampleSymbolTable = new SymbolTable(Map.of(
        characterA, 3L, 
        characterB, 2L, 
        characterC, 1L, 
        characterD, 1L
    ));

    @Test
    void generatePriorityQueueTest() {
        PriorityQueue<HuffmanTranslationTableGenerationService.HuffmanTreeNode> priorityQueue = 
            huffmanTranslationTableGenerationService.generatePriorityQueue(exampleSymbolTable);
        assertEquals(4, priorityQueue.size());
        assertEquals(1, priorityQueue.poll().getOccurrency());
        assertEquals(1, priorityQueue.poll().getOccurrency());
        assertEquals(2, priorityQueue.poll().getOccurrency());
        assertEquals(3, priorityQueue.poll().getOccurrency());
    }

    @Test
    void generateHuffmanTreeTest() {
        HuffmanTranslationTableGenerationService.HuffmanTreeNode root = 
            huffmanTranslationTableGenerationService.generateHuffmanTree(exampleSymbolTable);
        assertEquals(7, root.getOccurrency());
    }

    @Test
    void fillHuffmanTranslationTableFromHuffmanTreeTest() {
        HuffmanTranslationTableGenerationService.HuffmanTreeNode root = 
            huffmanTranslationTableGenerationService.generateHuffmanTree(exampleSymbolTable);
        BitSequence emptyPrefixUsedForRecursionInit = new BitSequence();
        Map<Byte, BitSequence> translationMapToBeFilled = new HashMap<>();
        huffmanTranslationTableGenerationService.fillHuffmanTranslationTableFromHuffmanTree(
            translationMapToBeFilled, root, emptyPrefixUsedForRecursionInit
        );
        assertEquals(4, translationMapToBeFilled.size());
        long characterAencodeLength = translationMapToBeFilled.get((byte) characterA).toString().length();
        long characterBencodeLength = translationMapToBeFilled.get((byte) characterB).toString().length();
        assertTrue(characterAencodeLength < characterBencodeLength);
    }
}
