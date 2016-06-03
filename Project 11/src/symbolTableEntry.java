/**
 * Created by Era on 31/05/2016.
 */
public class symbolTableEntry
{

    private String entryType;
    private int entryNum;
    private SymbolTable.kind entryKind;

    public symbolTableEntry(String type, SymbolTable.kind kind, int num) {
        this.entryType = type;
        this.entryKind = kind;
        this.entryNum = num;
    }
    public int getEntryNum() {
        return entryNum;
    }

    public String getEntryType() {

        return entryType;
    }


    public SymbolTable.kind getEntryKind() {
        return entryKind;
    }

    public void setEntryKind(SymbolTable.kind entryKind) {
        this.entryKind = entryKind;
    }

}
