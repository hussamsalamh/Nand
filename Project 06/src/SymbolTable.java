/**
 * Created by Era on 01/04/2016.
 */
import java.util.Hashtable;
public class SymbolTable {
    private Hashtable<String, String> symbolTable;
    // Constants for the default values of the symbolTable
    private final String SPAddress = String.format("%16s", Integer.toBinaryString(0)).replace(' ', '0');
    private final String LCLAddress = String.format("%16s", Integer.toBinaryString(1)).replace(' ', '0');
    private final String ARGAddress = String.format("%16s", Integer.toBinaryString(2)).replace(' ', '0');
    private final String THISAddress = String.format("%16s", Integer.toBinaryString(3)).replace(' ', '0');
    private final String THATAddress = String.format("%16s", Integer.toBinaryString(4)).replace(' ', '0');
    private final String R0 = String.format("%16s", Integer.toBinaryString(0)).replace(' ', '0');
    private final String R1 = String.format("%16s", Integer.toBinaryString(1)).replace(' ', '0');
    private final String R2 = String.format("%16s", Integer.toBinaryString(2)).replace(' ', '0');
    private final String R3 = String.format("%16s", Integer.toBinaryString(3)).replace(' ', '0');
    private final String R4 = String.format("%16s", Integer.toBinaryString(4)).replace(' ', '0');
    private final String R5 = String.format("%16s", Integer.toBinaryString(5)).replace(' ', '0');
    private final String R6 = String.format("%16s", Integer.toBinaryString(6)).replace(' ', '0');
    private final String R7 = String.format("%16s", Integer.toBinaryString(7)).replace(' ', '0');
    private final String R8 = String.format("%16s", Integer.toBinaryString(8)).replace(' ', '0');
    private final String R9 = String.format("%16s", Integer.toBinaryString(9)).replace(' ', '0');
    private final String R10 = String.format("%16s", Integer.toBinaryString(10)).replace(' ', '0');
    private final String R11 = String.format("%16s", Integer.toBinaryString(11)).replace(' ', '0');
    private final String R12 = String.format("%16s", Integer.toBinaryString(12)).replace(' ', '0');
    private final String R13 = String.format("%16s", Integer.toBinaryString(13)).replace(' ', '0');
    private final String R14 = String.format("%16s", Integer.toBinaryString(14)).replace(' ', '0');
    private final String R15 = String.format("%16s", Integer.toBinaryString(15)).replace(' ', '0');
    private final String screenAddress = String.format("%16s", Integer.toBinaryString(16384)).replace(' ', '0');
    private final String keyBoardAddress = String.format("%16s", Integer.toBinaryString(24576)).replace(' ', '0');
    /**
     * Constructor for the SymbolTable class.
     */
    public SymbolTable(){
        symbolTable = new Hashtable<>();
        setDefaultValues();
    }
    private void setDefaultValues()
    {
        symbolTable.put("SP", SPAddress);
        symbolTable.put("LCL", LCLAddress);
        symbolTable.put("ARG", ARGAddress);
        symbolTable.put("THAT", THATAddress);
        symbolTable.put("THIS", THISAddress);
        symbolTable.put("R0", R0);
        symbolTable.put("R1", R1);
        symbolTable.put("R2", R2);
        symbolTable.put("R3", R3);
        symbolTable.put("R4", R4);
        symbolTable.put("R5", R5);
        symbolTable.put("R6", R6);
        symbolTable.put("R7", R7);
        symbolTable.put("R8", R8);
        symbolTable.put("R9", R9);
        symbolTable.put("R10", R10);
        symbolTable.put("R11", R11);
        symbolTable.put("R12", R12);
        symbolTable.put("R13", R13);
        symbolTable.put("R14", R14);
        symbolTable.put("R15", R15);
        symbolTable.put("SCREEN", screenAddress);
        symbolTable.put("KBD", keyBoardAddress);
    }

    /**
     * Adds the pair (symbol, address) to the table.
     * @param symbol - The symbol.
     * @param address - The address.
     */
    public void addEntry(String symbol, int address){
        //TODO: Can we assume symbol doesn't already exist?
        symbolTable.put(symbol, address);
    }

    /**
     * Checks whether the symbol table contains the given symbol.
     * @param symbol - The symbol to check.
     * @return - True if the symbol is in the table, false otherwise.
     */
    public boolean contains(String symbol){
        return symbolTable.contains(symbol);
    }

    /**
     * Returns the address associated with the symbol.
     * @param symbol - The symbol whose address is requested.
     * @return - The address of the symbol.
     */
    public String GetAddress(String symbol){
        //TODO: check whether we can assume symbol is already in table.
        return symbolTable.get(symbol);
    }

}
