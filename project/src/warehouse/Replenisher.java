package warehouse;

/**
 * This worker replenish the stock levels when asked to.
 *
 * @author Peijun
 */
public class Replenisher {

  private String name;
  private Warehouse worksAt;

  /**
   * Initialize a Replenisher object.
   *
   * @param name    the name of the Replenisher
   * @param worksAt where the Replenisher works at
   */
  public Replenisher(String name, Warehouse worksAt) {
    this.name = name;
    this.worksAt = worksAt;
  }

  /**
   * A getter for its name.
   *
   * @return its name
   */
  public String getName() {
    return name;
  }

  /**
   * Replenish the warehouse when the stock level <= 5.
   *
   * @param sku the sku to be replenished
   */
  public void replenish(int sku) {
    if (worksAt.getToBeReplenished().contains(sku)) {
      System.out.println("Fascia of SKU " + String.valueOf(sku) + " has been "
          + "replenished.");
      worksAt.addFacsia(sku);
      worksAt.getToBeReplenished().remove(new Integer(sku));
    } else {
      System.out.println("Unneeded replenish, nothing was added to the "
          + "inventory");
    }
  }

}
