/**
 * Created by Era on 01/04/2016.
 */
import java.util.Hashtable;

/**
 * This is a static Code class which translates mnemonics to binary code.
 */
public class Code {

    /**
     * Static hashtable to translate comp mnemonics to binary
     */
    static final Hashtable<String, String> compTable = new Hashtable<String,String>() {{
        // Put predefined binary translations here
        // a = 0
        put("0", "0101010");
        put("1", "0111111");
        put("-1", "0111010");
        put("D", "0001100");
        put("A", "0110000");
        put("!D", "0001101");
        put("!A", "0110001");
        put("-D", "0001111");
        put("-A", "0110011");
        put("D+1", "0011111");
        put("A+1", "0110111");
        put("D-1", "0001110");
        put("A-1", "0110010");
        put("D+A", "0000010");
        put("D-A", "0010011");
        put("A-D", "0000111");
        put("D&A", "0000000");
        put("D|A", "0010101");

        // a=1
        put("M", "1110000");
        put("!M", "1110001");
        put("-M", "1110011");
        put("M+1", "1110111");
        put("M-1", "1110010");
        put("D+M", "1000010");
        put("D-M", "1010011");
        put("M-D", "1000111");
        put("D&M", "1000000");
        put("D|M", "1010101");

        // Extended
        put("D<<", "0110000");
        put("D>>", "0010000");
        put("A<<", "0100000");
        put("A>>", "0000000");
        put("M<<", "1100000");
        put("M>>", "1000000");
    }};

    /**
     * Static hashtable to translate dest mnemonics to binary
     */
    static final Hashtable<String, String> destTable = new Hashtable<String, String>() {{
        // Put predefined binary translations here
        // The "NULL" needs to be added when nothing appears
        put("NULL", "000");
        put("M", "001");
        put("D", "010");
        put("MD", "011");
        put("A", "100");
        put("AM", "101");
        put("AD", "110");
        put("AMD", "111");
    }};

    /**
     * Static hashtable to translate jmp mnemonics to binary
     */
    static final Hashtable<String, String> jmpTable = new Hashtable<String, String>() {{
        // Put predefined binary translations here

        // The "NULL" needs to be added when nothing appears
        put("NULL", "000");
        put("JGT", "001");
        put("JEQ", "010");
        put("JGE", "011");
        put("JLT", "100");
        put("JNE", "101");
        put("JLE", "110");
        put("JMP", "111");

    }};

    /**
     * Returns the binary code of the dest mnemonic.
     * @param destMem - The mnemonic to translate.
     * @return - The relevant 3 bits after translation.
     */
    public static String dest(String destMem){
        return destTable.get(destMem);
    }

    /**
     * Returns the binary code of the comp mnemonic.
     * @param compMem - The mnemonic to translate.
     * @return - The relevant 7 bits after translation.
     */
    public static String comp(String compMem)
    {
        return compTable.get(compMem);
    }

    /**
     * Returns the binary code of the jump mnemonic.
     * @param jumpMem - The mnemonic to translate.
     * @return - The relevant 3 bits after translation.
     */
    public static String jump(String jumpMem)
    {
        return jmpTable.get(jumpMem);
    }
}
