package com.printlang.parser.node;

import java.util.Objects;

public class AssignmentNode extends AbstractNode {
    private final String lhs;
    private final ExprNode rhs;

    public AssignmentNode(String lhs, ExprNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        AssignmentNode otherNode = (AssignmentNode) other;
        return Objects.equals(this.lhs, otherNode.lhs)
                && Objects.equals(this.rhs, otherNode.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lhs, rhs);
    }

    @Override
    public String toFormattedString(int depth) {
        return indent(depth)
                + "AssignmentNode(lhs=" + lhs + ")\n"
                + rhs.toFormattedString(depth + 1);
    }

    public String getLhs() {
        return lhs;
    }

    public ExprNode getRhs() {
        return rhs;
    }
}
