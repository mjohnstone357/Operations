package org.operationsproject.operations.graph;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.operationsproject.operations.graph.Graph.EdgeDirection;

/**
 * @author Matthew Johnstone
 *         Date: 31/05/13
 *         Time: 23:07
 */
public class NodeLink {

    public Node sourceNode;
    public Node targetNode;

    public NodeLink(@NotNull Node sourceNode, @NotNull Node targetNode) {
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
    }

    public @Nullable Node getNodeFromDirection(@NotNull Node node, @NotNull EdgeDirection direction) {
        if (direction == EdgeDirection.LINKS_TO && targetNode == node) {
                return sourceNode;
        } else if (direction == EdgeDirection.LINKED_TO_BY && sourceNode == node) {
            return targetNode;
        }
        return null;
    }
}
