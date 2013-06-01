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

    /**
     * Link nodes in the graph
     * @param node1 the source node
     * @param node2 the target node
     * @return true if and only if the link from node1 to node2 already existed in this Graph
     * @throws CycleException if adding the specified link would result in the graph containing a cycle
     * @throws UnknownNodeException if the node has not been added to the graph
     */
    public boolean linkNodes(@NotNull Node node1, @NotNull Node node2) throws CycleException, UnknownNodeException {
        if (!nodes.contains(node1) || !nodes.contains(node2)) {
            throw new UnknownNodeException();
        }

        if (holdsLinkBetween(node2, node1)) {
            // Primitive two-node cycle detection
            throw new CycleException();
        }

        boolean alreadyPresent = holdsLinkBetween(node1, node2);

        nodeLinks.add(new NodeLink(node1, node2));

        return alreadyPresent;
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
