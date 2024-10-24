package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.lang.Comparable;

import lombok.AllArgsConstructor;
import lombok.Getter;

import io.github.fdero.bits4j.core.BitList;
import model.HuffmanTranslationTable;
import model.SymbolTable;

@Service
public final class HuffmanTranslationTableGenerationService {

    private static final Logger logger = LoggerFactory.getLogger(HuffmanTranslationTableGenerationService.class);

    sealed interface HuffmanTreeNode
        extends Comparable<HuffmanTreeNode>
        permits LeafHuffmanTreeNode, BranchHuffmanTreeNode
    {
        long getOccurrency();

        @Override
        default int compareTo(HuffmanTreeNode other) {
            return Long.compare(this.getOccurrency(), other.getOccurrency());
        }
    }

    @Getter
    @AllArgsConstructor
    private static final class LeafHuffmanTreeNode implements HuffmanTreeNode {

        private final long occurrency;
        private final int symbol;
    }

    @Getter
    @AllArgsConstructor
    private static final class BranchHuffmanTreeNode implements HuffmanTreeNode {

        private final long occurrency;
        private final HuffmanTreeNode left;
        private final HuffmanTreeNode right;
    }

    PriorityQueue<HuffmanTreeNode> generatePriorityQueue(SymbolTable symbolTable) {
        PriorityQueue<HuffmanTreeNode> priorityQueue = new PriorityQueue<>();
        symbolTable.streamFromLeastFrequent().forEach(integerEncodedByte -> {
            long occurrency = symbolTable.getOccurrency(integerEncodedByte);
            assert integerEncodedByte >= 0;
            assert integerEncodedByte <= 255;
            byte byteValue = (byte) integerEncodedByte.intValue();
            priorityQueue.add(new LeafHuffmanTreeNode(occurrency, byteValue));
        });
        return priorityQueue;
    }

    HuffmanTreeNode generateHuffmanTree(SymbolTable symbolTable) {
        PriorityQueue<HuffmanTreeNode> priorityQueue = generatePriorityQueue(symbolTable);
        while (priorityQueue.size() > 1) {
            HuffmanTreeNode left = priorityQueue.poll();
            HuffmanTreeNode right = priorityQueue.poll();
            assert right != null;
            long occurrency = left.getOccurrency() + right.getOccurrency();
            priorityQueue.add(new BranchHuffmanTreeNode(occurrency, left, right));
        }
        return priorityQueue.poll();
    }

    void fillHuffmanTranslationTableFromHuffmanTree(Map<Integer, BitList> translationMap, HuffmanTreeNode huffmanTree, BitList prefix) {
        if (huffmanTree instanceof LeafHuffmanTreeNode leaf) {
            translationMap.put(leaf.getSymbol(), prefix);
        } else if (huffmanTree instanceof BranchHuffmanTreeNode branch) {
            BitList leftPrefix = new BitList();
            leftPrefix.addAll(prefix);
            leftPrefix.addZero();
            prefix.addOne();
            fillHuffmanTranslationTableFromHuffmanTree(translationMap, branch.getLeft(), leftPrefix);
            fillHuffmanTranslationTableFromHuffmanTree(translationMap, branch.getRight(), prefix);
        }
    }

    public HuffmanTranslationTable generateTranslationTable(SymbolTable symbolTable) {
        logger.info("generating the Huffman-translation-table...");
        HuffmanTreeNode huffmanTree = generateHuffmanTree(symbolTable);
        Map<Integer, BitList> translationMap = new HashMap<>();
        fillHuffmanTranslationTableFromHuffmanTree(translationMap, huffmanTree, new BitList());
        return new HuffmanTranslationTable(translationMap);
    }
}
