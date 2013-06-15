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

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.operationsproject.operations.graph.Graph.EdgeDirection.LINKED_TO_BY;
import static org.operationsproject.operations.graph.Graph.EdgeDirection.LINKS_TO;

/**
 * @author Matthew Johnstone
 *         Date: 31/05/13
 *         Time: 20:36
 */
public class GraphTest {

    private Graph<Object> graph;

    @Before
    public void setUp() {
        graph = new Graph<>();
    }

    /**
     * Add a single node, and check that it is returned.
     */
    @Test
    public void should_return_sole_added_node() {

        Node<Object> node = createNode();
        graph.addNode(node);

        Set<Node<Object>> allNodes = graph.getAllNodes();

        assertEquals(1, allNodes.size());
        assertEquals(node, allNodes.iterator().next());
    }

    /**
     * Add two nodes, unconnected, and check that they are both returned.
     */
    @Test
    public void should_return_both_added_nodes() {

        Node<Object> node1 = createNode();
        Node<Object> node2 = createNode();

        graph.addNode(node1);
        graph.addNode(node2);
        Set<Node<Object>> allNodes = graph.getAllNodes();

        assertEquals(2, allNodes.size());
        assertTrue(allNodes.contains(node1));
        assertTrue(allNodes.contains(node2));
    }

    /**
     * Add two nodes, connect them, and check that the first refers to the second, but not the other way round
     */
    @Test
    public void should_be_able_to_navigate_between_nodes() throws CycleException, UnknownNodeException {

        Node<Object> node1 = createNode();
        Node<Object> node2 = createNode();

        graph.addNode(node1);
        graph.addNode(node2);
        graph.linkNodes(node1, node2);

        Set<Node<Object>> nodesLinkingToNode1 = graph.getLinkedNodes(LINKS_TO, node1);
        assertEquals(0, nodesLinkingToNode1.size());

        Set<Node<Object>> nodesLinkedToByNode1 = graph.getLinkedNodes(LINKED_TO_BY, node1);
        assertEquals(1, nodesLinkedToByNode1.size());
        assertEquals(node2, nodesLinkedToByNode1.iterator().next());

        Set<Node<Object>> nodesLinkingToNode2 = graph.getLinkedNodes(LINKS_TO, node2);
        assertEquals(1, nodesLinkingToNode2.size());
        assertEquals(node1, nodesLinkingToNode2.iterator().next());

        Set<Node<Object>> nodesLinkedToByNode2 = graph.getLinkedNodes(LINKED_TO_BY, node2);
        assertEquals(0, nodesLinkedToByNode2.size());
    }

    @Test
    public void should_be_able_to_navigate_between_nodes_via_payloads() throws CycleException, UnknownNodeException {

        Node<Object> node1 = new Node<Object>("Test");
        Node<Object> node2 = new Node<Object>("Test2");

        graph.addNode(node1);
        graph.addNode(node2);
        graph.linkNodes(node1, node2);

        Set<Node<Object>> nodesLinkingToNode1 = graph.getLinkedNodesByPayload(LINKS_TO, "Test");
        assertEquals(0, nodesLinkingToNode1.size());

        Set<Node<Object>> nodesLinkedToByNode1 = graph.getLinkedNodesByPayload(LINKED_TO_BY, "Test");
        assertEquals(1, nodesLinkedToByNode1.size());
        assertEquals(node2, nodesLinkedToByNode1.iterator().next());

        Set<Node<Object>> nodesLinkingToNode2 = graph.getLinkedNodesByPayload(LINKS_TO, "Test2");
        assertEquals(1, nodesLinkingToNode2.size());
        assertEquals(node1, nodesLinkingToNode2.iterator().next());

        Set<Node<Object>> nodesLinkedToByNode2 = graph.getLinkedNodesByPayload(LINKED_TO_BY, "Test2");
        assertEquals(0, nodesLinkedToByNode2.size());
    }

    @Test(expected = UnknownNodeException.class)
    public void should_throw_exception_if_asked_to_get_linked_nodes_for_unknown_payload() throws UnknownNodeException {

        Node<Object> node1 = new Node<Object>("Test");
        Node<Object> node2 = new Node<Object>("Test2");

        graph.addNode(node1);
        graph.addNode(node2);

        graph.getLinkedNodesByPayload(LINKS_TO, "blarg");
    }

    @Test(expected = UnknownNodeException.class)
    public void should_object_if_asked_to_find_linked_nodes_for_a_node_not_present_in_the_graph() throws UnknownNodeException {
        Node<Object> node1 = createNode();
        graph.getLinkedNodes(LINKS_TO, node1);
    }

