package com.hit.algorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class MFUAlgoCacheImpl<K, V> implements IAlgoCache<K, V> {
    int capacity;
    int size;
    int maxFreq;
    Map<K, MFUAlgoCacheImpl.Node<K, V>> keysMap;
    TreeMap<Integer, LinkedList<MFUAlgoCacheImpl.Node<K, V>>> freqList;

    public MFUAlgoCacheImpl(int capacity) {
        this.capacity = capacity;
        this.maxFreq = 0;
        this.keysMap = new HashMap<>();
        this.freqList = new TreeMap<>();
    }

    public V getElement(K key) {
        MFUAlgoCacheImpl.Node<K, V> tempNode = this.keysMap.get(key);
        if (tempNode == null) {
            return null;
        } else {
            this.incrementFreq(tempNode);
            return tempNode.value;
        }
    }

    public V putElement(K key, V value) {
        if (this.capacity == 0 && !this.freqList.containsKey(this.maxFreq)) {
            return null;
        } else {
            V removedValue = null;
            if (this.keysMap.containsKey(key)) {
                this.removeElement(key);
            }
            if (this.capacity == this.size) {
                LinkedList<MFUAlgoCacheImpl.Node<K, V>> list = this.freqList.get(this.maxFreq);
                MFUAlgoCacheImpl.Node<K, V> removed = (MFUAlgoCacheImpl.Node<K, V>)list.removeFirst();
                this.keysMap.remove(removed.key);
                removedValue = removed.value;
                --this.size;
                if (removed.freq == this.maxFreq && this.freqList.get(removed.freq).isEmpty()) {
                    this.freqList.remove(removed.freq);
                    this.maxFreq = (Integer)(this.freqList).lastKey();
                }
            }

            MFUAlgoCacheImpl.Node<K, V> newNode = new MFUAlgoCacheImpl.Node<>(key, value, 1);
            this.keysMap.put(key, newNode);
            LinkedList<MFUAlgoCacheImpl.Node<K, V>> newList = this.freqList.getOrDefault(1, new LinkedList<MFUAlgoCacheImpl.Node<K, V>>());
            newList.offer(newNode);
            this.freqList.put(1, newList);
            if (this.maxFreq == 0) {
                ++this.maxFreq;
            }

            ++this.size;
            return removedValue;
        }
    }

    public void removeElement(K key) {
        MFUAlgoCacheImpl.Node<K, V> nodeToRemove = (MFUAlgoCacheImpl.Node<K, V>)this.keysMap.get(key);
        if (nodeToRemove != null) {
            int nodeFreq = nodeToRemove.freq;
            LinkedList<MFUAlgoCacheImpl.Node<K, V>> list = this.freqList.get(nodeFreq);
            list.remove(nodeToRemove);
            if (list.isEmpty()) {
                this.freqList.remove(nodeFreq);
                if (nodeFreq == this.maxFreq) {
                    this.maxFreq = (Integer)(this.freqList).lastKey();
                }
            }

            --this.size;
        }
    }

    private void incrementFreq(MFUAlgoCacheImpl.Node<K, V> node) {
        int nodeFreq = node.freq;
        LinkedList<MFUAlgoCacheImpl.Node<K, V>> list = this.freqList.get(nodeFreq);
        list.remove(node);
        if (list.isEmpty()) {
            this.freqList.remove(nodeFreq);
        }

        if (nodeFreq == this.maxFreq) {
            ++this.maxFreq;
        }

        ++nodeFreq;
        node.freq = nodeFreq;
        LinkedList<MFUAlgoCacheImpl.Node<K, V>> newList = this.freqList.getOrDefault(nodeFreq, new LinkedList<MFUAlgoCacheImpl.Node<K, V>>());
        newList.offer(node);
        this.freqList.put(nodeFreq, newList);
    }

    private static class Node<K, V> {
        K key;
        V value;
        int freq;

        public Node(K key, V value, int freq) {
            this.key = key;
            this.value = value;
            this.freq = freq;
        }
    }
}
