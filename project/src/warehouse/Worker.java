package warehouse;

import java.util.LinkedList;

/**
 * @author Andrew.
 */
abstract class Worker {

  protected String name;
  protected LinkedList<Integer> toBeScanned = new LinkedList<>();
  protected Warehouse worksAt;
  protected PickingRequest currPickingReq;

  Worker(String name, Warehouse worksAt) {
    this.name = name;
    this.worksAt = worksAt;
  }

  protected abstract LinkedList<Integer> getScanOrder();


  public String getName() {
    return name;
  }

}
