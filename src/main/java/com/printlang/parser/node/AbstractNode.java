package com.printlang.parser.node;

public abstract class AbstractNode {
    public abstract String toFormattedString(int depth);

    protected String indent(int depth) {
        return "\t".repeat(depth);
    }
}
