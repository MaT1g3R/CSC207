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

    String eventFile = "/media/umi/HDD/Java_Projects/group_0376/project//16orders.txt";
    String warehouseFile = "/media/umi/HDD/Java_Projects/group_0376/project//initial.csv";
    String translationFile = "/media/umi/HDD/Java_Projects/group_0376/project//translation.csv";
    String traversalFile = "/media/umi/HDD/Java_Projects/group_0376/project//traversal_table.csv";
    String outFile = "/media/umi/HDD/Java_Projects/group_0376/project//";

    Simulator mySim = new Simulator(eventFile, warehouseFile, translationFile,
        traversalFile, outFile);
    mySim.run();


  }

}
