
package warehouse;

import java.util.LinkedList;

/**
 * @author Andrew.
 */
public abstract class Worker {

  protected String name;
  protected LinkedList<Integer> toBeScanned;
  protected Warehouse worksAt;
  protected boolean isReady = true;
  protected PickingRequest currPickingReq;
  protected String role;

  protected Worker(String name, Warehouse worksAt) {
    this.name = name;
    this.worksAt = worksAt;
    this.role = String.class.getSimpleName();
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
    if (!toBeScanned.isEmpty() && !getIsReady()) {
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
    finish();
  }


  /**
   * Tells the worker that they are finished their current task,
   * and sets them to be ready for the next task.
   */
  public void finish() {
    System.out.println("Current action of " + role + " " + name + " has finished.");
    toBeScanned = new LinkedList<>();
    this.isReady = true;
    worksAt.assignWorkers(getClass().getSimpleName());
  }


  /**
   * @return true or false if a worker is ready or not, respectively.
   */
  public boolean getIsReady() {
    return isReady;
  }


  /**
   * @param currPickingReq the start button that a worker clicks.
   */
  public void start(PickingRequest currPickingReq) {
    this.isReady = false;
    this.currPickingReq = currPickingReq;
    this.toBeScanned = getScanOrder();
  }

}
