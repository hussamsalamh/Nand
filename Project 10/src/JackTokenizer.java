import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
// era fix for git
/**
 * Created by yonilip on 5/23/16.
 *
 *
 * Removes all comments and white space from the input stream and
 * breaks it into Jack-language tokens, as specified by the Jack grammar
 */
public class JackTokenizer {
    /* TODO make sure < is &lt, > is &gt, " is &quot, & is &amp

     */



    public enum LexicalElements {KEYWORD, SYMBOL, IDENTIFIER, INT_CONST, STRING_CONST}
    //TODO maybe make enums names lowercase

    public enum Keyword {CLASS, METHOD, FUNCTION,CONSTRUCTOR, INT, BOOLEAN, CHAR, VOID, VAR, STATIC, FIELD, LET,
        DO ,IF, ELSE, WHILE, RETURN, TRUE, FALSE, NULL, THIS}

    /*private static final HashMap<String, Keyword> keywordsSet = new HashMap<String, Keyword>() {{
        put("class", Keyword.CLASS);
        put("constructor", Keyword.CONSTRUCTOR);
        put("function", Keyword.FUNCTION);
        put("method", Keyword.METHOD);
        put("field", Keyword.FIELD);
        put("static", Keyword.STATIC);
        put("var", Keyword.VAR);
        put("int", Keyword.INT);
        put("char", Keyword.CHAR);
        put("boolean", Keyword.BOOLEAN);
        put("void", Keyword.VOID);
        put("true", Keyword.TRUE);
        put("false", Keyword.FALSE);
        put("null", Keyword.NULL);
        put("this", Keyword.THIS);
        put("let", Keyword.LET);
        put("do", Keyword.DO);
        put("if", Keyword.IF);
        put("else", Keyword.ELSE);
        put("while", Keyword.WHILE);
        put("return", Keyword.RETURN);
    }};
    */
    private static final HashSet<String> keywordsSet = new HashSet<String>() {{
        add("class");
        add("constructor");
        add("function");
        add("method");
        add("field");
        add("static");
        add("var");
        add("int");
        add("char");
        add("boolean");
        add("void");
        add("true");
        add("false");
        add("null");
        add("this");
        add("let");
        add("do");
        add("if");
        add("else");
        add("while");
        add("return");
    }};


    private static final HashSet<Character> symbolSet = new HashSet<Character>() {{
        add('{');
        add('}');
        add('(');
        add(')');
        add('[');
        add(']');
        add('.');
        add(',');
        add(';');
        add('+');
        add('-');
        add('*');
        add('/');
        add('&');
        add('|');
        add('<');
        add('>');
        add('=');
        add('~');
    }};


    private StreamTokenizer streamTokenizer;
    private int currentToken;
    private LexicalElements curTokenType;
    private String stringValue;


    /**
     * Opens the input file/stream and gets
     * ready to tokenize it.
     * @param file
     */
    public JackTokenizer(BufferedReader file) {
        streamTokenizer = new StreamTokenizer(file);
        streamTokenizer.quoteChar('\"'); //TODO make sure that dont need to catch single quote
    }



    /**
     * Do we have more tokens in the input?
     * @return
     */
    public boolean hasMoreTokens() throws IOException {
        currentToken = streamTokenizer.nextToken();
        return currentToken != StreamTokenizer.TT_EOF;
    }

    /**
     * Gets the next token from the input and makes it the current token. This
     * method should only be called if hasMoreTokens() is true. Initially
     * there is no current token.
     */
    public void advance() {
        //TODO is there a use for this here? we could use streamTokenizer.pushBack() for going a step back...
    }

    /**
     * Returns the type of the current token
     * @return
     */
    public LexicalElements tokenType() throws IOException {
        if (currentToken == StreamTokenizer.TT_WORD) {
            // use streamTokenizer.sval
            //if (keywordsSet.containsKey(streamTokenizer.sval.toLowerCase())) {
            if (keywordsSet.contains(streamTokenizer.sval.toLowerCase())) {
                return LexicalElements.KEYWORD;
            }
            else return LexicalElements.IDENTIFIER;
        }
        else if (currentToken == StreamTokenizer.TT_NUMBER) {
            return LexicalElements.INT_CONST;
        }
        else if (symbolSet.contains((char)currentToken)) {
            return LexicalElements.SYMBOL;
        }
        else if (currentToken == '\"') {
            stringValue = "";
            hasMoreTokens();
            while (currentToken != '\"') {
                hasMoreTokens();
                stringValue += (char)currentToken;
            }
            return LexicalElements.STRING_CONST;
        }
        return null; // case shouldnt happen
    }

    /**
     * Returns the keyword which is the current token. Should be called only when tokenType() is KEYWORD .
     * @return
     */
    public String keyWord() {
        //return keywordsSet.get(streamTokenizer.sval.toLowerCase());
        return streamTokenizer.sval.toLowerCase();
    }

    /**
     * Returns the character which is the current token. Should be called only when tokenType() is SYMBOL .
     * @return
     */
    public char symbol() {
        return (char) currentToken;
    }

    /**
     * Returns the identifier which is the current token. Should be called only when tokenType() is IDENTIFIER .
     * @return
     */
    public String identifier() {
        return streamTokenizer.sval;
    }

    /**
     * Returns the integer value of the current token. Should be called only when tokenType() is INT_CONST .
     * @return
     */
    public int intVal() {
        return (int) streamTokenizer.nval;
    }

    /**
     * Returns the string value of the current token, without the double quotes. Should be called only when
     * tokenType() is STRING_CONST .
     * @return
     */
    public String stringVal() {
        return stringValue;
    }

    /**
     * Test!
     * TODO dont submit this!
     * @param args
     */
    public static void main(String[] args) {
        String str = args[0];
        try(FileReader fr1 = new FileReader(str); BufferedReader r = new BufferedReader(fr1)) {

            JackTokenizer a = new JackTokenizer(r);
            while (a.hasMoreTokens()) {
                switch (a.tokenType()) {
                    case KEYWORD:
                        System.out.println("KEYWORD: " + a.keyWord());
                        break;
                    case SYMBOL:
                        System.out.println("SYMBOL: " + a.symbol());
                        break;
                    case IDENTIFIER:
                        System.out.println("IDENT: " + a.identifier());
                        break;
                    case INT_CONST:
                        System.out.println("INT: " + a.intVal() );
                        break;
                    case STRING_CONST:
                        System.out.println("STRING: " + a.stringVal());
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
