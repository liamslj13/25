import java.util.HashMap;
import java.util.Map;

/* =================================================================== //
                    BASE TOKEN CLASS FOR USE EVERYWHERE
// =================================================================== */
class Token {
    TokenType type;
    String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Token(%s, %s)", type, value);
    }

    public void setType(TokenType type) {
        this.type = type;
    }
    public TokenType getType() {
        return type;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
// =================================================================== //


enum TokenType {

    /*
     * FOR EASE OF ORGANIZATION, TOKENS ALREADY HANDLED IN
     * THE LEXER IMPLEMENTATION WILL BE DENOTED BY AN 'X'.
     */

    //Identifier
    INT, //                     -X
    FLOAT, //                   -X
    IDENTIFIER, //              -X

    //Operators * Grouping
    EQUALS, // =                -X

    OPENPAREN, // (             -X
    CLOSEPAREN, // )            -X
    BINARYOPERATOR, //          -X
    OPENBRACE, // {             -X
    CLOSEBRACE, // }            -X
    OPENBRACKET, // [           -X
    CLOSEBRACKET, // ]          -X
    QUOTATION, // "             -X
    GREATER, // >               -X
    LESSER, // <                -X
    EQUALSCOMPARE, // ==        -X
    NOTEQUALSCOMPARE, // !=     -X
    FACTORIAL, // !             -X
    EXPONENTIAL, // ^           -X
    AMPERSAND, // &             -X
    AND, // &&                  -X
    OR, // ||                   -X
    ARROW, // ->                -X
    DOT, // .                   -X
    COLON, // :                 -X
    SEMICOLON, // ;             -X

    //For comma, a pipe char '|' also works as a separator
    COMMA, // ,                 -X

    //Other
    EOF, //End of file          -X
    NEWLINE, //New line         -X

    //Keywords                  -X
    IF, //If branch
    ELIF, //Else-if branch
    ELSE, //Else branch
    FOR, //For loops
    WHILE, //While loops
    NULL, //Null value
    BREAK, //Break a loop
    FUNC, //Function
    NOT, //Boolean negation
    TRY, //Try block
    CATCH, //Catch error

    //***COMMENTS***//
    // ">>" FOR SINGLE LINE

    // >>>
    // "CONTENT
    // HERE"
    // <<< FOR MULTI LINE
}

class TokenChars {
    private static Map<String, TokenType> TOKEN_CHARS = new HashMap<>();
    static {
        TOKEN_CHARS.put("(", TokenType.OPENPAREN);
        TOKEN_CHARS.put(")", TokenType.CLOSEPAREN);
        TOKEN_CHARS.put("{", TokenType.OPENBRACE);
        TOKEN_CHARS.put("}", TokenType.CLOSEBRACE);
        TOKEN_CHARS.put("[", TokenType.OPENBRACKET);
        TOKEN_CHARS.put("]", TokenType.CLOSEBRACKET);
        TOKEN_CHARS.put("+", TokenType.BINARYOPERATOR);
        TOKEN_CHARS.put("*", TokenType.BINARYOPERATOR);
        TOKEN_CHARS.put("/", TokenType.BINARYOPERATOR);
        TOKEN_CHARS.put("%", TokenType.BINARYOPERATOR);
        TOKEN_CHARS.put("<", TokenType.LESSER);
        TOKEN_CHARS.put(".", TokenType.DOT);
        TOKEN_CHARS.put(":", TokenType.COLON);
        TOKEN_CHARS.put(";", TokenType.SEMICOLON);
        TOKEN_CHARS.put(",", TokenType.COMMA);
        TOKEN_CHARS.put("\"", TokenType.QUOTATION);
        TOKEN_CHARS.put("\n", TokenType.NEWLINE);
    }
    public static TokenType getTokenType(String chr) {
        return TOKEN_CHARS.get(chr);
    }
}