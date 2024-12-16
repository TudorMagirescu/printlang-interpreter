package com.printlang;

import com.printlang.lexer.Lexer;
import com.printlang.lexer.token.AbstractToken;

import java.io.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer(new FileInputStream("examples/input0.txt"));
        PrintWriter output = new PrintWriter("examples/output0.txt");

        for (AbstractToken token : lexer) {
            output.write(token.toString() + "\n");
        }

        output.flush();
        output.close();
    }

}
