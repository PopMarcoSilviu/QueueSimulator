package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public interface WriteToFile
{
    String filename = "log.txt";

    static void write(String toWrite)
    {
        File file = new File(filename);

        try
        {
            FileWriter fileWriter = new FileWriter(filename, true);
            fileWriter.append(toWrite);
            fileWriter.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
