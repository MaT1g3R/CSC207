package warehouse;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author Chaitanya
 *
 */

public class Loader extends Worker {

  private int[] frontPallet = new int[4];
  private int[] backPallet = new int[4];
  // private static ArrayList<Truck> trucks; //MOVED TO WAREHOUSE

  public Loader(String name, Warehouse worksAt) {
    super(name, worksAt);
  }

  public void load(int[] frontPallet, int[] backPallet) {
    // int truckNum = trucks.size();
    // if(truckNum == 0){ //Creates a new truck if none exist.
    // trucks.add(new Truck());
    // truckNum++;
    // }
    // else if(trucks.get(truckNum - 1).isFull()){
    //
    // }
  }

  @Override
  protected LinkedList<Integer> getScanOrder() {
    return new LinkedList<>();
  }

}
