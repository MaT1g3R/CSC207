
package warehouse;

import java.util.LinkedList;

/**
 * @author Andrew.
 */
public abstract class Worker {

  protected String name;
  protected LinkedList<Integer> toBeScanned = new LinkedList<>();
  protected Warehouse worksAt;
  protected boolean isReady = true;
  protected PickingRequest currPickingReq;
  protected String role;

  protected Worker(String name, Warehouse worksAt) {
    this.name = name;
    this.worksAt = worksAt;
    this.role = this.getClass().getSimpleName();
  }

  protected abstract LinkedList<Integer> getScanOrder();


  public String getName() {
    return name;
  }

  /**
   * @param sku the SKU to be scanned.
   */
  public void scan(int sku) {
    System.out.println(role + " " + name + " Scanned " + sku);
    if (!toBeScanned.isEmpty() && !isReady()) {
      if (sku != toBeScanned.pop()) {
        System.out.println(role + " " + name + " Wrong Scan");
        wrongScanHandle();
      } else {
        System.out.println(role + " " + name + " Correct Scan");
      }
    } else {
      System.out.println(role + " " + name + " Unneeded Scan!");
    }
  }

  /**
   * Handles the worker in the event of a wrong scan.
   */
  public void wrongScanHandle() {
    System.out.println("Aborting action of " + role + " " + name);
    worksAt.addUnpickedPickingRequest(currPickingReq);
    worksAt.assignWorkers("seqencer");
    getReady();
  }


  /**
   * Sets the worker to be ready for the next task, if any.
   */
  public void getReady() {
    System.out
        .println(role + " " + name + " is now ready.");
    toBeScanned = new LinkedList<>();
    this.isReady = true;
    worksAt.assignWorkers(this.getClass().getSimpleName().toLowerCase());
  }

  /**
   * checks if the worker should do anything other than scanning or gettingReady.
   */
  protected boolean shouldScanOrGetReady() {
    return this.isReady || !toBeScanned.isEmpty();

  }


  /**
   * @return true or false if a worker is ready or not, respectively.
   */
  public boolean isReady() {
    return isReady;
  }


  /**
   * @param currPickingReq the start button that a worker clicks.
   */
  public void start(PickingRequest currPickingReq) {
    this.isReady = false;
    this.currPickingReq = currPickingReq;
    this.toBeScanned = getScanOrder();
    String msg = this.role + " " + this.name + " should scan in order: ";
    for (int x: toBeScanned) {
      msg += " " + Integer.toString(x);
    }
    System.out.println(msg);
  }

}
