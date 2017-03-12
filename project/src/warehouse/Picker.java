package warehouse;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Tasbir on 2017-03-11.
 */
public class Picker extends Worker {

  public Picker(String name, Warehouse worksAt) {
    super(name, worksAt);
  }

  @Override
  public LinkedList<Integer> getScanOrder() {
    ArrayList<String> loc = WarehousePicking.optimize(currPickingReq.getSkus());

    String msg = this.role + " " + this.name + " should go to ";
    LinkedList<Integer> output = new LinkedList<>();
    for (String x : loc) {
      msg += x + "   ";
      output.add(SkuTranslator.getSkuFromLocation(x.split(",")));
    }
    msg += "in that order.";
    System.out.println(msg);
    return output;

  }

  @Override
  public void scan(int sku) {
    //Items are assumed to be taken when scanned
    super.scan(sku);
    if (worksAt.getInventory().get(sku) <= 0) {
      System.out.println("No more of " + Integer.toString(sku) + " left. Pick next item");
      toBeScanned.add(sku);
    } else {
      worksAt.removeFascia(sku);
    }


  }

  @Override
  public void wrongScanHandle() {
    //pickers just get the default notification when scanning the wrong thing

  }

  /**
   * When the worker notifies the system they've gone to the marshalling area.
   */
  public void goToMarshaling() {
    System.out.println(role + " " + name + " has gone to marshalling");
    if (this.isReady() || !this.toBeScanned.isEmpty()) {
      System.out.println(role + " " + name + " should not be in marshalling");
    } else {
      System.out.println(this.role + "" + this.name + " has went to marshalling for PickingRequest "
          + currPickingReq.getId());
      worksAt.addSequencingRequest(currPickingReq);
    }
  }

}
