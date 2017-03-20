package warehouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Warehouse {

  private final int maxStock;
  private HashMap<String, Integer> inventory = new HashMap<>();
  private ArrayList<Truck> trucks = new ArrayList<>();
  private LinkedList<String> toBeReplenished = new LinkedList<>();
  private PickingRequestManager pickingRequestManager;
  private WorkerManager workerManager;
  private FileSystem fileSystem;
  private SkuTranslator skuTranslator;
  private String warehouseFile;
  private String outFile;

  /**
   * Initialize a new warehouse.
   *
   * @param fileSystem            the file system it uses
   * @param skuTranslator         the skuTranslator it uses
   * @param pickingRequestManager the pickingRequestManager it uses.
   * @param workerManager         the workerManager it uses.
   * @param warehouseFile         the file path to read inventory from.
   * @param outFile               the output file dir.
   * @param max                   the max stock level.
   */
  public Warehouse(
      FileSystem fileSystem,
      SkuTranslator skuTranslator,
      PickingRequestManager pickingRequestManager,
      WorkerManager workerManager,
      String warehouseFile,
      String outFile,
      int max
  ) {
    this.maxStock = max;
    this.warehouseFile = warehouseFile;
    this.outFile = outFile;
    this.fileSystem = fileSystem;
    this.skuTranslator = skuTranslator;
    this.workerManager = workerManager;
    this.pickingRequestManager = pickingRequestManager;
    this.fileSystem.writeAll();
    this.setInventory();
  }

  /**
   * Set the stock levels from the initial file.
   */
  private void setInventory() {
    for (String sku : skuTranslator.getAllSku()) {
      inventory.put(sku, maxStock);
    }
    if (!fileSystem.getFileContent(warehouseFile).isEmpty()) {
      for (String s : fileSystem.getFileContent(warehouseFile)) {
        ArrayList<String> line = new ArrayList<>(Arrays.asList(s.split(",")));
        String[] location = line.subList(0, 4).toArray(new String[4]);
        int amount = Integer.valueOf(line.get(4));
        this.inventory.put(skuTranslator.getSkuFromLocation(location), amount);
      }
    }
  }

  /**
   * Add 25 facsia to the inventory.
   *
   * @param sku the sku of the facsia to be added
   */
  public void addFacsia(String sku) {
    this.inventory.put(sku, this.inventory.get(sku) + 25);
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
   * Add a truck to the warehouse.
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
   * A getter for PickingRequestManager.
   *
   * @return PickingRequestManager
   */
  public PickingRequestManager getPickingRequestManager() {
    return pickingRequestManager;
  }

  /**
   * A getter for skuTranslater.
   *
   * @return skuTranslater.
   */
  public SkuTranslator getSkuTranslator() {
    return skuTranslator;
  }

  /**
   * Output the warehouse simulation results.
   */
  public void outPutResult() {
    ArrayList<String> finalCsv = new ArrayList<>();
    for (String sku : this.inventory.keySet()) {
      if (inventory.get(sku) < 30) {
        finalCsv.add(skuTranslator.getLocation(sku) + "," + String
            .valueOf(inventory.get(sku)));
      }
    }
    this.fileSystem.setWritingFile(outFile + "final"
        + ".csv", finalCsv);
    this.fileSystem.writeAll();
  }

  /**
   * A getter fot workerManager.
   *
   * @return workerManager
   */
  public WorkerManager getWorkerManager() {
    return workerManager;
  }

  /**
   * A method to log loaded orders.
   *
   * @param order the order to be logged
   */
  public void logLoading(String order) {
    fileSystem.getWritingFileForEdit(outFile + "orders"
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
