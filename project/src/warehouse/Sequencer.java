package warehouse;

import java.util.LinkedList;
import warehouse.PickingRequest.Location;

/**
 * A class to represent sequencers.
 *
 * @author Andrew
 */
public class Sequencer extends Worker {

  /**
   * The initializer for Sequencer.
   *
   * @param name    the name
   * @param worksAt where it works at
   */
  public Sequencer(String name, Warehouse worksAt) {
    super(name, worksAt);
  }

  /**
   * The action for a worker being ready.
   */
  @Override
  public void ready() {
    setCurrPickingReq(
        getPickingRequestManager().popRequest(Location.marshall));
    if (getCurrPickingReq() != null) {
      resetScanCount();
      setToBeScanned(getScanOrder());
      System.out.println("Sequencer " + getName() + " is ready to marshall.");
    } else {
      System.out.println("Sequencer " + getName() + " tried to ready with no "
          + "picking request. Ready action aborted.");
    }
  }

  /**
   * The method for when a sequencer sequences.
   */
  public void sequence() {
    if (getScanCount() == 8 && getCurrPickingReq() != null) {
      LinkedList<Integer> skus = getCurrPickingReq().getProperSkus();
      int[] frontPallet = new int[4];
      int[] backPallet = new int[4];
      for (int i = 0; i < 4; i++) {
        frontPallet[i] = skus.get(i);
        backPallet[i] = skus.get(i + 4);
      }
      getCurrPickingReq().updateLocation(Location.load);
      getPickingRequestManager().putPalletes(new int[][]{frontPallet,
          backPallet}, getCurrPickingReq().getId());
      System.out.println("The sequencer " + getName() + " has finished "
          + "sequencing and sent the picking request for loading.");
    } else if (getCurrPickingReq() != null) {
      getCurrPickingReq().updateLocation(Location.pick);
      System.out.println("The sequencer tried to send an incomplete picking "
          + "request for loading, the picking request was sent to be re "
          + "picked instead.");
    } else {
      System.out.println("Sequencer " + getName() + " tried to marshall with "
          + "no picking request. Sequence action aborted.");
    }
    setCurrPickingReq(null);
  }
}
