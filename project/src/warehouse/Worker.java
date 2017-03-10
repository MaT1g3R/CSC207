/**
 * 
 */
package warehouse;

import java.util.ArrayList;

/**
 * @author Andrew
 *
 */
public abstract class Worker {

  private String name;
  private ArrayList<Fascia> currentFascias;
  private String logFilePath;
  
  protected Worker (String name, String logFilePath, ArrayList<Fascia> currentFascias){
    this.name = name;
    this.logFilePath = logFilePath;
    this.currentFascias = currentFascias;
  }

  /**
   * @return the name of the worker
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to change to if the worker decides to change their name
   */
  public void changeName(String name) {
    this.name = name;
  }
  
  /**
   * @return the currentFascias
   */
  public ArrayList<Fascia> getCurrentFascias() {
    return currentFascias;
  }

  protected abstract void logEvent();
  protected abstract void performJob();
  
  
}
