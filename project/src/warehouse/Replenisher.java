package warehouse;

/**
 * This worker replenish the stock levels when asked to.
 *
 * @author Peijun
 */
public class Replenisher extends Worker {

  /**
   * Initialize a Replenisher object.
   *
   * @param name the name of the Replenisher
   * @param worksAt where the Replenisher works at
   */
  public Replenisher(String name, Warehouse worksAt) {
    super(name, worksAt);
  }


}
