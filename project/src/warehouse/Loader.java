package warehouse;

/**
 * A class to represent Loaders.
 *
 * @author Chaitanya
 */

public class Loader extends Worker {

  /**
   * Initializes a new Loader.
   *
   * @param name The Loader's name.
   * @param worksAt The Warehouse object of which this worker works at.
   */
  public Loader(String name, Warehouse worksAt) {
    super(name, worksAt);
  }


}
