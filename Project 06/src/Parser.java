/**
 * Created by Era on 01/04/2016.
 */
public class Parser {
    public enum CommandType{A_COMMAND, C_COMMAND, L_COMMAND};
    private String fileName;
    private Code currentCommand;
    public Parser(String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * Checks if there are more commands in input
     * @return - True if there are more commands
     */
    public boolean hasMoreCommands(){
        return true;
    }

    /**
     * Reads the next command from the input and makes it the current command. Should be called only if
     * hasMoreCommands() is true.
     */
    public void advance(){
    }

    /**
     * Returns the type of the current command.
     * @return - ENUM representing the type of the command.
     */
    public CommandType commandtype()
    {
        return null;
    }

    /**
     * Returns the symbol or decimal XXX of the current command. Should be called only when command type is A_COMMAND
     * or L_COMMAND
     * @return
     */
    public String symbol()
    {
        return null;
    }

    /**
     * Returns the current dest mnemonic in the current C_COMMAND (8 possibilities). Should be called only when the
     * command type is C_COMMAND.
     * @return
     */
    public String dest()
    {
        return null;
    }

    /**
     * Returns the comp mnemonic in the current C_COMMAND. Should be called only when the
     * command type is C_COMMAND.
     * @return
     */
    public String comp()
    {
        return null;
    }

    /**
     * Returns the jump mnemonic in the current C_ccommand. Should be called only when the
     * command type is C_COMMAND.
     * @return
     */
    public String jump()
    {
        return null;
    }
}
