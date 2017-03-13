package warehouse;

import java.util.ArrayList;

/**
 * Orders are processed in groups of 4. These are picking requests. All picking
 * requests in a system have a unique int id.
 */
public class PickingRequest {

  private ArrayList<Order> orders;
  private int id;
  private boolean loadReady;


  /**
   * Constructor, which initializes given parameters.
   *
   * @param orders The orders associated with this picking request
   * @param id IDs are ints and never repeat
   */
  public PickingRequest(ArrayList<Order> orders, int id) {
    this.orders = orders;
    this.id = id;
  }


  public int getId() {
    return id;
  }

  /**
   * Returns all the possible SKUs in this pickingRequest. For fascia it takes
   * the form [F1,B1,F2,B2...,Fn,Bn] where the first char of each element is
   * whether the SKU is front or back, and the second is the nth order in the
   * orders ArrayList field.
   *
   * @return : The skus as an IntegerArray list.
   */
  public ArrayList<Integer> getSkus() {

  }

  public boolean getLoadReady() {
    return loadReady;
  }

  public void setLoadReady(boolean bln) {
    loadReady = bln;
  }

  public ArrayList<Order> getOrders() {
    return orders;
  }

}
