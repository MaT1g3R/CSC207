package warehouse;

/**
 * A class to represent Orders.
 *
 * @author Chaitanya
 */

public class Order {

  private int[] skus = new int[2];
  private String colour;
  private String model;

  /**
   * Initializes a new order based on input to the system.
   *
   * @param orderAsString The Order as it is inputed.
   */
  public Order(String orderAsString) {
    String[] orderSplit = orderAsString.split("\\s");
    model = orderSplit[1];
    colour = orderSplit[2];
    skus[0] = SkuTranslator.getSku(colour, model, true);
    skus[1] = SkuTranslator.getSku(colour, model, false);
  }


  /**
   * Returns the SKU numbers of the order.
   *
   * @return Array of 2 ints, the SKU number of the front object, then the back.
   */
  int[] getSkus() {
    return skus;
  }

  /**
   * Returns the Order as a String.
   *
   * @return String of the Order in the form "model, colour".
   */
  public String toString() {
    return model + ", " + colour;
  }

}
