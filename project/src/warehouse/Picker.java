package warehouse;

import java.util.ArrayList;
import java.util.LinkedList;

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
   * The expected scan order for the worker is of course the order of the
   * locations.
   *
   * @return The expected scan order
   */
  @Override
  public LinkedList<String> getScanOrder() {
    LinkedList<String> res = new LinkedList<>();
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
  public void scan(String sku) {
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
  private String expected() {
    return getToBeScanned().getFirst();
  }


  /**
   * A setter for locations.
   *
   * @param locations the locations to set to.
   */
  public void setLocations(ArrayList<String> locations) {
    this.locations = locations;
  }

  /**
   * A getter for locations.
   *
   * @return this.locations
   */
  public ArrayList<String> getLocations() {
    return this.locations;
  }
}
