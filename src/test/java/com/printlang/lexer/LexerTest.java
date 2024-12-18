package com.printlang.lexer;

import com.printlang.lexer.token.AbstractToken;
import com.printlang.lexer.token.IdToken;
import com.printlang.lexer.token.IdTokenType;
import com.printlang.lexer.token.NumToken;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {

    private Lexer buildLexer(String inputString) {
        return new Lexer(new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testSuccessfulTokenization() {
        String inputString = """
            x = 1
            print x
            scope {
                x = 2
            }
            print x
        """;
        List<AbstractToken> expectedOutput = List.of(
                // x = 1
                new IdToken("x", IdTokenType.USER_DEFINED),
                new IdToken("=", IdTokenType.EQUALS),
                new NumToken(1),

                // print x
                new IdToken("print", IdTokenType.PRINT),
                new IdToken("x", IdTokenType.USER_DEFINED),

                // scope {
                new IdToken("scope", IdTokenType.SCOPE),
                new IdToken("{", IdTokenType.LEFT_BRACKET),

                // x = 2
                new IdToken("x", IdTokenType.USER_DEFINED),
                new IdToken("=", IdTokenType.EQUALS),
                new NumToken(2),

                // }
                new IdToken("}", IdTokenType.RIGHT_BRACKET),

                // print x
                new IdToken("print", IdTokenType.PRINT),
                new IdToken("x", IdTokenType.USER_DEFINED)
        );

        Lexer lexer = buildLexer(inputString);
        List<AbstractToken> output = StreamSupport.stream(lexer.spliterator(), false).toList();

        assertEquals(output, expectedOutput);
    }

    @Test
    public void testInvalidNumToken() {
        String inputString = """
            x = 1cv
        """;

        Lexer lexer = buildLexer(inputString);
        assertThrows(LexerException.class, () -> { StreamSupport.stream(lexer.spliterator(), false).toList(); });
    }

    @Test
    public void testNumberExceedsIntBounds() {
        String inputString = """
            x = 1000000000000000
        """;

        Lexer lexer = buildLexer(inputString);
        assertThrows(LexerException.class, () -> { StreamSupport.stream(lexer.spliterator(), false).toList(); });
    }

    @Test
    public void testInvalidIdToken() {
        String inputString = """
            x = y.2
        """;

        Lexer lexer = buildLexer(inputString);
        assertThrows(LexerException.class, () -> { StreamSupport.stream(lexer.spliterator(), false).toList(); });
    }

}
