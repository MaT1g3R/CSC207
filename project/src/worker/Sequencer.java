package worker;

import java.util.LinkedList;
import util.MasterSystem;
import warehousefloor.Location;

/**
 * A class to represent sequencers.
 *
 * @author Andrew
 */
public class Sequencer extends Worker {

  private MasterSystem masterSystem;

  /**
   * The initializer for Sequencer.
   *
   * @param name         the name
   * @param masterSystem the masterSystem
   */
  public Sequencer(String name, MasterSystem masterSystem) {
    super(name, masterSystem);
    this.masterSystem = masterSystem;
  }

  /**
   * The sequencer doesn't do anything special for the ready action.
   */
  @Override
  void readyAction() {
  }

  /**
   * What a sequencer does when it finishes.
   */
  @Override
  void finishAction() {
    LinkedList<String> skus = getCurrPickingReq().getProperSkus();
    String[] frontPallet = new String[4];
    String[] backPallet = new String[4];
    for (int i = 0; i < 4; i++) {
      frontPallet[i] = skus.get(i);
      backPallet[i] = skus.get(i + 4);
    }
    getCurrPickingReq().updateLocation(Location.load);
    masterSystem.getPickingRequestManager()
        .putPalletes(new String[][]{frontPallet,
            backPallet}, getCurrPickingReq().getId());
    System.out
        .println("The sequencer " + getName() + " has finished "
            + "sequencing and sent the picking request for loading.");
  }
}
