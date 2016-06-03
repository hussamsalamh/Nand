import java.util.HashMap;

/**
 * Created by Era on 31/05/2016.
 */
public class SymbolTable
{
    public HashMap<String, symbolTableEntry> classTable;
    public HashMap<String, symbolTableEntry> subRoutineTable;
    public enum kind {STATIC, FIELD, ARG, VAR, NONE, CLASS, SUBROUTINE}
    int numStatic = 0;
    int numField = 0;
    int numArg = 0;
    int numVar = 0;
    int numControlFlow = 0;
    String className;
    boolean classDefinition = false;
    boolean varDec = false;
    SymbolTable()
    {
        classTable = new HashMap<>();
        subRoutineTable = new HashMap<>();
        return;
    }

    public void setClassName(String name)
    {
        this.className = name;
    }
    public void startSubroutine()
    {
        subRoutineTable.clear();
        numArg = 0;;
        numVar = 0;
        numControlFlow = 0;
    }

    public void Define(String name, String type, kind kindIdentifier)
    {
        int currEntryNum = 0;
        if (kindIdentifier == kind.ARG)
        {
            currEntryNum = numArg;
            numArg += 1;
        }
        else if (kindIdentifier == kind.FIELD)
        {
            currEntryNum = numField;
            numField += 1;
        }
        else if (kindIdentifier == kind.STATIC)
        {
            currEntryNum = numStatic;
            numStatic += 1;
        }
        else if (kindIdentifier == kind.VAR)
        {
            currEntryNum = numVar;
            numVar += 1;
        }
        symbolTableEntry currEntry = new symbolTableEntry(type, kindIdentifier, currEntryNum);
        if (kindIdentifier == kind.VAR || kindIdentifier == kind.ARG)
        {
            subRoutineTable.put(name, currEntry);
        }
        else if (kindIdentifier == kind.STATIC || kindIdentifier == kind.FIELD)
        {
            System.out.println("put check");
            System.out.println(name);
            classTable.put(name, currEntry);
            System.out.println(classTable.containsKey(name));
        }
        else
        {
            //TODO: REMOVE THIS
            System.out.println("shouldn't be here");
        }
    }

    public int VarCount(kind kindIdentifier) {
        switch (kindIdentifier) {
            case ARG:
                return numArg;
            case FIELD:
                return numField;
            case STATIC:
                return numStatic;
            case VAR:
                return numVar;
            default:
                return 0;
        }

    }

    public kind KindOf(String name)
    {
       symbolTableEntry currEntry;
        if (subRoutineTable.containsKey(name))
        {
            currEntry = subRoutineTable.get(name);
            return currEntry.getEntryKind();
        }
        else if (classTable.containsKey(name))
        {
            currEntry = classTable.get(name);
            return currEntry.getEntryKind();
        }
        else
        {
            return kind.NONE;
        }
    }

    public String TypeOf(String name)
    {
            symbolTableEntry currEntry;
            if (subRoutineTable.containsKey(name))
            {
                currEntry = subRoutineTable.get(name);
                return currEntry.getEntryType();
            }
            else if (classTable.containsKey(name))
            {
                currEntry = classTable.get(name);
                return currEntry.getEntryType();
            }
            else
            {
                // TODO: is this ok? should be..
                return null;
            }
    }

    public int IndexOf(String name)
    {
        symbolTableEntry currEntry;
        if (subRoutineTable.containsKey(name))
        {
            currEntry = subRoutineTable.get(name);
            return currEntry.getEntryNum();
        }
        else if(classTable.containsKey(name))
        {
            currEntry = classTable.get(name);
            return currEntry.getEntryNum();
        }
        return 0;
    }

}
