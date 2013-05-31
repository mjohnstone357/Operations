package org.operationsproject.operations.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Matthew Johnstone
 *         Date: 31/05/13
 *         Time: 20:39
 */
public class Graph {

    private Set<Node> nodes;

    public Graph() {
        this.nodes = new HashSet<>();
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public Set<Node> getAllNodes() {
        return nodes;
    }

}
