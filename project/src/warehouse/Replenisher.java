package warehouse;

/**
 * This worker replenish the stock levels when asked to.
 *
 * @author Peijun
 */
public class Replenisher {

  private String name;
  private Warehouse worksAt;
  private String sku;
  private boolean needed;

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
  public void scan(String sku) {
    needed = sku.equals(this.sku);
  }

  /**
   * A method for when a replenisher is finished.
   */
  public void finish() {
    if (needed) {
      System.out.println("Fascia of SKU " + sku + " has been "
          + "replenished by " + name);
      worksAt.addFacsia(sku);
    } else {
      System.out.println("Unneeded replenish, nothing was added to the "
          + "inventory");
    }
    this.sku = null;
  }

  /**
   * Ready action for the replenisher.
   */
  public void ready() {
    sku = worksAt.popReplenished();
  }
}
