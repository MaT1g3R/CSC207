package warehouse;

import java.util.ArrayList;

public class WarehousePicking {
  
  public static ArrayList<String> optimize(ArrayList<Integer> skus){
    
    ArrayList<String> locations = new ArrayList<>();
    
    for (int sku : skus){
      locations.add(SkuTranslator.getLocation(sku));
    }
    
    return locations;
    
  }

}
