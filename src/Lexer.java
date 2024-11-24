import java.util.*;

class Lexer {
    //Determine if char is valid int
    public static boolean isInt(String str) {
        char c = str.charAt(0);
        int lowerBound = '0';
        int upperBound = '9';
        return (int) c >= lowerBound && (int) c <= upperBound;
    }
    //Determine if char is alphabetic
    public static boolean isAlpha(String str) {
        char c = str.charAt(0);
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }
    //Determine if a char is whitespace
    public static boolean isSkippable(String str) {
        return str.equals(" ") || str.equals("\n") || str.equals("\t") || str.equals("\r");
    }

    public List<Token> tokenize(String sourceCode)  {
        List<Token> tokens = new ArrayList<>();

        ArrayList<String> src = new ArrayList<>();

        //Split src into chars -> add into array
        for (char ch : sourceCode.toCharArray()) {
            src.add(String.valueOf(ch));
        }
        while (!src.isEmpty()) {
            //Get current char @index 0 from the array
            String current = src.get(0);
            TokenType type = TokenChars.getTokenType(current);
            //Check for int
            if (isInt(current)) {
                StringBuilder num = new StringBuilder();
                boolean isFloat = false;

                while (!src.isEmpty() && (isInt(src.get(0)) || src.get(0).equals("."))) {
                    if (src.get(0).equals(".")) {
                        if (isFloat) {
                            throw new RuntimeException("Invalid floating point number syntax");
                        }
                        isFloat = true;
                    }
                    num.append(src.get(0));
                    src.remove(0);
                }

                if (isFloat) {
                    tokens.add(new Token(TokenType.FLOAT, num.toString()));
                } else {
                    tokens.add(new Token(TokenType.INT, num.toString()));
                }
                continue;
            } else {
                switch (current) {
                    //Handle comments
                    //***THIS BRANCH IS EXTREMELY MESSY***
                    case ">":
                        src.remove(0);
                        if (!src.isEmpty() && src.get(0).equals(">")) {
                            src.remove(0);
                            //Check for multiline comment start
                            if (!src.isEmpty() && src.get(0).equals(">")) {
                                src.remove(0);
                                boolean endFound = false;
                                while (!src.isEmpty()) {
                                    if (src.get(0).equals("<")) {
                                        src.remove(0);
                                        if (!src.isEmpty() && src.get(0).equals("<")) {
                                            src.remove(0);
                                            if (!src.isEmpty() && src.get(0).equals("<")) {
                                                src.remove(0);
                                                endFound = true;
                                                break;
                                            }
                                        }
                                    } else {
                                        src.remove(0);
                                    }
                                }
                                if (!endFound) {
                                    //Handle error: unclosed multiline comment
                                    throw new RuntimeException("Multiline comment not closed.");
                                }
                            } else {
                                //Single-line comment
                                while (!src.isEmpty() && !src.get(0).equals("\n")) {
                                    src.remove(0);
                                }
                            }
                        } else {
                            tokens.add(new Token(TokenType.GREATER, current));
                        }
                        continue;
                        //Check for subtraction binary operator or ternary
                    case "-":
                        src.remove(0);
                        if (!src.get(0).equals(">")) {
                            tokens.add(new Token(TokenType.BINARYOPERATOR, "-"));
                        } else {
                            tokens.add(new Token(TokenType.ARROW, "->"));
                        }
                        continue;
                    case "=":
                        src.remove(0);
                        if (src.get(0).equals("=")) {
                            src.remove(0);
                            tokens.add(new Token(TokenType.EQUALSCOMPARE, "=="));
                        } else {
                            tokens.add(new Token(TokenType.EQUALS, "="));
                        }
                        continue;
                    case "!":
                        src.remove(0);
                        if (src.get(0).equals("=")) {
                            src.remove(0);
                            tokens.add(new Token(TokenType.NOTEQUALSCOMPARE, "!="));
                        } else {
                            tokens.add(new Token(TokenType.FACTORIAL, "!"));
                        }
                        continue;
                    case "|":
                        src.remove(0);
                        if (src.get(0).equals("|")) {
                            src.remove(0);
                            tokens.add(new Token(TokenType.OR, "||"));
                        } else {
                            tokens.add(new Token(TokenType.COMMA, "|"));
                        }
                        continue;
                    case "&":
                        src.remove(0);
                        if (src.get(0).equals("&")) {
                            src.remove(0);
                            tokens.add(new Token(TokenType.AND, "&&"));
                        } else {
                            tokens.add(new Token(TokenType.AMPERSAND, "&"));
                        }
                        continue;
                    default:
                        //Branch for individual character tokens
                        if (type != null) {
                            tokens.add(new Token(type, current));
                        }
                        //Check for whitespace tokens
                        else if (isSkippable(current)) {
                            src.remove(0);
                            continue;
                        } else if (isAlpha(current)) {
                            StringBuilder identifier = new StringBuilder();
                            while (!src.isEmpty() && isAlpha(src.get(0))) {
                                identifier.append(src.get(0));
                                src.remove(0);
                            }
                            //Check for some reserved keywords
                            String ident = identifier.toString().toLowerCase();
                            TokenType reserved = Keywords.getTokenType(ident);
                            if (reserved != null) {
                                tokens.add(new Token(reserved, ident));
                            } else {
                                tokens.add(new Token(TokenType.IDENTIFIER, ident));
                            }
                            continue;
                        } else {
                            System.err.println("Unrecognized character found in the source: " + src.get(0));
                            System.exit(1);
                        }
                        break;
                }
            }
            src.remove(0);
        }
        //Push EOF and return token array
        tokens.add(new Token(TokenType.EOF, "EndOfFile"));
        return tokens;
    }
}