    /**
     * Add two nodes to a graph, and attempt to link them together, forming a cycle
     */
    @Test(expected = CycleException.class)
    public void should_prevent_cycle_between_two_nodes_from_being_allowed() throws CycleException, UnknownNodeException {
        Node<Object> node1 = createNode();
        Node<Object> node2 = createNode();

        graph.addNode(node1);
        graph.addNode(node2);

        graph.linkNodes(node1, node2);
        graph.linkNodes(node2, node1);
    }

    /**
     * Try to link two nodes having added neither of them to the graph
     */
    @Test(expected = UnknownNodeException.class)
    public void should_only_link_node_if_it_exists_in_the_graph_0() throws CycleException, UnknownNodeException {
        Node<Object> node1 = createNode();
        Node<Object> node2 = createNode();

        graph.linkNodes(node1, node2);
    }

    /**
     * Try to link two nodes having only added the first to the graph
     */
    @Test(expected = UnknownNodeException.class)
    public void should_only_link_node_if_it_exists_in_the_graph_1a() throws CycleException, UnknownNodeException {
        Node<Object> node1 = createNode();
        Node<Object> node2 = createNode();

        graph.addNode(node1);

        graph.linkNodes(node1, node2);
    }

    /**
     * Try to link two nodes having only added the second to the graph
     */
    @Test(expected = UnknownNodeException.class)
    public void should_only_link_node_if_it_exists_in_the_graph_1b() throws CycleException, UnknownNodeException {
        Node<Object> node1 = createNode();
        Node<Object> node2 = createNode();

        graph.addNode(node2);

        graph.linkNodes(node1, node2);
    }

    /**
     * Add three nodes A, B and C to a graph, and attempt to link A to B. B to C and C to A, forming a cycle
     */
    @Test(expected = CycleException.class)
    public void should_prevent_cycle_between_three_nodes_from_being_allowed() throws CycleException, UnknownNodeException {
        Node<Object> node1 = createNode();
        Node<Object> node2 = createNode();
        Node<Object> node3 = createNode();

        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);

