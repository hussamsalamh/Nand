import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by Era on 19/04/2016.
 */
public class CodeWriter
{
    // Counts the number of "conditional arithmetic flows" in order to number the labels properly
    private int flowCounter = 0;
    // The base of the temp segment is always 5
    private final int tempBase = 5;
    // The base of the pointer segment is always 3
    private final int pointerBase = 3;
    // The name of the current vm being processed, used to give names to static variables
    private String vmName;

    // Translates to the proper HACK commands
    static final Hashtable<String, String> conditionalArithmetic = new Hashtable<String,String>() {{
        // Put predefined HACK translations here
        put("eq", "JEQ");
        put("gt", "JGT");
        put("lt", "JLT");
    }};

    static final Hashtable<String, String> binaryTable = new Hashtable<String,String>() {{
        // Put predefined HACK translations here
        put("add", "+");
        put("sub", "-");
        put("and", "&");
        put("or", "|");
    }};
    static final Hashtable<String, String> unaryTable = new Hashtable<String,String>() {{
        // Put predefined HACK translations here
        put("not", "!");
        put("neg", "-");
    }};
    static final Hashtable<String, String> segments = new Hashtable<String,String>() {{
        // Put predefined HACK translations here
        // a = 0
        put("local", "LCL");
        put("that", "THAT");
        put("argument", "ARG");
        put("this", "THIS");
    }};

    // The buffered writer used to write the file to
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
     * Writes HACK commands to the buffer which pop the top of the stack (Y) to D
     * @throws IOException
     */
    private void popToD() throws IOException
    {
        outputFile.write("@SP\n");
        outputFile.write("M=M-1\n");
        outputFile.write("A=M\n");
        outputFile.write("D=M\n");
    }

    /**
     * Writes HACK commands to the buffer which pop the top of the stack (X) to A
     * @throws IOException
     */
    private void popToA() throws IOException
    {
        outputFile.write("@SP\n");
        outputFile.write("M=M-1\n");
        outputFile.write("A=M\n");
    }

    /**
     * Writes HACK commands to the buffer which push D to the top of the stack.
     * @throws IOException
     */
    private void pushD() throws IOException
    {
        outputFile.write("@SP\n");
        outputFile.write("A=M\n");
        outputFile.write("M=D\n");
    }

