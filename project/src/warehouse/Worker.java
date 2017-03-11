/**
 *
 */
package warehouse;

import java.util.LinkedList;


/**
 * @author Andrew
 */
public abstract class Worker {

  private String name;
  protected LinkedList<Integer> toBeScanned;
  protected Warehouse worksAt;
  private boolean isReady = true;
  protected PickingRequest currPickingReq;
  protected String role;

  protected Worker(String name, Warehouse worksAt) {
    this.name = name;
    this.worksAt = worksAt;
    this.role = String.class.getSimpleName();
  }

  protected abstract LinkedList<Integer> getScanOrder();

  /**
   * @return the name of the worker
   */
  public String getName() {
    return name;
  }
  


  /**
   * @param sku the SKU to be scanned.
   */
  public void scan(int sku) {
    System.out.println(role + " " + name + " Scanned " + sku );
    if (!toBeScanned.isEmpty()) {
      if (sku != toBeScanned.pop()){
        System.out.println(role + " " + name + " Wrong Scan");
        wrongScanHandle();
      }
      else{
        System.out.println(role + " " + name + " Correct Scan");
      }
    } 
    else{
      System.out.println(role + " " + name + " Unneeded Scan!");
    }
  }
  
  public void wrongScanHandle(){
    //default behaviour is wrongScan = new picking request.
    //REPLENEISHER MUST OVERRIDE THIS METHOD SINCE THEY DONT HANDLE PICK REQUESTS
    System.out.println("Aborting action of " + role + " " + name);
    worksAt.pickingRequests.add();
    finish();
  }
  
  public void finish(){
    System.out.println("Current action of " + role + " " + name + " has finished.");
    toBeScanned = new LinkedList<>();
    this.isReady = true;
  }


  /**
   * @return true or false if a worker is ready or not, respectively
   */
  public boolean getIsReady() {
    return isReady;
  }


  public void start (PickingRequest currPickingReq){
    
    this.isReady = false;
    this.currPickingReq = currPickingReq;
    this.toBeScanned = getScanOrder();
    
  }


}
