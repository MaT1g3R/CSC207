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
    LinkedList<Integer> output = new LinkedList<>();
    for (String x : loc) {

      output.add(SkuTranslator.getSkuFromLocation(x.split(",")));
    }
    return output;

  }

  @Override
  public void scan(int sku) {
    //Items are assumed to be taken when scanned
    if (isReady) {
      super.scan(sku);
      worksAt.removeFascia(sku);
    }


  }

  @Override
  public void wrongScanHandle() {
    //pickers just get a notification
    System.out.println(role + " " + name + " scanned wrong sku.");

  }

  /**
   * When the worker notifies the system they've gone to the marshalling area
   */
  public void goToMarshaling() {
    System.out.println(role + " " + name + " has gone to marshalling");
    worksAt.addSequencingRequest(currPickingReq);
  }

}
