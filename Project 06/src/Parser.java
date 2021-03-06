import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Era on 01/04/2016.
 */
public class Parser {
    public enum CommandType{A_COMMAND, C_COMMAND, L_COMMAND};

    private CommandType commandType;
    private BufferedReader file;
    private String currentCommand;
    private String currentLine;
    private final int START_INDEX = 0;
    public Parser(BufferedReader file)
    {
        this.file = file;

    }

    public CommandType getCommandType() {
        return commandType;
    }

    /**
     * Checks if there are more commands in input
     * @return - True if there are more commands
     */
    public boolean hasMoreCommands() throws IOException
    {
        if ((currentLine=file.readLine()) == null)
        {
            return false;
        }
        while ("".equals(currentLine) || "\n".equals(currentLine) || "\r".equals(currentLine))
        {
            currentLine = file.readLine();
        }
        if (currentLine != null)
        {
            currentLine = currentLine.replaceAll("\\s+", "");
            int indexOfComment = currentLine.indexOf("//");
            if (indexOfComment > 0)
            {
                currentLine = currentLine.substring(0, indexOfComment);
            }
            else if (indexOfComment == 0)
            {
                return hasMoreCommands();
            }
        }
        return (currentLine != null);
    }

    /**
     * Reads the next command from the input and makes it the current command. Should be called only if
     * hasMoreCommands() is true.
     */
    public void advance()
    {
        this.currentCommand = currentLine;
        commandType = commandtype();
    }
    public static String BinaryLeftPad(String num)
    {
        return String.format("%16s", Integer.toBinaryString(Integer.parseInt(num))).replace(' ', '0');
    }
    public static String BinaryLeftPad(Integer num)
    {
        return String.format("%16s", Integer.toBinaryString(num)).replace(' ', '0');
    }

    /**
     * Returns the type of the current command.
     * @return - ENUM representing the type of the command.
     */
    public CommandType commandtype()
    {
        char startChar = currentCommand.charAt(START_INDEX);
        if (startChar == '(')
        {
            return CommandType.L_COMMAND;
        }
        else if (startChar == '@')
        {
            return CommandType.A_COMMAND;
        }
        else
        {
            return CommandType.C_COMMAND;
        }
    }

    /**
     * Returns the symbol or decimal XXX of the current command. Should be called only when command type is A_COMMAND
     * or L_COMMAND
     * @return
     */
    public String symbol()
    {
        if (commandType == CommandType.L_COMMAND){
            currentCommand = currentCommand.replaceAll("[()]", "");
            return currentCommand;
        }
        else if (commandType == CommandType.A_COMMAND) {
            currentCommand = currentCommand.replaceAll("@", "");
            return currentCommand; // TODO should we differ between symbol var or decimal?
        }
        return "NULL";
    }

    /**
     * Returns the current dest mnemonic in the current C_COMMAND (8 possibilities). Should be called only when the
     * command type is C_COMMAND.
     * @return
     */
    public String dest()
    {
        if (commandType != CommandType.C_COMMAND) return "NULL";
        int indexOfEquals = currentCommand.indexOf("=");
        if (indexOfEquals == -1)
        {
            return "NULL";
        }
        else
        {
            return currentCommand.substring(0, indexOfEquals);
        }
    }

    /**
     * Returns the comp mnemonic in the current C_COMMAND. Should be called only when the
     * command type is C_COMMAND.
     * @return
     */
    public String comp()
    {
        if (commandType != CommandType.C_COMMAND) return "NULL";
        int indexOfEquals = currentCommand.indexOf("=") + 1;
        int indexOfSemiC = currentCommand.indexOf(";");
        if (indexOfSemiC == -1)
        {
            indexOfSemiC = currentCommand.length();
        }
        return currentCommand.substring(indexOfEquals, indexOfSemiC);

    }

    /**
     * Returns the jump mnemonic in the current C_ccommand. Should be called only when the
     * command type is C_COMMAND.
     * @return
     */
    public String jump()
    {
        if (commandType != CommandType.C_COMMAND) return "NULL";
        int indexOfSemiC = currentCommand.indexOf(";");
        if (indexOfSemiC == -1)
        {
            return "NULL";
        }
        indexOfSemiC++;
        String jmpDest = currentCommand.substring(indexOfSemiC);
        if (indexOfSemiC == currentCommand.length())
        {
            return "NULL";
        }
        else{
            return jmpDest;
        }
    }
}
