package warehouse;

import java.util.ArrayList;

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
   * The main event loop.
   */
  void run() {

  }

}
