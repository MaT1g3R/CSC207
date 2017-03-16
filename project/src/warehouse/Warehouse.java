package warehouse;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
  private SkuTranslator skuTranslator;
  private String warehouseFile;
  private String outFile;

  /**
   * Initialize a new warehouse object.
   *
   * @param warehouseFile   the path for initial.csv
   * @param translationFile the path for translation.csv
   * @param traversalFile   the path for traversal_table.csv
   * @param outFile         the output dir
   * @param max             the max stock level for the racks
   */
  public Warehouse(
      String warehouseFile,
      String translationFile,
      String traversalFile,
      String outFile,
      int max
  ) {
    this.maxStock = max;
    this.trucks.add(new Truck(0));
    this.warehouseFile = warehouseFile;
    this.outFile = outFile;
    this.fileSystem = new FileSystem(
        new String[]{
            this.warehouseFile,
            translationFile,
            traversalFile
        },
        new String[]{
            this.outFile + File.separator + "final.csv",
            this.outFile + File.separator + "orders.csv"
        });
    this.skuTranslator = new SkuTranslator(
        this.fileSystem.getFileContent(traversalFile),
        this.fileSystem.getFileContent(translationFile)
    );
    this.fileSystem.writeAll();
    this.setInventory();
  }

  /**
   * A setter for pickingRequestManager.
   */
  public void setPickingRequestManager(
      PickingRequestManager pickingRequestManager) {
    this.pickingRequestManager = pickingRequestManager;
  }

  /**
   * A setter for workerManager.
   */
  public void setWorkerManager(WorkerManager workerManager) {
    this.workerManager = workerManager;
  }

  /**
   * Set the stock levels from the initial file.
   */
  private void setInventory() {
    for (int sku : skuTranslator.getAllSku()) {
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
    for (int sku : this.inventory.keySet()) {
      if (inventory.get(sku) < 30) {
        finalCsv.add(skuTranslator.getLocation(sku) + "," + String
            .valueOf(inventory.get(sku)));
      }
    }
    this.fileSystem.setWritingFile(this.outFile + File.separator + "final"
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
    fileSystem.getWritingFileForEdit(outFile + File.separator + "orders"
        + ".csv").add(order);
  }
}
