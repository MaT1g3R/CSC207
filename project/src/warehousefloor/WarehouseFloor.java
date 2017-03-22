package warehousefloor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import util.MasterSystem;

public class WarehouseFloor {

  private final int maxStock;
  private HashMap<String, Integer> inventory = new HashMap<>();
  private ArrayList<Truck> trucks = new ArrayList<>();
  private LinkedList<String> toBeReplenished = new LinkedList<>();
  private String warehouseFile;
  private String outFile;
  private MasterSystem masterSystem;

  /**
   * Initialize a new warehousefloor.
   *
   * @param warehouseFile the file path to read inventory from.
   * @param outFile       the output file dir.
   * @param max           the max stock level.
   */
  public WarehouseFloor(
      String warehouseFile,
      String outFile,
      MasterSystem masterSystem,
      int max
  ) {
    this.masterSystem = masterSystem;
    this.maxStock = max;
    this.warehouseFile = warehouseFile;
    this.outFile = outFile;
  }

  /**
   * Set the stock levels from the initial file.
   */
  public void setInventory() {
    for (String sku : masterSystem.getSkuTranslator().getAllSku()) {
      inventory.put(sku, maxStock);
    }
    if (!masterSystem.getFileSystem().getFileContent(warehouseFile).isEmpty()) {
      for (String s : masterSystem.getFileSystem()
          .getFileContent(warehouseFile)) {
        ArrayList<String> line = new ArrayList<>(Arrays.asList(s.split(",")));
        String[] location = line.subList(0, 4).toArray(new String[4]);
        int amount = Integer.valueOf(line.get(4));
        this.inventory
            .put(masterSystem.getSkuTranslator().getSkuFromLocation(location),
                amount);
      }
    }
  }

  /**
   * Add facsias to the inventory.
   *
   * @param sku    the sku of the facsia to be added
   * @param amount the amount to be added
   */
  public void addFacsia(String sku, int amount) {
    this.inventory.put(sku, this.inventory.get(sku) + amount);
  }

  /**
   * Removes one quantity of SKU from inventory if there are any. If the
   * quantity is <= 5 a replenish request is created for that SKU if there isn't
   * any.
   *
   * @param sku the sku being removed
   */
  public void removeFascia(String sku) {
    if (inventory.get(sku) < 1) {
      System.out.println(
          "An attempt of trying to remove fascia of SKU " + sku
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
   * Add a truck to the warehousefloor.
   *
   * @param truck the truck to be added.
   */
  public void addTruck(Truck truck) {
    trucks.add(truck);
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
   * Output the warehousefloor simulation results.
   */
  public void outPutResult() {
    ArrayList<String> finalCsv = new ArrayList<>();
    for (String sku : this.inventory.keySet()) {
      if (inventory.get(sku) < 30) {
        finalCsv.add(
            masterSystem.getSkuTranslator().getLocation(sku) + "," + inventory
                .get(sku));
      }
    }
    masterSystem.getFileSystem().setWritingFile(outFile + "final"
        + ".csv", finalCsv);
    masterSystem.getFileSystem().writeAll();
  }

  /**
   * A method to log loaded orders.
   *
   * @param order the order to be logged
   */
  public void logLoading(String order) {
    masterSystem.getFileSystem().getWritingFileForEdit(outFile + "orders"
        + ".csv").add(order);
  }

  /**
   * Return and remove the first element of toBeReplenished.
   *
   * @return first element of toBeReplenished
   */
  public String popReplenished() {
    return toBeReplenished.isEmpty() ? null : toBeReplenished.pop();
  }
}
