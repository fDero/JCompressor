package service;

import io.github.fdero.bits4j.core.BitList;
import org.junit.jupiter.api.Test;

import model.SymbolTable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanTranslationTableGenerationServiceTest {

    private final HuffmanTranslationTableGenerationService huffmanTranslationTableGenerationService
            = new HuffmanTranslationTableGenerationService();

    private final int characterA = 'a';
    private final int characterB = 'b';
    private final int characterC = 'c';
    private final int characterD = 'd';

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
        BitList emptyPrefixUsedForRecursionInit = new BitList();
        Map<Integer, BitList> translationMapToBeFilled = new HashMap<>();
        huffmanTranslationTableGenerationService.fillHuffmanTranslationTableFromHuffmanTree(
                translationMapToBeFilled, root, emptyPrefixUsedForRecursionInit
        );
        assertEquals(4, translationMapToBeFilled.size());
        long characterAencodeLength = translationMapToBeFilled.get(characterA).toString().length();
        long characterBencodeLength = translationMapToBeFilled.get(characterB).toString().length();
        assertTrue(characterAencodeLength < characterBencodeLength);
    }
}