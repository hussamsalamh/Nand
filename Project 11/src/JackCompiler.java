import java.io.*;
import java.util.ArrayList;
import java.util.List;
//just so I get it
/**
 * Created by yonilip on 5/23/16.
 *
 *
 * top-level driver that sets up and invokes the other modules;
 *
 * The analyzer program operates on a given source, where source is either a file name
 * of the form Xxx.jack or a directory name containing one or more such files. For
 * each source Xxx.jack file, the analyzer goes through the following logic:
 * 1. Create a JackTokenizer from the Xxx.jack input file.
 * 2. Create an output file called Xxx.xml and prepare it for writing.
 * 3. Use the CompilationEngine to compile the input JackTokenizer into the output file.
 *
 *
 *
 * Notes from submission:
 * The ' " ' sign is never translated.
 * The command is: JackCompiler source. Where source is either an .jack file or a directory where the *.jack file are.
 * All .xml files should be inside this (where the .jack files are) directory.
 * A code is legal only if after removing the comment and replacing it with one white space it is a legal code.
 * The submitted project should not create the T.xml files.
 * We will use diff -w to compare your files.
 * Escaped characters should not be interpret. '\t' is translated to '\t'.
 * Empty tags should be translated: "<xxx> \n </xxx>" where \n is a new line.
 * You can assume the files are a legal jack files ( you do not need to handle illegal files).
 */
public class JackCompiler {


    private static String[] getFileArray(String pathName)
    {
        File directory = new File(pathName);
        List<String> fileArray = new ArrayList<String>();
        if (directory.isDirectory())
        {
            // If the path is a directory, the name of the file to write to is the name of the file in dir.
            for (File file : directory.listFiles())
            {
                // append to array all the files that end with jack
                String fileName = file.getName();
                int indexOfSuffix = fileName.lastIndexOf(".");
                if (indexOfSuffix > 0) {
                    String suffix = fileName.substring(indexOfSuffix);
                    if (suffix.equals(".jack")) {
                        fileArray.add(file.getAbsolutePath());
                    }
                }
            }
        }
        else
        {
            int indexOfSuffix = directory.getAbsolutePath().lastIndexOf(".");
            if (indexOfSuffix == -1)
            {
                indexOfSuffix = 0;
            }
            // return array with single file, check that has jack
            if (directory.getAbsolutePath().substring(indexOfSuffix).equals(".jack")) {
                fileArray.add(directory.getAbsolutePath());
            }
        }
        return fileArray.toArray(new String[fileArray.size()]);
    }

    /**
     * Replace the suffix of given input with given newSuffix
     * @param inputName the name to make output from
     * @param newSuffix the suffix that should be output (should include dot in beginning)
     * @return the output with new suffix, if suffix exists
     */
    private static String replaceSuffix(String inputName, String newSuffix) {
        String outputName = inputName;
        int indexOfSuffix = outputName.lastIndexOf('.');
        if (indexOfSuffix > 0) {
            outputName = outputName.substring(0, indexOfSuffix) + newSuffix;
        }
        return outputName;
    }


    public static void main(String[] args) {
        String[] fileNameArray = JackCompiler.getFileArray(args[0]);
        for (int i = 0; i < fileNameArray.length; i++)
        {
            String outputName = replaceSuffix(fileNameArray[i], ".vm");
            try(FileReader fr = new FileReader(fileNameArray[i]);BufferedReader br = new BufferedReader(fr);
                FileWriter fw = new FileWriter(outputName); BufferedWriter bw = new BufferedWriter(fw);)
            {
                CompilationEngine ce = new CompilationEngine(br, bw); //should create JackTokenizer

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
