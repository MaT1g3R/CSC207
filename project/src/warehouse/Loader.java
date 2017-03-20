package warehouse;

/**
 * A class to represent Loaders.
 *
 * @author Chaitanya
 */

public class Loader extends Worker {


  private String[] frontPallet = new String[4];
  private String[] backPallet = new String[4];

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
   * A getter for frontPallet.
   *
   * @return frontPallet
   */
  public String[] getFrontPallet() {
    return frontPallet;
  }

  /**
   * A getter for backPallet.
   *
   * @return backPallet
   */
  public String[] getBackPallet() {
    return backPallet;
  }

  /**
   * A setter for front and back pallets.
   *
   * @param frontPallet the front pallet
   * @param backPallet  the back pallet
   */
  public void setPallets(String[] frontPallet, String[] backPallet) {
    this.frontPallet = frontPallet;
    this.backPallet = backPallet;
  }

}
