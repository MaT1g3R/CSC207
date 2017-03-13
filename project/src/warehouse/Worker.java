package warehouse;

import java.util.LinkedList;

/**
 * @author Andrew.
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
  protected LinkedList<Integer> getScanOrder() {
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
  void setCurrPickingReq(PickingRequest req) {
    currPickingReq = req;
  }

  /**
   * The result of a scan from a worker.
   *
   * @param sku the sku scanned.
   * @return true if the scan matched else false.
   */
  boolean scanResult(int sku, int expected) {
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
  void scan(int sku) {
    if (!scanResult(sku, expected())) {
      getWorksAt().sendBackToPicking(getCurrPickingReq());
    } else {
      addScanCount();
    }
  }


  /**
   * A getter fot worksAt.
   *
   * @return where this worker works at
   */
  Warehouse getWorksAt() {
    return worksAt;
  }


  /**
   * A getter for currPickingReq
   *
   * @return currPickingReq
   */
  PickingRequest getCurrPickingReq() {
    return currPickingReq;
  }

  /**
   * A getter for toBeScanned
   *
   * @return toBeScanned
   */
  LinkedList<Integer> getToBeScanned() {
    return toBeScanned;
  }

  /**
   * A setter for toBeScanned
   *
   * @param scanOrder the value to set to
   */
  void setToBeScanned(LinkedList<Integer> scanOrder) {
    toBeScanned = scanOrder;
  }

  /**
   * A getter for the name.
   *
   * @return name
   */
  String getName() {
    return name;
  }

  /**
   * Add 1 to scan count
   */
  void addScanCount() {
    scanCount++;
  }

  /**
   * A getter for scan count
   *
   * @return scanCount
   */
  int getScanCount() {
    return scanCount;
  }

  /**
   * Resets the scancount to 0
   */
  void resetScanCount() {
    scanCount = 0;
  }
}
