/**
 * Created by Era on 01/04/2016.
 */
import java.util.Hashtable;

/**
 * This is a static Code class which translates mnemonics to binary code.
 */
public  class Code {

    /**
     * Static hashtable to translate comp mnemonics to binary
     */
    static final Hashtable<String, Integer> compTable = new Hashtable<String,Integer>() {{
        // Put predefined binary translations here
        // a = 0
        put("0", 0b0101010);
        put("1", 0b0111111);
        put("-1", 0b0111010);
        put("D", 0b0001100);
        put("A", 0b0110000);
        put("!D", 0b0001101);
        put("!A", 0b0110001);
        put("-D", 0b0001111);
        put("-A", 0b0110011);
        put("D+1", 0b0011111);
        put("A+1", 0b0110111);
        put("D-1", 0b0001110);
        put("A-1", 0b0110010);
        put("D+A", 0b0000010);
        put("D-A", 0b0010011);
        put("A-D", 0b0000111);
        put("D&A", 0b0000000);
        put("D|A", 0b0010101);

        // a=1
        put("M", 0b1110000);
        put("!M", 0b1110001);
        put("-M", 0b1110011);
        put("M+1", 0b1110111);
        put("M-1", 0b1110010);
        put("D+M", 0b1000010);
        put("D-M", 0b1010011);
        put("M-D", 0b1000111);
        put("D&M", 0b1000000);
        put("D|M", 0b1010101);
    }};

    /**
     * Static hashtable to translate dest mnemonics to binary
     */
    static final Hashtable<String, Integer> destTable = new Hashtable<String, Integer>() {{
        // Put predefined binary translations here
        // The "NULL" needs to be added when nothing appears
        put("NULL", 0b000);
        put("M", 0b001);
        put("D", 0b010);
        put("MD", 0b011);
        put("A", 0b100);
        put("AM", 0b101);
        put("AD", 0b110);
        put("AMD", 0b111);
    }};

    /**
     * Static hashtable to translate jmp mnemonics to binary
     */
    static final Hashtable<String, Integer> jmpTable = new Hashtable<String, Integer>() {{
        // Put predefined binary translations here

        // The "NULL" needs to be added when nothing appears
        put("NULL", 0b000);
        put("JGT", 0b001);
        put("JEQ", 0b010);
        put("JGE", 0b011);
        put("JLT", 0b100);
        put("JNE", 0b101);
        put("JLE", 0b110);
        put("JMP", 0b111);

    }};

    /**
     * Returns the binary code of the dest mnemonic.
     * @param destMem - The mnemonic to translate.
     * @return - The relevant 3 bits after translation.
     */
    public static int dest(String destMem){
        return destTable.get(destMem);
    }

    /**
     * Returns the binary code of the comp mnemonic.
     * @param compMem - The mnemonic to translate.
     * @return - The relevant 7 bits after translation.
     */
    public static int comp(String compMem)
    {
        return compTable.get(compMem);
    }

    /**
     * Returns the binary code of the jump mnemonic.
     * @param jumpMem - The mnemonic to translate.
     * @return - The relevant 3 bits after translation.
     */
    public static int jump(String jumpMem)
    {
        return jmpTable.get(jumpMem);
    }
}
