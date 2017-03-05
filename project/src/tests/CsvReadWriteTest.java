package tests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import warehouse.CsvReadWrite;

/**
 * The unit tests for the CsvReadWrite class.
 */
class CsvReadWriteTest {

  /**
   * The path of the test file.
   */
  private static final String PATH = "translation_test.csv";
  /**
   * The default data used for the test file.
   */
  private final ArrayList<String> defaultData = new ArrayList<>(
      Arrays.asList(new String[]{"Colour,Model,SKU (front),SKU (back)",
          "White,S,1,2",
          "White,SE,3,4", "White,SES,5,6"}));

  /**
   * Writes the default data into the test file before each test.
   *
   * @throws IOException File not found, probably
   */
  @BeforeEach
  void setUp() throws IOException {
    FileWriter fw = new FileWriter(PATH);
    for (String data : defaultData) {
      fw.write(data + "\n");
    }
    fw.close();
  }

  /**
   * Delete the test file after each test.
   */
  @AfterEach
  void tearDown() {
    File f = new File(PATH);
    f.delete();
  }

  /**
   * Test for the readCsv method.
   *
   * @throws IOException File not found, probably
   */
  @Test
  void readCsv() throws IOException {
    ArrayList<String> result = CsvReadWrite.readCsv(PATH);
    Assertions.assertEquals(defaultData, result);
  }

  /**
   * Test for the addLine method.
   *
   * @throws IOException File not found, probably
   */
  @Test
  void addLine() throws IOException {
    CsvReadWrite.addLine("New Line!", PATH);
    ArrayList<String> result = CsvReadWrite.readCsv(PATH);
    ArrayList<String> expected = new ArrayList<>();
    for (String s : defaultData) {
      expected.add(s);
    }
    expected.add("New Line!");
    Assertions.assertEquals(expected, result);
  }

  /**
   * Test for the overWrite method.
   *
   * @throws IOException ile not found, probably
   */
  @Test
  void overWrite() throws IOException {
    CsvReadWrite.overWrite(new ArrayList<>(
        Collections.singletonList("Content")), PATH);

    ArrayList<String> result = CsvReadWrite.readCsv(PATH);

    ArrayList<String> expected = new ArrayList<>(
        Collections.singletonList("Content"));

    Assertions.assertEquals(expected, result);
  }
}
