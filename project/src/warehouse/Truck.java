package warehouse;

import java.util.ArrayList;

/**
 * A class to represent Trucks.
 *
 * @author Chaitanya
 */

public class Truck {

  /**
   * A list of 10 levels, with 4 pallets per level. Each pallet has 4 Facsia.
   * The four pallets are represented like this in each level: FRONTLEFT
   * BACKLEFT FRONTRIGHT BACKRIGHT
   */
  private ArrayList<ArrayList<int[]>> cargo = new ArrayList<>();
  private int currReqId;

  /**
   * Current level that the pallets should be added to.
   */
  private int currentLevel = 0;

  /**
   * Decides whether to add to left side of truck or right.
   */
  private boolean addToRight = false;

  /**
   * Initializes a new, empty Truck.
   *
   * @param startingId the picking request id that the truck starts at
   */
  public Truck(int startingId) {
    currReqId = startingId;
  }

  /**
   * Adds pallets to an appropriate location on the Truck.
   *
   * @param frontPallet The pallet containing the four front SKU's
   * @param backPallet  The pallet containing the four rear SKU's
   * @return if the cargo is loaded
   */
  public boolean addCargo(int[] frontPallet, int[] backPallet, int reqId) {
    if (reqId == currReqId && !this.isFull()) {
      if (!addToRight) {
        this.cargo.add(currentLevel, new ArrayList<>());
      }
      this.cargo.get(currentLevel).add(frontPallet);
      this.cargo.get(currentLevel).add(backPallet);
      if (addToRight) {
        currentLevel++;
      }
      addToRight = !addToRight; // Next time add to the other side.
      currReqId++;
      return true;
    }
    return false;
  }

  /**
   * Calculates whether the truck is full.
   *
   * @return Boolean of whether the truck is full.
   */

  public boolean isFull() {
    if (cargo.size() < 10) {
      return false;
    } else if (cargo.get(9).size() < 4) {
      return false;
    }
    return true;
  }

}
