package warehouse;

public class SkuTranslator {


  private SkuTranslator() {
  }


  /**
   * Using the given translation table, this function takes in a sku as an int and returns its
   * properties like Model, Colour, and whether or not it's a front or back fascia.
   *
   * @param sku The SKU who's property is being looked for.
   * @return Strin
   */
  public static String[] getProperties(int sku) {
    return new String[0];
  }

  /**
   * Using the given translation table, this function takes in a sku as an int and returns its
   * properties like Model, Colour, and whether or not it's a front or back fascia.
   *
   * @param sku The SKU who's property is being looked for.
   * @return Strin
   */
  public static int getSku(String colour, String model, boolean isFront) {
    return 0;
  }

  public static String[] getLocation(int sku) {
    return new String[4];
  }

  public static void setPropertiesPath(String path){}

  public static void setLocationPath(String path){}
}
