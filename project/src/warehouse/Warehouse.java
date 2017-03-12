package warehouse;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class Warehouse {

  private final int maxStock;
  private int numPickingRequests = 0;

  //orders that haven't been made into a picking request yet
  private ArrayList<Order> outstandingOrders = new ArrayList<>();


  //The directory where final.csv and orders.csv will be saved
  private String outputFileDir;
  //maps sku:quantity
  private HashMap<Integer, Integer> inventory = new HashMap<>();
  //The field maps (job title as lower case string):(worker name, proper case String):(instance)
  private HashMap<String, HashMap<String, Worker>> workers = new HashMap<>();

  // job title of request as string: instance of request as linkedList
  private HashMap<String, LinkedList<Object>> requests = new HashMap<>();
  private ArrayList<Truck> trucks = new ArrayList<>();

  public Warehouse(String inputFilePath, String outputFileDirPath) {
    this(inputFilePath, outputFileDirPath, 30);
  }

  /**
   * Initializes inventory values, Hashmap values, and sets max stock value.
   * @param inputFilePath path to initial inventory file
   * @param outputFileDirPath path to directory for output files
   * @param max how much items a rack level holds
   */
  public Warehouse(String inputFilePath, String outputFileDirPath, int max) {
    this.workers.put("picker", new HashMap<>());
    this.workers.put("replenisher", new HashMap<>());
    this.workers.put("loader", new HashMap<>());
    this.workers.put("sequencer", new HashMap<>());
    this.requests.put("picker", new LinkedList<>());
    this.requests.put("replenisher", new LinkedList<>());
    this.requests.put("loader", new LinkedList<>());
    this.requests.put("sequencer", new LinkedList<>());
    this.outputFileDir = outputFileDirPath;
    this.setInventoryFromFile(inputFilePath);
    this.maxStock = max;
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
   * @param inputFilePath path to file with inventory information.
   */
  private void setInventoryFromFile(String inputFilePath) {
    ArrayList<ArrayList<String>> input = CsvReadWrite
        .readAsArrays(inputFilePath);

    for (int sku : SkuTranslator.getAllSku()) { //anything not in file is assumed to be max amount.
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


  /**
   * Looks through requests for <type></type> and assigns any ready worker or
   * <type></type> to it.
   *
   * @param type can be replenisher, picker, loader, or sequencer.
   */
  public void assignWorkers(String type) {
    LinkedList requests;
    Collection<Worker> people;
    people = workers.get(type.toLowerCase()).values();
    requests = this.requests.get(type.toLowerCase());
    for (Worker worker : people) {
      if (worker.isReady() && !requests.isEmpty()) {
        //Replenishers start method take int instead of PickingRequest
        if (worker instanceof Replenisher) {
          ((Replenisher) worker).start((int) requests.remove());
        } else {
          PickingRequest request = (PickingRequest) requests.peek();
          //loaders can only get assigned requests in order of pickingRequest ID and
          //pickingRequest has to be load ready
          if (request != null && (!(worker instanceof Loader) || request
              .getLoadReady())) {
            worker.start(request);
            requests.remove();
          }
        }
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
    int amount = inventory.get(sku);
    // Nothing happens if you try to remove something with 0 quantity
    if (amount > 0) {
      System.out.println(
          "Removed one sku " + Integer.toString(sku) + " from inventory.");
      this.inventory.put(sku, amount - 1);
    }

    // If <= 5, and theres no request to replenish it, it needs to be replenished
    if (this.inventory.get(sku) <= 5 && !requests.get("replenisher").contains(sku)) {
      addReplenishRequest(sku);
    }
  }

  public Worker getWorker(String name, String title) {
    return workers.get(title).get(name);
  }

  /**
   * Adds a replenish request, to the end of the list of pending
   * replenishRequests. Prints that it has been added.
   *
   * @param sku the sku for the request
   */
  public void addReplenishRequest(Integer sku) {
    System.out
        .println("Replenish request for sku " + Integer.toString(sku) + ".");
    requests.get("replenisher").add(new Integer(sku));
    this.assignWorkers("replenisher");


  }

  /**
   * Adds a sequencing request to the list of requests to be processed.
   * @param request the request to be processed.
   */
  public void addSequencingRequest(PickingRequest request) {
    requests.get("sequencer").add(request);
    this.assignWorkers("sequencer");

  }

  /**
   * Adds picking request to the front of the queue.
   */
  public void addUnpickedPickingRequest(PickingRequest request) {
    requests.get("picker").add(0, request);
    this.assignWorkers("picker");
    System.out.println("Added high-priority PickingRequest " + request.getId());
  }

  /**
   * Creates a new PickingRequest and adds it to the end of the list of unPickedPicking request
   * and loading requests.
   *
   * @param orders used to create picking request. Should have four orders.
   */
  public void createPickingRequest(ArrayList<Order> orders) {
    PickingRequest request = new PickingRequest(orders, numPickingRequests);
    requests.get("picker").add(request);
    numPickingRequests++;
    requests.get("loader").add(request);
    this.assignWorkers("picker");
    System.out
        .println("Added new PickingRequest " + request.getId());
  }

  /**
   *  Adds an order to the list of outstanding orders. If there are 4, then a new picking request
   *  is generated.
   * @param order the order to be added.
   */
  public void addOrder(Order order) {
    outstandingOrders.add(order);
    System.out.println("Added order " + order.toString());
    if (outstandingOrders.size() == 4) {
      createPickingRequest(outstandingOrders);
      outstandingOrders = new ArrayList<Order>();
    }
  }

  public HashMap<Integer, Integer> getInventory() {
    return inventory;
  }

  public ArrayList<Truck> getTrucks() {
    return trucks;
  }

  public void addWorker(final Worker person, String type) {
    this.workers.get(type).put(person.getName(), person);
  }

  public String getOutputFileDir() {
    return outputFileDir;
  }

}
