package warehouse;

import java.util.ArrayList;

public class Truck {

  private ArrayList<ArrayList<String>> cargo;
  private boolean isFull;

  public ArrayList<ArrayList<String>> getCargo() {
    return cargo;
  }

  public boolean isFull() {
    return isFull;
  }

  public void setCargo(ArrayList<ArrayList<String>> cargo) {
    this.cargo = cargo;
  }

  private void eventLogger() {

  }
}
