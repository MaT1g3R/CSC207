package warehouse;

import java.util.HashMap;

public class Warehouse {

  private HashMap<String, HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>> floor;
  private String loggerPath;

  public Warehouse(String loggerPath) {
    this.loggerPath = loggerPath;
  }

  public void update(PickingRequest request) {

  }

  private void eventLogger() {

  }
}
