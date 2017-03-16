package warehouse;

/**
 * A class to represent Loaders.
 *
 * @author Chaitanya
 */

public class Loader extends Worker {

  private int[] frontPallet = new int[4];
  private int[] backPallet = new int[4];

  /**
   * Initializes a new Loader.
   *
   * @param name    The Loader's name.
   * @param worksAt The Warehouse object of which this worker works at.
   */
  public Loader(String name, Warehouse worksAt) {
    super(name, worksAt);
  }

  /**
   * The action for a worker being ready.
   */
  @Override
  public void ready() {
    resetScanCount();
    setCurrPickingReq(getPickingRequestManager().popRequest(Location.load));
    if (getCurrPickingReq() != null) {
      int[][] pallets = getPickingRequestManager()
          .popPallets(getCurrPickingReq().getId());
      setPallets(pallets[0], pallets[1]);
      setToBeScanned(getScanOrder());
      System.out.println("Loader " + getName() + " is ready to load.");
    } else {
      System.out.println("Loader " + getName() + " tried to ready with no "
          + "picking requests. Ready action aborted.");
    }
  }

  public void setPallets(int[] frontPallet, int[] backPallet) {
    this.frontPallet = frontPallet;
    this.backPallet = backPallet;
  }

  /**
   * Method for loading.
   */
  public void load() {
    if (getCurrPickingReq() != null) {
      if (getScanCount() == 8) {
        Truck truck = getWorksAt().getFirstNonFullTruck();
        if (truck.addCargo(frontPallet, backPallet, getCurrPickingReq().getId()
        )) {
          System.out.println("Loader " + getName() + " loaded picking request"
              + " " + String.valueOf(getCurrPickingReq().getId()));
          for (Order o : getCurrPickingReq().getOrders()) {
            getWorksAt().logLoading(o.toString());
          }

        } else {
          System.out.println("Loader " + getName() + " could not load picking "
              + "request " + String.valueOf(getCurrPickingReq().getId())
              + "\nThe picking request is sent back to loading area.");
          getCurrPickingReq().updateLocation(Location.load);
          getPickingRequestManager()
              .putPalletes(new int[][]{frontPallet, backPallet},
                  getCurrPickingReq().getId());
        }

      } else {
        getCurrPickingReq().updateLocation(Location.pick);
        System.out.println("The loader tried to load an incomplete picking "
            + "request, the picking request was sent to be re "
            + "picked instead.");
      }
    } else {
      System.out.println("Loader " + getName() + " tried to load with no "
          + "picking request. Load action aborted.");
    }
    setCurrPickingReq(null);
    setPallets(new int[4], new int[4]);
  }
}
