package warehouse;

import java.util.HashMap;

/**
 * A class to keep track of all workers.
 *
 * @author Peijun
 */
public class WorkerManager {

  private HashMap<String, Picker> pickers = new HashMap<>();
  private HashMap<String, Loader> loaders = new HashMap<>();
  private HashMap<String, Sequencer> sequencers = new HashMap<>();
  private HashMap<String, Replenisher> replenishers = new HashMap<>();

  /**
   * Add a loader to the warehouse.
   *
   * @param loader the loader to be added
   */
  public void addLoader(Loader loader) {
    loaders.put(loader.getName(), loader);
  }

  /**
   * Add a sequencer to the warehouse.
   *
   * @param sequencer the sequencer to be added
   */
  public void addSequencer(Sequencer sequencer) {
    sequencers.put(sequencer.getName(), sequencer);
  }

  /**
   * Add a picker to the warehouse.
   *
   * @param picker the picker to be added
   */
  public void addPicker(Picker picker) {
    pickers.put(picker.getName(), picker);
  }

  /**
   * Add a replenisher to the warehouse.
   *
   * @param replenisher the replenisher to be added
   */
  public void addReplenisher(Replenisher replenisher) {
    replenishers.put(replenisher.getName(), replenisher);
  }

  /**
   * Get a loader by name.
   *
   * @param name the name
   * @return the loader with that name
   */
  public Loader getLoader(String name) {
    return loaders.get(name);
  }

  /**
   * Get a picker by name.
   *
   * @param name the name
   * @return the picker with that name
   */
  public Picker getPicker(String name) {
    return pickers.get(name);
  }

  /**
   * Get a sequencer by name.
   *
   * @param name the name
   * @return the sequencer with that name
   */
  public Sequencer getSequencer(String name) {
    return sequencers.get(name);
  }

  /**
   * Get a replenisher by name.
   *
   * @param name the name
   * @return the replenisher with that name
   */
  public Replenisher getReplenisher(String name) {
    return replenishers.get(name);
  }

}
