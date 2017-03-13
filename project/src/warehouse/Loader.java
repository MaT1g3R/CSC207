package warehouse;

import java.io.File;
import java.util.LinkedList;

/**
 * A class to represent Loaders.
 *
 * @author Chaitanya
 */

public class Loader extends Worker {

  /**
   * Initializes a new Loader.
   *
   * @param name The Loader's name.
   * @param worksAt The Warehouse object of which this worker works at.
   */
  public Loader(String name, Warehouse worksAt) {
    super(name, worksAt);
  }

  /**
   * The action for a worker being ready.
   */
  @Override
  void ready() {
    resetScanCount();
    getWorksAt().readyLoader(this);
    setToBeScanned(getScanOrder());
    System.out.println("Loader " + getName() + " is ready to load.");
  }

  /**
   * Method for loading
   */
  void load() {
    if (getScanCount() == 8) {
      Truck truck = getWorksAt().getFirstNonFullTruck();
      LinkedList<Integer> skus = getCurrPickingReq().getProperSkus();
      int[] frontPallet = new int[4];
      int[] backPallet = new int[4];
      for (int i = 0; i < 4; i++) {
        frontPallet[i] = skus.get(i * 2);
        backPallet[i] = skus.get(i * 2 + 1);
      }
      if (truck.addCargo(frontPallet, backPallet, getCurrPickingReq().getId()
      )) {
        System.out.println("Loader " + getName() + " loaded picking request"
            + " " + String.valueOf(getCurrPickingReq().getId()));
        for (Order o : getCurrPickingReq().getOrders()) {
          CsvReadWrite.addLine(o.toString(), getWorksAt().getOutputFileDir()
              + File.separator + "orders.csv");
        }

      } else {
        System.out.println("Loader " + getName() + " could not load picking "
            + "request " + String.valueOf(getCurrPickingReq().getId()) + "\nThe"
            + " picking request is sent back to loading area.");
      }

    } else {
      getWorksAt().sendBackToPicking(getCurrPickingReq());
      System.out.println("The loader tried to load an incomplete picking "
          + "request, the picking request was sent to be re "
          + "picked instead.");
    }
  }
}
