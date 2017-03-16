package warehouse;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * A class to simulate real world events from an input file.
 *
 * @author Peijun
 */
public class Simulator {

  /**
   * The warehouse the Simulator is simulating.
   */
  private Warehouse warehouse;
  /**
   * The list of events the simulator is simulating.
   */
  private ArrayList<String> eventList;

  /**
   * This initialize a simulator object.
   *
   * @param eventFile           the event file this will read from.
   * @param warehouseFilePath   the file to the initial state of the warehouse
   * @param translationFilePath the file path to the translation table
   * @param traversalFilePath   the file path to the traversal table
   * @param outFilePath         the file path for output
   */
  public Simulator(final String eventFile, final String warehouseFilePath,
      final String translationFilePath, final String traversalFilePath,
      final String outFilePath) {
    this.eventList = CsvReadWrite.readCsv(eventFile);
    this.warehouse = new Warehouse(warehouseFilePath, translationFilePath,
        traversalFilePath, outFilePath, 30);
    this.warehouse
        .setPickingRequestManager(new PickingRequestManager(this.warehouse));
    this.warehouse.setWorkerManager(new WorkerManager());
  }

  /**
   * Check if the string is for adding an order.
   *
   * @param s the string to be checked
   * @return true if it's adding an order
   */
  private boolean isOrder(String s) {
    return Pattern.matches("Order \\w+ [A-Z]+", s);
  }

  /**
   * Check if the string is for ready a picker.
   *
   * @param s the string to be checked
   * @return true if it's ready a picker
   */
  private boolean pickerReady(String s) {
    return Pattern.matches("Picker \\w+ ready", s);
  }

  /**
   * Check if the string is for picker pick.
   *
   * @param s the string to be checked
   * @return true if it's picker pick
   */
  private boolean pickerPick(String s) {
    return Pattern.matches("Picker \\w+ pick [0-9]+", s);
  }

  /**
   * Check if the string is for picker to marshall.
   *
   * @param s the string to be checked
   * @return true if it's picker to marshall
   */
  private boolean pickerMarshall(String s) {
    return Pattern.matches("Picker \\w+ to marshalling", s);
  }

  /**
   * Check if the string is for ready a sequencer.
   *
   * @param s the string to be checked
   * @return true if it's ready a sequencer
   */
  private boolean sequencerReady(String s) {
    return Pattern.matches("Sequencer \\w+ ready", s);
  }

  /**
   * Check if the string is for sequencer scan.
   *
   * @param s the string to be checked
   * @return true if it's sequencer scan
   */
  private boolean sequencerScan(String s) {
    return Pattern.matches("Sequencer \\w+ scan [0-9]+", s);
  }

  /**
   * Check if the string is for sequencer sequence.
   *
   * @param s the string to be checked
   * @return true if it's sequencer sequence
   */
  private boolean sequencerSequence(String s) {
    return Pattern.matches("Sequencer \\w+ sequence", s);
  }

  /**
   * Check if the string is for ready a loader.
   *
   * @param s the string to be checked
   * @return true if it's ready a loader
   */
  private boolean loaderReady(String s) {
    return Pattern.matches("Loader \\w+ ready", s);
  }

  /**
   * Check if the string is for loader scan.
   *
   * @param s the string to be checked
   * @return true if it's loader scan
   */
  private boolean loaderScan(String s) {
    return Pattern.matches("Loader \\w+ scan [0-9]+", s);
  }

  /**
   * Check if the string is for loader load.
   *
   * @param s the string to be checked
   * @return true if it's loader load
   */
  private boolean loaderLoad(String s) {
    return Pattern.matches("Loader \\w+ load", s);
  }

  /**
   * Check if the string is for replenish.
   *
   * @param s the string to be checked
   * @return true if it's replenish
   */
  private boolean replenish(String s) {
    return Pattern.matches("Replenisher \\w+ replenish [0-9]+", s);
  }


  /**
   * Get the name of the worker from an event string.
   *
   * @param s the event string
   * @return the name of the worker
   */
  private String getName(String s) {
    return s.split("\\s")[1];
  }

  /**
   * Get the sku number from an event string.
   *
   * @param s the event string
   * @return the sku number
   */
  private int getSku(String s) {
    return Integer.valueOf(s.split("\\s")[3]);
  }


  /**
   * The main event loop.
   */
  public void run() {
    for (String s : eventList) {
      if (isOrder(s)) {
        warehouse.getPickingRequestManager().addOrder(s);
      }
      if (pickerReady(s)) {
        if (warehouse.getWorkerManager().getPicker(s) == null) {
          warehouse.getWorkerManager().addPicker(new Picker(getName(s),
              warehouse));
        }
        warehouse.getWorkerManager().getPicker(getName(s)).ready();
      }
      if (pickerPick(s)) {
        warehouse.getWorkerManager().getPicker(getName(s)).scan(getSku(s));
      }
      if (pickerMarshall(s)) {
        warehouse.getWorkerManager().getPicker(getName(s)).goToMarshall();
      }
      if (sequencerReady(s)) {
        if (warehouse.getWorkerManager().getSequencer(getName(s)) == null) {
          warehouse.getWorkerManager()
              .addSequencer(new Sequencer(getName(s), warehouse));
        }
        warehouse.getWorkerManager().getSequencer(getName(s)).ready();
      }
      if (sequencerScan(s)) {
        warehouse.getWorkerManager().getSequencer(getName(s)).scan(getSku(s));
      }
      if (sequencerSequence(s)) {
        warehouse.getWorkerManager().getSequencer(getName(s)).sequence();
      }
      if (loaderReady(s)) {
        if (warehouse.getWorkerManager().getLoader(getName(s)) == null) {
          warehouse.getWorkerManager()
              .addLoader(new Loader(getName(s), warehouse));
        }
        warehouse.getWorkerManager().getLoader(getName(s)).ready();
      }
      if (loaderScan(s)) {
        warehouse.getWorkerManager().getLoader(getName(s)).scan(getSku(s));
      }
      if (loaderLoad(s)) {
        warehouse.getWorkerManager().getLoader(getName(s)).load();
      }
      if (replenish(s)) {
        if (warehouse.getWorkerManager().getReplenisher(getName(s)) == null) {
          warehouse.getWorkerManager()
              .addReplenisher(new Replenisher(getName(s), warehouse));
        }
        warehouse.getWorkerManager().getReplenisher(getName(s))
            .replenish(getSku(s));
      }
    }
    warehouse.outPutResult();
  }
}
