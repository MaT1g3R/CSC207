package fascia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import warehousefloor.Location;

/**
 * Orders are processed in groups of 4. These are picking requests. All picking
 * requests in a system have a unique int id.
 *
 * @author Tasbir
 */
public class PickingRequest extends Observable implements
    Comparable<PickingRequest> {

  private ArrayList<Order> orders;
  private int id;
  private Location location;


  /**
   * Constructor, which initializes given parameters.
   *
   * @param orders The orders associated with this picking request
   * @param id     IDs are ints and never repeat
   */
  public PickingRequest(ArrayList<Order> orders, int id,
      PickingRequestManager manager) {
    this.orders = orders;
    this.id = id;
    this.location = Location.pick;
    addObserver(manager);
  }

  /**
   * Get the id of this picking request.
   *
   * @return the id of this picking request.
   */
  public int getId() {
    return id;
  }

  /**
   * Return the proper order that the Skus should be in for loading
   *
   * @return : The skus as an IntegerArray list.
   */
  public LinkedList<String> getProperSkus() {
    LinkedList<String> res = new LinkedList<>();
    for (Order o : orders) {
      res.add(o.getSkus()[0]);
    }
    for (Order o : orders) {
      res.add(o.getSkus()[1]);
    }
    return res;
  }

  /**
   * A getter for the orders.
   *
   * @return orders
   */
  public ArrayList<Order> getOrders() {
    return orders;
  }


  /**
   * Compares this object with the specified object for order.
   * Returns a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the specified object.
   *
   * @param request the request to be compared.
   * @return a negative integer, zero, or a positive integer as this object is
   *        less than, equal to, or greater than the specified object.
   */
  @Override
  public int compareTo(PickingRequest request) {
    return this.id - request.id;
  }

  /**
   * Update the picking request's location
   *
   * @param location the location to set to.
   */
  public void updateLocation(Location location) {
    this.location = location;
    setChanged();
    notifyObservers(this.location);
  }
}
