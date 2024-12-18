package com.printlang;

import com.printlang.lexer.Lexer;
import com.printlang.lexer.token.AbstractToken;
import com.printlang.parser.Parser;
import com.printlang.parser.node.AbstractNode;

import java.io.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer(new FileInputStream("examples/input0.txt"));
        PrintWriter output = new PrintWriter("examples/output0.txt");

        output.write("Lexer output:\n");
        for (AbstractToken token : lexer) {
            output.write(token.toString() + "\n");
        }

        output.write("\nParser output:\n");
        Parser parser = new Parser(new Lexer(new FileInputStream("examples/input0.txt")));
        AbstractNode root = parser.parse();
        output.write(root.toFormattedString(0));

        output.flush();
        output.close();
    }

}
