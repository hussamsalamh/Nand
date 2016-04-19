import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

/**
 * Created by Era on 01/04/2016.
 */
public class Assembler {

    private static SymbolTable symbolTable;

    private static void parseFile(String readFile)
    {

        String writeFile = readFile;
        int indexOfSuffix = writeFile.lastIndexOf(".");
        writeFile = writeFile.substring(0, indexOfSuffix) + ".hack";

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
        try (FileReader fr = new FileReader(readFile); BufferedReader br = new BufferedReader(fr))
        {
            Parser p = new Parser(br);
            while (p.hasMoreCommands())
            {
                p.advance();
                if (p.commandtype().equals(Parser.CommandType.L_COMMAND))
                {
                    symbolTable.addROMEntry(p.symbol(), Parser.BinaryLeftPad(lineNum));
                } else {
                    lineNum++;
                }
            }
        }
        catch (Exception e)
        {
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
        try (FileReader fr1 = new FileReader(readFile);
             BufferedReader br1 = new BufferedReader(fr1);

             FileWriter fw1 = new FileWriter(writeFile);
             BufferedWriter bw = new BufferedWriter(fw1))
        {
            Parser p = new Parser(br1);
            while (p.hasMoreCommands())
            {
                p.advance();
                Parser.CommandType type = p.getCommandType();
                if (type.equals(Parser.CommandType.A_COMMAND))
                {
                    // Auxiliary function which checks
                    // check if only num:
                    if (p.symbol().matches("[-+]?\\d*\\.?\\d+"))
                    {
                        String numInBinary = Parser.BinaryLeftPad(p.symbol());
                        bw.write(numInBinary + "\n");
                        continue;
                    }
                    if (symbolTable.GetAddress(p.symbol()) == null)
                    {
                        symbolTable.addVariableEntry(p.symbol());
                    }
                    String writeSymb = symbolTable.GetAddress(p.symbol());
                    bw.write(writeSymb + "\n");
                }
                else if (type.equals(Parser.CommandType.C_COMMAND))
                {
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
            System.out.println(e.getMessage());
            exit(0);
        }
    }



    private static String[] getFileArray(String pathName)
    {
        File directory = new File(pathName);

        List<String> fileArray = new ArrayList<String>();
        if (directory.isDirectory())
        {
            for (File file : directory.listFiles())
            {
                // append to array all the files that end with asm
                String fileName = file.getName();
                int indexOfSuffix = fileName.lastIndexOf(".");
                if (indexOfSuffix > 0)
                {
                    String suffix = fileName.substring(indexOfSuffix);
                    if (suffix.equals(".asm"))
                    {
                        fileArray.add(file.getAbsolutePath());
                    }
                }

            }
        }
        else
        {
            // return array with single file, check that has asm suffix
            int indexOfSuffix = pathName.lastIndexOf(".");
            if (pathName.substring(indexOfSuffix).equals(".asm"))
            {
                fileArray.add(directory.getAbsolutePath());
                System.out.println(directory.getAbsolutePath());
            }
        }
        return fileArray.toArray(new String[fileArray.size()]);
    }


    public static void main(String[] args) {
        // The first argument should be the name of the file to be parsed
        symbolTable = new SymbolTable();

        //get the file array
        String[] fileNameArray = getFileArray(args[0]);

        for (String fileName : fileNameArray)
        {
            parseFile(fileName);
        }
    }
}
