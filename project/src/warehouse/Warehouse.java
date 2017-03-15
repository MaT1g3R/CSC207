package warehouse;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Warehouse {

  private final int maxStock;
  private String outputFileDir;
  private HashMap<Integer, Integer> inventory = new HashMap<>();
  private ArrayList<Truck> trucks = new ArrayList<>();
  private LinkedList<PickingRequest> outStandingPickingRequests = new
      LinkedList<>();
  private LinkedList<PickingRequest> marshallingArea = new LinkedList<>();
  private LinkedList<ArrayList<Object>> loadingArea = new LinkedList<>();
  private LinkedList<Order> orders = new LinkedList<>();
  private int pickingReqId = 0;
  private LinkedList<Integer> toBeReplenished = new LinkedList<>();
  private HashMap<String, Picker> pickers = new HashMap<>();
  private HashMap<String, Loader> loaders = new HashMap<>();
  private HashMap<String, Sequencer> sequencers = new HashMap<>();
  private HashMap<String, Replenisher> replenishers = new HashMap<>();


  /**
   * Initializes inventory values, Hashmap values, and sets max stock value.
   *
   * @param inputFilePath     path to initial inventory file
   * @param outputFileDirPath path to directory for output files
   * @param max               how much items a rack level holds
   */
  public Warehouse(String inputFilePath, String outputFileDirPath, int max) {
    this.outputFileDir = outputFileDirPath;
    this.maxStock = max;
    this.setInventoryFromFile(inputFilePath);
    this.trucks.add(new Truck(0));
    CsvReadWrite
        .overWrite(new ArrayList<>(), outputFileDir + File.separator + "orders"
            + ".csv");
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
        if (Integer.parseInt(s.get(4)) <= 5) {
          toBeReplenished.add(sku);
        }
      }
    }
  }

  /**
   * Method for adding an order to the system.
   *
   * @param order the order as a string to be added
   */
  public void addOrder(String order) {
    System.out.println(order + " has been added to the warehouse.");
    orders.add(new Order(order));
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
   * This method generate a new picking request.
   * This method should never be called if there's less than 4 orders in
   * outstanding Orders.
   *
   * @return A picking request from 4 orders.
   */
  private PickingRequest generatePickingReq() {
    ArrayList<Order> toBeSent = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      toBeSent.add(orders.pop());
    }

    return new PickingRequest(toBeSent, pickingReqId++);
  }

  /**
   * When a picker is ready this hands him a picking request.
   *
   * @param picker the Picker who's ready.
   */
  public void readyPicker(Picker picker) {
    if (outStandingPickingRequests.isEmpty() && orders.size() >= 4) {
      picker.setCurrPickingReq(generatePickingReq());
    } else if (!outStandingPickingRequests.isEmpty()) {
      picker.setCurrPickingReq(outStandingPickingRequests.pop());
    }
  }

  /**
   * When a sequencer it gets a picking request to sequence.
   *
   * @param sequencer the sequencer who's ready.
   */
  public void readySequencer(Sequencer sequencer) {
    if (!marshallingArea.isEmpty()) {
      sequencer.setCurrPickingReq(marshallingArea.pop());
    }
  }


  /**
   * When a loader is ready it gets a picking request to laod.
   *
   * @param loader the loader who's ready.
   */
  public void readyLoader(Loader loader) {
    if (!loadingArea.isEmpty()) {
      ArrayList<Object> toBeSent = loadingArea.pop();
      loader.setCurrPickingReq((PickingRequest) toBeSent.get(0));
      loader.setPallets((int[]) toBeSent.get(1), (int[]) toBeSent.get(2));
    }
  }


  /**
   * When a picker goes to marshalling area he dumps his picking request
   *
   * @param request the picking request to be marshalled.
   */
  public void sendToMarshalling(PickingRequest request) {
    marshallingArea.add(request);
  }

  /**
   * When a sequencer is finished it sends the picking request to loading
   *
   * @param request     the picking request to be loaded.
   * @param frontPallet the front pallet to be loaded.
   * @param backPallet  the back pallet to be loaded.
   */
  public void sendToLoading(PickingRequest request, int[] frontPallet, int[]
      backPallet) {
    loadingArea.add(new ArrayList<>(
        Arrays.asList(new Object[]{request, frontPallet, backPallet})));
    loadingArea.sort((p1, p2) -> ((PickingRequest) p1.get(0)).compareTo(
        (PickingRequest) p2.get(0)));
  }


  /**
   * When a picking request failed it's sent back to picking.
   *
   * @param request the failed picking request
   */
  public void sendBackToPicking(PickingRequest request) {
    outStandingPickingRequests.add(request);
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
   * A getter for outPutFileDir.
   *
   * @return outPutFileDir
   */
  public String getOutputFileDir() {
    return outputFileDir;
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
   * Add a loader to the warehouse.
   *
   * @param loader the loader to be added
   */
  public void addLoader(Loader loader) {
    loaders.put(loader.getName(), loader);
  }

  /**
   * Add a sequencer to the warehouse.
   *
   * @param sequencer the sequencer to be added
   */
  public void addSequencer(Sequencer sequencer) {
    sequencers.put(sequencer.getName(), sequencer);
  }

  /**
   * Add a picker to the warehouse.
   *
   * @param picker the picker to be added
   */
  public void addPicker(Picker picker) {
    pickers.put(picker.getName(), picker);
  }

  /**
   * Add a replenisher to the warehouse.
   *
   * @param replenisher the replenisher to be added
   */
  public void addReplenisher(Replenisher replenisher) {
    replenishers.put(replenisher.getName(), replenisher);
  }

  /**
   * Get a loader by name.
   *
   * @param name the name
   * @return the loader with that name
   */
  public Loader getLoader(String name) {
    return loaders.get(name);
  }

  /**
   * Get a picker by name.
   *
   * @param name the name
   * @return the picker with that name
   */
  public Picker getPicker(String name) {
    return pickers.get(name);
  }

  /**
   * Get a sequencer by name.
   *
   * @param name the name
   * @return the sequencer with that name
   */
  public Sequencer getSequencer(String name) {
    return sequencers.get(name);
  }

  /**
   * Get a replenisher by name.
   *
   * @param name the name
   * @return the replenisher with that name
   */
  public Replenisher getReplenisher(String name) {
    return replenishers.get(name);
  }
}
