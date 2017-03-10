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
  private ArrayList<Integer> currentFascias;
  private String logFilePath;
  
  protected Worker (String name, String logFilePath){
    this.name = name;
    this.logFilePath = logFilePath;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }
  
  protected abstract void scan();
  protected abstract void logEvent();
  
  
}
