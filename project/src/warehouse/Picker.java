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
  public LinkedList<Integer> getScanOrder(PickingRequest currPickReq) {
    ArrayList<String> loc = WarehousePicking.optimize(currPickingReq.getSkus());
    LinkedList<Integer> output = new LinkedList<>();
    for(String x:loc) {

      output.add(SkuTranslator.getSkuFromLocation(x.split(",")));
    }
    return output;

  }

  @Override
  public  void scan(int sku){}


}
