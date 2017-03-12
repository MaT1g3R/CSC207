package warehouse;

import java.util.Scanner;

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
    Scanner sc = new Scanner(System.in);
    String eventFile = null;
    String warehouseFile = null;
    String translationFile = null;
    String traversalFile = null;
    String outFile = null;

//    while (eventFile == null) {
//      System.out.println("Enter the file path for the events to be simulated.");
//      eventFile = sc.nextLine();
//    }
//
//    while (warehouseFile == null) {
//      System.out.println("\nEnter the file path for the initial state of the "
//          + "warehouse.");
//      warehouseFile = sc.nextLine();
//    }
//
//    while (translationFile == null) {
//      System.out.println("\nEnter the file path for the translation table.");
//      translationFile = sc.nextLine();
//    }
//
//    while (traversalFile == null) {
//      System.out.println("\nEnter the file path for the traversal table.");
//      traversalFile = sc.nextLine();
//    }
//
//    while (outFile == null) {
//      System.out.println("\nEnter the file path for final.csv");
//      outFile = sc.nextLine();
//    }
    eventFile = "/media/umi/HDD/Java_Projects/group0376/project/16orders.txt";
    warehouseFile = null;
    translationFile = "/media/umi/HDD/Java_Projects/group0376/project/translation.csv";
    traversalFile = "/media/umi/HDD/Java_Projects/group0376/project/traversal_table.csv";
    outFile = "/media/umi/HDD/Java_Projects/group0376/project/";

    Simulator mySim = new Simulator(eventFile, warehouseFile, translationFile,
        traversalFile, outFile);
    mySim.run();


  }

}
