package warehouse;

import java.util.LinkedList;

/**
 * This worker replenish the stock levels when asked to.
 *
 * @author Peijun
 */
public class Replenisher extends Worker {

  public Replenisher(final String name, final Warehouse worksAt) {
    super(name, worksAt);
  }

  @Override
  protected LinkedList<Integer> getScanOrder(final PickingRequest currPick) {
    return null;
  }

  public void replenish(String[] location) {
  }

  @Override
  public void scan(int sku) {

  }

}
