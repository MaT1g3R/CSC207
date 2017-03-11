package warehouse;

import java.util.ArrayList;

public class Truck {

  /**
   * A list of 10 levels, with 4 pallets per level. Each pallet has 4 Facsia.
   * 
   * the four pallets are represented like this in each level: FRONTLEFT BACKLEFT FRONTRIGHT
   * BACKRIGHT
   */
  private ArrayList<ArrayList<int[]>> cargo;
  
  /**
   * Current level that the pallets should be added to.
   */
  private int currentLevel;
  
  /**
   * Decides whether to add to left side of truck or right.
   */
  private boolean addToRight;
  
  /**
   * Initializes a new, empty Truck.
   */
  public Truck() {
    cargo = new ArrayList<>();
    cargo.add(new ArrayList<>()); // adding the first level to cargo
    addToRight = true;
    currentLevel = 0;
  }

  /**
   * Adds pallets to an appropriate location on the Truck.
   * 
   * @param frontPallet The pallet containing the four front SKU's
   * @param backPallet  The pallet containing the four rear SKU's
   */
  public void addCargo(int[] frontPallet, int[] backPallet) {

    this.cargo.get(currentLevel).add(frontPallet);
    this.cargo.get(currentLevel).add(backPallet);
    if (addToRight) {
      currentLevel++;
    }
    addToRight = !addToRight; // Next time add to the other side.
  }
  
  /**
   * To access the cargo of the Truck.
   * 
   * @return Nested arrayList of entire contents of truck.
   */
  public ArrayList<ArrayList<int[]>> getCargo() {
    return cargo;
  }
  
  /**
   * Calculates whether the truck is full.
   * 
   * @return Boolean of whether the truck is full.
   */
  public boolean isFull() {
    if (cargo.size() != 10) {
      return false;
    } else if (cargo.get(9).size() != 4) {
      return false;
    }
    return true;
  }

}
