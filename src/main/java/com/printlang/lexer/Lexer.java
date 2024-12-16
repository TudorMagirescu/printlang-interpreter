package com.printlang.lexer;

import com.printlang.lexer.token.AbstractToken;
import com.printlang.lexer.token.TokenFactory;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Scanner;

public class Lexer implements Iterable<AbstractToken> {

    private final Scanner scanner;

    public Lexer(InputStream inputStream) {
        this.scanner = new Scanner(inputStream);
    }

    @Override
    public Iterator<AbstractToken> iterator() {
        return new Iterator<AbstractToken>() {

            @Override
            public boolean hasNext() {
                return scanner.hasNext();
            }

            @Override
            public AbstractToken next() {
                return TokenFactory.createInstance(scanner.next());
            }
        };
    }

}
