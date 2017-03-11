package warehouse;

import java.util.ArrayList;

public class Truck {

  /*
   * A list of 10 levels, with 4 pallets per level. Each pallet has 4 Facsia
   * 
   * the four pallets are represented like this in each level: FRONTLEFT BACKLEFT FRONTRIGHT
   * BACKRIGHT
   */
  private ArrayList<ArrayList<int[]>> cargo;

  private int currentLevel; // Current level that the pallets should be added to.
  private boolean addToRight; // Decides whether to add to left side of truck or right.

  public Truck() {
    cargo = new ArrayList<>();
    cargo.add(new ArrayList<>()); // adding the first level to cargo
    addToRight = true;
    currentLevel = 0;
  }

  // loader gets 2 pallets(4 facsia each) from sequencer and adds it to truck.
  public void addCargo(int[] frontPallet, int[] backPallet) {

    this.cargo.get(currentLevel).add(frontPallet);
    this.cargo.get(currentLevel).add(backPallet);
    if (addToRight) {
      currentLevel++;
    }
    addToRight = !addToRight; // Next time add to the other side.
  }

  public ArrayList<ArrayList<int[]>> getCargo() {
    return cargo;
  }

  public boolean isFull() {
    if (cargo.size() != 10) {
      return false;
    } else if (cargo.get(9).size() != 4) {
      return false;
    }
    return true;
  }

}
