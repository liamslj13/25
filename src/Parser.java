import java.util.*;

public class Parser {
    private List<Token> tokens;
    private final Lexer lexer = new Lexer();

    private Token eat() {
        Token prev = this.tokens.get(0);
        this.tokens.remove(0);
        return prev;
    }
    private boolean notEOF() {
        return this.tokens.get(0).type != TokenType.EOF;
    }

    private Token at() {
        return this.tokens.get(0);
    }

    public Program produceAST(String sourceCode) {
        this.tokens = lexer.tokenize(sourceCode);
        List<Stmt> statements = new ArrayList<>();

        while(this.notEOF()) {
            statements.add(this.parseStmt());
        }
        return new Program(statements);
    }

    private Token expect(TokenType type, String err) {
        if (tokens.isEmpty()) {
            handleError(err, null, type);
            return null;
        }
        Token prev = tokens.remove(0);
        if (prev == null || !prev.getType().equals(type)) {
            handleError(err, prev, type);
        }
        return prev;
    }

    private Stmt parseStmt() {
        return this.parseExpr();
    }

    private Expr parseExpr() {
        return this.parseAdditiveExpr();
    }

    private Expr parseAdditiveExpr() {
        Expr left = this.parseMultiplicitaveExpr();

        while (this.at().value.equals("+") || this.at().value.equals("-")) {
            String operator = this.eat().value;
            Expr right = this.parseMultiplicitaveExpr();
            left = new BinaryExpr(left, right, operator);
        }
        return left;
    }

    private Expr parseMultiplicitaveExpr() {
        Expr left = this.parsePrimaryExpr();

        while (this.at().value.equals("*") || this.at().value.equals("/") || this.at().value.equals("%")) {
            String operator = this.eat().value;
            Expr right = this.parsePrimaryExpr();
            left = new BinaryExpr(left, right, operator);
        }
        return left;
    }

    private Expr parsePrimaryExpr() {
        TokenType token = this.at().type;

        switch (token) {
            //User defined values
            case IDENTIFIER:
                return parseIdentifier();

            //Numeric literals (float and int)
            case INT:
                return parseInt();
            case FLOAT:
                return parseFloat();

            case NULL:
                this.eat();
                return new NullLiteral("null");

            //Expressions
            case OPENPAREN:
                this.eat();
                Expr value = this.parseExpr();
                this.expect(TokenType.CLOSEPAREN,
                        "Unexpected token found inside expression. " +
                                "Expected closing parenthesis.");
                return value;

            default:
                System.err.println("Unexpected token! " + this.at());
                System.exit(1);
        }
        return null;
    }

    /* =================================================================== //
                                   PARSE CASES
    // =================================================================== */
    private Identifier parseIdentifier() {
        Token token = eat();
        if (token.getType() != TokenType.IDENTIFIER) {
            handleError("Expected identifier, found: ", token, token.getType());
        }
        return new Identifier(token.getValue());
    }

    private IntLiteral parseInt() {
        Token token = eat();
        if (token.getType() != TokenType.INT) {
            handleError("Expected integer literal, found: ", token, token.getType());
        }
        return new IntLiteral(Integer.parseInt(token.getValue()));
    }

    private FloatLiteral parseFloat() {
        Token token = eat();
        if (token.getType() != TokenType.FLOAT) {
            handleError("Expected float literal, found: ", token, token.getType());
        }
        return new FloatLiteral(Float.parseFloat(token.getValue()));
    }

    // =================================================================== //
    private void handleError(String err, Token prev, TokenType type) {
        System.err.println("Parser Error:\n" + err +
                " " + prev + " - Expected: " + type);
        System.exit(1);
    }
    // =================================================================== //
}