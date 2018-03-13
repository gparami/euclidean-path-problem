package com.company;

import java.util.ArrayList;
import java.util.List;

public class Node<E> {
    private E data;
    private List<Node<E>> children;
    private Node<E> parent;

    public Node(E data) {
        this.data = data;
        this.children = new ArrayList<Node<E>>();
    }

    public Node(Node<E> node) {
        this.data = node.getData();
        children = new ArrayList<Node<E>>();
    }

    public void addChild(Node<E> child) {
        child.setParent(this);
        children.add(child);
    }

    public E getData() { return this.data; }
    public Node<E> getParent() { return this.parent; }
    public List<Node<E>> getChildren() { return this.children; }

    public void setData(E data) { this.data = data; }
    public void setParent(Node<E> parent) { this.parent = parent; }

    @Override
    public String toString() {
        return this.data.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj)
            return false;

        if (obj instanceof Node) {
            if (((Node<?>) obj).getData().equals(this.data))
                return true;
        }

        return false;
    }

}