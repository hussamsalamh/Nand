import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import static java.lang.System.exit;

/**
 * Created by Era on 01/04/2016.
 */
public class Assembler {

    private SymbolTable symbolTable;

    public static void main(String[] args) {
        // The first argument should be the name of the file to be parsed
        SymbolTable st = new SymbolTable();

        //create the output file name
        //String outFileName = args[0];
        String outFileName = "Max.asm";
        int indexOfSuffix = outFileName.indexOf(".");
        outFileName = outFileName.substring(0, indexOfSuffix) + ".hack";

        // First pass
        /**
         *         Go through the entire assembly program, line by line, and build the
         symbol table without generating any code. As you march through the program lines,
         keep a running number recording the ROM address into which the current command
         will be eventually loaded. This number starts at 0 and is incremented by 1 whenever
         a C-instruction or an A-instruction is encountered, but does not change when a label
         pseudocommand or a comment is encountered. Each time a pseudocommand (Xxx)
         is encountered, add a new entry to the symbol table, associating Xxx with the ROM
         address that will eventually store the next command in the program. This pass results
         in entering all the program’s labels along with their ROM addresses into the symbol
         table. The program’s variables are handled in the second pass.
         */
        int lineNum = 0;
        try (FileReader fr = new FileReader("Max.asm"); BufferedReader br = new BufferedReader(fr))
        {
            Parser p = new Parser(br);
            while (p.hasMoreCommands())
            {
                p.advance();
                if (p.commandtype().equals(Parser.CommandType.L_COMMAND))
                {
                    st.addROMEntry(p.symbol(), Parser.BinaryLeftPad(lineNum));
                    continue;
                }else{
                    lineNum++;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("happened");
            System.out.println(e.getMessage());
            exit(0);
        }
        // Second pass
        /**
         * Now go again through the entire program, and parse each line. Each
         time a symbolic A-instruction is encountered, namely, @Xxx where Xxx is a symbol
         and not a number, look up Xxx in the symbol table. If the symbol is found in the
         table, replace it with its numeric meaning and complete the command’s translation.
         If the symbol is not found in the table, then it must represent a new variable. To
         handle it, add the pair (Xxx, n) to the symbol table, where n is the next available
         RAM address, and complete the command’s translation. The allocated RAM
         addresses are consecutive numbers, starting at address 16 ( just after the addresses
         allocated to the predefined symbols).
         This completes the assembler’s implementation.
         */
        try (FileReader fr1 = new FileReader("Max.asm");
             BufferedReader br1 = new BufferedReader(fr1);

             FileWriter fw1 = new FileWriter(outFileName);
             BufferedWriter bw = new BufferedWriter(fw1))
        {
            Parser p = new Parser(br1);
            int outLineNum = 0;
            while (p.hasMoreCommands())
            {
                p.advance();
                Parser.CommandType type = p.getCommandType();
                if (type.equals(Parser.CommandType.A_COMMAND))
                {
                    outLineNum++;
                    // Auxiliary function which checks
                    // check if only num:
                    if (p.symbol().matches("[-+]?\\d*\\.?\\d+"))
                    {
                        String numInBinary = Parser.BinaryLeftPad(p.symbol());
                        bw.write(numInBinary + "\n");
                        continue;
                    }
                    if (st.GetAddress(p.symbol()) == null)
                    {
                        st.addVariableEntry(p.symbol());
                    }
                    String writeSymb = st.GetAddress(p.symbol());
                    bw.write(writeSymb + "\n");
                }
                else if (type.equals(Parser.CommandType.C_COMMAND))
                {
                    outLineNum++;
                    String startingTriplet = "111";
                    if (p.comp().contains("<<") || p.comp().contains(">>"))
                    {
                        startingTriplet = "101";
                    }
                    String cCommand = startingTriplet + Code.comp(p.comp()) + Code.dest(p.dest()) +
                            Code.jump(p.jump()) + "\n";

                    bw.write(cCommand);
                }

            }
        }
        catch (Exception e)
        {
            System.out.println("happened");
            exit(0);
        }
    }
}
