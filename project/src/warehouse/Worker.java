/**
 * 
 */
package warehouse;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author Andrew
 *
 */
public abstract class Worker {

  private String name;
  private LinkedList<Integer> toBeScanned;
  private Warehouse worksAt;
  private boolean isReady;
  private PickingRequest currPickingReq;
  
  protected Worker (String name, Warehouse worksAt){
    this.name = name;
    this.worksAt = worksAt;
  
  }
  
  protected abstract LinkedList<Integer> getScanOrder(PickingRequest currPick);

  /**
   * @return the name of the worker
   */
  public String getName() {
    return name;
  }

  
  /**
   * @param sku the SKU to be scanned. 
   */
  public void scan (int sku){
    if (sku == toBeScanned.pop()){
      System.out.println("Scan successful - correct SKU selected.");
    }
    else{
      System.out.println("ERROR: Scan unsuccessful - incorrect SKU selected.");
    }
  }
  

  /**
   * @return true or false if a worker is ready or not, respectively
   */
  public boolean getIsReady() {
    return isReady;
  }

  /**
   * @param changes if a worker is ready or not
   */
  public void setIsReady(boolean isReady) {
    this.isReady = isReady;
  }
  
  public void setCurrPickingReq(PickingRequest currPickingReq){
    this.currPickingReq = currPickingReq;
  }
  
  
  
}
