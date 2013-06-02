package org.operationsproject.operations.graph;

/**
 * Class representing a node on a Graph.
 * @author Matthew Johnstone
 *         Date: 31/05/13
 *         Time: 20:34
 */
public class Node<T> {

    private T payload;

    public Node(T payload) {
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }
}
