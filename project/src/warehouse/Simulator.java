package warehouse;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * A class to simulate real world events from an input file.
 *
 * @author Peijun
 */
class Simulator {

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
   * @param eventFile the event file this will read from.
   * @param warehouseFilePath the file to the initial state of the warehouse
   * @param translationFilePath the file path to the translation table
   * @param traversalFilePath the file path to the traversal table
   * @param outFilePath the file path for output
   */
  Simulator(final String eventFile, final String warehouseFilePath,
      final String translationFilePath, final String traversalFilePath,
      final String outFilePath) {
    this.eventList = CsvReadWrite.readCsv(eventFile);
    SkuTranslator.setLocations(traversalFilePath);
    SkuTranslator.setProperties(translationFilePath);
    this.warehouse = new Warehouse(warehouseFilePath, outFilePath, 30);
  }

  /**
   * See if the string is for adding an order
   *
   * @param s the string to be checked
   * @return true if it's adding an order
   */
  private boolean isOrder(String s) {
    return Pattern.matches("Order \\w+ [A-Z]+", s);
  }

  private boolean pickerReady(String s) {
    return Pattern.matches("Picker \\w+ ready", s);
  }

  private boolean pickerPick(String s) {
    return Pattern.matches("Picker \\w+ pick [0-9]+", s);
  }

  private boolean pickerMarshall(String s) {
    return Pattern.matches("Picker \\w+ goto marshalling", s);
  }

  private boolean sequencerReady(String s) {
    return Pattern.matches("Sequencer \\w+ ready", s);
  }

  private boolean sequencerScan(String s) {
    return Pattern.matches("Sequencer \\w+ scan [0-9]+", s);
  }

  private boolean sequencerSequence(String s) {
    return Pattern.matches("Sequencer \\w+ sequence", s);
  }

  private boolean loaderReady(String s) {
    return Pattern.matches("Loader \\w+ Ready", s);
  }

  private boolean loaderScan(String s) {
    return Pattern.matches("Loader \\w+ scan [0-9]+", s);
  }

  private boolean loaderLoad(String s) {
    return Pattern.matches("Loader \\w+ load", s);
  }

  private boolean replen(String s) {
    return Pattern.matches("Replenisher \\w+ replenish [0-9]+", s);
  }

  private String getName(String s) {
    return s.split("\\s")[1];
  }

  private int getSku(String s) {
    return Integer.valueOf(s.split("\\s")[3]);
  }


  /**
   * The main event loop.
   */
  void run() {
    for (String s : eventList) {
      if (isOrder(s)) {
        warehouse.addOrder(s);
      }
      if (pickerReady(s)) {
        if (warehousegetName(s))
      }
    }
  }

}
