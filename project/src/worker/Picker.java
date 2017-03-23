package worker;

import fascia.Order;
import java.util.ArrayList;
import java.util.LinkedList;
import util.MasterSystem;
import warehousefloor.Location;

/**
 * A class to represent pickers.
 *
 * @author Tasbir
 */
public class Picker extends Worker {

  private ArrayList<String> locations;
  private MasterSystem masterSystem;

  /**
   * Initialize an instance of a picker.
   *
   * @param name         the name of the picker
   * @param masterSystem where it works at
   */
  public Picker(String name, MasterSystem masterSystem) {
    super(name, masterSystem);
    this.masterSystem = masterSystem;
  }

  /**
   * What a picker does when it becomes ready.
   */
  @Override
  void readyAction() {
    StringBuilder displayString = new StringBuilder();
    ArrayList<String> toBeOptimized = new ArrayList<>();
    for (Order o : getCurrPickingReq().getOrders()) {
      toBeOptimized.add(o.getSkus()[0]);
      toBeOptimized.add(o.getSkus()[1]);
    }
    locations = WarehousePicking
        .optimize(toBeOptimized, masterSystem.getSkuTranslator());
    displayString.append("It will go to locations:\n");
    for (String loc : locations) {
      displayString.append(loc);
      displayString.append("\n");

    }
    System.out.println(displayString.toString());
  }

  /**
   * What a picker does when it finishes.
   */
  @Override
  void finishAction() {
    getCurrPickingReq().updateLocation(Location.marshall);
    System.out.println(
        "Picker " + getName() + " has gone to marshalling area.");
  }

  /**
   * The expected scan order for the worker is of course the order of the
   * locations.
   *
   * @return The expected scan order
   */
  @Override
  LinkedList<String> getScanOrder() {
    LinkedList<String> res = new LinkedList<>();
    for (String location : locations) {
      String[] toBeTr = location.split(",");
      res.add(masterSystem.getSkuTranslator().getSkuFromLocation(toBeTr));
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
  public void scan(String sku) {
    if (getCurrPickingReq() != null
        && masterSystem.getWarehouseFloor().removeFascia(sku)
        && scanResult(sku, expected())) {
      addScanCount();
      getToBeScanned().removeFirst();
    } else if (getCurrPickingReq() == null) {
      System.out.println("Picker " + getName() + " tried to scan with no "
          + "picking order assigned. Scan action aborted.");
    }
  }

  /**
   * Return the expected scan sku.
   *
   * @return the expected scan sku
   */
  private String expected() {
    return getToBeScanned().getFirst();
  }


}