    /**
     * Writes HACK commands to the buffer which set the value of D to index
     * @param index - the value to set
     * @throws IOException
     */
    private void setDtoIndex(int index) throws IOException
    {
        outputFile.write("@" + index + "\n");
        outputFile.write("D=A\n");
    }
    /**
     * Writes HACK commands to the buffer which increment the SP
     * @throws IOException
     */
    private void incrementSP() throws IOException
    {
        outputFile.write("@SP\n");
        outputFile.write("M=M+1\n");
    }
    /**
     * Writes the assembly code that is the
     * translation of the given arithmetic command.
     * @param command
     */
    public void writeArithmetic(String command, Boolean... params) throws IOException {
        boolean safe = params.length > 0 ? params[0].booleanValue() : false;
        // Splits the command line according to the spaces (the command is already parsed to have single spaces only)
        String[] commands = command.split(Parser.COMMAND_DELIMITER);
        // Handle case where the command is binary
        if (binaryTable.containsKey(commands[0]))
        {
              // Load y to D (pop)
                popToD();

              // Load x to A (pop)
                popToA();

              // D Equals x (binary command) y
              outputFile.write("D=M" + binaryTable.get(commands[0]) + "D\n");

              // Set the top of the stack to D
              pushD();

              // Increment SP to point to next empty slot
                incrementSP();
        }
        // Handle case where the command is binary, but is conditional
        else if(conditionalArithmetic.containsKey(commands[0]))
        {
            if (!safe)
            {
            // Load y to D (pop)
            popToD();
            outputFile.write("@R13\n");
            outputFile.write("M=D\n");
            pushD();
            incrementSP();
            WritePushPop(Parser.CommandType.C_PUSH, "constant", 0);
            outputFile.write("@inboundsFlow" + flowCounter + "\n");
            outputFile.write("0;JMP\n");
            writeArithmetic("gt", true);
            popToD();
            outputFile.write("@R14\n");
            outputFile.write("M=D\n");

            // Load x to D (pop)
            popToD();
            outputFile.write("@R15\n");
            outputFile.write("M=D\n");
            pushD();
            incrementSP();
            WritePushPop(Parser.CommandType.C_PUSH, "constant", 0);
            outputFile.write("@inboundsFlow" + flowCounter + "\n");
            outputFile.write("0;JMP\n");
            writeArithmetic("gt", true);
            popToD();
            outputFile.write("@R16\n");
            outputFile.write("M=D\n");

            // Check difference
            outputFile.write("@R14\n");
            outputFile.write("D=M\n");
            pushD();
            incrementSP();
            outputFile.write("@R16\n");
            outputFile.write("D=M\n");
            pushD();
            incrementSP();

            // Difference is either -1 or 0
            writeArithmetic("sub");

            // Check if difference equal to 0
            popToD();
            outputFile.write("@inboundsFlow" + flowCounter + "\n");
            outputFile.write("D;JEQ\n");
            outputFile.write("@R15\n");
            outputFile.write("D=M\n");
            outputFile.write("@" + "condJump" + flowCounter+"\n");
            if (commands[0].equals("gt"))
            {
                outputFile.write("D;JGT\n");
            }
            else
            {
                outputFile.write("D;JLT\n");
            }
            // If condition is not met, the flow will continue here:
            // Set top of stack to be 0, and increment SP
            outputFile.write("@SP\n");
            outputFile.write("A=M\n");
            outputFile.write("M=0\n");
            incrementSP();

            // Jump to end of flow
            outputFile.write("@endJump" + flowCounter+"\n");
            outputFile.write("0;JMP\n");


            }
            outputFile.write("(inboundsFlow" + flowCounter + ")\n");
            if (!safe)
            {
                outputFile.write("@R15\n");
                outputFile.write("D=M\n");
                pushD();
                incrementSP();
                outputFile.write("@R13\n");
                outputFile.write("D=M\n");
                pushD();
                incrementSP();
            }
            // Load y to D (pop)
            popToD();


            // Load x to A (pop)
            popToA();

            // Set D equal to x-y
            outputFile.write("D=M-D\n");

            // Create the flow with a conditional JMP in HACK
            outputFile.write("@" + "condJump" + flowCounter+"\n");

            // Jump if relevant condition is met (translate condition to HACK)
            outputFile.write("D;" + conditionalArithmetic.get(commands[0])+"\n");

            // If condition is not met, the flow will continue here:
            // Set top of stack to be 0, and increment SP
            outputFile.write("@SP\n");
            outputFile.write("A=M\n");
            outputFile.write("M=0\n");
            incrementSP();

            // Jump to end of flow
            outputFile.write("@endJump" + flowCounter+"\n");
            outputFile.write("0;JMP\n");

            // If the condition is met, write -1 to top of stack and increment SP
            outputFile.write("(condJump" + flowCounter + ")\n");
            outputFile.write("  @SP\n");
            outputFile.write("  A=M\n");
            outputFile.write("  M=-1\n");
            incrementSP();
            // After fulfilling condition and changing stack, go to end of flow
            outputFile.write("  @endJump" + flowCounter + "\n");
            outputFile.write("  0;JMP\n");

            // End of flow
            outputFile.write("(endJump" + flowCounter + ")\n");
            flowCounter += 1;
        }
        else{
            // If unary command, load x to A
            popToA();
            // Set top of stack to be (unaryOperation) x
            outputFile.write("M=" + unaryTable.get(commands[0]) + "M\n");
            // Increment SP
            incrementSP();
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
        // Handle PUSH commands
        if (command == Parser.CommandType.C_PUSH )
        {
            if (segment.equals("constant"))
            {
                if (index >= 0)
                {
                    // Load index to A and set D=A
                    setDtoIndex(index);
                    // Dereference pointer and set its value to D
                    pushD();
                    // Increment SP
                    incrementSP();
                }
            }
            // Handle case where segment is LOCAL,ARGUMENT,THIS,THAT
            else if (segments.containsKey(segment))
            {
                // Set D to value of index
                setDtoIndex(index);
                // Set A to base location
                outputFile.write("@" + segments.get(segment) + "\n");
                // Set D to equal base location pointed by segment + index
                outputFile.write("D=D+M\n");

                // Make A point to the base location + index, set D to equal value at that location
                outputFile.write("A=D\n");
                outputFile.write("D=M\n");

                // Set SP to value of D and increment SP
                pushD();
                incrementSP();
            }
            else if (segment.equals("temp"))
            {
                // Set value of D to equal index
                setDtoIndex(index);

                // Set A to value of the base location of temp.
                outputFile.write("@" + tempBase + "\n");

                // Set D to equal base location + index
                outputFile.write("D=D+A\n");

                // Set D to equal value at that location (pointed to by D)
                outputFile.write("A=D\n");
                outputFile.write("D=M\n");

                // Set top of stack to equal value of D
                pushD();

                // Increment SP
                incrementSP();
            }
            else if (segment.equals("pointer"))
            {
                // Set index pointed to
                int pointerIndex = pointerBase + index;

                // Set D to value at pointerBase + index
                outputFile.write("@R" + pointerIndex + "\n");
                outputFile.write("D=M\n");

                // Push D and increment SP
                pushD();
                incrementSP();
            }
            else if (segment.equals("static"))
            {
                // Set value to be the name of the vm being processed + the desired index
                String staticVarName = vmName + "." + index;

                // Set D to the desired variable value
                outputFile.write("@" + staticVarName + "\n");
                outputFile.write("D=M\n");

                // Push D and increment SP
                pushD();
                incrementSP();
            }
        }
        else
        {
            // Handle case where segment is LOCAL,ARGUMENT,THIS,THAT
            if (segments.containsKey(segment))
            {
                // Pop D, and write to temporary (i.e for general usage) (we use R13)
                popToD();
                outputFile.write("@R13\n");
                outputFile.write("M=D\n");

                // Set D to value of index, calculate index + dereference pointer of LCL/ARGUMENT/THIS/THAT
                setDtoIndex(index);
                outputFile.write("@" + segments.get(segment) + "\n");
                // Set D to value of calculated RAM location, store in R14
                outputFile.write("D=D+M\n");
                outputFile.write("@R14\n");
                outputFile.write("M=D\n");

                // Load R13 to D (value of popped item), set R14 to D
                outputFile.write("@R13\n");
                outputFile.write("D=M\n");
                outputFile.write("@R14\n");
                outputFile.write("A=M\n");
                outputFile.write("M=D\n");
            }
            else if (segment.equals("temp"))
            {
                // Pop top of stack to D
                popToD();

                // Calculate index to access, set RAM[index]=D
                int tempIndex = tempBase + index;
                outputFile.write("@R" + tempIndex + "\n");
                outputFile.write("M=D\n");
            }
            else if (segment.equals("pointer"))
            {
                // Set pointerIndex to RAM location to access
                int pointerIndex = pointerBase + index;
                // Load top of stack to D
                popToD();

                // Set RAM location at pointerIndex to D
                outputFile.write("@R" + pointerIndex + "\n");
                outputFile.write("M=D\n");
            }
            else if (segment.equals("static"))
            {
                // Set staticVarName to equal the name of the vm being processed and the given index
                String staticVarName = vmName + "." + index;

                // Pop the stack
                popToD();

                // Set staticVarName to equal D (what was popped off the stack)
                outputFile.write("@" + staticVarName + "\n");
                outputFile.write("M=D\n");
            }
        }
    }

}
