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

        while (this.notEOF()) {
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
        return switch (this.at().type) {
            case VAR, CONST -> this.parseVarDeclaration();
            default -> this.parseExpr();
        };
    }

    private Expr parseObjExpr() {
        if (this.at().type != TokenType.OPENBRACE) {
            return this.parseArrayExpr();
        }
        this.eat();

        ArrayList<Property> properties = new ArrayList<>();

        while (this.notEOF() && this.at().type != TokenType.CLOSEBRACE) {

            String key = this.expect(TokenType.IDENTIFIER, "Expected object literal key.").value;

            // key : -> { key, ... } No value specified
            if (this.at().type == TokenType.COMMA) {
                this.eat();
                properties.add(new Property(key));
                continue;
            } else if (this.at().type == TokenType.CLOSEBRACE) {
                properties.add(new Property(key));
                continue;
            }
            // represents {key : value} structure
            this.expect(
                    TokenType.COLON,
                    "Missing colon following identifier in ObjectExpr."
            );
            Expr value = this.parseExpr();
            properties.add(new Property(key, value));
            if (this.at().type != TokenType.CLOSEBRACE) {
                this.expect(
                        TokenType.COMMA,
                        "Expected comma or closing bracket following previous property."
                );
            }
        }
        this.expect(TokenType.CLOSEBRACE, "Object literal missing closing brace");
        return new ObjLiteral(properties);
    }

    private Expr parseArrayExpr() {
        if (this.at().type != TokenType.OPENBRACKET) {
            return this.parseTryCatchExpr();
        }
        this.eat();

        ArrayList<Expr> values = new ArrayList<>();

        while (this.notEOF() && this.at().type != TokenType.CLOSEBRACKET) {
            values.add(this.parseExpr());
            if (this.at().type != TokenType.CLOSEBRACKET) {
                this.expect(TokenType.COMMA, "Expected comma (\",\") or closing bracket (\"]\") after array element.");
            }
        }
        this.expect(TokenType.CLOSEBRACKET, "Expected closing bracket (\"]\") at the end of array expression.");
        return new ArrayLiteral(values);
    }

    private Expr parseTryCatchExpr() {
        return null;
    }

    private Stmt parseVarDeclaration() {
        boolean isConstant = this.eat().type == TokenType.CONST;
        String identifier = Objects.requireNonNull(this.expect(
                TokenType.IDENTIFIER,
                "Expected identifier name following variable declaration expression (VAR, CONST)."
        )).value;

        if (this.at().type == TokenType.SEMICOLON) {
            this.eat();
            if (isConstant) {
                System.err.println("Error: Constant variable " + identifier + " must be initialized with a value.");
                System.exit(1);
            }
            return new VariableDeclaration(false, new Identifier(identifier), null);
        }
        if (this.at().type == TokenType.EQUALS) {
            this.eat();
            Expr initializer = this.parseExpr();
            if (initializer == null) {
                System.err.println("Error: Missing expression after equals sign for variable initialization.");
                System.exit(1);
            }
            this.expect(TokenType.SEMICOLON, "Expected \";\", got \"" + this.tokens.get(0) + "\".");
            return new VariableDeclaration(isConstant, new Identifier(identifier), initializer);
        }
        System.err.println("Error: Unexpected token after variable declaration.");
        System.exit(1);
        return null;
    }

    private Expr parseExpr() {
        return this.parseAssignmentExpr();
    }

    private Expr parseAssignmentExpr() {
        Expr left = this.parseAdditiveExpr();

        if (this.at().type == TokenType.EQUALS) {
            this.eat();
            Expr value = this.parseAssignmentExpr();
            return new AssignmentExpr(left, value);
        }

        return left;
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
            case BOOL:
                return parseBool();

            //Expressions
            case OPENPAREN:
                this.eat();
                Expr value = this.parseExpr();
                this.expect(TokenType.CLOSEPAREN,
                        "Error: Unexpected token found inside expression. " +
                                "Expected closing parenthesis.");
                return value;

            case EOF:
                System.exit(0);
            default:
                System.err.println("Error: Unexpected token, " + this.at());
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

    private BoolLiteral parseBool() {
        Token token = eat();
        if (token.getType() != TokenType.BOOL) {
            handleError("Expected boolean literal, found: ", token, token.getType());
        }
        return new BoolLiteral(Boolean.parseBoolean(token.getValue()));
    }

    // =================================================================== //
    private void handleError(String err, Token prev, TokenType type) {
        System.err.println("Parser Error:\n" + err +
                " " + prev + " - Expected: " + type);
        System.exit(1);
    }
    // =================================================================== //
}