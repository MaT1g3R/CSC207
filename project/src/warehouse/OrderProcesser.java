package warehouse;

import java.util.LinkedList;

/**
 * The OrderProcesser class takes in orders
 * and sends them out to picking requests.
 */
public class OrderProcesser {

  /**
   * A list of all orders in the system.
   */
  private LinkedList<String> orders = new LinkedList<>();

  /**
   * The amout of orders sent out each time.
   */
  private static final int ORDER_AMOUNT = 4;

  /**
   * Add an order to the system.
   *
   * @param order The order to be added
   */
  public void addOrder(final String order) {
    this.orders.add(order);
  }

  /**
   * Sends the order to picking requests,
   * only sends them out when there're >= 4 orders in the system.
   *
   * @return a list of 4 orders to be picked when
   * there're enough orders in the system, else returns null.
   */
  public LinkedList<String> sendOrder() {
    if (this.orders.size() >= ORDER_AMOUNT) {
      LinkedList<String> res = new LinkedList<>();
      for (int i = 0; i < ORDER_AMOUNT; i++) {
        res.add(this.orders.pop());
      }
      return res;
    } else {
      return null;
    }
  }

  /**
   * Get a list of all orders.
   *
   * @return a list of all orders.
   */
  public LinkedList<String> getOrders() {
    return this.orders;
  }

}
