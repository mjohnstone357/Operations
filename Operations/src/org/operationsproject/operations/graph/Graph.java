/*
 * Copyright (c) 2013, Matthew Johnstone
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 	Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 	Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.operationsproject.operations.graph;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static org.operationsproject.operations.graph.Graph.EdgeDirection.LINKED_TO_BY;

/**
 * Class representing an acyclic directed graph.
 * @author Matthew Johnstone
 *         Date: 31/05/13
 *         Time: 20:39
 */
public class Graph<T> {

    public static enum EdgeDirection{LINKS_TO, LINKED_TO_BY}

    private Set<Node<T>> nodes;
    private List<NodeLink<T>> nodeLinks;

    public Graph() {
        this.nodes = new HashSet<>();
        nodeLinks = new ArrayList<>();
    }

    /**
     * Add a node to the graph. The node won't be connected to any other node on the graph.
     * @param node the node to add
     */
    public void addNode(Node<T> node) {
        // TODO Should we assert that each payload only appears in the graph once?
        nodes.add(node);
    }

    public @Nullable Node<T> getNodeWithPayload(T payload) {
        for (Node<T> node : nodes) {
            if (payload.equals(node.getPayload())) {
                return node;
            }
        }
        return null;
    }

    /**
     * Link nodes in the graph.
     * @param node1 the source node
     * @param node2 the target node
     * @return true if and only if the link from node1 to node2 already existed in this Graph
     * @throws CycleException if adding the specified link would result in the graph containing a cycle
     * @throws UnknownNodeException if the node has not been added to the graph
     */
    public boolean linkNodes(@NotNull Node<T> node1, @NotNull Node<T> node2) throws CycleException, UnknownNodeException {
        validateNodesArePresentInGraph(node1, node2);

        if (addingLinkWouldCreateCycle(node1, node2)) {
            throw new CycleException();
        }

        boolean alreadyPresent = holdsLinkBetween(node1, node2);

        nodeLinks.add(new NodeLink<>(node1, node2));

        return alreadyPresent;
    }

    public boolean linkPayloads(@NotNull T payload1, @NotNull T payload2) throws UnknownNodeException, CycleException {
        Node<T> node1 = getNodeWithPayload(payload1);
        Node<T> node2 = getNodeWithPayload(payload2);

        if (node1 == null || node2 == null) {
            throw new UnknownNodeException();
        }

        return linkNodes(node1, node2);
    }

    private void validateNodesArePresentInGraph(@NotNull Node... nodesToCheck) throws UnknownNodeException {
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

            Set<Node<T>> linkedNodes = getLinkedNodes(LINKED_TO_BY, node);

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
    public Set<Node<T>> getAllNodes() {
        return nodes;
    }

    /**
     * Get nodes linking to, or linked to by the given node.
     * @param direction the direction to look for nodes with respect to the given node
     * @param node the node whose neighbours are wanted
     * @return all of the nodes in the Graph which link to- or are linked to by- the given node, as requested
     */
    public Set<Node<T>> getLinkedNodes(EdgeDirection direction, @NotNull Node node) throws UnknownNodeException {
        validateNodesArePresentInGraph(node);
        Set<Node<T>> linkedNodes = new HashSet<>();
        for (NodeLink<T> nodeLink : nodeLinks) {
            Node<T> resultNode = nodeLink.getNodeFromDirection(node, direction);
            if (resultNode != null) {
                linkedNodes.add(resultNode);
            }
        }
        return linkedNodes;
    }

    public Set<Node<T>> getLinkedNodesByPayload(EdgeDirection direction, @NotNull T payload) throws UnknownNodeException {
        Node<T> node = getNodeWithPayload(payload);
        if (node == null) {
            throw new UnknownNodeException();
        }
        return getLinkedNodes(direction, node);
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
