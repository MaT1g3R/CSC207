package worker;

import fascia.PickingRequest;
import java.util.LinkedList;
import java.util.Observable;
import util.MasterSystem;
import warehousefloor.Location;

/**
 * A generic worker class.
 *
 * @author Andrew
 */
public abstract class Worker extends Observable {

  private String name;
  private LinkedList<String> toBeScanned = new LinkedList<>();
  private PickingRequest currPickingReq;
  private int scanCount = 0;
  private MasterSystem masterSystem;

  Worker(String name, MasterSystem masterSystem) {
    this.name = name;
    this.masterSystem = masterSystem;
    addObserver(masterSystem.getWorkerManager());
  }

  /**
   * The action for a worker trying to ready.
   */
  public void tryReady() {
    setChanged();
    notifyObservers(true);
  }

  /**
   * What a worker does when it becomes ready.
   */
  abstract void readyAction();

  /**
   * The action for a worker being finished.
   */
  public void finish() {
    setChanged();
    notifyObservers(false);
  }

  /**
   * What a worker does when it finishes.
   */
  abstract void finishAction();

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
   * Returns the expected sku (a.k.a the sku that is next to be scanned).
   *
   * @return the expected sku.
   */
  private String expected() {
    return toBeScanned.pop();
  }

  /**
   * The result of a scan from a worker.
   *
   * @param sku      the sku being scanned.
   * @param expected the expected sku to match up with the sku being scanned.
   * @return true if the scan matches, else returns false.
   */
  boolean scanResult(String sku, String expected) {
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
   * The action the worker takes when it scans.
   *
   * @param sku the sku being scanned.
   */
  public void scan(String sku) {
    if (getCurrPickingReq() != null) {
      if (!scanResult(sku, expected())) {
        masterSystem.getPickingRequestManager()
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
   * An event for rescan.
   */
  public void rescan() {
    resetScanCount();
    setToBeScanned(getScanOrder());
    System.out.println(getClass().getSimpleName() + " " + getName() + " "
        + "restarted its scan process.");
  }

  /**
   * A getter for currPickingReq.
   *
   * @return the current Picking Request of this worker.
   */
  PickingRequest getCurrPickingReq() {
    return currPickingReq;
  }

  /**
   * A getter for toBeScanned.
   *
   * @return the sku's to be scanned.
   */
  LinkedList<String> getToBeScanned() {
    return toBeScanned;
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
   * A getter for scan count.
   *
   * @return scanCount
   */
  int getScanCount() {
    return scanCount;
  }

  /**
   * A setter for toBeScanned.
   *
   * @param scanOrder sets the order that the sku's should be in.
   */
  void setToBeScanned(LinkedList<String> scanOrder) {
    toBeScanned = scanOrder;
  }

  /**
   * Sets the current picking request for this worker.
   *
   * @param req the picking request the worker is handling.
   */
  void setCurrPickingReq(PickingRequest req) {
    currPickingReq = req;
  }

  /**
   * Add 1 to scan count.
   */
  void addScanCount() {
    scanCount++;
  }

  /**
   * Resets the scanCount to 0.
   */
  void resetScanCount() {
    scanCount = 0;
  }
}
