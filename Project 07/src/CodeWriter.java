import javax.xml.ws.WebServiceProvider;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by Era on 19/04/2016.
 */
public class CodeWriter
{
    private int flowCounter = 0;
    private final int tempBase = 5;
    private String vmName;
    static final Hashtable<String, String> conditionalArithmetic = new Hashtable<String,String>() {{
        // Put predefined binary translations here
        put("eq", "JEQ");
        put("gt", "JGT");
        put("lt", "JLT");
    }};

    static final Hashtable<String, String> binaryTable = new Hashtable<String,String>() {{
        // Put predefined binary translations here
        // a = 0
        put("add", "+");
        put("sub", "-");
        put("and", "&");
        put("or", "|");
    }};
    static final Hashtable<String, String> unaryTable = new Hashtable<String,String>() {{
        // Put predefined binary translations here
        // a = 0
        put("not", "!");
        put("neg", "-");
    }};
    static final Hashtable<String, String> segments = new Hashtable<String,String>() {{
        // Put predefined binary translations here
        // a = 0
        put("local", "LCL");
        put("that", "THAT");
        put("argument", "ARG");
        put("this", "THIS");
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
        this.vmName = fileName;
    }

    /**
     * Writes the assembly code that is the
     * translation of the given arithmetic command.
     * @param command
     */
    public void writeArithmetic(String command) throws IOException {
        String[] commands = command.split(Parser.COMMAND_DELIMITER);
        if (binaryTable.containsKey(commands[0]))
        {
              outputFile.write("@SP\n");
              outputFile.write("M=M-1\n");
              outputFile.write("A=M\n");
              outputFile.write("D=M\n");
              outputFile.write("@SP\n");
              outputFile.write("M=M-1\n");
              outputFile.write("A=M\n");
              outputFile.write("D=M" + binaryTable.get(commands[0]) + "D\n");
              outputFile.write("@SP\n");
              outputFile.write("A=M\n");
              outputFile.write("M=D\n");
              outputFile.write("@SP\n");
              outputFile.write("M=M+1\n");
        }
        else if(conditionalArithmetic.containsKey(commands[0]))
        {
            outputFile.write("@SP\n");
            outputFile.write("M=M-1\n");
            outputFile.write("A=M\n");
            outputFile.write("D=M\n");
            outputFile.write("@SP\n");
            outputFile.write("M=M-1\n");
            outputFile.write("A=M\n");
            outputFile.write("D=M-D\n");
            outputFile.write("@" + "condJump" + flowCounter+"\n");
            outputFile.write("D;" + conditionalArithmetic.get(commands[0])+"\n");
            outputFile.write("@SP\n");
            outputFile.write("A=M\n");
            outputFile.write("M=0\n");
            outputFile.write("  @SP\n");
            outputFile.write("  M=M+1\n");
            outputFile.write("@endJump" + flowCounter+"\n");
            outputFile.write("0;JMP\n");
            outputFile.write("(condJump" + flowCounter + ")\n");
            outputFile.write("  @SP\n");
            outputFile.write("  A=M\n");
            outputFile.write("  M=-1\n");
            outputFile.write("  @SP\n");
            outputFile.write("  M=M+1\n");
            outputFile.write("  @endJump" + flowCounter + "\n");
            outputFile.write("  0;JMP\n");
            outputFile.write("(endJump" + flowCounter + ")\n");
            flowCounter += 1;
        }
        else{
            outputFile.write("@SP\n");
            outputFile.write("M=M-1\n");
            outputFile.write("A=M\n");
            outputFile.write("M=" + unaryTable.get(commands[0]) + "M\n");
            outputFile.write("@SP\n");
            outputFile.write("M=M+1\n");
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
    public void WritePushPop(Parser.CommandType command, String segment, int index) throws IOException {
        if (command == Parser.CommandType.C_PUSH )
        {
            if (segment.equals("constant"))
            {
                outputFile.write("@" + index + "\n");
                outputFile.write("D=A\n");
                outputFile.write("@SP\n");
                outputFile.write("A=M\n");
                outputFile.write("M=D\n");
                outputFile.write("@SP\n");
                outputFile.write("M=M+1\n");
            }
            else if (segments.containsKey(segment))
            {
                outputFile.write("@" + index + "\n");
                outputFile.write("D=A\n");
                outputFile.write("@" + segments.get(segment) + "\n");
                outputFile.write("D=D+M\n");
                outputFile.write("A=D\n");
                outputFile.write("D=M\n");
                outputFile.write("@SP\n");
                outputFile.write("A=M\n");
                outputFile.write("M=D\n");
                outputFile.write("@SP\n");
                outputFile.write("M=M+1\n");
            }
            else if (segment.equals("temp"))
            {
                outputFile.write("@" + index + "\n");
                outputFile.write("D=A\n");
                outputFile.write("@" + tempBase + "\n");
                outputFile.write("D=D+A\n");
                outputFile.write("A=D\n");
                outputFile.write("D=M\n");
                outputFile.write("@SP\n");
                outputFile.write("A=M\n");
                outputFile.write("M=D\n");
                outputFile.write("@SP\n");
                outputFile.write("M=M+1\n");
            }
            else if (segment.equals("pointer"))
            {
                int pointerIndex = 3 + index;
                outputFile.write("@R" + pointerIndex + "\n");
                outputFile.write("D=M\n");
                outputFile.write("@SP\n");
                outputFile.write("A=M\n");
                outputFile.write("M=D\n");
                outputFile.write("@SP\n");
                outputFile.write("M=M+1\n");
            }
            else if (segment.equals("static"))
            {
                String staticVarName = vmName + "." + index;
                outputFile.write("@" + staticVarName + "\n");
                outputFile.write("D=M\n");
                outputFile.write("@SP\n");
                outputFile.write("A=M\n");
                outputFile.write("M=D\n");
                outputFile.write("@SP\n");
                outputFile.write("M=M+1\n");
            }
        }
        else
        {
            if (segments.containsKey(segment))
            {
                outputFile.write("@SP\n");
                outputFile.write("M=M-1\n");
                outputFile.write("A=M\n");
                outputFile.write("D=M\n");
                outputFile.write("@R13\n");
                outputFile.write("M=D\n");
                outputFile.write("@" + index + "\n");
                outputFile.write("D=A\n");
                outputFile.write("@" + segments.get(segment) + "\n");
                outputFile.write("D=D+M\n");
                outputFile.write("@R14\n");
                outputFile.write("M=D\n");
                outputFile.write("@R13\n");
                outputFile.write("D=M\n");
                outputFile.write("@R14\n");
                outputFile.write("A=M\n");
                outputFile.write("M=D\n");
            }
            else if (segment.equals("temp"))
            {
                outputFile.write("@SP\n");
                outputFile.write("M=M-1\n");
                outputFile.write("A=M\n");
                outputFile.write("D=M\n");
                int tempIndex = tempBase + index;
                outputFile.write("@R" + tempIndex + "\n");
                outputFile.write("M=D\n");
            }
            else if (segment.equals("pointer"))
            {
                int pointerIndex = 3 + index;
                outputFile.write("@SP\n");
                outputFile.write("M=M-1\n");
                outputFile.write("A=M\n");
                outputFile.write("D=M\n");
                outputFile.write("@R" + pointerIndex + "\n");
                outputFile.write("M=D\n");
            }
            else if (segment.equals("static"))
            {
                String staticVarName = vmName + "." + index;
                outputFile.write("@SP\n");
                outputFile.write("M=M-1\n");
                outputFile.write("A=M\n");
                outputFile.write("D=M\n");
                outputFile.write("@" + staticVarName + "\n");
                outputFile.write("M=D\n");
            }
        }
    }

    /**
     * Closes the output file.
     */
    public void close()
    {

    }

}
