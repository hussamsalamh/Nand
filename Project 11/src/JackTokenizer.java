import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
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


    public enum LexicalElements {KEYWORD, SYMBOL, IDENTIFIER, integerConstant, stringConstant}

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


    protected static final HashSet<Character> symbolSet = new HashSet<Character>() {{
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
    private int formerToken;
    private String formerSval;
    private LexicalElements formerTokenType;
    private LexicalElements curTokenType;
    private String stringValue;
    private String formerToString;
    private boolean peeked;

    /**
     * Opens the input file/stream and gets
     * ready to tokenize it.
     * @param file
     */
    public JackTokenizer(BufferedReader file)
    {
        streamTokenizer = new StreamTokenizer(file);
        streamTokenizer.quoteChar('\"'); //TODO make sure that dont need to catch single quote
        streamTokenizer.ordinaryChar('-');
        streamTokenizer.ordinaryChar('.');
        streamTokenizer.ordinaryChar('/');
        streamTokenizer.wordChars('_', '_');
        streamTokenizer.ordinaryChar(':');
        streamTokenizer.wordChars(':', ':');
        streamTokenizer.slashSlashComments(true);
        streamTokenizer.slashStarComments(true);
    }



    /**
     * Do we have more tokens in the input?
     * @return
     */
    public boolean advance() throws IOException {
        if (!peeked)
        {
            currentToken = streamTokenizer.nextToken();
            return currentToken != StreamTokenizer.TT_EOF;
        }
        else
        {
            peeked = false;
            return true;
        }
    }

    public void peek() throws IOException
    {
        peeked = true;
        formerToken = currentToken;
        formerTokenType = curTokenType;
        if (curTokenType == LexicalElements.KEYWORD)
        {
            formerSval = streamTokenizer.sval.toLowerCase();
        }
        else
        {
            formerSval = streamTokenizer.sval;
        }
        formerToString = streamTokenizer.toString();
        currentToken = streamTokenizer.nextToken();
    }

    /**
     * Returns the type of the current token
     * @return
     */
    public LexicalElements tokenType() throws IOException {
        int usedToken;
        String usedSval;
        String usedToString;
        if (peeked)
        {
            usedToken = formerToken;
            usedSval = formerSval;
            usedToString = formerToString;
        }
        else
        {
            usedToken = currentToken;
            usedSval = streamTokenizer.sval;
            usedToString = streamTokenizer.toString();
        }
        if (usedToken == StreamTokenizer.TT_WORD) {
            // use streamTokenizer.sval
            //if (keywordsSet.containsKey(streamTokenizer.sval.toLowerCase())) {
            if (keywordsSet.contains(usedSval.toLowerCase())) {
                return LexicalElements.KEYWORD;
            }
            else return LexicalElements.IDENTIFIER;
        }
        else if (usedToken == StreamTokenizer.TT_NUMBER) {
            return LexicalElements.integerConstant;
        }
        else if (symbolSet.contains((char)usedToken)) {
            return LexicalElements.SYMBOL;
        }
        else if (usedToken == '\"') {
            //String originalString = streamTokenizer.toString();
            stringValue = usedToString.toString();
            int findEnd = 0;
            while (stringValue.indexOf("]", findEnd+1) != -1)
            {
                findEnd = stringValue.indexOf("]", findEnd+1);
            }
            stringValue = stringValue.substring(6, findEnd);
            //advance();
            //   while (currentToken != '\"') {
            //       advance();
            //       stringValue += (char)currentToken;
            //   }
            stringValue = stringValue.replace("\t", "\\t");
            stringValue = stringValue.replace("&", "&amp;");
            stringValue = stringValue.replace("<", "&lt;");
            stringValue = stringValue.replace(">", "&gt;");
            stringValue = stringValue.replace("\"", "&quot;");
            stringValue = stringValue.replace("\n", "\\n");

            return LexicalElements.stringConstant;
        }
        return null; // case shouldnt happen
    }

    /**
     * Returns the keyword which is the current token. Should be called only when tokenType() is KEYWORD .
     * @return
     */
    public String keyWord() {
        //return keywordsSet.get(streamTokenizer.sval.toLowerCase());
        if (peeked)
        {
            return formerSval;
        }
        return streamTokenizer.sval.toLowerCase();
    }

    public boolean isPeekedDot()
    {
        if (symbolSet.contains((char)currentToken) && (char) currentToken == '.')
        {
            System.out.println("hit here");
            return true;
        }
        return false;
    }

    /**
     * Returns the character which is the current token. Should be called only when tokenType() is SYMBOL .
     * @return
     */
    public char symbol()
    {
        if (peeked)
        {
            return (char) formerToken;
        }
        return (char) currentToken;
    }

    /**
     * Returns the identifier which is the current token. Should be called only when tokenType() is IDENTIFIER .
     * @return
     */
    public String identifier()
    {
        if (peeked)
        {
            return formerSval;
        }
        else
        {
            return streamTokenizer.sval;
        }
    }

    /**
     * Returns the integer value of the current token. Should be called only when tokenType() is integerConstant .
     * @return
     */
    public int intVal() {
        return (int) streamTokenizer.nval;
    }

    /**
     * Returns the string value of the current token, without the double quotes. Should be called only when
     * tokenType() is stringConstant .
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
            while (a.advance()) {
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
                    case integerConstant:
                        System.out.println("INT: " + a.intVal() );
                        break;
                    case stringConstant:
                        System.out.println("STRING: " + a.stringVal());
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
