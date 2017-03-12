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
  public Replenisher(String name, Warehouse worksAt) {
    super(name, worksAt);
  }


  /**
   * This method replenishes the warehouse with Fascia of the SKU number.
   *
   * @param sku the SKU number.
   */
  public void replenish(int sku) {
    super.worksAt.addFacsia(sku);
    scan(sku);
  }

  /**
   * A barcode scanning action that takes place when Fascia is replenished.
   *
   * @param sku the SKU to be scanned.
   */
  @Override
  public void scan(int sku) {
    System.out.println("Fascia with SKU number " + String.valueOf(sku) + " "
        + "has been replenished");
  }
  
  public void start(int sku) {
    isReady = false;
    replenish(sku);
  }

  /**
   * This method should never be used.
   * Throws UnsupportedOperationException when called.
   */
  @Override
  protected LinkedList<Integer> getScanOrder() {
    throw new UnsupportedOperationException();
  }

}
