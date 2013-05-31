package org.operationsproject.operations.graph;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Matthew Johnstone
 *         Date: 31/05/13
 *         Time: 20:39
 */
public class Graph {

    public static enum EdgeDirection{LINKS_TO, LINKED_TO_BY}

    private Set<Node> nodes;
    private List<NodeLink> nodeLinks;

    public Graph() {
        this.nodes = new HashSet<>();
        nodeLinks = new ArrayList<>();
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void addNodes(Node... nodes) {
        Collections.addAll(this.nodes, nodes);
    }

    public void linkNodes(@NotNull Node node1, @NotNull Node node2) throws CycleException {
        assert nodes.contains(node1);
        assert nodes.contains(node2);
        nodeLinks.add(new NodeLink(node1, node2));
        if (holdsLinkBetween(node2, node1)) {
            // Primitive two-node cycle detection
            throw new CycleException();
        }
    }

    public Set<Node> getAllNodes() {
        return nodes;
    }

    public Set<Node> getLinkedNodes(EdgeDirection direction, Node node) {
        Set<Node> linkedNodes = new HashSet<>();
        for (NodeLink nodeLink : nodeLinks) {
            Node resultNode = nodeLink.getNodeFromDirection(node, direction);
            if (resultNode != null) {
                linkedNodes.add(resultNode);
            }
        }
        return linkedNodes;
    }

    private boolean holdsLinkBetween(Node node1, Node node2) {
        for (NodeLink nodeLink : nodeLinks) {
            if (nodeLink.sourceNode == node1 && nodeLink.targetNode == node2) {
                return true;
            }
        }
        return false;
    }
}
