package com.printlang.parser.node;

import java.util.Objects;

public class IdNode extends ExprNode {
    private final String id;

    public IdNode(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        IdNode otherNode = (IdNode) other;
        return Objects.equals(this.id, otherNode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toFormattedString(int depth) {
        return indent(depth) + "IdNode(id=" + id + ")";
    }
}
