package warehouse;

import java.util.ArrayList;

public class WarehousePicking {

  /**
   * @param skus the list of SKU numbers to be converted to locations.
   * @return an ArrayList containing the locations of the SKU's.
   */
  public static ArrayList<String> optimize(ArrayList<Integer> skus) {
    ArrayList<String> locations = new ArrayList<>();

    for (int sku : skus) {
      locations.add(SkuTranslator.getLocation(sku));
    }
    return locations;
  }
}
