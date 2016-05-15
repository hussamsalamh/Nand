import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by yonilip on 5/13/16.
 */
public class CodeWriter {
    // Counts the number of "conditional arithmetic flows" in order to number the labels properly
    private int flowCounter = 0;
    // The base of the temp segment is always 5
    private final static int tempBase = 5;
    // The base of the pointer segment is always 3
    private final static int pointerBase = 3;
    // The name of the current vm being processed, used to give names to static variables
    private String vmName;

    // The buffered writer used to write the file to
    private BufferedWriter outputFile;

    /**
     * counter for giving labels different unique names
     */
    private int funcLabelCounter = 0;

    private String curFunc = null;


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


    /**
     * Writes into the output file
     * @param outputFile
     */
    public CodeWriter(BufferedWriter outputFile) throws IOException {
        this.outputFile = outputFile;
        writeInit();
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
        // Checks whether this function is being called from within the function
        // (used for the sign difference check)
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
                // Write y to R13
                outputFile.write("@R13\n");
                outputFile.write("M=D\n");
                // Push y back to D
                pushD();
                incrementSP();

                // Check whether y is bigger than 0
                WritePushPop(Parser.CommandType.C_PUSH, "constant", 0);
                outputFile.write("@inboundsFlow" + flowCounter + "\n");
                outputFile.write("0;JMP\n");
                writeArithmetic("gt", true);
                popToD();
                // Writer (-1) to R14 if bigger than 0, else 0.
                outputFile.write("@R14\n");
                outputFile.write("M=D\n");

                // Load x to D (pop)
                popToD();
                // Write x to R15
                outputFile.write("@R15\n");
                outputFile.write("M=D\n");
                // Write x back to top of stack
                pushD();
                incrementSP();

                // Check whether x is bigger than 0
                WritePushPop(Parser.CommandType.C_PUSH, "constant", 0);
                outputFile.write("@inboundsFlow" + flowCounter + "\n");
                outputFile.write("0;JMP\n");
                writeArithmetic("gt", true);
                popToD();

                // Write -1 to R16 if x is bigger than 0, else 0
                outputFile.write("@R16\n");
                outputFile.write("M=D\n");

                // Check difference between R14 and R16 to check sign difference
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

                // Check if difference equal to 0, if so proceed as usual (since no overflow can happen)
                popToD();
                outputFile.write("@inboundsFlow" + flowCounter + "\n");
                outputFile.write("D;JEQ\n");
                // If the sign is different, load R15 (value of X)
                outputFile.write("@R15\n");
                outputFile.write("D=M\n");
                outputFile.write("@" + "condJump" + flowCounter+"\n");
                // Write the correct condition according to gt or lt
                if (commands[0].equals("gt"))
                {
                    outputFile.write("D;JGT\n");
                }
                else if (commands[0].equals("lt"))
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

    /**
     * @return the current func name with $ sign if not null
     */
    private String getCurFunc() {
        return curFunc != null ? curFunc + "$" : "";
    }

    /**
     * writes the assembly code that effects the VM initialization, also called
     * "bootstrap code". This code must be placed at the beginning of the output file.
     * called from the constructor
     */
    public void writeInit() throws IOException {
        //Init Sp to 256
        outputFile.write("@256\n");
        outputFile.write("D=A\n");
        outputFile.write("@SP\n");
        outputFile.write("M=D\n");

        //init all other pointers to -1
/*        outputFile.write("@LCL\n");
        outputFile.write("M=-1\n");
        outputFile.write("@ARG\n");
        outputFile.write("M=-1\n");
        outputFile.write("@THIS\n");
        outputFile.write("M=-1\n");
        outputFile.write("@THAT\n");
        outputFile.write("M=-1\n");*/

        //init curFunc
        writeCall("Sys.init", 0);
    }

    /**
     * writes the assembly code that is the translation of the LABEL command
     * @param label
     */
    public void writeLabel(String label) throws IOException {
        outputFile.write("(" + getCurFunc() + label + ")\n");
    }

    /**
     * writes the assembly code that is the translation of the GOTO command
     * @param label
     */
    public void writeGoto(String label) throws IOException {
        outputFile.write("@" + getCurFunc() + label + "\n");
        outputFile.write("0;JMP" + "\n");
    }

    /**
     * writes the assembly code that is the translation of the IF-GOTO command
     * @param label
     */
    public void writeIf(String label) throws IOException {
        /*
        stacks topmost val is popped. if the val isnt 0 (eq?), exec cont from the location marked by the label,
        else, exec cont from the next cmd in the prog. jmp dest must be in the same func
         */
        popToD();
        outputFile.write("@" + getCurFunc() + label + "\n");
        outputFile.write("D;JNE\n");
    }

    /**
     * writes the assembly code that is the translation of the CALL command
     * @param funcName
     * @param numArgs
     */
    public void writeCall(String funcName, int numArgs) throws IOException {
    /*
     * call f n
     * (Calling a function f after n args have been pushed to the stack)
     *        //curFunc = "Sys.init";
     * push return-address // using the label declared below
     * push LCL // save LCL of the calling func
     * push ARG // save ARG of the calling func
     * push THIS // save THIS of the calling func
     * push THAT // save THAT of the calling func
     * ARG = SP - n - 5 //Reposition ARG (n=num of args)
     * LCL = SP // Reposition LCL
     * goto f // transfer control
     * (return-address) // declare a label fo the return-address
     *
     */
        //assign D with the item to push, then push and move one
        //push return-address
        String returnAddress = "RETURN_" + funcName + Integer.toString(funcLabelCounter);
        ++funcLabelCounter;
        outputFile.write("@" + returnAddress + "\n");
        outputFile.write("D=A\n");
        pushD();
        incrementSP();

        //push LCL
        outputFile.write("@LCL\n");
        outputFile.write("D=M\n");
        pushD();
        incrementSP();

        //push ARG
        outputFile.write("@ARG\n");
        outputFile.write("D=M\n");
        pushD();
        incrementSP();

        //push THIS
        outputFile.write("@THIS\n");
        outputFile.write("D=M\n");
        pushD();
        incrementSP();

        //push THAT
        outputFile.write("@THAT\n");
        outputFile.write("D=M\n");
        pushD();
        incrementSP();

        //ARG = SP - n - 5
        outputFile.write("@SP\n");
        outputFile.write("D=M\n");

        outputFile.write("@LCL\n"); // LCL = SP
        outputFile.write("M=D\n");

        String n = Integer.toString(numArgs +5);
        outputFile.write("@" + n + "\n");
        outputFile.write("D=D-A\n");
        outputFile.write("@ARG\n");
        outputFile.write("M=D\n");

        //LCL = SP
        outputFile.write("@SP\n");
        outputFile.write("D=M\n");
        outputFile.write("@LCL\n");
        outputFile.write("M=D\n");

        //writeGoto(funcName);
        //writeLabel(returnAddress);
        outputFile.write("@" + funcName + "\n");
        outputFile.write("0;JMP\n");
        outputFile.write("(" + returnAddress + ")\n");
    }

    /**
     * writes the assembly code that is the translation of the RETURN command
     */
    public void writeReturn() throws IOException {
    /*
     * return
     * (from a func)
     *
     * FRAME = LCL  //FRAME is a temp var
     * RET=*(FRAME-5) //put the return-address in a temp var
     * *ARG=POP // reposition the return value for the caller
     * SP=ARG+1 //restore SP of the caller
     * THAT=*(FRAME-1) //restore THAT of the caller
     * THIS=*(FRAME-2) //restore THIS of the caller
     * ARG=*(FRAME-3) //restore ARG of the caller
     * LCL=*(FRAME-4) //restore LCL of the caller
     * goto RET //goto return address (in the callers code)
     *
     */
        //Frame = LCL
        outputFile.write("@LCL\n");
        outputFile.write("D=M\n");
        outputFile.write("@R14\n"); // @R14 is the FRAME var
        outputFile.write("M=D\n"); // R14 = LCL

        //RET = *(FRAME-5)
        outputFile.write("@5\n");
        outputFile.write("D=A\n");
        outputFile.write("@R14\n"); //address of frame-5
        outputFile.write("A=M-D\n");
        outputFile.write("D=M\n");
        outputFile.write("@R15\n"); // @R15 is RET
        outputFile.write("M=D\n"); //RET = *(FRAME-5)

        // *ARG = POP
        popToD();
        outputFile.write("@ARG\n");
        outputFile.write("A=M\n");
        outputFile.write("M=D\n");

        //SP = ARG+1
        outputFile.write("@ARG\n");
        outputFile.write("D=M\n");
        outputFile.write("@SP\n");
        outputFile.write("M=D+1\n");

        //THAT=*(FRAME-1)
        outputFile.write("@R14\n");
        outputFile.write("M=M-1\n");
        outputFile.write("A=M\n");
        outputFile.write("D=M\n");
        outputFile.write("@THAT\n");
        outputFile.write("M=D\n");

        //THIS=*(FRAME-2)
        outputFile.write("@R14\n");
        outputFile.write("M=M-1\n");
        outputFile.write("A=M\n");
        outputFile.write("D=M\n");
        outputFile.write("@THIS\n");
        outputFile.write("M=D\n");

        //ARG=*(FRAME-3)
        outputFile.write("@R14\n");
        outputFile.write("M=M-1\n");
        outputFile.write("A=M\n");
        outputFile.write("D=M\n");
        outputFile.write("@ARG\n");
        outputFile.write("M=D\n");

        //LCL=*(FRAME-4)
        outputFile.write("@R14\n");
        outputFile.write("M=M-1\n");
        outputFile.write("A=M\n");
        outputFile.write("D=M\n");
        outputFile.write("@LCL\n");
        outputFile.write("M=D\n");

        //goto RET
        outputFile.write("@R15\n"); //@R15 is RET
        outputFile.write("A=M\n");
        outputFile.write("0;JMP\n");

    }

    /**
     * writes the assembly code that is the translation of the FUNCTION command
     * @param funcName
     * @param numLocals
     */
    public void writeFunction(String funcName, int numLocals) throws IOException {
    /*
     * function f k
     * (declaring a function f that has k local vars)
     *
     * (f) //declare a label for the func entry
     * repeat k times: //k == numLocals
     * push 0 //init all of them to 0
     */
        curFunc = funcName;

        outputFile.write("(" + funcName + ")\n");
        for (int i = 0; i < numLocals; i++) {
            //outputFile.write("@0\n");
            outputFile.write("D=0\n");
            pushD();
            incrementSP();
        }
    }

}

