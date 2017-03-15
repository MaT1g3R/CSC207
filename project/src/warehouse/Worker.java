package warehouse;

import java.util.LinkedList;

/**
 * A generic worker class.
 *
 * @author Andrew
 */
abstract class Worker {

  private String name;
  private LinkedList<Integer> toBeScanned = new LinkedList<>();
  private Warehouse worksAt;
  private PickingRequest currPickingReq;
  private int scanCount = 0;

  Worker(String name, Warehouse worksAt) {
    this.name = name;
    this.worksAt = worksAt;
  }

  /**
   * The expected order where the worker should scan in.
   * The picker class overwrites this method
   *
   * @return The expected order where the worker should scan in.
   */
  public LinkedList<Integer> getScanOrder() {
    return new LinkedList<>(currPickingReq.getProperSkus());
  }

  /**
   * The action for a worker being ready.
   */
  abstract void ready();

  /**
   * Sets the current picking request for this worker.
   *
   * @param req the picking request the worker is handling.
   */
  public void setCurrPickingReq(PickingRequest req) {
    currPickingReq = req;
  }

  /**
   * The result of a scan from a worker.
   *
   * @param sku the sku scanned.
   * @return true if the scan matched else false.
   */
  public boolean scanResult(int sku, int expected) {
    System.out.println(this.getClass().getSimpleName() + " " + name + " "
        + "preformed a scan action!");
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
   * Return the expected scan sku.
   *
   * @return the expected scan sku
   */
  private int expected() {
    return toBeScanned.pop();
  }

  /**
   * The action the worker takes when it scans.
   *
   * @param sku the sku scanned.
   */
  public void scan(int sku) {
    if (!scanResult(sku, expected())) {
      getWorksAt().sendBackToPicking(getCurrPickingReq());
    } else {
      addScanCount();
    }
  }


  /**
   * A getter for worksAt.
   *
   * @return where this worker works at
   */
  public Warehouse getWorksAt() {
    return worksAt;
  }


  /**
   * A getter for currPickingReq.
   *
   * @return currPickingReq
   */
  public PickingRequest getCurrPickingReq() {
    return currPickingReq;
  }

  /**
   * A getter for toBeScanned.
   *
   * @return toBeScanned
   */
  public LinkedList<Integer> getToBeScanned() {
    return toBeScanned;
  }

  /**
   * A setter for toBeScanned.
   *
   * @param scanOrder the value to set to
   */
  public void setToBeScanned(LinkedList<Integer> scanOrder) {
    toBeScanned = scanOrder;
  }

  /**
   * A getter for the name.
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Add 1 to scan count.
   */
  public void addScanCount() {
    scanCount++;
  }

  /**
   * A getter for scan count.
   *
   * @return scanCount
   */
  public int getScanCount() {
    return scanCount;
  }

  /**
   * Resets the scancount to 0.
   */
  public void resetScanCount() {
    scanCount = 0;
  }
}
