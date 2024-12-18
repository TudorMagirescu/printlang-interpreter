package com.printlang.lexer.token;

import com.printlang.lexer.LexerException;

public class TokenFactory {

    public static AbstractToken createInstance(String s) {
        if (Character.isDigit(s.charAt(0))) {
            return createNumToken(s);
        }

        return createIdToken(s);
    }

    private static NumToken createNumToken(String s) {
        if (!s.matches("\\d+")) {
            throw new LexerException("Numeric token " + s + " contains non-digit characters.");
        }

        try {
            int val = Integer.parseInt(s);
            return new NumToken(val);
        } catch (NumberFormatException e) {
            throw new LexerException("Numeric token exceeds the range of a 32-bit integer.");
        }
    }

    private static IdToken createIdToken(String s) {
        switch (s) {
            case "{":
                return new IdToken(s, IdTokenType.LEFT_BRACKET);
            case "}":
                return new IdToken(s, IdTokenType.RIGHT_BRACKET);
            case "=":
                return new IdToken(s, IdTokenType.EQUALS);
            case "print":
                return new IdToken(s, IdTokenType.PRINT);
            case "scope":
                return new IdToken(s, IdTokenType.SCOPE);
            default:
                if (!s.matches("^[a-zA-Z][a-zA-Z0-9_]*$"))
                    throw new LexerException("Id token " + s + " is not valid.");
                return new IdToken(s, IdTokenType.USER_DEFINED);
        }
    }

}
