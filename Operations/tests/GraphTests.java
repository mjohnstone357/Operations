import org.junit.Before;
import org.junit.Test;
import org.operationsproject.operations.graph.Graph;
import org.operationsproject.operations.graph.Node;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Matthew Johnstone
 *         Date: 31/05/13
 *         Time: 20:36
 */
public class GraphTests {

    private Graph graph;

    @Before
    public void setUp() {
        graph = new Graph();
    }

    /**
     * Add a single node, and check that it is returned.
     */
    @Test
    public void should_return_sole_added_node() {

        Node node = createNode();
        graph.addNode(node);

        Set<Node> allNodes = graph.getAllNodes();

        assertEquals(1, allNodes.size());
        assertEquals(node, allNodes.iterator().next());
    }

    /**
     * Add two nodes, unconnected, and check that they are both returned.
     */
    @Test
    public void should_return_both_added_nodes() {

        Node node1 = createNode();
        Node node2 = createNode();

        graph.addNode(node1);
        graph.addNode(node2);
        Set<Node> allNodes = graph.getAllNodes();

        assertEquals(2, allNodes.size());
        assertTrue(allNodes.contains(node1));
        assertTrue(allNodes.contains(node2));
    }

    /**
     * Add two nodes, connect them, and check that each is referenced by the other.
     */
    @Test
    public void should_be_able_to_navigate_between_nodes() {

        Node node1 = createNode();
        Node node2 = createNode();

        node1.linkTo(node2);

        Set<Node> linkedNodesFromNode1 = node1.getLinkedNodes();
        assertEquals(1, linkedNodesFromNode1.size());
        assertEquals(node2, linkedNodesFromNode1.iterator().next());

        Set<Node> linkedNodesFromNode2 = node2.getLinkedNodes();
        assertEquals(1, linkedNodesFromNode2.size());
        assertEquals(node1, linkedNodesFromNode2.iterator().next());
    }

    /**
     * Add two nodes to a graph, and
     */

    private Node createNode() {
        return new Node();
    }

}
