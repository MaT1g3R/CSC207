package warehouse;

import java.util.LinkedList;

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
  public Replenisher(final String name, final Warehouse worksAt) {
    super(name, worksAt);
  }


  /**
   * This method replenishes the warehouse with Fascia of the SKU number.
   *
   * @param sku the sku number
   */
  public void replenish(final int sku) {
    super.worksAt.addFacsia(sku);
    scan(sku);
  }

  /**
   * A barcide scanning action that takes place when Fascia is replenished.
   *
   * @param sku the SKU to be scanned.
   */
  @Override
  public void scan(final int sku) {
    System.out.println("Fascia with SKU number " + String.valueOf(sku) + " "
        + "has been replenished");
  }

  /**
   * This method should never be used.
   * Throws UnsupportedOperationException when called
   */
  @Override
  protected LinkedList<Integer> getScanOrder() {
    throw new UnsupportedOperationException();
  }

}
