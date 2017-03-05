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


class CsvReadWriteTest {

  //The path to the test file
  private final String PATH = "translation_test.csv";
  //The default data for the test file
  private final ArrayList<String> defaultData = new ArrayList<>(
      Arrays.asList(new String[]{"Colour,Model,SKU (front),SKU (back)",
          "White,S,1,2",
          "White,SE,3,4", "White,SES,5,6"}));


  @BeforeEach
  void setUp() throws IOException {
    //Writes the default data into the test file before each test
    FileWriter fw = new FileWriter(PATH);
    for (String data : defaultData) {
      fw.write(data + "\n");
    }
    fw.close();
  }

  @AfterEach
  void tearDown() {
    //Delete the test file after each test
    File f = new File(PATH);
    f.delete();
  }

  @Test
  void readCsv() {
    ArrayList<String> result = CsvReadWrite.readCsv(PATH);
    Assertions.assertEquals(defaultData, result);
  }

  @Test
  void addLine() {
    CsvReadWrite.addLine("Content", PATH);
    ArrayList<String> result = CsvReadWrite.readCsv(PATH);
    ArrayList<String> expected = new ArrayList<>();
    for (String s : defaultData) {
      expected.add(s);
    }
    expected.add("Content");
    Assertions.assertEquals(expected, result);
  }

  @Test
  void overWrite() {
    CsvReadWrite.overWrite(new ArrayList<>(Collections.singletonList("Content")), PATH);
    ArrayList<String> result = CsvReadWrite.readCsv(PATH);
    ArrayList<String> expected = new ArrayList<>(Collections.singletonList("Content"));
    Assertions.assertEquals(expected, result);
  }
}