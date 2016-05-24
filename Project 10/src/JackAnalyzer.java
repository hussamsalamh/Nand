import java.io.IOException;
import java.io.StreamTokenizer;
//just so I get it
/**
 * Created by yonilip on 5/23/16.
 *
 *
 * top-level driver that sets up and invokes the other modules;
 *
 * The analyzer program operates on a given source, where source is either a file name
 * of the form Xxx.jack or a directory name containing one or more such files. For
 * each source Xxx.jack file, the analyzer goes through the following logic:
 * 1. Create a JackTokenizer from the Xxx.jack input file.
 * 2. Create an output file called Xxx.xml and prepare it for writing.
 * 3. Use the CompilationEngine to compile the input JackTokenizer into the output file.
 *
 *
 *
 * Notes from submission: TODO make sure this is accounted for
 * The ' " ' sign is never translated.
 * The command is: JackAnalyzer source. Where source is either an .jack file or a directory where the *.jack file are. All .xml files should be inside this (where the .jack files are) directory.
 * A code is legal only if after removing the comment and replacing it with one white space it is a legal code.
 * The submitted project should not create the T.xml files.
 * We will use diff -w to compare your files.
 * Escaped characters should not be interpret. '\t' is translated to '\t'.
 * Empty tags should be translated: "<xxx> \n </xxx>" where \n is a new line.
 * You can assume the files are a legal jack files ( you do not need to handle illegal files).
 */
public class JackAnalyzer {
/*
    public static void main(String[] args) throws IOException {
        JackTokenizer a = new JackTokenizer();
        while (a.hasMoreTokens()) {
            if (a.currentToken == StreamTokenizer.TT_WORD) {
                System.out.println(a.streamTokenizer.sval);
            }
            else if (a.currentToken == StreamTokenizer.TT_NUMBER) System.out.println(a.streamTokenizer.nval);
            else {
                System.out.println((char) a.currentToken);
            }
        }
    }


*/



}
