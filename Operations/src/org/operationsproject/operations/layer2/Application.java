package org.operationsproject.operations.layer2;

import org.operationsproject.operations.graph.CycleException;
import org.operationsproject.operations.graph.Graph;
import org.operationsproject.operations.graph.Node;
import org.operationsproject.operations.graph.UnknownNodeException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class representing an Operations application.
 * @author Matthew Johnstone
 *         Date: 01/06/13
 *         Time: 23:00
 */
public class Application {

    private Set<Function> functions;
    private Graph<Function> graph;

    public Application() {
        functions = new HashSet<>();
        // For now, the graph just contains functions. In future it'll have functions, values and constraints
        graph = new Graph<>();
    }

    public void addFunction(Function function) {
        if (!functions.contains(function)) {
            functions.add(function);
            Node<Function> node = new Node<>(function);

            graph.addNode(node);

        }

    }

    public List<String> evaluate(Function function) {
        // Get the node for the function

        // Traverse the graph, finding the root data source
        Node<Function> node = graph.getNodeWithPayload(function);

        Set<Node<Function>> linkedNodes;

        Node<Function> finalNode = node;

        while (!(linkedNodes = graph.getLinkedNodes(Graph.EdgeDirection.LINKS_TO, finalNode)).isEmpty()) {
            assert linkedNodes.size() == 1;
            finalNode = linkedNodes.iterator().next();
        }

        assert finalNode != null;

        Node<Function> returnNode = finalNode;

        List<String> currentInputs = finalNode.getPayload().apply();

        while (true) {

            Function currentFunction = returnNode.getPayload();


            currentFunction.setInputs(currentInputs);
            currentInputs = currentFunction.apply();

            Set<Node<Function>> linkedNodes1 = graph.getLinkedNodes(Graph.EdgeDirection.LINKED_TO_BY, returnNode);

            if (!linkedNodes1.isEmpty()) {
                returnNode = linkedNodes1.iterator().next();
            } else {
                break;
            }


            if (returnNode == node) {
                break;
            }

        }

        return currentInputs;

    }

    public void addDataDependency(Function sourceFunction, Function destinationFunction) throws CycleException, UnknownNodeException {
        graph.linkPayloads(sourceFunction, destinationFunction);
    }
}
