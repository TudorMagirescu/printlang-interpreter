package com.printlang.parser.node;

import java.util.List;
import java.util.Objects;

public class ScopeNode extends AbstractNode {
    private final List<AbstractNode> children;

    public ScopeNode(List<AbstractNode> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        ScopeNode otherNode = (ScopeNode) other;
        return Objects.equals(this.children, otherNode.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(children);
    }

    @Override
    public String toFormattedString(int depth) {
        StringBuilder sb = new StringBuilder(indent(depth) + "ScopeNode\n");
        for (AbstractNode child : children) {
            sb.append(child.toFormattedString(depth + 1)).append("\n");
        }

        return sb.toString();
    }

    public List<AbstractNode> getChildren() {
        return children;
    }
}
