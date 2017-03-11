package warehouse;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author Chaitanya
 *
 */

public class Loader extends Worker {

  public Loader(String name, Warehouse worksAt) {
    super(name, worksAt);
  }

  public void load() {
    if (!super.getIsReady() || !currPickingReq.getLoadReady()) {
      // If either the loader is not ready or the request is not ready, do nothing.
      System.out.println(
          "Loader " + super.getName() + " did not load PickingRequest " + currPickingReq.getId());
    } else {
      int[] frontPallet = currPickingReq.getSequencedPallets()[0];
      int[] backPallet = currPickingReq.getSequencedPallets()[1];
      ArrayList<Truck> trucks = worksAt.getTrucks();
      int truckNum = trucks.size();
      // Creates a new truck if none exist or the latest one is full.
      if (truckNum == 0 || trucks.get(truckNum - 1).isFull()) {
        trucks.add(new Truck());
        truckNum++;
      }
      // Assuming sequencer outputs in order.
      trucks.get(truckNum - 1).addCargo(frontPallet, backPallet);
      System.out.println(
          "Loader " + super.getName() + " loaded PickingRequest " + currPickingReq.getId());
      outputToCsv();
    }
  }

  private void outputToCsv() {
    ArrayList<Order> currentOrders = currPickingReq.getOrders();
    for (int i = 0; i < currentOrders.size(); i++) {
      CsvReadWrite.addLine(currentOrders.get(i).toString(), "orders.csv");
    }
  }

  @Override
  protected LinkedList<Integer> getScanOrder(PickingRequest currPick) {
    LinkedList<Integer> scanOrder = new LinkedList<>();
    ArrayList<Order> currentOrders = currPick.getOrders();
    for (int i = 0; i < currentOrders.size(); i++) {
      scanOrder.add(currentOrders.get(i).getSkus()[0]);
    }
    for (int i = 0; i < currentOrders.size(); i++) {
      scanOrder.add(currentOrders.get(i).getSkus()[1]);
    }
    return scanOrder;
  }

}