        graph.linkNodes(node1, node2);
        graph.linkNodes(node2, node3);
        graph.linkNodes(node3, node1);
    }

    /**
     * Add four nodes A, B, C and D to a graph, and attempt to link A to B. B to C, C to D and D to A, forming a cycle
     */
    @Test(expected = CycleException.class)
    public void should_prevent_cycle_between_four_nodes_from_being_allowed() throws CycleException, UnknownNodeException {
        Node<Object> node1 = createNode();
        Node<Object> node2 = createNode();
        Node<Object> node3 = createNode();
        Node<Object> node4 = createNode();

        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);
        graph.addNode(node4);

        graph.linkNodes(node1, node2);
        graph.linkNodes(node2, node3);
        graph.linkNodes(node3, node4);
        graph.linkNodes(node4, node1);
    }

    /**
     * A convoluted graph which almost has a cycle
     */
    @Test
    public void should_allow_convoluted_graph_to_be_constructed() throws CycleException, UnknownNodeException {
        Node<Object> a = createNode();
        Node<Object> b = createNode();
        Node<Object> c = createNode();
        Node<Object> d = createNode();
        Node<Object> e = createNode();

        Node<Object> x = createNode();

        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addNode(d);
        graph.addNode(e);
        graph.addNode(x);

        graph.linkNodes(a, b);
        graph.linkNodes(a, c);
        graph.linkNodes(b, d);
        graph.linkNodes(c, d);
        graph.linkNodes(c, e);
        graph.linkNodes(d, e);

        graph.linkNodes(e, x);
    }

    /**
     * A convoluted graph which does have a cycle
     */
    @Test(expected = CycleException.class)
    public void should_not_allow_convoluted_graph_with_a_cycle_to_be_constructed() throws CycleException, UnknownNodeException {
        Node<Object> a = createNode();
        Node<Object> b = createNode();
        Node<Object> c = createNode();
        Node<Object> d = createNode();
        Node<Object> e = createNode();

        Node<Object> x = createNode();

        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addNode(d);
        graph.addNode(e);
        graph.addNode(x);

        graph.linkNodes(a, b);
        graph.linkNodes(a, c);
        graph.linkNodes(b, d);
        graph.linkNodes(c, d);
        graph.linkNodes(c, e);
        graph.linkNodes(d, e);

        graph.linkNodes(e, x);

        graph.linkNodes(x, b);
    }

    /**
     * Add three nodes with string payloads, and check that they are all retrieved correctly from the graph. Also check
     * that querying for a payload which isn't on any node in the graph returns null.
     */
    @Test
    public void should_get_nodes_with_payload_and_without() {

        Node<Object> node1 = new Node<Object>("One");
        Node<Object> node2 = new Node<Object>("Two");
        Node<Object> node3 = new Node<Object>("Three");

        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);

        assertEquals(node1, graph.getNodeWithPayload("One"));
        assertEquals(node2, graph.getNodeWithPayload("Two"));
        assertEquals(node3, graph.getNodeWithPayload("Three"));

        assertNull(graph.getNodeWithPayload("Four"));
    }

    /**
     * Add two nodes to a graph, and link them together twice, asserting that this is indicated by linkNodes and that
     * the link does not count twice.
     */
    @Test
    public void should_indicate_that_link_already_existed() throws CycleException, UnknownNodeException {
        Node<Object> node1 = createNode();
        Node<Object> node2 = createNode();

        graph.addNode(node1);
        graph.addNode(node2);

        assertFalse(graph.linkNodes(node1, node2));
        assertTrue(graph.linkNodes(node1, node2));

        Set<Node<Object>> linkedNodes = graph.getLinkedNodes(LINKED_TO_BY, node1);
        assertEquals(1, linkedNodes.size());
    }

    @Test
    public void should_return_nodes_in_bfs_ordering_1() throws UnknownNodeException, CycleException {

        Node<Object> root = createNode();

        graph.addNode(root);

        List<Node<Object>> nodes = graph.getNodesInBreadthFirstOrdering(root);

        Node[] expected = {root};

        assertEquals(expected.length, nodes.size());
        assertEquals(expected[0], root);
    }

    @Test
    public void should_return_nodes_in_bfs_ordering_2() throws UnknownNodeException, CycleException {

        Node<Object> root = createNode();
        Node<Object> child = createNode();

        graph.addNode(root);
        graph.addNode(child);

        graph.linkNodes(child, root);

        List<Node<Object>> nodesInBreadthFirstOrdering = graph.getNodesInBreadthFirstOrdering(root);

        Node[] expected = {root, child};

        assertEquals(expected.length, nodesInBreadthFirstOrdering.size());

        assertEquals(expected[0], root);
        assertEquals(expected[1], child);
    }

    @Test
    public void should_return_nodes_in_bfs_ordering_3() throws UnknownNodeException, CycleException {

        Node<Object> root = createNode();
        Node<Object> leftChild = createNode();
        Node<Object> rightChild = createNode();

        graph.addNode(root);
        graph.addNode(leftChild);
        graph.addNode(rightChild);

        graph.linkNodes(leftChild, root);
        graph.linkNodes(rightChild, root);

        List<Node<Object>> nodes = graph.getNodesInBreadthFirstOrdering(root);

        Node[] expected = {root, leftChild, rightChild};

        assertEquals(expected.length, nodes.size());

        assertTrue(nodes.indexOf(root) < nodes.indexOf(leftChild));
        assertTrue(nodes.indexOf(root) < nodes.indexOf(rightChild));

    }

    @Test
    public void should_return_nodes_in_bfs_ordering_4() throws UnknownNodeException, CycleException {

        Node<Object> root = createNode();
        Node<Object> left = createNode();
        Node<Object> right = createNode();
        Node<Object> leftLeft = createNode();
        Node<Object> leftRight = createNode();
        Node<Object> rightLeft = createNode();
        Node<Object> rightRight = createNode();

        graph.addNode(root);
        graph.addNode(left);
        graph.addNode(right);
        graph.addNode(leftLeft);
        graph.addNode(leftRight);
        graph.addNode(rightLeft);
        graph.addNode(rightRight);

        graph.linkNodes(left, root);
        graph.linkNodes(right, root);

        graph.linkNodes(leftLeft, left);
        graph.linkNodes(leftRight, left);

        graph.linkNodes(rightLeft, right);
        graph.linkNodes(rightRight, right);

        List<Node<Object>> nodes = graph.getNodesInBreadthFirstOrdering(root);

        Node[] expected = {root, left, right, leftLeft, leftRight, rightLeft, rightRight};

        assertEquals(expected.length, nodes.size());

        assertTrue(nodes.indexOf(root) < nodes.indexOf(left));
        assertTrue(nodes.indexOf(root) < nodes.indexOf(right));

        assertTrue(nodes.indexOf(left) < nodes.indexOf(leftLeft));
        assertTrue(nodes.indexOf(left) < nodes.indexOf(leftRight));
        assertTrue(nodes.indexOf(left) < nodes.indexOf(rightLeft));
        assertTrue(nodes.indexOf(left) < nodes.indexOf(rightRight));

        assertTrue(nodes.indexOf(right) < nodes.indexOf(leftLeft));
        assertTrue(nodes.indexOf(right) < nodes.indexOf(leftRight));
        assertTrue(nodes.indexOf(right) < nodes.indexOf(rightLeft));
        assertTrue(nodes.indexOf(right) < nodes.indexOf(rightRight));

    }

    private Node<Object> createNode() {
        return new Node<>(null);
    }

}
