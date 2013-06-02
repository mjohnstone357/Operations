package org.operationsproject.operations.graph;

import org.junit.Before;
import org.junit.Test;

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

    private Node<Object> createNode() {
        return new Node<>(null);
    }

}
