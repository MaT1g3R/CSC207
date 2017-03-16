package tests;

import java.io.File;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;
import warehouse.CsvReadWrite;
import warehouse.Simulator;


/**
 * A test for the Simulator class.
 *
 * @author Peijun
 */
public class SimulatorTest {

  /**
   * A test for the simulator run method.
   */
  @Test
  public void run() {
    String eventFile = "../events.txt";
    String warehouseFile = "../initial.csv";
    String translationFile = "../translation.csv";
    String traversalFile = "../traversal_table.csv";
    String outFile = "tests/";
    Simulator simulator = new Simulator(eventFile, warehouseFile,
        translationFile, traversalFile, outFile);
    simulator.run();

    ArrayList<String> expectedFinal = CsvReadWrite
        .readCsv("tests/expected_final.csv");
    ArrayList<String> expectedOrders = CsvReadWrite
        .readCsv("tests/expected_orders.csv");
    ArrayList<String> actualFinal = CsvReadWrite.readCsv("tests/final.csv");
    ArrayList<String> actualOrders = CsvReadWrite.readCsv("tests/orders.csv");
    File finalFile = new File("tests/final.csv");
    finalFile.delete();
    Assert.assertTrue(expectedFinal.equals(actualFinal) && expectedOrders
        .equals(actualOrders));
  }

}