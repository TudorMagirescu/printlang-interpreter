# Printlang Interpreter

**Printlang Interpreter** is an interpreter for a language I've called *Printlang*, due to its primary feature of printing variables. In fact, the *Printlang* supports only the following type of statements: variable assignments and variable printing. The source code of the language contains (potentially nested) scope blocks, which indicate the lifetime of the variable definitions.

The codebase consists of 3 key components:
* Lexer: for tokenising the source code
* Parser: for generating the abstract syntax tree (AST)
* Interpreter: for generating the output of the code based on the AST

The source code can be found under `src/main` and tests are located under `src/test`. By running `Main.java`, the output and the partial transformation of the data of the example found in `examples/input0.txt` is written to `examples/output0.txt`. Feel free to change `examples/input0.txt` to observe the interpreter's behavior in different scenarios.

## Language Specification

The following statements are valid in *Printlang*:
1. `x = 99` (syntax: \<name> = \<integer value>) -- assign a variable to some integer value.
2. `x = y` (syntax: \<name> = \<another name>) -- assign a variable to some other variable's value.
3. `scope {` -- open a scope
4. `}` -- exit the last opened scope.
5. `print x` (syntax: print \<variable name>) -- prints the variable's name on the screen or prints "null" if the variable doesn't exist.

Input Example:
```
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
```
Output Example:
```
1
2
3
3
2
null
1
```

## Lexer (`com.printlang.lexer`)

The **Lexer** component transforms the source code into a stream of `AbstractTokens`. The `AbstractTokens` are of two types:
1. `NumToken` - represents an integer
2. `IdToken` - represents the other identifiers; the `IdTokens` have types, such as `USER_DEFINED` or `EQUALS`.

If unsuccessful, the **Lexer** throws a `LexerException`.

Notes:
* Tokens must be separated by whitespace (i.e. `scope{` or `a=b` will not be parsed properly).
* `NumTokens` must hold 32-bit integers. Otherwise, a `LexerException` is thrown.
* `USER_DEFINED` `IdTokens` must match the following regex: `^[a-zA-Z][a-zA-Z0-9_]*$`.

## Parser (`com.printlang.parser`)

The **Parser** component parses the stream of `AbstractTokens` and produces the AST of the language. The implementation of the parser is that of a classical top-down recursive descent parser. The AST contains the following **concrete** nodes:
1. `NumNode` - holds an integer
2. `IdNode` - holds an identifier (i.e. variable)
3. `AssignmentNode` - represents a variable assignment; the `lhs` is a `String` representing the identifier which is defined and the `rhs` is either a `NumNode` or an `IdNode`.
4. `PrintNode` - represents a `print` statement; its child is the identifier whose value is to be printed.
5. `ScopeNode` - represents a `scope`; its children are the statements inside the `scope`.

If unsuccessful, the **Parser** throws a `ParserException`.

Notes:
* An implicit `scope` encapsulates the entire source code (i.e. the root of the AST is a `ScopeNode`).
* Other abstract classes such as `ExprNode` are used to group together other node types that can be used in similar contexts.
* To log the AST, use the `toFormattedString` method. It adds nice indentation :).

## Interpreter (`com.printlang.interpreter`)

The **Interpreter** component traverses the AST in a depth-first order and generates the output of the code. Each node type is handled differently. Notably, each scope is assigned an `Env` object, storing the variables defined in the current scope and their values. When a new scope is entered, the `Env` object is copied and the copy will be the one modified. When a scope is exited, the current `Env` object is discarded. This way, an implicit `stack` of `Env` objects is created.

If unsuccessful, the **Interpreter** throws an `InterpreterException`.