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
public class Sequencer {

  /**
   * 
   */
  public Sequencer(String name, Warehouse worksAt) {
    super(name, worksAt);
  }
  

  public LinkedList<Integer> getScanOrder(PickingRequest currPick){
    LinkedList<Integer> scanOrderSkus = new LinkedList<>();
    ArrayList<Integer> skus = currPick.getSKU;
    
    ArrayList<Integer> frontSkus = new ArrayList<Integer>();
    ArrayList<Integer> backSkus = new ArrayList<Integer>();
    
    for (int i = 0; i < skus.size(); i += 2){
      frontSkus.add(skus.get(i));
      backSkus.add(skus.get(i+1));
    }
    
    scanOrderSkus.addAll(frontSkus);
    scanOrderSkus.addAll(backSkus);
    
    return scanOrderSkus;
    
  }

}
