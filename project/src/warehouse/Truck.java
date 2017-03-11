package warehouse;

import java.util.ArrayList;

public class Truck{

  /*A list of 10 levels, with 4 pallets per level. Each pallet has 4 Facsia
   * 
   * the four pallets are represented like this in each level:
   * FRONTLEFT BACKLEFT FRONTRIGHT BACKRIGHT
   * */
  private ArrayList<ArrayList<int[]>> cargo;
  
  private int currentLevel; // Current level that the pallets should be added to.
  private boolean addToLeft; // Decides whether to add to left side of truck or right.
  
  public Truck(){
    cargo = new ArrayList<>();
    cargo.add(new ArrayList<>()); //adding the first level to cargo
    addToLeft = true;
    currentLevel = 0;
  }
  
  public ArrayList<ArrayList<int[]>> getCargo() {
    return cargo;
  }

  public boolean isFull() {
    if(cargo.size() != 10){
      return false;
    }
    else if(cargo.get(9).size() != 4){
      return false;
    }
    return true;
  }

  // loader gets 2 pallets(4 facsia each) from sequencer and adds it to truck.
  public void addCargo(int[] frontPallet,int[] backPallet) {
//    if(addToLeft){
//      this.cargo.get(currentLevel).get
//    } else{
//      this.cargo.bad
//      currentLevel++;
//    }
//    addToLeft = !addToLeft; //Next time add to the other side.
  }
}
