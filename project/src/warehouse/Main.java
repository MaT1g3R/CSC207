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

    String eventFile = "../group0376/project/16orders.txt";
    String warehouseFile = "../group0376/project/initial.csv";
    String translationFile = "../group0376/project"
        + "/translation.csv";
    String traversalFile = "../group0376/project"
        + "/traversal_table.csv";
    String outFile = "../group0376/project/";

    Simulator mySim = new Simulator(eventFile, warehouseFile, translationFile,
        traversalFile, outFile);
    mySim.run();


  }

}
