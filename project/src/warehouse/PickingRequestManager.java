package warehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/**
 * A class to keep track of all PickingRequests.
 *
 * @author Peijun
 */
public class PickingRequestManager implements Observer {

  private LinkedList<PickingRequest> outStandingPickingRequests = new
      LinkedList<>();
  private LinkedList<PickingRequest> marshallingArea = new LinkedList<>();
  private LinkedList<PickingRequest> loadingArea = new LinkedList<>();
  private HashMap<Integer, String[][]> pallets = new HashMap<>();
  private LinkedList<Order> orders = new LinkedList<>();
  private int pickingReqId = 0;
  private Warehouse warehouse;

  /**
   * A setter for this.warehouse.
   *
   * @param warehouse the warehouse to set to
   */
  public void setWarehouse(Warehouse warehouse) {
    this.warehouse = warehouse;
  }

  /**
   * This method is called whenever the observed picking request is changed.
   *
   * @param request the observable object.
   * @param arg     an argument passed to the notifyObservers
   */
  @Override
  public void update(Observable request, Object arg) {
    if (arg == Location.pick) {
      outStandingPickingRequests.add((PickingRequest) request);
    } else if (arg == Location.marshall) {
      marshallingArea.add((PickingRequest) request);
    } else {
      loadingArea.add((PickingRequest) request);
      loadingArea.sort(PickingRequest::compareTo);
    }
  }

  /**
   * This method generate a new picking request.
   * This method should never be called if there's less than 4 orders in
   * outstanding Orders.
   *
   * @return A picking request from 4 orders.
   */
  private PickingRequest generatePickingReq() {
    ArrayList<Order> toBeSent = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      toBeSent.add(orders.pop());
    }
    return new PickingRequest(toBeSent, pickingReqId++, this);
  }

  /**
   * Pop off a picking request from one of the areas based on the requested
   * area.
   *
   * @param location the requested location.
   */
  public PickingRequest popRequest(Location location) {
    if (location == Location.pick && !outStandingPickingRequests
        .isEmpty()) {
      return outStandingPickingRequests.pop();
    } else if (location == Location.marshall && !marshallingArea.isEmpty()) {
      return marshallingArea.pop();
    } else if (location == Location.load && !loadingArea.isEmpty()) {
      return loadingArea.pop();
    }
    return null;
  }

  /**
   * Get a picking request for the picker.
   *
   * @return a picking request for the picker.
   */
  public PickingRequest getForPicking() {
    if (!outStandingPickingRequests.isEmpty()) {
      return popRequest(Location.pick);
    } else if (orders.size() >= 4) {
      return generatePickingReq();
    } else {
      return null;
    }
  }

  /**
   * Method for adding an order to the system.
   *
   * @param order the order as a string to be added
   */
  public void addOrder(String order) {
    System.out.println(order + " has been added to the warehouse.");
    orders.add(new Order(order, warehouse.getSkuTranslator()));
  }

  /**
   * Add front and back pallets based on their picking request id.
   *
   * @param pallet the front and back pallets.
   * @param id     the picking request id.
   */
  public void putPalletes(String[][] pallet, int id) {
    this.pallets.put(id, pallet);
  }

  /**
   * Remove and return pallets by picking request id.
   *
   * @param id the picking request id.
   */
  public String[][] popPallets(int id) {
    if (pallets.containsKey(id)) {
      String[][] result = pallets.get(id);
      pallets.remove(id);
      return result;
    } else {
      return null;
    }
  }
}
