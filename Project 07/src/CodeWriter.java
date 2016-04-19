import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by Era on 19/04/2016.
 */
public class CodeWriter
{
    static final Hashtable<String, String> binaryTable = new Hashtable<String,String>() {{
        // Put predefined binary translations here
        // a = 0
        put("add", "+");
        put("sub", "-");
        put("eq", "XXXX");
        put("gt", "XXXX");
        put("lt", "XXXX");
        put("and", "&");
        put("or", "|");
    }};

    private BufferedWriter outputFile;
    /**
     * Writes into the output file
     * @param outputFile
     */
    public CodeWriter(BufferedWriter outputFile)
    {
        this.outputFile = outputFile;
    }

    /**
     * Informs the code writer that the translation of
     * a new VM file is started.
     * @param fileName
     */
    public void setFileName(String fileName)
    {

    }

    /**
     * Writes the assembly code that is the
     * translation of the given arithmetic command.
     * @param command
     */
    public void writeArithmetic(String command) throws IOException {
        String[] commands = command.split(Parser.COMMAND_DELIMITER);
        if (binaryTable.contains(commands[0]))
        {

              outputFile.write("@SP;");
              outputFile.write("A=M");
              outputFile.write("D=M");
              outputFile.write("@SP");
              outputFile.write("M=M-1");
              outputFile.write("A=M");
              outputFile.write("D=M" + binaryTable.get(commands[0]) + "D");
              outputFile.write("@SP");
              outputFile.write("A=M");
              outputFile.write("M=D");
        }
    }

    /**
     * Writes the assembly code that is the
     * translation of the given command, where
     * command is either C_PUSH or C_POP.
     * @param command
     * @param segment
     * @param index
     */
    public void WritePushPop(Parser.CommandType command, String segment, int index)
    {

    }

    /**
     * Closes the output file.
     */
    public void close()
    {

    }

}
