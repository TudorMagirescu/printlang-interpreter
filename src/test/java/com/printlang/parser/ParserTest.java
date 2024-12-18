package com.printlang.parser;

import com.printlang.lexer.Lexer;
import com.printlang.parser.node.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {

    private Lexer buildLexer(String inputString) {
        return new Lexer(new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testSuccessfulParsing() {
        String inputString = """
            x = 1
            print x
            scope {
                x = 2
            }
            print x
        """;

        AbstractNode expectedOutput =
                new ScopeNode(List.of(
                        new AssignmentNode(
                                "x",
                                new NumNode(1)
                        ),
                        new PrintNode(
                                new IdNode("x")
                        ),
                        new ScopeNode(List.of(
                                new AssignmentNode(
                                        "x",
                                        new NumNode(2)
                                )
                        )),
                        new PrintNode(
                                new IdNode("x")
                        )
                ));

        Lexer lexer = buildLexer(inputString);
        AbstractNode output = new Parser(lexer).parse();
        assertEquals(output, expectedOutput);
    }

    @Test
    public void testEqualsTokenNotPrecededByIdentifier() {
        String inputString = """
           1 = 1
        """;

        assertThrows(ParserException.class, () -> {
            Lexer lexer = buildLexer(inputString);
            new Parser(lexer).parse();
        });
    }

    @Test
    public void testEqualsTokenNotFollowedByAnExpression() {
        String inputString = """
           x = print x
        """;

        assertThrows(ParserException.class, () -> {
            Lexer lexer = buildLexer(inputString);
            new Parser(lexer).parse();
        });
    }

    @Test
    public void testScopeTokenNotFollowedByLeftBracket() {
        String inputString = """
           scope
            x = 1
           print x
        """;

        assertThrows(ParserException.class, () -> {
            Lexer lexer = buildLexer(inputString);
            new Parser(lexer).parse();
        });
    }

    @Test
    public void testLeftBracketNotPrecededByScopeToken() {
        String inputString = """
           {
            x = 1
           }
        """;

        assertThrows(ParserException.class, () -> {
            Lexer lexer = buildLexer(inputString);
            new Parser(lexer).parse();
        });
    }

    @Test
    public void testPrintTokenNotFollowedByIdentifier() {
        String inputString = """
           print 1
        """;

        assertThrows(ParserException.class, () -> {
            Lexer lexer = buildLexer(inputString);
            new Parser(lexer).parse();
        });
    }

    @Test
    public void testExpectedRightBracket() {
        String inputString = """
           scope {
            print x
        """;

        assertThrows(ParserException.class, () -> {
            Lexer lexer = buildLexer(inputString);
            new Parser(lexer).parse();
        });
    }

    @Test
    public void testStandaloneNumber() {
        String inputString = """
           x = 1
           1
        """;

        assertThrows(ParserException.class, () -> {
            Lexer lexer = buildLexer(inputString);
            new Parser(lexer).parse();
        });
    }

    @Test
    public void testStandaloneIdentifier() {
        String inputString = """
           x = 1
           x
        """;

        assertThrows(ParserException.class, () -> {
            Lexer lexer = buildLexer(inputString);
            new Parser(lexer).parse();
        });
    }

}
