package warehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Warehouse {

  private final int maxStock;
  private HashMap<Integer, Integer> inventory = new HashMap<>();
  private ArrayList<Truck> trucks = new ArrayList<>();
  private LinkedList<Integer> toBeReplenished = new LinkedList<>();
  private PickingRequestManager pickingRequestManager;
  private WorkerManager workerManager;
  private FileSystem fileSystem;

  public Warehouse(PickingRequestManager pickingRequestManager,
      WorkerManager workerManager,
      FileSystem fileSystem, int
      max) {
    this.maxStock = max;
    this.trucks.add(new Truck(0));
    this.pickingRequestManager = pickingRequestManager;
    this.workerManager = workerManager;
    this.fileSystem = fileSystem;
  }


  /**
   * Add 25 facsia to the inventory.
   *
   * @param sku the sku of the facsia to be added
   */
  public void addFacsia(int sku) {
    this.inventory.put(sku, this.inventory.get(sku) + 25);
  }

  /**
   * Removes one quantity of SKU from inventory if there are any. If the
   * quantity is <= 5 a replenish request is created for that SKU if there isn't
   * any.
   *
   * @param sku the sku being removed
   */
  public void removeFascia(int sku) {
    if (inventory.get(sku) < 1) {
      System.out.println(
          "An attempt of trying to remove fascia of SKU " + String.valueOf(sku)
              + " from an empty rack was made");
    } else {
      inventory.put(sku, inventory.get(sku) - 1);
      System.out.println("A fascia of SKU " + String.valueOf(sku) + " was "
          + "taken.");
    }
    if (inventory.get(sku) <= 5 && !toBeReplenished.contains(sku)) {
      // put a replenish request in the system when the amout is <= 5
      toBeReplenished.add(sku);
    }
  }


  /**
   * Get the first truck that's not full in all trucks.
   *
   * @return the not full truck
   */
  public Truck getFirstNonFullTruck() {
    for (Truck t : trucks) {
      if (!t.isFull()) {
        return t;
      }
    }
    return null;
  }


  /**
   * A getter for toBeReplenished.
   *
   * @return toBeReplenished
   */
  public LinkedList<Integer> getToBeReplenished() {
    return toBeReplenished;
  }


}
