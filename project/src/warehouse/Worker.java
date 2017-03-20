package warehouse;

import java.util.LinkedList;
import java.util.Observable;

/**
 * A generic worker class.
 *
 * @author Andrew
 */
abstract class Worker extends Observable {

  private String name;
  private LinkedList<String> toBeScanned = new LinkedList<>();
  private Warehouse worksAt;
  private PickingRequest currPickingReq;
  private int scanCount = 0;
  private PickingRequestManager pickingRequestManager;

  Worker(String name, Warehouse worksAt) {
    this.name = name;
    this.worksAt = worksAt;
    this.pickingRequestManager = this.worksAt.getPickingRequestManager();
    addObserver(worksAt.getWorkerManager());
  }

  /**
   * The expected order that the worker should scan in.
   * The picker class overwrites this method.
   *
   * @return The expected order that the worker should scan in.
   */
  public LinkedList<String> getScanOrder() {
    return new LinkedList<>(currPickingReq.getProperSkus());
  }

  /**
   * The action for a worker trying to ready.
   */
  public void tryReady() {
    setChanged();
    notifyObservers(true);
  }

  /**
   * The action for a worker being finished.
   */
  public void finish() {
    setChanged();
    notifyObservers(false);
  }

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
   * @param sku      the sku being scanned.
   * @param expected the expected sku to match up with the sku being scanned.
   * @return true if the scan matches, else returns false.
   */
  public boolean scanResult(String sku, String expected) {
    System.out.println(this.getClass().getSimpleName() + " " + name + " "
        + "preformed a scan action!");
    if (sku.equals(expected)) {
      System.out
          .println("Scan of SKU " + sku + " matched with"
              + " the expected result");
      return true;
    } else {
      System.out
          .println("Scan of SKU " + sku + " did not match "
              + "with the expected result of SKU " + expected);
      return false;
    }
  }

  /**
   * Returns the expected sku (a.k.a the sku that is next to be scanned).
   *
   * @return the expected sku.
   */
  private String expected() {
    return toBeScanned.pop();
  }

  /**
   * The action the worker takes when it scans.
   *
   * @param sku the sku being scanned.
   */
  public void scan(String sku) {
    if (getCurrPickingReq() != null) {
      if (!scanResult(sku, expected())) {
        getWorksAt().getPickingRequestManager()
            .update(getCurrPickingReq(), Location.load);
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
  public LinkedList<String> getToBeScanned() {
    return toBeScanned;
  }

  /**
   * A setter for toBeScanned.
   *
   * @param scanOrder sets the order that the sku's should be in.
   */
  public void setToBeScanned(LinkedList<String> scanOrder) {
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

  /**
   * A getter for PickingRequestManager.
   *
   * @return PickingRequestManager
   */
  public PickingRequestManager getPickingRequestManager() {
    return pickingRequestManager;
  }

  /**
   * An event for rescan.
   */
  public void rescan() {
    resetScanCount();
    setToBeScanned(getScanOrder());
    System.out.println(getClass().getSimpleName() + " " + getName() + " "
        + "restarted its scan process.");
  }

}
