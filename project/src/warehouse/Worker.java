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
   * The expected order that the worker should scan in.
   * The picker class overwrites this method.
   *
   * @return The expected order that the worker should scan in.
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
   * @param sku the sku being scanned.
   * @param expected the expected sku to match up with the sku being scanned.
   * @return true if the scan matches, else returns false.
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
   * Returns the expected sku (a.k.a the sku that is next to be scanned).
   *
   * @return the expected sku.
   */
  private int expected() {
    return toBeScanned.pop();
  }

  /**
   * The action the worker takes when it scans.
   *
   * @param sku the sku being scanned.
   */
  public void scan(int sku) {
    if (getCurrPickingReq() != null) {
      if (!scanResult(sku, expected())) {
        getWorksAt().sendBackToPicking(getCurrPickingReq());
      } else {
        addScanCount();
      }
    } else {
      System.out.println(getClass().getSimpleName() + " " + getName() + " "
          + "tried to scan with no picking request. Scan action aborted.");
    }
  }


  /**
   * A getter for worksAt.
   *
   * @return where this worker works at.
   */
  public Warehouse getWorksAt() {
    return worksAt;
  }


  /**
   * A getter for currPickingReq.
   *
   * @return the current Picking Request of this worker.
   */
  public PickingRequest getCurrPickingReq() {
    return currPickingReq;
  }

  /**
   * A getter for toBeScanned.
   *
   * @return the sku's to be scanned.
   */
  public LinkedList<Integer> getToBeScanned() {
    return toBeScanned;
  }

  /**
   * A setter for toBeScanned.
   *
   * @param scanOrder sets the order that the sku's should be in.
   */
  public void setToBeScanned(LinkedList<Integer> scanOrder) {
    toBeScanned = scanOrder;
  }

  /**
   * A getter for the name.
   *
   * @return name of the worker.
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
   * Resets the scanCount to 0.
   */
  public void resetScanCount() {
    scanCount = 0;
  }


}
