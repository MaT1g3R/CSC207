package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This class reads and writes to files.
 *
 * @author Peijun
 */
public final class CsvReadWrite {

  /**
   * This is never called.
   */
  private CsvReadWrite() {
  }

  /**
   * This reads the file into an ArrayList, line by line.
   *
   * @param fileName The file name
   * @return An ArrayList representing the file
   */
  public static ArrayList<String> readCsv(final String fileName) {
    ArrayList<String> result = new ArrayList<>();
    Path pathToFile = Paths.get(fileName);
    try {
      BufferedReader br = Files.newBufferedReader(pathToFile);
      // read the first line from the text file
      String line = br.readLine();

      // loop until all lines are read
      while (line != null) {
        result.add(line);
        line = br.readLine();
      }

    } catch (IOException error) {
      return null;
    }
    return result;

  }


  /**
   * This overrides the file.
   *
   * @param content  An ArrayList, each item is a new line
   * @param fileName The name of the file
   */
  public static void overWrite(final ArrayList<String> content,
      final String fileName) {
    try {
      File file = new File(fileName);
      if (!file.exists() && !file.createNewFile()) {
        throw new IOException();
      }
      FileWriter output = new FileWriter(file);
      for (String s : content) {
        output.write(s + "\n");
      }
      output.close();
    } catch (IOException error) {
      System.out.println("Cannot read the file");
    }
  }
}
