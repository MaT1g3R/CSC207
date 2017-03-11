package warehouse;

import java.util.ArrayList;

public class PickingRequest {

  private ArrayList<Order> orders;
  private  int id;
  private  boolean loadReady;
  public PickingRequest(ArrayList<Order> orders, int id) {
  }
  public int getId() {
    return id;
  }

  public ArrayList<Order> getOrders() {
    return orders;
  }

  public boolean isLoadReady() {
    return loadReady;
  }

  public void setReady(boolean rdy) {
    loadReady = rdy;
  }

  public ArrayList<Integer> getSKU{
    return new ArrayList<Integer>;
  }






}
