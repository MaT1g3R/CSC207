package util;

/**
 * The main run class.
 *
 * @author Peijun
 */
public class Main {

  /**
   * The main run class.
   *
   * @param args meh
   */
  public static void main(String[] args) {
    try {
      String eventFile = args[0];
      String warehouseFile = "../initial.csv";
      String translationFile = "../translation.csv";
      String traversalFile = "../traversal_table.csv";
      String outFile = "../";
      Simulator mySim = new Simulator(eventFile, warehouseFile, translationFile,
          traversalFile, outFile);
      mySim.run();
    } catch (IndexOutOfBoundsException | NullPointerException ex) {
      System.out.println("Please provide a vaild path to the event file.");
    }
  }
}

