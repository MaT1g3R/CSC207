package warehouse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Warehouse {

  private int numPickingRequests = 0;
  private ArrayList<Order> outstandingOrders;
  private HashMap<Integer, Integer> inventory = new HashMap<>();
  private HashMap<String, Picker> pickers = new HashMap<>();
  private HashMap<String, Loader> loaders = new HashMap<>();
  private HashMap<String, Sequencer> sequencers = new HashMap<>();
  private HashMap<String, Replenisher> replenishers = new HashMap<>();
  private LinkedList<Integer> replenishRequests = new LinkedList<>();
  private LinkedList<PickingRequest> unPickedPickingRequests = new
      LinkedList<>();
  private LinkedList<PickingRequest> sequenceRequests = new LinkedList<>();
  private ArrayList<Truck> trucks = new ArrayList<>();

  // Items are queued in order of pickingRequestID
  private Queue<PickingRequest> loadingRequests = new LinkedList<>();

  public Warehouse(String warehouseFilePath) {
    for (ArrayList<String> s : CsvReadWrite.readAsArrays(warehouseFilePath)) {
      //{Zone, Aisle, Rack, Rack Level}
      SkuTranslator.getSkuFromLocation((String[]) s.subList(0, 3).toArray());
    }
  }

  private void assignNonReplenishers(String type) {
    Queue<PickingRequest> requests = new LinkedList<>();
    Collection<Worker> people = new LinkedList<>();
    if (type.toLowerCase().equals("sequencer")) {
      requests = sequenceRequests;
      people = new LinkedList<>(sequencers.values());
    } else if (type.toLowerCase().equals("picker")) {
      requests = unPickedPickingRequests;
      people = new LinkedList<>(pickers.values());

    } else if (type.toLowerCase().equals("loader")) {
      requests = loadingRequests;
      people = new LinkedList<>(loaders.values());
    }
    for (Worker worker : people) {
      if (worker.getIsReady() && !requests.isEmpty()) {
        PickingRequest request = requests.peek();
        if (!(worker instanceof Loader) || request.getLoadReady()) {
          requests.remove();
          worker.start(request);
        }
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
    if (type.toLowerCase().equals("replenisher")) {
      assignReplenishers();
    } else {
      assignNonReplenishers(type);
    }
  }


  private void assignReplenishers() {
    Queue<Integer> requests = replenishRequests;
    for (Replenisher replenisher : replenishers.values()) {
      if (replenisher.getIsReady() && !requests.isEmpty()) {
        replenisher.start(requests.remove());
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
   * @param sku: the sku being removed
   */
  public void removeFascia(int sku) {
    int amount = inventory.get(sku);
    // Nothing happens if you try to remove something with 0 quantity
    if (amount > 0) {
      System.out.println(
          "Removed one sku " + Integer.toString(sku) + " from inventory.");
      this.inventory.put(sku, amount - 1);
      // If <= 5, and theres no request to replenish it, it needs to be replenished
      if (this.inventory.get(sku) <= 5 && replenishRequests.contains(sku)) {
        addReplenishRequest(sku);
      }
    }
  }

  /**
   * Adds a replenish request, to the end of the list of pending
   * replenishRequests. Prints that it has been added.
   *
   * @param sku: the sku for the request
   */
  public void addReplenishRequest(int sku) {
    System.out
        .println("Replenish request for sku " + Integer.toString(sku) + ".");
    replenishRequests.add(sku);

  }

  public void addSequencingRequest(PickingRequest request) {
    sequenceRequests.add(request);
  }

  /**
   * Adds picking request to the front of the queue.
   */
  public void addUnpickedPickingRequest(PickingRequest request) {
    unPickedPickingRequests.add(0, request);
    System.out.println("Added PickingRequest " + request.getId() + "to front.");
  }

  /**
   * Creates a new PickingRequest and adds it to the back of the queue;
   */
  public void createPickingRequest(ArrayList<Order> orders) {
    PickingRequest request = new PickingRequest(orders, numPickingRequests);
    unPickedPickingRequests.add(request);
    numPickingRequests++;
    loadingRequests.add(request);
    System.out
        .println("Added new PickingRequest " + request.getId() + "to end.");
  }

  public void addOrder(Order order) {
    outstandingOrders.add(order);
    System.out.println("Added order " + order.toString());
    if (outstandingOrders.size() == 4) {
      createPickingRequest(outstandingOrders);
      outstandingOrders = new ArrayList<Order>();
    }
  }

  public Picker getPickerByName(final String name) {
    return this.pickers.get(name);
  }

  public Loader getLoaderByName(final String name) {
    return this.loaders.get(name);
  }

  public Sequencer getSequencerByName(final String name) {
    return this.sequencers.get(name);
  }

  public Replenisher getReplenisherByName(final String name) {
    return this.replenishers.get(name);
  }

  public HashMap<Integer, Integer> getInventory() {
    return inventory;
  }

  public ArrayList<Truck> getTrucks() {
    return trucks;
  }

  public void addPicker(final Picker picker) {
    this.pickers.put(picker.getName(), picker);
  }

  public void addLoader(final Loader loader) {
    this.loaders.put(loader.getName(), loader);
  }

  public void addSequencer(final Sequencer sequencer) {
    this.sequencers.put(sequencer.getName(), sequencer);
  }

  public void addReplenisher(final Replenisher replenisher) {
    this.replenishers.put(replenisher.getName(), replenisher);
  }
}
