/**
 * Created by Era on 01/04/2016.
 */
import java.util.Hashtable;
public class SymbolTable {
    private Hashtable<String, String> symbolTable;
    // Constants for the default values of the symbolTable
    final String SPAddress = "0";
    final String LCLAddress = "1";
    final String ARGAddress = "2";
    final String THISAddress = "3";
    final String THATAddress = "4";
    final String R0 = "0";
    final String R1 = "1";
    final String R2 = "2";
    final String R3 = "3";
    final String R4 = "4";
    final String R5 = "5";
    final String R6 = "6";
    final String R7 = "7";
    final String R8 = "8";
    final String R9 = "9";
    final String R10 = "10";
    final String R11 = "11";
    final String R12 = "12";
    final String R13 = "13";
    final String R14 = "14";
    final String R15 = "15";
    final String screenAddress = "16384";
    final String keyBoardAddress = "24576";

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
    public int GetAddress(String symbol){
        //TODO: check whether we can assume symbol is already in table.
        return symbolTable.get(symbol);
    }

}
