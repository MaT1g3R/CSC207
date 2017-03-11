package warehouse;

import java.util.ArrayList;
import java.util.HashMap;
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
   * The file path to the reanslation table.
   */
  private String translationPath;

  /**
   * The file path to the transversal table.
   */
  private String traversalPath;

  /**
   * The file path to the output file.
   */
  private String outPath;

  /**
   * The max amout on a rack is 30.
   */
  private static final int MAX_STOCK = 30;

  /**
   * This initialize a simulator object.
   *
   * @param eventFile the event file this will read from.
   * @param warehouseFilePath the file to the initial state of the warehouse
   * @param translationFilePath the file path to the translation table
   * @param traversalFilePath the file path to the traversal table
   * @param outFilePath the file path for output
   */
  public Simulator(final String eventFile, final String warehouseFilePath,
      final String translationFilePath, final String traversalFilePath,
      final String
          outFilePath) {
    this.eventList = CsvReadWrite.readCsv(eventFile);
    this.warehouse = new Warehouse(warehouseFilePath);
    this.translationPath = translationFilePath;
    this.traversalPath = traversalFilePath;
    this.outPath = outFilePath;
  }

  /**
   * This output the simulation result.
   */
  private void outPutResult() {
    ArrayList<String> result = new ArrayList<>();
    for (HashMap.Entry<Integer, Integer> entry : this.warehouse.getInventory()
        .entrySet()) {
      if (entry.getValue() < MAX_STOCK) {
        String location = SkuTranslator.getLocation(entry.getKey());
        result.add(location + "," + entry.getValue());
      }
    }
    CsvReadWrite.overWrite(result, outPath);
  }

  /**
   * The main event loop.
   */
  public void run() {
    SkuTranslator.setLocations(translationPath);
    SkuTranslator.setProperties(traversalPath);
    final int skuIndex = 3;
    for (String s : this.eventList) {
      if (Pattern.matches("Order [A-Z]+ [A-Z][a-z]+", s)) {
        // Process an order
        this.warehouse.addOrder(new Order(s));
      } else if (Pattern.matches("Picker \\w+ ready", s)) {
        // Picker ready
        String name = s.split("\\s")[1];
        if (warehouse.getPickerByName(name) != null) {
          this.warehouse.addPicker(new Picker(name, this.warehouse));
        }
      } else if (Pattern.matches("Picker \\w+ pick [0-9]", s)) {
        // Picker picks
        String name = s.split("\\s")[1];
        int sku = Integer.parseInt(s.split("\\s")[skuIndex]);
        this.warehouse.getPickerByName(name).scan(sku);
      } else if (Pattern.matches("Picker \\w+ to Marshaling", s)) {
        // Picker to marshaling
        String name = s.split("\\s")[1];
        this.warehouse.getPickerByName(name).goToMarshaling();
      } else if (Pattern.matches("Sequencer \\w+ ready", s)) {
        // Sequencer ready
        String name = s.split("\\s")[1];
        if (warehouse.getSequencerByName(name) != null) {
          this.warehouse.addSequencer(new Sequencer(name, this.warehouse));
        }
      } else if (Pattern.matches("Sequencer \\w+ sequences", s)) {
        // Sequencer sequences
        String name = s.split("\\s")[1];
        this.warehouse.getSequencerByName(name).sequence();
      } else if (Pattern.matches("Loader \\w+ ready", s)) {
        // Loader ready
        String name = s.split("\\s")[1];
        if (warehouse.getLoaderByName(name) != null) {
          this.warehouse.addLoader(new Loader(name, this.warehouse));
        }
      } else if (Pattern.matches("Loader \\w+ loads", s)) {
        // Loader loads
        String name = s.split("\\s")[1];
        this.warehouse.getLoaderByName(name).load();
      } else if (Pattern.matches("Replenisher \\w+ ready", s)) {
        // Replenisher ready
        String name = s.split("\\s")[1];
        if (warehouse.getReplenisherByName(name) != null) {
          this.warehouse.addReplenisher(new Replenisher(name, this.warehouse));
        }
      } else if (Pattern.matches("Replenisher \\w+ replenish [0-9]+", s)) {
        // Replenisher replenish
        String name = s.split("\\s")[1];
        int sku = Integer.parseInt(s.split("\\s")[skuIndex]);
        this.warehouse.getReplenisherByName(name).replenish(sku);
      }
    }
    outPutResult();
  }

}
