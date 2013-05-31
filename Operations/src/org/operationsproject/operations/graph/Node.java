package org.operationsproject.operations.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Matthew Johnstone
 *         Date: 31/05/13
 *         Time: 20:34
 */
public class Node<T> { 

    final T Payload; // Type-safety is cool. 
    private Set<Node<T>> linkedNodes;


    public Node<T>(T payload) {
        linkedNodes = new HashSet<>();
        Payload = payload;
        // Why is the payload pointer immutable? Because we (probably) don't want people moving it around
        // once it's installed in the graph. 
    }

    public T getPayload() {
        return Payload;
    }

    // And thus we guarentee no cycles are possible, because we never add in pre-existing nodes to the graph.
    public static Node<T> attachTo(Node[] parents, T object)
    {
        Node<T> newNode = new Node<T>(object);
        for(Node<T> N:parents)
        {
            N.linkedNodes.add(newNode);
        }
        return newNode;
    }

    public void unlinkFrom(Node<T> target)
    {
        linkedNodes.remove(target);
        
    }

    public Set<Node> getLinkedNodes() {
        return (Set<Node>) linkedNodes.clone(); // So that consumers can't mess with our members when we're not looking.
    }
}
