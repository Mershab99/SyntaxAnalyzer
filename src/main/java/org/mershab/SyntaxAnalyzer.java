package org.mershab;


import java.io.FileReader;
import java.util.List;
import java.util.Stack;

public class SyntaxAnalyzer {
    private int currentTokenIndex = 0;
    private List<Integer> tokens;
    private List<Integer> lineNumbers;

    public SyntaxAnalyzer(FileReader inputFile) {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        lexicalAnalyzer.processFile(inputFile);

        this.tokens = lexicalAnalyzer.getTokenList();
        this.lineNumbers = lexicalAnalyzer.getTokenLineNumberList();

    }

    private int currentToken() {
        return tokens.get(currentTokenIndex);
    }

    /*

    public boolean analyze(List<Integer> tokens) {
        for (TokenType token : tokens) {
            switch (token) {
                case LEFT_PAREN:
                case LEFT_BRACE:
                    stack.push(token);
                    break;
                case CLOSE_PAREN:
                    if (stack.isEmpty() || stack.peek().getType() != TokenType.OPEN_PAREN) return false;
                    stack.pop();
                    break;
                case CLOSE_BRACKET:
                    if (stack.isEmpty() || stack.peek().getType() != TokenType.OPEN_BRACKET) return false;
                    stack.pop();
                    break;
                case CLOSE_CURLY:
                    if (stack.isEmpty() || stack.peek().getType() != TokenType.OPEN_CURLY) return false;
                    stack.pop();
                    break;
                case FUNCTION:
                case DECLARATION:
                    stack.push(token);
                    break;
                // ... handle other cases ...
            }
        }

        // Ensure all open symbols have been closed
        return stack.isEmpty();
    }
     */

    public String parse() throws SyntaxException {
        Stack<Integer> bracketStack = new Stack<>();
        Stack<Integer> parenStack = new Stack<>();
        boolean insideStringLiteral = false;
        boolean expectSemicolon = false;

        while (currentTokenIndex < tokens.size()) {
            int token = currentToken();

            if (insideStringLiteral && token != TokenTypes.STR_LIT) {
                currentTokenIndex++;
                continue;
            }

            switch (token) {
                case TokenTypes.LEFT_BRACE:
                    bracketStack.push(token);
                    expectSemicolon = false;
                    break;

                case TokenTypes.RIGHT_BRACE:
                    checkStack(bracketStack, TokenTypes.LEFT_BRACE, "Unmatched closing }");
                    break;

                case TokenTypes.LEFT_PAREN:
                    parenStack.push(token);
                    expectSemicolon = false;
                    break;

                case TokenTypes.RIGHT_PAREN:
                    checkStack(parenStack, TokenTypes.LEFT_PAREN, "Unmatched closing )");
                    break;

                case TokenTypes.STR_LIT:
                    insideStringLiteral = !insideStringLiteral;
                    break;

                case TokenTypes.SEMICOLON:
                    expectSemicolon = false;
                    break;

                // Tokens that typically conclude a statement and expect a semicolon afterwards
                case TokenTypes.ASSIGN_OP:
                case TokenTypes.ADD_OP:
                case TokenTypes.SUB_OP:
                case TokenTypes.MULT_OP:
                case TokenTypes.DIV_OP:
                case TokenTypes.INT_LIT:
                case TokenTypes.FLOAT_LIT:
                    expectSemicolon = true;
                    break;

                // Control structures and other cases that reset the expectation for a semicolon
                case TokenTypes.COMMENT:
                case TokenTypes.EQUALS:
                case TokenTypes.NOT_EQUALS:
                case TokenTypes.IF:
                case TokenTypes.ELSE:
                case TokenTypes.FOR:
                case TokenTypes.WHILE:
                    expectSemicolon = false;
                    break;

                // Add more cases as needed for other tokens

                default:
                    // Handle other tokens or ignore
            }

            if (expectSemicolon && currentTokenIndex < tokens.size() - 1 && tokens.get(currentTokenIndex + 1) != TokenTypes.SEMICOLON) {
                throw new SyntaxException("Missing semi colon at line " + lineNumbers.get(currentTokenIndex));
            }

            currentTokenIndex++;
        }

        if (!bracketStack.isEmpty()) {
            throw new SyntaxException("Unmatched opening { at line " + lineNumbers.get(bracketStack.peek()));
        }

        if (!parenStack.isEmpty()) {
            throw new SyntaxException("Unmatched opening ( at line " + lineNumbers.get(parenStack.peek()));
        }

        if (insideStringLiteral) {
            throw new SyntaxException("Unclosed string literal at line " + lineNumbers.get(currentTokenIndex - 1));
        }

        if (expectSemicolon) {
            throw new SyntaxException("Missing semi colon at end of file");
        }

        return "Syntax analysis succeed";
    }
    private void checkStack(Stack<Integer> stack, int matchingToken, String errorMessage) throws SyntaxException {
        if (stack.isEmpty() || stack.peek() != matchingToken) {
            throw new SyntaxException(errorMessage + " at line " + lineNumbers.get(currentTokenIndex));
        }
        stack.pop();
    }


    public static class SyntaxException extends RuntimeException {
        public SyntaxException(String message) {
            super("Syntax analysis failed\n" + "syntax_analyzer_error - " + message);
        }
    }

    static class TokenTypes {
        /* Token codes */
        private static final int INT_LIT = 10;
        private static final int FLOAT_LIT = 12;
        private static final int IDENT = 11;
        private static final int STR_LIT = 13;
        private static final int ASSIGN_OP = 20;
        private static final int ADD_OP = 21;
        private static final int SUB_OP = 22;
        private static final int MULT_OP = 23;
        private static final int DIV_OP = 24;
        private static final int LEFT_PAREN = 25;
        private static final int RIGHT_PAREN = 26;
        private static final int LEFT_BRACE = 27;
        private static final int RIGHT_BRACE = 28;
        private static final int SEMICOLON = 29;
        private static final int LESS_THAN = 30;
        private static final int GREATER_THAN = 31;
        private static final int EQUALS = 32;
        private static final int NOT_EQUALS = 33;
        private static final int AND_OP = 34;
        private static final int OR_OP = 35;
        private static final int IF = 36;
        private static final int ELSE = 37;
        private static final int FOR = 38;
        private static final int WHILE = 39;
        private static final int COMMENT = 40;
        private static final int QUESTION_MARK = 41;
        private static final int COLON = 42;
    }
}

