import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Era on 19/04/2016.
 */
public class VMTranslator {
    private static String fileName;
    public static void translate(String fileToTranslate, CodeWriter cw) {
        try (FileReader fr1 = new FileReader(fileToTranslate);
             BufferedReader br1 = new BufferedReader(fr1)) {
            Parser p = new Parser(br1);
            while (p.hasMoreCommands())
            {
                p.advance();
                if (p.commandType() == Parser.CommandType.C_ARITHMETIC)
                {
                    cw.writeArithmetic(p.arg1());
                }
                else{
                    cw.WritePushPop(p.commandType(), p.arg1(), p.arg2());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Problem!!");
        }
    }

    private static String[] getFileArray(String pathName) {
        File directory = new File(pathName);

        List<String> fileArray = new ArrayList<String>();
        if (directory.isDirectory()) {
            VMTranslator.fileName= pathName + ".asm";
            for (File file : directory.listFiles()) {
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
        } else {
            // return array with single file, check that has asm vm
            int indexOfSuffix = pathName.lastIndexOf(".");
            if (pathName.substring(indexOfSuffix).equals(".vm")) {
                VMTranslator.fileName = directory.getName() + ".asm";
                fileArray.add(directory.getAbsolutePath());
                System.out.println(directory.getAbsolutePath());
            }
        }
        return fileArray.toArray(new String[fileArray.size()]);
    }

    public static void main(String[] args) {
        String[] fileNameArray = getFileArray(args[0]);
        try(FileWriter fw1 = new FileWriter(VMTranslator.fileName);
            BufferedWriter bw = new BufferedWriter(fw1)){
            CodeWriter cw = new CodeWriter(bw);
        for (String fileName : fileNameArray) {
            // Check if this is correct
            int indexOfSuffix = fileName.lastIndexOf(".");
            cw.setFileName(fileName.substring(0, indexOfSuffix));
                translate(fileName, cw);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

