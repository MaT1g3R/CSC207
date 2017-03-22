package worker;

import java.util.ArrayList;
import util.SkuTranslator;

class WarehousePicking {

  /**
   * @param skus          the list of SKU numbers to be converted to locations.
   * @param skuTranslator the skuTranslator
   * @return an ArrayList containing the locations of the SKU's.
   */
  public static ArrayList<String> optimize(ArrayList<String> skus,
      SkuTranslator skuTranslator) {
    ArrayList<String> locations = new ArrayList<>();

    for (String sku : skus) {
      locations.add(skuTranslator.getLocation(sku));
    }
    return locations;
  }
}
