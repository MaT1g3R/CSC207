package warehouse;

import java.util.ArrayList;

/**
 *  This class is responsible for taking in csv files that have data about SKUs and returning
 *  information for a particular set of that data, or about an SKU.
 *
 */

public class SkuTranslator {


  private SkuTranslator() {
  }

  private static ArrayList<ArrayList<String>> locations;
  private static ArrayList<ArrayList<String>> properties;


  /**
   * Using the given translation table, this function takes in a <sku></sku> as an int and returns
   * its properties like Model, Colour, and whether or not it's a front or back fascia.
   *
   * @param sku The SKU who's property is being looked for.
   * @return String array representing properties of sku. The format is [model, colour, direction]
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
   *  Using given translation table, returns SKU unit given the info.
   * @param colour the colour of this sku
   * @param model the model of this sku
   * @param isFront whether or not the sku is on the front or the back
   * @return : the sku with the three above traits as an int
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

  /**
   *  Using given traversal table, returns location of <sku></sku> in warehouse.
   * @param sku whether or not the sku is on the front or the back
   * @return : the <sku></sku> with the given location as an int
   */
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



  /**
   *  Using traversal table, fetches sku stored in <location></location>.
   * @param location is in String array format {Zone, Aisle, Rack, Rack Level}
   * @return sku stored in given area according to translation table
   */
  public static int getSkuFromLocation(String[] location) {
    for (ArrayList<String> x: locations) {
      if (String.join(",", location).toLowerCase().equals(
          String.join(",",x.subList(0, x.size() - 1)).toLowerCase())) {
        return Integer.parseInt(x.get(x.size() - 1));
      }
    }
    return -1;
  }

  /**
   *  Sets the traversal table to the csv file located in <path></path>.
   * @param path must be a valid path to a csv file
   */
  public static void setLocations(String path)  {
    locations = CsvReadWrite.readAsArrays(path);


  }

  /**
   *  Sets the translation table to the csv file located in <path></path>.
   * @param path must be a valid path to a csv file
   */
  public static void setProperties(String path) {
    properties = CsvReadWrite.readAsArrays(path);
  }

  public int[] getAllSku(){
    return null;
  }
}
