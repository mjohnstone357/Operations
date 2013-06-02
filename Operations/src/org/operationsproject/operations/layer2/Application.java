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

    public List<String> evaluate(Function function) throws UnknownNodeException {

        List<Function> functionsToEvaluate = new ArrayList<>();
        functionsToEvaluate.add(function);

        Stack<Function> functionStack = new Stack<>();
        functionStack.add(function);

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



        return function.getResults();
    }

    public void addDataDependency(Function sourceFunction, Function destinationFunction) throws CycleException, UnknownNodeException {
        graph.linkPayloads(sourceFunction, destinationFunction);
    }
}
