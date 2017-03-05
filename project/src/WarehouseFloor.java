import java.util.HashMap;

public class WarehouseFloor {

  private HashMap<String, HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>> floor;
  private String loggerPath;

  public WarehouseFloor(String loggerPath) {
    this.loggerPath = loggerPath;
  }

  public void update(PickingRequest request) {

  }

  private void eventLogger() {

  }
}
