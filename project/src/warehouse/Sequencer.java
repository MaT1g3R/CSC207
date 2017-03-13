package warehouse;

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
  void ready() {
    getWorksAt().readySequencer(this);
    resetScanCount();
    setToBeScanned(getScanOrder());
    System.out.println("Sequencer " + getName() + " is ready to sequence.");
  }

  /**
   * The method for when a sequencer sequence.
   */
  void sequence() {
    if (getScanCount() == 8) {
      getWorksAt().sendToLoading(getCurrPickingReq());
      System.out.println("The sequencer " + getName() + " has finished "
          + "sequencing and sent the picking request for loading.");
    } else {
      getWorksAt().sendBackToPicking(getCurrPickingReq());
      System.out.println("The sequencer tried to send an incomplete picking "
          + "request for loading, the picking request was sent to be re "
          + "picked instead.");
    }
  }
}
