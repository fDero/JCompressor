package service;

import org.springframework.stereotype.Service;

import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.lang.Comparable;

import lombok.AllArgsConstructor;
import lombok.Getter;

import model.BitSequence;
import model.HuffmanTranslationTable;
import model.SymbolTable;

@Service
public final class HuffmanTranslationTableGenerationService {

    static sealed interface HuffmanTreeNode
        extends Comparable<HuffmanTreeNode>
        permits LeafHuffmanTreeNode, BranchHuffmanTreeNode
    {
        long getOccurrency();

        @Override
        default int compareTo(HuffmanTreeNode other) {
            return Long.compare(this.getOccurrency(), other.getOccurrency());
        }
    };

    @Getter
    @AllArgsConstructor
    private static final class LeafHuffmanTreeNode implements HuffmanTreeNode {

        private final long occurrency;
        private final byte symbol;
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
            assert integerEncodedByte != null;
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
            long occurrency = left.getOccurrency() + right.getOccurrency();
            priorityQueue.add(new BranchHuffmanTreeNode(occurrency, left, right));
        }
        return priorityQueue.poll();
    }

    void fillHuffmanTranslationTableFromHuffmanTree(Map<Byte, BitSequence> translationMap, HuffmanTreeNode huffmanTree, BitSequence prefix) {
        if (huffmanTree instanceof LeafHuffmanTreeNode) {
            LeafHuffmanTreeNode leaf = (LeafHuffmanTreeNode) huffmanTree;
            translationMap.put(leaf.getSymbol(), prefix);
        } else if (huffmanTree instanceof BranchHuffmanTreeNode) {
            BranchHuffmanTreeNode branch = (BranchHuffmanTreeNode) huffmanTree;
            BitSequence leftPrefix = prefix.generateCopy().pushZero();
            BitSequence rightPrefix = prefix.pushOne();
            fillHuffmanTranslationTableFromHuffmanTree(translationMap, branch.getLeft(), leftPrefix);
            fillHuffmanTranslationTableFromHuffmanTree(translationMap, branch.getRight(), rightPrefix);
        }
    }

    public HuffmanTranslationTable generateTranslationTable(SymbolTable symbolTable) {
        HuffmanTreeNode huffmanTree = generateHuffmanTree(symbolTable);
        Map<Byte, BitSequence> translationMap = new HashMap<>();
        fillHuffmanTranslationTableFromHuffmanTree(translationMap, huffmanTree, new BitSequence());
        return new HuffmanTranslationTable(translationMap);
    }
}
