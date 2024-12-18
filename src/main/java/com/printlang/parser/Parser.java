package com.printlang.parser;

import com.printlang.lexer.Lexer;
import com.printlang.lexer.token.AbstractToken;
import com.printlang.lexer.token.IdToken;
import com.printlang.lexer.token.IdTokenType;
import com.printlang.lexer.token.NumToken;
import com.printlang.parser.node.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Parser {
    private final Iterator<AbstractToken> tokenStream;
    private AbstractToken currentToken;

    public Parser(Lexer lexer) {
        tokenStream = lexer.iterator();
    }

    public AbstractNode parse() {
        // The root of the abstract syntax tree will be a `ScopeNode`.
        // This way, we consider the code to be inside an implicit scope.
        return parseScopeNode(0);
    }

    private AbstractToken getNextToken() {
        if (currentToken != null) return currentToken;
        if (!tokenStream.hasNext()) return null;
        currentToken = tokenStream.next();
        return currentToken;
    }

    private void consumeCurrentToken() {
        currentToken = null;
    }

    private boolean isNumToken(AbstractToken token) {
        return token instanceof NumToken;
    }

    private boolean isIdToken(AbstractToken token, IdTokenType expectedType) {
        return (token instanceof IdToken) && ((IdToken) token).getIdTokenType() == expectedType;
    }

    private ScopeNode parseScopeNode(int depth) {
        List<AbstractNode> children = new ArrayList<>();

        AbstractToken token = getNextToken();
        boolean metRightBracketToken = false;
        while (token != null && !metRightBracketToken) {
            if (isNumToken(token)) {
                children.add(parseNumNode());
                token = getNextToken();
                continue;
            }

            IdToken idToken = (IdToken) token;
            switch (idToken.getIdTokenType()) {
                case EQUALS -> {
                    // Check that the `=` token is preceded by an identifier.
                    if (children.isEmpty() || !(children.get(children.size() - 1) instanceof IdNode lhs)) {
                        throw new ParserException("`=` token not preceded by an identifier.");
                    }

                    // Pop the `lhs` node from the `children` list.
                    children.remove(children.size() - 1);

                    children.add(parseAssignmentNode(lhs));
                }
                case SCOPE -> {
                    // Consume `scope` token.
                    consumeCurrentToken();

                    // Check if the next token is a `{` and consume it.
                    AbstractToken leftParenthesisToken = getNextToken();
                    if (!isIdToken(leftParenthesisToken, IdTokenType.LEFT_BRACKET)) {
                        throw new ParserException("`scope` token not followed by a `{` token.");
                    }
                    consumeCurrentToken();

                    children.add(parseScopeNode(depth + 1));
                }
                case LEFT_BRACKET -> {
                    // This should've been preceded by a `scope` token.
                    throw new ParserException("`{` token not preceded by a 'scope' token.");
                }
                case RIGHT_BRACKET -> {
                    // Consume the `}' token.
                    consumeCurrentToken();

                    metRightBracketToken = true;
                }
                case PRINT -> {
                    children.add(parsePrintNode());
                }
                case USER_DEFINED -> {
                    children.add(parseIdNode());
                }
                default -> {
                    throw new ParserException("Unexpected token found: " + token + ".");
                }
            }
            token = getNextToken();
        }

        // Check if all scopes are properly closed.
        if (!metRightBracketToken && depth > 0) {
            throw new ParserException("Expected `}` token.");
        }

        // Check if a direct child of the `ScopeNode` is a `NumNode` or `IdNode`.
        // In that case, throw an exception, since standalone numbers and identifier are not valid expressions.
        for (AbstractNode child : children) {
            if ((child instanceof NumNode) || (child instanceof IdNode)) {
                throw new ParserException("Standalone numbers or identifiers are not valid expressions.");
            }
        }

        return new ScopeNode(children);
    }

    private PrintNode parsePrintNode() {
        // Consume `print` token.
        consumeCurrentToken();

        AbstractToken token = getNextToken();
        if (isIdToken(token, IdTokenType.USER_DEFINED)) {
            return new PrintNode(parseIdNode());
        }

        throw new ParserException("`print` token not followed by an identifier.");
    }

    private AssignmentNode parseAssignmentNode(IdNode lhs) {
        // Consume `=` token.
        consumeCurrentToken();

        AbstractToken token = getNextToken();

        if (isNumToken(token)) {
            return new AssignmentNode(lhs.getId(), parseNumNode());
        }

        if (isIdToken(token, IdTokenType.USER_DEFINED)) {
            return new AssignmentNode(lhs.getId(), parseIdNode());
        }

        throw new ParserException("`=` token not followed by a number or identifier.");
    }

    private NumNode parseNumNode() {
        NumToken token = (NumToken) currentToken;
        consumeCurrentToken();
        return new NumNode(token.getVal());
    }

    private IdNode parseIdNode() {
        IdToken token = (IdToken) currentToken;
        consumeCurrentToken();
        return new IdNode(token.getId());
    }
}
