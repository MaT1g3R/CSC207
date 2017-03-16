package warehouse;

import java.util.ArrayList;
import java.util.LinkedList;
import warehouse.PickingRequest.Location;

/**
 * A class to represent pickers.
 *
 * @author Tasbir
 */
public class Picker extends Worker {

  private ArrayList<String> locations;

  /**
   * Initialize an instance of a picker.
   *
   * @param name    the name of the picker
   * @param worksAt where it works at
   */
  public Picker(String name, Warehouse worksAt) {
    super(name, worksAt);
  }

  /**
   * When a picker is ready, give it a picking request, a list of locations,
   * and a scan order.
   */
  @Override
  public void ready() {
    setCurrPickingReq(getPickingRequestManager().getForPicking());
    if (getCurrPickingReq() != null) {
      resetScanCount();
      ArrayList<Integer> toBeOptimized = new ArrayList<>();
      for (Order o : getCurrPickingReq().getOrders()) {
        toBeOptimized.add(o.getSkus()[0]);
        toBeOptimized.add(o.getSkus()[1]);
      }
      locations = WarehousePicking
          .optimize(toBeOptimized, getWorksAt().getSkuTranslator());
      setToBeScanned(getScanOrder());
      // For printing
      String displayString = "Picker " + getName() + " is ready, it will go to "
          + "locations:\n";
      for (String loc : locations) {
        displayString += loc + "\n";
      }
      System.out.println(displayString);
    } else {
      System.out.println("Picker " + getName() + " tried to ready with no "
          + "picking request. Ready action aborted.");
    }
  }

  /**
   * The expected scan order for the worker is of course the order of the
   * locations.
   *
   * @return The expected scan order
   */
  @Override
  public LinkedList<Integer> getScanOrder() {
    LinkedList<Integer> res = new LinkedList<>();
    for (String location : locations) {
      String[] toBeTr = location.split(",");
      res.add(getWorksAt().getSkuTranslator().getSkuFromLocation(toBeTr));
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
  public void scan(int sku) {
    if (getCurrPickingReq() != null) {
      getWorksAt().removeFascia(sku);
      if (scanResult(sku, expected())) {
        addScanCount();
        getToBeScanned().removeFirst();
      }
    } else {
      System.out.println("Picker " + getName() + " tried to scan with no "
          + "picking order assigned. Scan action aborted.");
    }
  }

  /**
   * Return the expected scan sku.
   *
   * @return the expected scan sku
   */
  private int expected() {
    return getToBeScanned().getFirst();
  }

  /**
   * Method for going to the marshalling area.
   */
  public void goToMarshall() {
    if (getScanCount() == 8 && getCurrPickingReq() != null) {
      getCurrPickingReq().updateLocation(Location.marshall);
      System.out.println("Picker " + getName() + " has gone to marshalling area"
          + ".");
    } else if (getCurrPickingReq() != null) {
      getCurrPickingReq().updateLocation(Location.pick);
      System.out.println("Picker " + getName() + " tried to go to marshalling "
          + "area with less than 8 fascias picked, the picking request has "
          + "been sent back to be picked again.");
    } else {
      System.out.println("Picker " + getName() + "tried to go to marshall "
          + "with no picking request assigned. Go to marshall action aborted.");
    }
    setCurrPickingReq(null);
    resetScanCount();
  }
}
