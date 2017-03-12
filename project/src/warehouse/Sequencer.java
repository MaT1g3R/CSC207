/**
 *
 */

package warehouse;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author Andrew.
 */
public class Sequencer extends Worker {


  public Sequencer(String name, Warehouse worksAt) {
    super(name, worksAt);
  }


  @Override
  public LinkedList<Integer> getScanOrder() {
    LinkedList<Integer> scanOrderSkus = new LinkedList<>();
    ArrayList<Integer> skus = currPickingReq.getSkus();

    ArrayList<Integer> frontSkus = new ArrayList<Integer>();
    ArrayList<Integer> backSkus = new ArrayList<Integer>();

    for (int i = 0; i < skus.size(); i += 2) {
      frontSkus.add(skus.get(i));
      backSkus.add(skus.get(i + 1));
    }

    scanOrderSkus.addAll(frontSkus);
    scanOrderSkus.addAll(backSkus);

    return scanOrderSkus;
  }

  /**
   * The button a worker presses to begin sequencing.
   */
  public void sequence() {
    System.out.println("Sequence attempt " + role + " " + this.name);
    if (this.shouldScanOrGetReady()) {
      System.out.println("You have more left to do " + role + " " + this.name);
    } else {
      System.out.println(
          role + " " + this.name + " sequenced pickingRequest ID " + currPickingReq.getId());
      currPickingReq.setLoadReady(true);
      worksAt.assignWorkers("loader");
    }
  }

}
