package com.printlang.interpreter;

import com.printlang.parser.node.*;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Interpreter {
    private final AbstractNode root;
    private final Writer outputStream;
    private final Map<Class<? extends AbstractNode>, BiConsumer<AbstractNode, Env>> handlers;

    public Interpreter(AbstractNode root, Writer outputStream) {
        this.root = root;
        this.outputStream = outputStream;

        this.handlers = new HashMap<>();
        handlers.put(ScopeNode.class, (node, env) -> interpretScopeNode((ScopeNode) node, env));
        handlers.put(PrintNode.class, (node, env) -> interpretPrintNode((PrintNode) node, env));
        handlers.put(AssignmentNode.class, (node, env) -> interpretAssignmentNode((AssignmentNode) node, env));
    }

    public void interpret() {
        interpret(root, new Env());
    }

    private void interpret(AbstractNode node, Env env) {
        BiConsumer<AbstractNode, Env> handler = handlers.get(node.getClass());
        if (handler != null) {
            handler.accept(node, env);
        } else {
            throw new InterpreterException("Unexpected node type encountered.");
        }
    }

    private void interpretScopeNode(ScopeNode node, Env env) {
        // Create a new `Env` object associated with the current scope.
        Env scopeEnv = new Env(env);

        for (AbstractNode child : node.getChildren()) {
            interpret(child, scopeEnv);
        }
    }

    private void interpretPrintNode(PrintNode node, Env env) {
        Integer output = evaluateIdNode(node.getArg(), env);

        try {
            if (output == null) {
                outputStream.write("null\n");
                return;
            }

            outputStream.write(output.toString() + '\n');
        } catch (IOException e) {
            throw new InterpreterException("Error writing to output stream.");
        }

    }

    private void interpretAssignmentNode(AssignmentNode node, Env env) {
        String id = node.getLhs();
        Integer val = evaluateExprNode(node.getRhs(), env);

        env.put(id, val);
    }

    private Integer evaluateExprNode(ExprNode exprNode, Env env) {
        if (exprNode instanceof NumNode numNode) {
            return evaluateNumNode(numNode);
        }

        return evaluateIdNode((IdNode) exprNode, env);
    }

    private Integer evaluateNumNode(NumNode node) {
        return node.getVal();
    }

    private Integer evaluateIdNode(IdNode node, Env env) {
        return env.get(node.getId());
    }

}
