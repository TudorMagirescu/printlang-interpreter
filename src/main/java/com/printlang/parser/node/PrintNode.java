package com.printlang.parser.node;

import java.util.Objects;

public class PrintNode extends AbstractNode {
    private final IdNode arg;

    public PrintNode(IdNode arg) {
        this.arg = arg;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        PrintNode otherNode = (PrintNode) other;
        return Objects.equals(this.arg, otherNode.arg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arg);
    }

    @Override
    public String toFormattedString(int depth) {
        return indent(depth)
                + "PrintNode\n"
                + arg.toFormattedString(depth + 1);
    }

    public IdNode getArg() {
        return arg;
    }
}
