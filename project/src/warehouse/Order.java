package warehouse;

public class Order {
  
  private int[] skus = new int[2];
  private String colour;
  private String model;
  
  public Order(String orderAsString) {
    String[] orderSplit = orderAsString.split("\\s");
    model = orderSplit[1];
    colour = orderSplit[2];
    skus[0] = SkuTranslator.getSku(colour, model, true);
    skus[1] = SkuTranslator.getSku(colour, model, false);
    
//  TODO: HANDLE INVALID INPUTS. RAISE EXCEPTION AND DOCUMENT
//    if(orderSplit.length == 3 && orderSplit[0] == "Order")
//
  }
  
  public String getColour(){
    return colour;
  }
  public String getModel(){
    return model;
  }
  public int[] getSkus(){
    return skus;
  }
  public String toString(){
    return model + ", " + colour;
  }
  
}