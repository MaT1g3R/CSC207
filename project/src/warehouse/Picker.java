package warehouse;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Tasbir on 2017-03-11.
 */
public class Picker extends Worker {

  private ArrayList<String> locations;

  /**
   * Initialize an instance of a picker.
   *
   * @param name the name of the picker
   * @param worksAt where it works at
   */
  Picker(String name, Warehouse worksAt) {
    super(name, worksAt);
  }

  /**
   * When a picker is ready, give it a picking request, a list of locations,
   * and a scan order.
   */
  @Override
  void ready() {
    getWorksAt().readyPicker(this);
    ArrayList<Integer> toBeOptimized = new ArrayList<>();
    for (Order o : getCurrPickingReq().getOrders()) {
      toBeOptimized.add(o.getSkus()[0]);
      toBeOptimized.add(o.getSkus()[1]);
    }
    locations = WarehousePicking.optimize(toBeOptimized);
    setToBeScanned(getScanOrder());
    // For printing
    String displayString = "Picker " + getName() + " is ready, it will go to "
        + "locations:\n";
    for (String loc : locations) {
      displayString += loc + "\n";
    }
    System.out.println(displayString);
  }

  /**
   * The expected scan order for the worker is of course the order of the
   * locations.
   *
   * @return The expected scan order
   */
  @Override
  protected LinkedList<Integer> getScanOrder() {
    LinkedList<Integer> res = new LinkedList<>();
    for (String location : locations) {
      String[] toBeTr = location.split(",");
      res.add(SkuTranslator.getSkuFromLocation(toBeTr));
    }
    return res;
  }

  /**
   * When a picker scans it's assumed it took the fascia off the rack.
   * It will add the fascia to the picking request if it matched the scan,
   * And will throw it out if it didn't.
   *
   * @param sku the sku scanned.
   */
  @Override
  void scan(int sku) {
    getWorksAt().removeFascia(sku);
    if (scanResult(sku)) {
      addScanCount();
      getToBeScanned().removeFirst();
    }
  }

  /**
   * The result of a scan, note this doesnt pop the element when a worker
   * fails because it will keep trying to scan for a matching SKU.
   *
   * @param sku the sku scanned.
   * @return true if the scan matched else false.
   */
  @Override
  boolean scanResult(int sku) {
    System.out.println(this.getClass().getSimpleName() + " " + getName() + " "
        + "preformed a scan action!");
    int expected = getToBeScanned().getFirst();
    if (sku == expected) {
      System.out
          .println("Scan of SKU " + String.valueOf(sku) + " matched with"
              + " the expected result");
      return true;
    } else {
      System.out
          .println("Scan of SKU " + String.valueOf(sku) + " did not match "
              + "with the expected result of SKU " + String
              .valueOf(expected));
      return false;
    }
  }

  /**
   * Method for going to the marshalling area
   */
  void goToMarshall() {
    if (getScanCount() == 8) {
      getWorksAt().sendToMarshalling(getCurrPickingReq());
      System.out.println("Picker " + getName() + "has gone to marshalling area"
          + ".");
    } else {
      getWorksAt().sendBackToPicking(getCurrPickingReq());
      System.out.println("Picker " + getName() + "tried to go to marshalling "
          + "area with less than 8 fascias picked, the picking request has "
          + "been sent back to be picked again.");
    }
  }
}
