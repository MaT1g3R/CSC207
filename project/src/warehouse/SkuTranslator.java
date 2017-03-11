package warehouse;

import java.util.ArrayList;

public class SkuTranslator {


  private SkuTranslator() {
  }

  private static ArrayList<ArrayList<String>> locations;
  private static ArrayList<ArrayList<String>> properties;


  /**
   * Using the given translation table, this function takes in a sku as an int and returns its
   * properties like Model, Colour, and whether or not it's a front or back fascia.
   *
   * @param sku The SKU who's property is being looked for.
   * @return String array representing properties. The format is [model, colour, direction]
   */
  public static String[] getProperties(int sku) {
    String[] output = {"None","None", "None"};
    for (ArrayList<String> x:properties) {
      if ((x.get(x.size() - 1)).equals(Integer.toString(sku))) {
        output = new String[] {x.get(1), x.get(0), "Back"};
      } else if ((x.get(x.size() - 2)).equals(Integer.toString(sku))) {
        output = new  String[]{x.get(1),x.get(0), "Front"};
      }
    }
    return output;

  }

  /**
   *  Using given translation table, returns SKU unit 
   * @param colour
   * @param model
   * @param isFront
   * @return
   */
  public static int getSku(String colour, String model, boolean isFront) {
    for (ArrayList<String> x : properties) {
      if (String.join(",", x.subList(0,2)).toLowerCase().equals((colour
          + "," + model).toLowerCase())) {
        if (isFront) {
          return Integer.parseInt(x.get(2));
        } else {
          return  Integer.parseInt(x.get(3));
        }
      }
    }
    return  -1;
  }

  public static String getLocation(int sku) {
    String output = "None";
    for (ArrayList<String> x : locations) {
      if (x.get(x.size() - 1).equals(Integer.toString(sku))) {
        output = String.join(",", x.subList(0, x.size() - 1));
        return output;
      }
    }
    return output;
  }

  public static int getSkuFromLocation(String[] location) {
    for (ArrayList<String> x: locations) {
      if (String.join(",", location).toLowerCase().equals(
          String.join(",",x.subList(0, x.size() - 1)).toLowerCase())) {
        return Integer.parseInt(x.get(x.size() - 1));
      }
    }
    return -1;
  }

  public static void setLocations(String path)  {
    locations = CsvReadWrite.readAsArrays(path);


  }


  public static void setProperties(String path) {
    properties = CsvReadWrite.readAsArrays(path);
  }
}
