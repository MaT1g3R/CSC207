package warehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

public class Warehouse {


  private HashMap<Integer, Integer> inventory;
  private String loggerPath;
  private HashMap<String, Picker> pickers;
  private HashMap<String, Loader> loaders;
  private HashMap<String, Sequencer> sequencers;
  private HashMap<String, Replenisher> replenishers;
  private ArrayList<Integer> replenishRequests;
  private ArrayList<PickingRequest> unPickedPickingRequests;
  private ArrayList<PickingRequest> sequenceRequests;

  //Items are queued in order of pickingRequestID
  private Queue<PickingRequest> loadingRequests;

  public Warehouse(String loggerPath) {
    this.loggerPath = loggerPath;
  }

  public void update(PickingRequest request) {

  }

  private void eventLogger() {

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
    //Nothing happens if you try to remove something with 0 quantity
    if (amount > 0) {
      System.out.println(
          "Removed one sku " + Integer.toString(sku) + " from inventory.");
      this.inventory.put(sku, amount - 1);
      //If  <= 5, and theres no request to replenish it, it needs to be replenished
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

  public void addOrder(Order order) {
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


  public void addPicker(Picker picker) {
  }

  public void addLoader(Loader loader) {
  }

  public void addSequencer(Sequencer sequencer) {
  }

  public void addReplenisher(Replenisher replenisher) {
  }

  public ArrayList<Truck> getTrucks() {
    return null;
  }

}
