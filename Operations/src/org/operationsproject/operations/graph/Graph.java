package org.operationsproject.operations.graph;

import org.jetbrains.annotations.NotNull;

import java.util.*;

import static org.operationsproject.operations.graph.Graph.EdgeDirection.LINKED_TO_BY;

/**
 * Class representing an acyclic directed graph.
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

    /**
     * Add a node to the graph. The node won't be connected to any other node on the graph.
     * @param node the node to add
     */
    public void addNode(Node node) {
        nodes.add(node);
    }

    /**
     * Add a set of nodes the graph.
     * @param nodes the nodes to add
     */
    public void addNodes(Node... nodes) {
        Collections.addAll(this.nodes, nodes);
    }

    /**
     * Link nodes in the graph.
     * @param node1 the source node
     * @param node2 the target node
     * @return true if and only if the link from node1 to node2 already existed in this Graph
     * @throws CycleException if adding the specified link would result in the graph containing a cycle
     * @throws UnknownNodeException if the node has not been added to the graph
     */
    public boolean linkNodes(@NotNull Node node1, @NotNull Node node2) throws CycleException, UnknownNodeException {
        validateNodesArePresentInGraph(node1, node2);

        if (addingLinkWouldCreateCycle(node1, node2)) {
            throw new CycleException();
        }

        boolean alreadyPresent = holdsLinkBetween(node1, node2);

        nodeLinks.add(new NodeLink(node1, node2));

        return alreadyPresent;
    }

    private void validateNodesArePresentInGraph(Node... nodesToCheck) throws UnknownNodeException {
        for (Node node : nodesToCheck) {
            if (!nodes.contains(node)) {
                throw new UnknownNodeException();
            }
        }
    }

    private boolean addingLinkWouldCreateCycle(@NotNull Node node1, @NotNull Node node2) throws UnknownNodeException {

        validateNodesArePresentInGraph(node1, node2);

        // Find every node which is accessible from node2. If it includes node1, then adding a link from node1 to node2
        // would create a cycle.

        Set<Node> nodesToExplore = new HashSet<>();
        nodesToExplore.add(node2);

        while (!nodesToExplore.isEmpty()) {
            Node node = nodesToExplore.iterator().next();
            nodesToExplore.remove(node);

            Set<Node> linkedNodes = getLinkedNodes(LINKED_TO_BY, node);

            if (linkedNodes.contains(node1)) {
                return true;
            }

            nodesToExplore.addAll(linkedNodes);
        }

        return false;

    }

    /**
     * @return all of the nodes contained within this Graph.
     */
    public Set<Node> getAllNodes() {
        return nodes;
    }

    /**
     * Get nodes linking to, or linked to by the given node.
     * @param direction the direction to look for nodes with respect to the given node
     * @param node the node whose neighbours are wanted
     * @return all of the nodes in the Graph which link to- or are linked to by- the given node, as requested
     */
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
