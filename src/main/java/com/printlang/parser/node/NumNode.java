package com.printlang.parser.node;

import java.util.Objects;

public class NumNode extends ExprNode {
    private final int val;

    public NumNode(int val) {
        this.val = val;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        NumNode otherNode = (NumNode) other;
        return this.val == otherNode.val;
    }

    @Override
    public int hashCode() {
        return Objects.hash(val);
    }

    @Override
    public String toFormattedString(int depth) {
        return indent(depth) + "NumNode(val=" + val + ")";
    }
}
