import java.io.BufferedReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.HashSet;

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

    public enum Keyword {CLASS, METHOD, FUNCTION,CONSTRUCTOR, INT, BOOLEAN, CHAR, VOID, VAR, STATIC, FIELD, LET,
        DO ,IF, ELSE, WHILE, RETURN, TRUE, FALSE, NULL, THIS}


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

    private static final HashSet<String> symbolSet = new HashSet<String>() {{
        add("{");
        add("}");
        add("(");
        add(")");
        add("[");
        add("]");
        add(".");
        add(",");
        add(";");
        add("+");
        add("-");
        add("*");
        add("/");
        add("&");
        add("|");
        add("<");
        add(">");
        add("=");
        add("~");
    }};

    private BufferedReader file;
    private StreamTokenizer streamTokenizer;
    private int currentToken;
    private LexicalElements curTokenType;


    /**
     * Opens the input file/stream and gets
     * ready to tokenize it.
     * @param file
     */
    public JackTokenizer(BufferedReader file) {
        this.file = file;
        streamTokenizer = new StreamTokenizer(file);
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
    public LexicalElements tokenType() {
        if (currentToken == StreamTokenizer.TT_WORD) {
            // use streamTokenizer.sval
            if (keywordsSet.contains(streamTokenizer.sval)) {
                return LexicalElements.KEYWORD;
            }
        }
        else if (currentToken == StreamTokenizer.TT_NUMBER) {
            return LexicalElements.INT_CONST;
        }
        else if (symbolSet.contains(currentToken)) {
            return LexicalElements.SYMBOL;
        }
    }

    /**
     * Returns the keyword which is the current token. Should be called only when tokenType() is KEYWORD .
     * @return
     */
    public Keyword keyWord() {

    }

    /**
     * Returns the character which is the current token. Should be called only when tokenType() is SYMBOL .
     * @return
     */
    public char symbol() {

    }

    /**
     * Returns the identifier which is the current token. Should be called only when tokenType() is IDENTIFIER .
     * @return
     */
    public String identifier() {

    }

    /**
     * Returns the integer value of the current token. Should be called only when tokenType() is INT_CONST .
     * @return
     */
    public int intVal() {

    }

    /**
     * Returns the string value of the current token, without the double quotes. Should be called only when
     * tokenType() is STRING_CONST .
     * @return
     */
    public String stringVal() {
        //TODO dont forget to make sure throws away double quote chars
    }







}
