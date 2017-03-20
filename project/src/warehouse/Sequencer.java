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
}
