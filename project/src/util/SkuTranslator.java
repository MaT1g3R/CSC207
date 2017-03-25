package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is responsible for taking in csv files that have data about SKUs
 * and returning information for a particular set of that data, or about an
 * SKU.
 *
 * @author Tasbir
 */

public class SkuTranslator {
  
  private ArrayList<String> locations;
  private ArrayList<String> properties;

  public SkuTranslator(ArrayList<String> locations,
      ArrayList<String> properties) {
    this.locations = locations;
    this.properties = properties;
  }

  /**
   * Using given translation table, returns SKU unit given the info.
   *
   * @param colour  the colour of this sku
   * @param model   the model of this sku
   * @param isFront whether or not the sku is on the front or the back
   * @return : the sku with the three above traits as an int
   */
  public String getSku(String colour, String model, boolean isFront) {
    for (String s : properties) {
      String[] line = s.split(",");
      if (colour.equals(line[0]) && model.equals(line[1])) {
        return isFront ? line[2] : line[3];
      }
    }
    return null;
  }

  /**
   * Using given traversal table, returns location of <sku></sku> in
   * warehousefloor.
   *
   * @param sku whether or not the sku is on the front or the back
   * @return : the <sku></sku> with the given location as an int
   */
  public String getLocation(String sku) {
    for (String s : locations) {
      String[] line = s.split(",");
      if (sku.equals(line[4])) {
        List<String> result = Arrays.asList(line);
        return String.join(",", result.subList(0, 4));
      }
    }
    return null;
  }

  /**
   * Using traversal table, fetches sku stored in <location></location>.
   *
   * @param location is in String array format {Zone, Aisle, Rack, Rack Level}
   * @return sku stored in given area according to translation table
   */
  public String getSkuFromLocation(String[] location) {
    for (String s : locations) {
      List<String> line = Arrays.asList(s.split(","));
      String[] locationArray = line.subList(0, 4).toArray(new String[4]);
      if (Arrays.equals(location, locationArray)) {
        return line.get(4);
      }
    }
    return null;
  }

  /**
   * Get a list of all skus from translation table.
   *
   * @return a list of all skus from translation table.
   */
  public ArrayList<String> getAllSku() {
    ArrayList<String> result = new ArrayList<>();
    for (String s : locations) {
      List<String> line = Arrays.asList(s.split(","));
      result.add(line.get(4));
    }
    return result;
  }
}
