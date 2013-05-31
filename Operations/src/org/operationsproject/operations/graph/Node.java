package org.operationsproject.operations.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Matthew Johnstone
 *         Date: 31/05/13
 *         Time: 20:34
 */
public class Node {

    private Set<Node> linkedNodes;

    public Node() {
        linkedNodes = new HashSet<>();
    }

    public void linkTo(Node otherNode) {
        if (!linkedNodes.contains(otherNode)) {
            linkedNodes.add(otherNode);
            otherNode.linkTo(this);
        }
    }

    public Set<Node> getLinkedNodes() {
        return linkedNodes;
    }
}
