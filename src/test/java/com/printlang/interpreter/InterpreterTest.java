package com.printlang.interpreter;

import com.printlang.lexer.Lexer;
import com.printlang.parser.Parser;
import com.printlang.parser.node.AbstractNode;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InterpreterTest {

    private Lexer buildLexer(String inputString) {
        return new Lexer(new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testInterpretExampleSuccessfully() {
        String inputString = """
            x = 1
            print x
            scope {
                x = 2
                print x
                scope {
                    x = 3
                    y = x
                    print x
                    print y
                }
                print x
                print y
            }
            print x
        """;

        StringWriter writer = new StringWriter();
        AbstractNode root = new Parser(buildLexer(inputString)).parse();
        (new Interpreter(root, writer)).interpret();

        String expectedOutput = "1\n2\n3\n3\n2\nnull\n1\n";
        assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testPrintNullVariables() {
        String inputString = """
            print a
            a = b
            print a
            print b
        """;

        StringWriter writer = new StringWriter();
        AbstractNode root = new Parser(buildLexer(inputString)).parse();
        (new Interpreter(root, writer)).interpret();

        String expectedOutput = "null\nnull\nnull\n";
        assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testAssignToSelf() {
        String inputString = """
            x = 2
            x = x
            print x
        """;

        StringWriter writer = new StringWriter();
        AbstractNode root = new Parser(buildLexer(inputString)).parse();
        (new Interpreter(root, writer)).interpret();

        String expectedOutput = "2\n";
        assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testNestedScopes() {
        String inputString = """
            scope {
                scope {
                    scope {
                        x = 2
                        print x
                    }
                    print x
                }
                print x
            }
        """;

        StringWriter writer = new StringWriter();
        AbstractNode root = new Parser(buildLexer(inputString)).parse();
        (new Interpreter(root, writer)).interpret();

        String expectedOutput = "2\nnull\nnull\n";
        assertEquals(expectedOutput, writer.toString());
    }
}
