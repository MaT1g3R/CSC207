package warehouse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This class reads and writes to files.
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
   * @throws IOException File not found, probably
   */
  public static ArrayList<String> readCsv(final String fileName)
      throws IOException {
    ArrayList<String> result = new ArrayList<>();
    Path pathToFile = Paths.get(fileName);
    BufferedReader br = Files.newBufferedReader(pathToFile);
    // read the first line from the text file
    String line = br.readLine();

    // loop until all lines are read
    while (line != null) {
      result.add(line);
      line = br.readLine();
    }

    return result;

  }

  /**
   * This appends a line to the file.
   *
   * @param content The string to be appended
   * @param fileName The name of the file
   * @throws IOException File not found, probably
   */
  public static void addLine(final String content, final String fileName)
      throws IOException {
    Writer output = new BufferedWriter(new FileWriter(fileName, true));
    output.append(content);
    output.close();
  }

  /**
   * This overrides the file.
   *
   * @param content An ArrayList, each item is a new line
   * @param fileName The name of the file
   * @throws IOException File not found, probably
   */
  public static void overWrite(final ArrayList<String> content,
      final String fileName) throws IOException {
    FileWriter fw = new FileWriter(fileName);
    for (String data : content) {
      fw.write(data + "\n");
    }
    fw.close();
  }

}
