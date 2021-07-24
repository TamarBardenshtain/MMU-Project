package com.hit.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RandomReplacementAlgoCacheImpl<K, V> implements IAlgoCache<K, V> {
    private static final Random rand = new Random();
    int capacity;
    int size;
    List<RandomReplacementAlgoCacheImpl.Node<K, V>> elements;
    HashMap<K, Integer> elementsMap;

    public RandomReplacementAlgoCacheImpl(int capacity) {
        this.elements = new ArrayList<>(capacity);
        this.elementsMap = new HashMap<>();
        this.capacity = capacity;
        this.size = 0;
    }

    public V getElement(K key) {
        Integer index = (Integer)this.elementsMap.get(key);
        if (index == null) {
            return null;
        } else {
            RandomReplacementAlgoCacheImpl.Node<K, V> tempNode = (RandomReplacementAlgoCacheImpl.Node<K, V>)this.elements.get(index);
            return tempNode.value;
        }
    }

    public V putElement(K key, V value) {
        V deletedElement = null;
        if (this.elementsMap.containsKey(key)) {
            this.removeElement(key);
        }
        if (this.size == this.capacity) {
            int index = rand.nextInt(this.size);
            RandomReplacementAlgoCacheImpl.Node<K, V> lastNode = (RandomReplacementAlgoCacheImpl.Node<K, V>)this.elements.remove(this.size - 1);
            RandomReplacementAlgoCacheImpl.Node<K, V> deletedNode = (RandomReplacementAlgoCacheImpl.Node<K, V>)this.elements.set(index, lastNode);
            deletedElement = deletedNode.value;
            this.elementsMap.remove(deletedNode.key);
            this.elementsMap.replace(lastNode.key, index);
            --this.size;
        }

        RandomReplacementAlgoCacheImpl.Node<K, V> newNode = new RandomReplacementAlgoCacheImpl.Node<K, V>(key, value);
        this.elements.add(newNode);
        this.elementsMap.put(key, this.size);
        ++this.size;
        return deletedElement;
    }

    public void removeElement(K key) {
        Integer index = (Integer)this.elementsMap.remove(key);
        if (index != null) {
            RandomReplacementAlgoCacheImpl.Node<K, V> lastNode = (RandomReplacementAlgoCacheImpl.Node<K, V>)this.elements.remove(this.size - 1);
            this.elements.set(index, lastNode);
            this.elementsMap.replace(lastNode.key, index);
            --this.size;
        }
    }

    private static class Node<K, V> {
        K key;
        V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
