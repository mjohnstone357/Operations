package org.operationsproject.operations.graph;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.operationsproject.operations.graph.Graph.EdgeDirection;

/**
 * Class representing the directed link from one Node in a Graph to another Node in that Graph.
 * @author Matthew Johnstone
 *         Date: 31/05/13
 *         Time: 23:07
 */
public class NodeLink<T> {

    public Node<T> sourceNode;
    public Node<T> targetNode;

    public NodeLink(@NotNull Node<T> sourceNode, @NotNull Node<T> targetNode) {
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
    }

    public @Nullable Node<T> getNodeFromDirection(@NotNull Node node, @NotNull EdgeDirection direction) {
        if (direction == EdgeDirection.LINKS_TO && targetNode == node) {
                return sourceNode;
        } else if (direction == EdgeDirection.LINKED_TO_BY && sourceNode == node) {
            return targetNode;
        }
        return null;
    }
}
