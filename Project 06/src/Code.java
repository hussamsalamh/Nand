/**
 * Created by Era on 01/04/2016.
 */
import java.util.Hashtable;
public  class Code {
    static final Hashtable<String, Integer> comp0Table = new Hashtable<String,Integer>() {{
        // Put predefined binary translations here
        put("foo", 0b10);
        put("x", 0b10);
    }};
    static final Hashtable<String, Integer> comp1Table = new Hashtable<String, Integer>() {{
        // Put predefined binary translations here
        put("foo", 0b10);
        put("x", 0b10);
    }};
    static final Hashtable<String, Integer> destTable = new Hashtable<String, Integer>() {{
        // Put predefined binary translations here
        put("M", 0b001);
        put("D", 0b010);
    }};
    static final Hashtable<String, Integer> jmpTable = new Hashtable<String, Integer>() {{
        // Put predefined binary translations here
        put("foo", 0b10);
        put("x", 0b10);
    }};

    /**
     * Returns the binary code of the dest mnemonic.
     * @param destMem - The mnemonic to translate.
     * @return - The relevant 3 bits after translation.
     */
    public static int dest(String destMem){
        return 0;
    }

    /**
     * Returns the binary code of the comp mnemonic.
     * @param compMem - The mnemonic to translate.
     * @return - The relevant 7 bits after translation.
     */
    public static int comp(String compMem)
    {
        return 0;
    }

    /**
     * Returns the binary code of the jump mnemonic.
     * @param jumpMem - The mnemonic to translate.
     * @return - The relevant 3 bits after translation.
     */
    public static int jump(String jumpMem)
    {
        return 0;
    }
}
