package warehouse;

/**
 * Created by peijun.
 */
public class Main {

  /**
   * The main run class.
   *
   * @param args meh
   */
  public static void main(String[] args) {
    String eventFile = "../events.txt";
    String warehouseFile = "../initial.csv";
    String translationFile = "../translation.csv";
    String traversalFile = "../traversal_table.csv";
    String outFile = "../";
    Simulator mySim = new Simulator(eventFile, warehouseFile, translationFile,
        traversalFile, outFile);
    mySim.run();
  }

}
