package com.hit.algorithm;

import java.util.HashMap;
import java.util.Map;

public class LRUAlgoCacheImpl<K, V> implements IAlgoCache<K, V> {
    Map<K, Node<K, V>> elements = new HashMap<>();
    LRUAlgoCacheImpl.Node<K, V> lru = new LRUAlgoCacheImpl.Node<>(null, null, null, null);
    LRUAlgoCacheImpl.Node<K, V> mru = new LRUAlgoCacheImpl.Node<>(null, null, null, null);
    int capacity;
    int size;

    public LRUAlgoCacheImpl(int capacity) {
        this.capacity = capacity;
        this.size = 0;
    }

    public V getElement(K key) {
        LRUAlgoCacheImpl.Node<K, V> tempNode = this.elements.get(key);
        if (tempNode == null) {
            return null;
        } else if (tempNode.key == this.mru.key) {
            return this.mru.value;
        } else {
            LRUAlgoCacheImpl.Node<K, V> nextNode = tempNode.next;
            LRUAlgoCacheImpl.Node<K, V> prevNode = tempNode.prev;
            if (tempNode.key == this.lru.key) {
                nextNode.prev = null;
                this.lru = nextNode;
            } else {
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
            }

            tempNode.prev = this.mru;
            this.mru.next = tempNode;
            this.mru = tempNode;
            this.mru.next = null;
            return tempNode.value;
        }
    }

    public V putElement(K key, V value) {
        V deletedElement = null;
        if (this.elements.containsKey(key)) {
            this.removeElement(key);
        }
        LRUAlgoCacheImpl.Node<K, V> newNode = new LRUAlgoCacheImpl.Node(this.mru, (LRUAlgoCacheImpl.Node)null, key, value);
        if (this.size == this.capacity) {
            this.elements.remove(this.lru.key);
            deletedElement = this.lru.value;
            this.lru = this.lru.next;
            this.lru.prev = null;
            --this.size;
        }

        this.mru.next = newNode;
        this.elements.put(key, newNode);
        this.mru = newNode;
        if (this.size == 0) {
            this.lru = newNode;
        }

        ++this.size;
        return deletedElement;
    }

    public void removeElement(K key) {
        LRUAlgoCacheImpl.Node<K, V> tempNode = (LRUAlgoCacheImpl.Node)this.elements.remove(key);
        if (tempNode != null) {
            LRUAlgoCacheImpl.Node<K, V> nextNode = tempNode.next;
            LRUAlgoCacheImpl.Node<K, V> prevNode = tempNode.prev;
            if (tempNode.key == this.lru.key) {
                nextNode.prev = null;
                this.lru = nextNode;
            }

            if (tempNode.key == this.mru.key) {
                prevNode.next = null;
                this.mru = prevNode;
            } else {
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
            }

            --this.size;
        }
    }

    private static class Node<K, V> {
        K key;
        V value;
        LRUAlgoCacheImpl.Node<K, V> prev;
        LRUAlgoCacheImpl.Node<K, V> next;

        public Node(LRUAlgoCacheImpl.Node<K, V> prev, LRUAlgoCacheImpl.Node<K, V> next, K key, V value) {
            this.prev = prev;
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }
}
