package tests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import warehouse.CsvReadWrite;

/**
 * The unit tests for the CsvReadWrite class.
 */
public class CsvReadWriteTest {


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
  @Before
  public void setUp() throws IOException {
    FileWriter fw = new FileWriter(PATH);
    for (String data : defaultData) {
      fw.write(data + "\n");
    }
    fw.close();
  }

  /**
   * Delete the test file after each test.
   */
  @After
  public void tearDown() {
    File f = new File(PATH);
    f.delete();
  }

  /**
   * Test for the readCsv method.
   *
   * @throws IOException File not found, probably
   */
  @Test
  public void readCsv() throws IOException {
    ArrayList<String> result = CsvReadWrite.readCsv(PATH);
    Assert.assertEquals(defaultData, result);
  }

  /**
   * Test for the addLine method.
   *
   * @throws IOException File not found, probably
   */
  @Test
  public void addLine() throws IOException {
    CsvReadWrite.addLine("New Line!", PATH);
    ArrayList<String> result = CsvReadWrite.readCsv(PATH);
    ArrayList<String> expected = new ArrayList<>();
    for (String s : defaultData) {
      expected.add(s);
    }
    expected.add("New Line!");
    Assert.assertEquals(expected, result);
  }

  /**
   * Test for the overWrite method.
   *
   * @throws IOException File not found, probably
   */
  @Test
  public void overWrite() throws IOException {
    CsvReadWrite.overWrite(new ArrayList<>(
        Collections.singletonList("Content")), PATH);

    ArrayList<String> result = CsvReadWrite.readCsv(PATH);

    ArrayList<String> expected = new ArrayList<>(
        Collections.singletonList("Content"));

    Assert.assertEquals(expected, result);
  }

  /**
   * Test for when the file doesn't exist.
   *
   * @throws IOException I dunno
   */
  @Test
  public void overWriteEmpty() throws IOException {
    String newName = "Kappa.txt";
    CsvReadWrite.overWrite(defaultData, newName);
    ArrayList<String> result = CsvReadWrite.readCsv(newName);
    Assert.assertEquals(defaultData, result);
    File f = new File(newName);
    f.delete();
  }

  /**
   * Test for reading as arrays.
   *
   * @throws IOException File not found, probably
   */
  @Test
  public void readAsArrays() throws IOException {
    ArrayList<ArrayList<String>> expected = new ArrayList<>();
    for (String s : defaultData) {
      expected.add(new ArrayList<>(Arrays.asList(s.split(","))));
    }
    ArrayList<ArrayList<String>> result = CsvReadWrite.readAsArrays(PATH);
    Assert.assertEquals(expected, result);
  }
}
