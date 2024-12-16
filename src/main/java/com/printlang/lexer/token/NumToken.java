package com.printlang.lexer.token;

import java.util.Objects;

public class NumToken extends AbstractToken {

    private final int val;

    public NumToken(int val) {
        this.val = val;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        NumToken otherToken = (NumToken) other;
        return this.val == otherToken.val;
    }

    @Override
    public int hashCode() {
        return Objects.hash(val);
    }

    @Override
    public String toString() {
        return "NumToken(val='" + val + "')";
    }
}
