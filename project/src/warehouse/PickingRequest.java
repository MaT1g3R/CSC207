package warehouse;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Orders are processed in groups of 4. These are picking requests. All picking
 * requests in a system have a unique int id.
 */
class PickingRequest implements Comparable<PickingRequest> {

  private ArrayList<Order> orders;
  private int id;

  /**
   * Constructor, which initializes given parameters.
   *
   * @param orders The orders associated with this picking request
   * @param id IDs are ints and never repeat
   */
  PickingRequest(ArrayList<Order> orders, int id) {
    this.orders = orders;
    this.id = id;
  }

  /**
   * Get the id of this picking request.
   *
   * @return the id of this picking request.
   */
  int getId() {
    return id;
  }

  /**
   * Return the proper order that the Skus should be in for loading
   *
   * @return : The skus as an IntegerArray list.
   */
  LinkedList<Integer> getProperSkus() {
    LinkedList<Integer> res = new LinkedList<>();
    for (Order o : orders) {
      res.add(o.getSkus()[0]);
    }
    for (Order o : orders) {
      res.add(o.getSkus()[1]);
    }
    return res;
  }

  /**
   * A getter for the orders
   *
   * @return orders
   */
  ArrayList<Order> getOrders() {
    return orders;
  }

  /**
   * Compares this object with the specified object for order.  Returns a
   * negative integer, zero, or a positive integer as this object is less
   * than, equal to, or greater than the specified object.
   *
   * @param request the request to be compared.
   * @return a negative integer, zero, or a positive integer as this object is
   * less than, equal to, or greater than the specified object.
   * @throws NullPointerException if the specified object is null
   * @throws ClassCastException if the specified object's type prevents it from
   * being compared to this object.
   */
  @Override
  public int compareTo(PickingRequest request) {
    if (this.id == request.id) {
      return 0;
    } else {
      return this.id > request.id ? 1 : -1;
    }
  }
}
