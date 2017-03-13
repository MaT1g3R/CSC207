package warehouse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Warehouse {

  private final int maxStock;
  private String outputFileDir;
  private HashMap<Integer, Integer> inventory = new HashMap<>();
  private ArrayList<Truck> trucks = new ArrayList<>();


  /**
   * Initializes inventory values, Hashmap values, and sets max stock value.
   *
   * @param inputFilePath path to initial inventory file
   * @param outputFileDirPath path to directory for output files
   * @param max how much items a rack level holds
   */
  public Warehouse(String inputFilePath, String outputFileDirPath, int max) {
    this.outputFileDir = outputFileDirPath;
    this.maxStock = max;
    this.setInventoryFromFile(inputFilePath);
  }

  /**
   * This output the final inventory as csv to the outputFileDir.
   */
  public void outPutInventory() {
    ArrayList<String> result = new ArrayList<>();
    for (HashMap.Entry<Integer, Integer> entry : inventory.entrySet()) {
      if (entry.getValue() < maxStock) {
        String location = SkuTranslator.getLocation(entry.getKey());
        result.add(location + "," + entry.getValue());
      }
    }
    CsvReadWrite
        .overWrite(result, outputFileDir + File.separator + "final.csv");
  }

  /**
   * Sets non max stock levels.
   *
   * @param inputFilePath path to file with inventory information.
   */
  private void setInventoryFromFile(String inputFilePath) {
    ArrayList<ArrayList<String>> input = CsvReadWrite
        .readAsArrays(inputFilePath);

    for (int sku : SkuTranslator
        .getAllSku()) { //anything not in file is assumed to be max amount.
      this.inventory.put(sku, maxStock);
    }

    //getting input inventory if there's a file
    if (input != null) {
      for (ArrayList<String> s : input) {
        //{Zone, Aisle, Rack, Rack Level}
        int sku = SkuTranslator
            .getSkuFromLocation(s.subList(0, 4).toArray(new String[4]));
        this.inventory.put(sku, Integer.parseInt(s.get(4)));
      }
    }
  }


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

  }


}
