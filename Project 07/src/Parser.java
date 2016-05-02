import java.io.BufferedReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.regex.Pattern;
/**
 * Created by Era on 19/04/2016.
 */
public class Parser {
    public enum CommandType{C_ARITHMETIC, C_PUSH, C_POP, C_LABEL, C_GOTO, C_IF, C_FUNCTION, C_RETURN, C_CALL};
    // Same logic as Project 6 in terms of currentline etc...
    private BufferedReader file;
    private CommandType commandType;
    private String currentCommand;
    private String currentLine;

    // Delimiter of commands is a space
    public final static String COMMAND_DELIMITER = " ";
    // Set containing the arithmetic commands
    private static final HashSet<String> arithmeticCommands = new HashSet<String>() {{
        // Put predefined binary translations here
        // a = 0
        add("add");
        add("sub");
        add("neg");
        add("eq");
        add("gt");
        add("lt");
        add("and");
        add("or");
        add("not");
    }};

    /**
     * Opens the input file/stream and gets ready
     * to parse it.
     * @param file
     */
    public Parser(BufferedReader file)
    {
        this.file = file;
    }

    /**
     * Are there more commands in the input?
     * @return - True or false accordingly
     */
    public boolean hasMoreCommands() throws IOException {
        if ((currentLine=file.readLine()) == null)
        {
            return false;
        }
        currentLine = currentLine.trim().replaceAll("[ \t\r\n]+", COMMAND_DELIMITER);
        while ("".equals(currentLine) || " ".equals(currentLine))
        {
            currentLine = file.readLine();
            if (currentLine == null)
            {
                break;
            }
            currentLine = currentLine.trim().replaceAll("[ \t\r\n]+", COMMAND_DELIMITER);
        }
        if (currentLine != null)
        {
            // Strip multiple spaces and replace with single space

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
     * Reads the next command from the input and
     * makes it the current command. Should be
     * called only if hasMoreCommands() is true.
     * Initially there is no current command.
     */
    public void advance()
    {
        this.currentCommand = currentLine;
        commandType = commandType();
    }

    /**
     * Returns the type of the current VM
     * command. C_ARITHMETIC is returned for
     * all the arithmetic commands.
     * @return - The command type
     */
    public CommandType commandType()
    {
        // Split command line by delimiter
        String[] currentCommands = currentLine.split(COMMAND_DELIMITER);

        // Check if first word is in arithmetic commands set
        if (arithmeticCommands.contains(currentCommands[0]))
        {
            this.commandType = CommandType.C_ARITHMETIC;
        }
        // Check if push command
        else if(currentCommands[0].equals("push"))
        {
            this.commandType = CommandType.C_PUSH;
        }
        // Check if pop command
        else if(currentCommands[0].equals("pop"))
        {
            this.commandType = CommandType.C_POP;
        }
        // Probably going to need to fill in more commands for Project 8
        return this.commandType;
    }

    /**
     * Returns the first argument of the current
     * command. In the case of C_ARITHMETIC,
     * the command itself (add, sub, etc.) is
     * returned. Should not be called if the current
     * command is C_RETURN.
     * @return
     */
    public String arg1()
    {
        // Split current line by delimiter
        String[] currentCommands = currentLine.split(COMMAND_DELIMITER);
        // If Arithmetic command, return first argument (even though at location 0)
        if (commandType().equals(CommandType.C_ARITHMETIC))
        {
            return currentCommands[0];
        }
        // Else, return first argument
        else
        {
            return currentCommands[1];
        }
    }

    /**
     * Returns the second argument of the current
     * command. Should be called only if the
     * current command is C_PUSH, C_POP,
     * C_FUNCTION, or C_CALL.
     * @return - The second argument of the current command.
     */
    public int arg2()
    {
        return Integer.parseInt(currentLine.split(COMMAND_DELIMITER)[2]);
    }
}
