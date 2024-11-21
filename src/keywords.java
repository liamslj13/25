import java.util.HashMap;
import java.util.Map;

class keywords {
    private static final Map<String, TokenType> KEYWORDS = new HashMap<>();
    static {
        KEYWORDS.put("if", TokenType.IF);
        KEYWORDS.put("elif", TokenType.ELIF);
        KEYWORDS.put("else", TokenType.ELSE);
        KEYWORDS.put("for", TokenType.FOR);
        KEYWORDS.put("while", TokenType.WHILE);
        KEYWORDS.put("break", TokenType.BREAK);
        KEYWORDS.put("func", TokenType.FUNC);
        KEYWORDS.put("not", TokenType.NOT);
        KEYWORDS.put("try", TokenType.TRY);
        KEYWORDS.put("catch", TokenType.CATCH);
        KEYWORDS.put("null", TokenType.NULL);
    }
    public static TokenType getTokenType(String keyword) {
        return KEYWORDS.get(keyword);
    }
}
