import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Created by Era on 02/06/2016.
 */
public class VMWriter {
    private BufferedWriter outputFile;
    public enum SEGMENT {CONSTANT, ARGUMENT, LOCAL, THIS, THAT ,POINTER, TEMP, STATIC}
    public enum COMMAND {ADD, SUB, NEG, EQ, GT, LT, AND, OR, NOT, MULT, DIVIDE}
    public VMWriter(BufferedWriter bw)
    {
        this.outputFile = bw;
    }

    /*
    Writes a VM push command
     */
    public void writePush(SEGMENT pushSegement, int index) throws IOException
    {
        outputFile.write("push " + pushSegement.toString().toLowerCase() + " " + index + "\n");
    }
    public void writePop(SEGMENT popSegment, int index) throws IOException
    {
        outputFile.write("pop " + popSegment.toString().toLowerCase() + " " + index + "\n");
    }

    public void writeArithmetic(COMMAND currCommand) throws IOException
    {
        if (currCommand.toString() == "MULT")
        {
            outputFile.write("call Math.multiply 2\n");
        }
        else if (currCommand.toString() == "DIVIDE")
        {
            outputFile.write("call Math.divide 2\n");
        }
        else
        {
            outputFile.write(currCommand.toString().toLowerCase() + "\n");
        }
    }

    public void writeLabel(String labelName) throws IOException
    {
        outputFile.write("label " + labelName + "\n");

    }
    public void writeGoto(String gotoLabel) throws IOException
    {
        outputFile.write("goto " + gotoLabel + "\n");
    }
    public void writeIf(String label) throws IOException
    {
        outputFile.write("if-goto " + label + "\n");

    }
    public void writeCall(String name, int nArgs) throws IOException
    {
        outputFile.write("call " + name + " " + nArgs + "\n");
    }
    public void writeFunction(String name, int nLocals) throws IOException
    {
        outputFile.write("function " + name + " " + nLocals + "\n");
    }
    public void writeReturn() throws IOException
    {
        outputFile.write("return" + "\n");
    }
    public void close()
    {
        ///TODO: fill this out
    }
}
