package worker;

import fascia.Order;
import util.MasterSystem;
import warehousefloor.Location;
import warehousefloor.Truck;

/**
 * A class to represent Loaders.
 *
 * @author Chaitanya
 */
public class Loader extends Worker {

  private String[] frontPallet = new String[4];
  private String[] backPallet = new String[4];
  private MasterSystem masterSystem;

  /**
   * Initializes a new Loader.
   *
   * @param name         The Loader's name.
   * @param masterSystem The WarehouseFloor object of which this worker works
   *                     at.
   */
  public Loader(String name, MasterSystem masterSystem) {
    super(name, masterSystem);
    this.masterSystem = masterSystem;
  }

  /**
   * What a loder does when it gets ready.
   */
  @Override
  void readyAction() {
    String[][] pallets = masterSystem.getPickingRequestManager()
        .popPallets(getCurrPickingReq().getId());
    setPallets(pallets[0], pallets[1]);
  }

  /**
   * What a loder does when it tries to load to a truck.
   */
  @Override
  void finishAction() {
    Truck truck = masterSystem.getWarehouseFloor().getFirstNonFullTruck();
    if (truck.addCargo(frontPallet, backPallet, getCurrPickingReq()
        .getId()
    )) {
      System.out
          .println("Loader " + getName() + " loaded picking request"
              + " " + String.valueOf(getCurrPickingReq().getId()));
      for (Order o : getCurrPickingReq().getOrders()) {
        masterSystem.getWarehouseFloor().logLoading(o.toString());
      }
    } else {
      System.out
          .println("Loader " + getName() + " could not load picking "
              + "request " + String
              .valueOf(getCurrPickingReq().getId())
              + "\nThe picking request is sent back to loading area.");
      getCurrPickingReq().updateLocation(Location.load);
      masterSystem.getPickingRequestManager()
          .putPalletes(
              new String[][]{frontPallet, backPallet},
              getCurrPickingReq().getId());
    }
    setPallets(new String[4], new String[4]);
  }

  /**
   * A setter for front and back pallets.
   *
   * @param frontPallet the front pallet
   * @param backPallet  the back pallet
   */
  private void setPallets(String[] frontPallet, String[] backPallet) {
    this.frontPallet = frontPallet;
    this.backPallet = backPallet;
  }
}