package warehouse;

import java.util.ArrayList;
import java.util.HashMap;

public class Warehouse {


  private HashMap<Integer, Integer> inventory;
  private String loggerPath;
  private HashMap<String, Picker> pickers;
  private HashMap<String, Loader> loaders;
  private HashMap<String, Sequencer> sequencers;
  private HashMap<String, Replenisher> replenishers;

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

  public void addOrder(Order order) {
  }

  public HashMap<String, Picker> getPickers() {
    return pickers;
  }

  public HashMap<String, Loader> getLoaders() {
    return loaders;
  }

  public HashMap<String, Sequencer> getSequencers() {
    return sequencers;
  }

  public HashMap<String, Replenisher> getReplenishers() {
    return replenishers;
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
