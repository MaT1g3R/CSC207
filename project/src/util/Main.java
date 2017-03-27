package util;

import java.io.File;

/**
 * The main run class.
 *
 * @author Peijun
 */
public class Main {

  /**
   * The main run class.
   *
   * @param args the path to the event file.
   */
  public static void main(String[] args) {
    String eventFile = args[0];
    if (args.length == 1 && new File(eventFile).exists()) {
      String warehouseFile = "../initial.csv";
      String translationFile = "../translation.csv";
      String traversalFile = "../traversal_table.csv";
      String outFile = "../";
      Simulator mySim = new Simulator(eventFile, warehouseFile, translationFile,
          traversalFile, outFile);
      mySim.run();
    } else {
      throw new UnsupportedOperationException("Please enter a vaild path to "
          + "the event file.");
    }
  }
}

