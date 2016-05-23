import java.io.BufferedReader;

/**
 * Created by yonilip on 5/23/16.
 *
 *
 * Removes all comments and white space from the input stream and
 * breaks it into Jack-language tokens, as specified by the Jack grammar
 */
public class JackTokenizer {


    public enum LexicalElements {KEYWORD, SYMBOL, IDENTIFIER, INT_CONST, STRING_CONST}

    public enum Keyword {CLASS, METHOD, FUNCTION,CONSTRUCTOR, INT, BOOLEAN, CHAR, VOID, VAR, STATIC, FIELD, LET,
        DO ,IF, ELSE, WHILE, RETURN, TRUE, FALSE, NULL, THIS}


    private BufferedReader file;


    /**
     * Opens the input file/stream and gets
     * ready to tokenize it.
     * @param file
     */
    public JackTokenizer(BufferedReader file) {
        this.file = file;
    }


    /**
     * Do we have more tokens in the input?
     * @return
     */
    public boolean hasMoreTokens() {

    }

    /**
     * Gets the next token from the input and makes it the current token. This
     * method should only be called if hasMoreTokens() is true. Initially
     * there is no current token.
     */
    public void advance() {

    }

    /**
     * Returns the type of the current token
     * @return
     */
    public LexicalElements tokenType() {

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

    }







}
