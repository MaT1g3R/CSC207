package warehouse;

import java.io.File;
import java.util.ArrayList;
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
   * Loads pallets onto the first available truck if the Loader and PickingRequest are both ready.
   * Creates a new Truck if none are available.
   */
  public void load() {
    if (this.getIsReady() || !this.toBeScanned.isEmpty()) {
      // If doesn't have a duty  or didn't finish scanning, do nothing.
      System.out.println(
          "Loader " + super.getName() + " failed to load");
    } else {
      int[] frontPallet = currPickingReq.getSequencedPallets()[0];
      int[] backPallet = currPickingReq.getSequencedPallets()[1];
      ArrayList<Truck> trucks = worksAt.getTrucks();
      int truckNum = trucks.size();
      // Creates a new truck if none exist or the latest one is full.
      if (truckNum == 0 || trucks.get(truckNum - 1).isFull()) {
        trucks.add(new Truck());
        truckNum++;
      }
      // Assuming sequencer outputs in order.
      trucks.get(truckNum - 1).addCargo(frontPallet, backPallet);
      System.out.println(
          "Loader " + super.getName() + " loaded PickingRequest " + currPickingReq.getId());
      outputToCsv();
    }
  }

  /**
   * Outputs the current orders to a csv.
   */
  private void outputToCsv() {
    ArrayList<Order> currentOrders = currPickingReq.getOrders();
    for (int i = 0; i < currentOrders.size(); i++) {
      CsvReadWrite.addLine(currentOrders.get(i).toString(), worksAt.getOutputFileDir()
          + File.separator +"orders.csv");
    }
  }

  /**
   * Returns a LinkedList containing the SKU's in order of which they should be scanned.
   */
  @Override
  protected LinkedList<Integer> getScanOrder() {
    LinkedList<Integer> scanOrder = new LinkedList<>();
    ArrayList<Order> currentOrders = currPickingReq.getOrders();
    for (int i = 0; i < currentOrders.size(); i++) {
      scanOrder.add(currentOrders.get(i).getSkus()[0]);
    }
    for (int i = 0; i < currentOrders.size(); i++) {
      scanOrder.add(currentOrders.get(i).getSkus()[1]);
    }
    return scanOrder;
  }

}
