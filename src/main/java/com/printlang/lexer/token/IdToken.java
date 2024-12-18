package com.printlang.lexer.token;

import java.util.Objects;

public class IdToken extends AbstractToken {

    private final String id;
    private final IdTokenType type;

    public IdToken(String id, IdTokenType type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public String toString() {
        return "IdToken(id='" + id + "',type=" + type + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        IdToken otherToken = (IdToken) other;
        return Objects.equals(this.id, otherToken.id)
                && Objects.equals(this.type, otherToken.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    public String getId() {
        return id;
    }

    public IdTokenType getIdTokenType() {
        return type;
    }
}
