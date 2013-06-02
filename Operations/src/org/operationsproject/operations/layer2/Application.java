package org.operationsproject.operations.layer2;

import org.operationsproject.operations.graph.CycleException;
import org.operationsproject.operations.graph.Graph;
import org.operationsproject.operations.graph.Node;
import org.operationsproject.operations.graph.UnknownNodeException;

import java.util.*;

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

        List<Function> functionsToEvaluate = new ArrayList<>();
        functionsToEvaluate.add(function);

        Stack<Function> functionStack = new Stack<>();
        functionStack.add(function);

        try {
            while(!functionStack.isEmpty()) {
                Function currentFunction = functionStack.pop();
                Set<Node<Function>> nextNodes = graph.getLinkedNodesByPayload(Graph.EdgeDirection.LINKS_TO, currentFunction);
                for (Node<Function> node : nextNodes) {
                    // TODO Make the BFS a method on the graph
                    Function payload = node.getPayload();
                    functionStack.add(payload); // Add at the end for breadth-first search
                    assert !functionsToEvaluate.contains(payload);
                    functionsToEvaluate.add(payload);
                }
            }

            Collections.reverse(functionsToEvaluate);

            for (Function functionToEvaluate : functionsToEvaluate) {
                List<String> allInputsForFunction = new ArrayList<>();

                Set<Node<Function>> incomingDependencies = graph.getLinkedNodesByPayload(Graph.EdgeDirection.LINKS_TO, functionToEvaluate);

                for (Node<Function> dependency : incomingDependencies) {
                    List<String> dependencyResults = dependency.getPayload().getResults();
                    allInputsForFunction.addAll(dependencyResults);
                }
                // By this point, all of the function's dependencies have been computed and added to allInputsForFunction
                functionToEvaluate.setInputs(allInputsForFunction);
                functionToEvaluate.compute();
            }

        } catch (UnknownNodeException e) {
            throw new RuntimeException(e); // This shouldn't happen.
        }

        return function.getResults();
    }

    public void addDataDependency(Function sourceFunction, Function destinationFunction) throws CycleException, UnknownNodeException {
        graph.linkPayloads(sourceFunction, destinationFunction);
    }
}
