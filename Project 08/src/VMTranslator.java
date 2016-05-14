import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yonilip on 5/13/16.
 */
public class VMTranslator {
    //TODO: Fix path of file created -- ask Era about this
    // Static String containing the name of the current vm file being translated
    private static String fileName;

    /**
     * Take a vm file and translates it using a given codeWriter
     * @param fileToTranslate - The name of the vm file to process
     * @param cw - The codewriter used to write the file
     */
    public static void translate(String fileToTranslate, CodeWriter cw) {
        // Safely open a file reader and a buffer.
        try (FileReader fr1 = new FileReader(fileToTranslate);BufferedReader br1 = new BufferedReader(fr1))
        {
            Parser p = new Parser(br1);
            // Keep processing while there are more commands
            while (p.hasMoreCommands())
            {
                p.advance();
                // If an arithmetic command, use codewriter and input fithis!!!rst argument (which is the command itself)
                if (p.commandType() == Parser.CommandType.C_ARITHMETIC)
                {
                    cw.writeArithmetic(p.arg1());
                }
                else if (p.commandType() == Parser.CommandType.C_PUSH || p.commandType() == Parser.CommandType.C_POP)
                {
                    cw.WritePushPop(p.commandType(), p.arg1(), p.arg2());
                }
                else if (p.commandType() == Parser.CommandType.C_LABEL)
                {
                    cw.writeLabel(p.arg1()); //TODO check the whole function$label thing
                }
                else if (p.commandType() == Parser.CommandType.C_GOTO)
                {
                    cw.writeGoto(p.arg1()); //TODO same, check function$label
                }
                else if (p.commandType() == Parser.CommandType.C_IF)
                {
                    cw.writeIf(p.arg1());
                }
                else if (p.commandType() == Parser.CommandType.C_FUNCTION)
                {
                    cw.writeFunction(p.arg1(), p.arg2());
                }
                else if (p.commandType() == Parser.CommandType.C_CALL)
                {
                    cw.writeCall(p.arg1(), p.arg2());
                }
                else if (p.commandType() == Parser.CommandType.C_RETURN)
                {
                    cw.writeReturn();
                }
            }
        }
        // For any problem just print the stacktrace.
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Exacct same function as in Project 6, changed the file endings to fit.
     * @param pathName - Path name
     * @return - Array of files to translate
     */
    private static String[] getFileArray(String pathName)
    {
        File directory = new File(pathName);
        List<String> fileArray = new ArrayList<String>();
        if (directory.isDirectory())
        {
            // If the path is a directory, the name of the asm file to write to is the name of the directory.
            VMTranslator.fileName= pathName + File.separator + directory.getName() + ".asm";
            for (File file : directory.listFiles())
            {
                // append to array all the files that end with vm
                String fileName = file.getName();
                int indexOfSuffix = fileName.lastIndexOf(".");
                if (indexOfSuffix > 0) {
                    String suffix = fileName.substring(indexOfSuffix);
                    if (suffix.equals(".vm")) {
                        fileArray.add(file.getAbsolutePath());
                    }
                }

            }
        } else
        {
            int indexOfSuffix = directory.getAbsolutePath().lastIndexOf(".");
            if (indexOfSuffix == -1)
            {
                indexOfSuffix = 0;
            }
            // return array with single file, check that has asm vm
            if (directory.getAbsolutePath().substring(indexOfSuffix).equals(".vm")) {
                // If the path is a file, the .asm file to translate to is the same as the file name.
                VMTranslator.fileName = directory.getAbsolutePath().substring(0,indexOfSuffix) + ".asm";
                fileArray.add(directory.getAbsolutePath());
            }
        }
        return fileArray.toArray(new String[fileArray.size()]);
    }

    public static void main(String[] args) {
        String[] fileNameArray = getFileArray(args[0]);
        // Safely open a fileWriter and BufferedWriter to write into, according to name set by getFileArray
        try(FileWriter fw1 = new FileWriter(VMTranslator.fileName); BufferedWriter bw = new BufferedWriter(fw1))
        {
            // New codeWriter using the buffered writer we opened
            CodeWriter cw = new CodeWriter(bw);
            for (String fileName : fileNameArray)
            {
                // Check if this is correct
                int indexOfSuffix = fileName.lastIndexOf(".");
                int indexOfPrefix = fileName.lastIndexOf(File.separator);
                if (indexOfPrefix == -1)
                {
                    indexOfPrefix = 0;
                }
                else
                {
                    indexOfPrefix += 1;
                }
                // Tell the codwriter we're processing a new .vm, so it knows how to name the static files.
                cw.setFileName(fileName.substring(indexOfPrefix, indexOfSuffix));
                // Parse the given vm and write results using the code-writer we created.
                translate(fileName, cw);
            }
        }
        // Print stack trace in case of exception
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}


/*
TODO update makefile
TODO update README
TODO pass tests and talk about edge cases
TODO check the bootstrap thing including the sys.init thing
TODO are we supposed to handle static vars?
TODO make sure all instructions have new line

 */