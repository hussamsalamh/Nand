import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Created by yonilip on 5/23/16.
 *
 *
 * recursive top-down parser.
 * drives the parsing process in this project
 *
 * Effects the actual compilation output. Gets its input from a
 * JackTokenizer and emits its parsed structure into an output file/stream. The
 * output is generated by a series of compilexxx() routines, one for every syntactic
 * element xxx of the Jack grammar. The contract between these routines is that each
 * compilexxx() routine should read the syntactic construct xxx from the input,
 * advance() the tokenizer exactly beyond xxx , and output the parsing of xxx . Thus,
 * compilexxx() may only be called if indeed xxx is the next syntactic element of the input.
 * In the first version of the compiler, described in chapter 10, this module emits a
 * structured printout of the code, wrapped in XML tags. In the final version of the
 * compiler, described in chapter 11, this module generates executable VM code. In
 * both cases, the parsing logic and module API are exactly the same.
 *
 */
public class CompilationEngine {


    /**
     * Creates a new compilation engine with the given input and output. The next routine called
     * must be compileClass() .
     * @param inputFile
     * @param outputFile
     */
    public CompilationEngine(BufferedReader inputFile, BufferedWriter outputFile) {
        //TODO add <tokens> to start of file and make sure file ends with </tokens>
    }

    /**
     * Compiles a complete class.
     */
    public void compileClass() {

    }

    /**
     * Compiles a static declaration or a field declaration.
     */
    public void compileClassVarDec() {

    }

    /**
     * Compiles a complete method, function, or constructor.
     */
    public void compileSubroutine() {

    }

    /**
     * Compiles a (possibly empty) parameter list, not including the enclosing ‘‘ () ’’.
     */
    public void compileParameterList() {

    }

    /**
     * Compiles a var declaration.
     */
    public void compileVarDec() {

    }

    /**
     * Compiles a sequence of statements, not including the enclosing ‘‘{}’’.
     */
    public void compileStatements() {

    }

    /**
     * Compiles a do declaration.
     */
    public void compileDo() {

    }

    /**
     * Compiles a let declaration.
     */
    public void compileLet() {

    }

    /**
     * Compiles a while declaration.
     */
    public void compileWhile() {

    }

    /**
     * Compiles a return declaration.
     */
    public void compileReturn() {

    }

    /**
     * Compiles an if statement, possibly with a trailing else clause.
     */
    public void compileIf() {

    }

    /**
     * Compiles an expression.
     */
    public void compileExpression() {

    }

    /**
     * Compiles a term. This routine is faced with a slight difficulty when trying to decide between
     * some of the alternative parsing rules. Specifically, if the current token is an identifier, the routine
     * must distinguish between a variable, an array entry, and a subroutine call.
     * A single lookahead token, which may be one of ‘‘[’’, ‘‘(’’, or ‘‘.’’ suffices to
     * distinguish between the three possibilities. Any other token is not part of this term and should not
     * be advanced over.
     */
    public void compileTerm() {

    }

    /**
     * Compiles a (possibly empty) comma-separated list of expressions.
     */
    public void compileExpressionList() {

    }

}