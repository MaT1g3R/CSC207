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
   * @param eventFile the event file this will read from.
   * @param warehouseFilePath the file to the initial state of the warehouse
   * @param translationFilePath the file path to the translation table
   * @param traversalFilePath the file path to the traversal table
   * @param outFilePath the file path for output
   */
  public Simulator(final String eventFile, final String warehouseFilePath,
      final String translationFilePath, final String traversalFilePath,
      final String outFilePath) {
    this.eventList = CsvReadWrite.readCsv(eventFile);
    SkuTranslator.setLocations(traversalFilePath);
    SkuTranslator.setProperties(translationFilePath);
    this.warehouse = new Warehouse(warehouseFilePath, outFilePath);

  }


  /**
   * The main event loop.
   */
  public void run() {

    final int skuIndex = 3;
    for (String s : this.eventList) {
      if (Pattern.matches("Order [A-Z][a-z]+ [A-Z]+", s)) {
        // Process an order
        this.warehouse.addOrder(new Order(s));
      } else if (Pattern.matches("Picker \\w+ ready", s)) {
        // Picker ready
        String name = s.split("\\s")[1];
        if (warehouse.getWorker(name, "picker") == null) {
          this.warehouse.addWorker(new Picker(name, this.warehouse), "picker");
        }
        this.warehouse.getWorker(name, "picker").getReady();
      } else if (Pattern.matches("Picker \\w+ pick [0-9]+", s)) {
        // Picker picks
        String name = s.split("\\s")[1];
        int sku = Integer.parseInt(s.split("\\s")[skuIndex]);
        this.warehouse.getWorker(name, "picker").scan(sku);
      } else if (Pattern.matches("Picker \\w+ to Marshaling", s)) {
        // Picker to marshaling
        String name = s.split("\\s")[1];
        ((Picker) this.warehouse.getWorker(name, "picker")).goToMarshaling();
      } else if (Pattern.matches("Sequencer \\w+ ready", s)) {
        // Sequencer ready
        String name = s.split("\\s")[1];
        if (warehouse.getWorker(name, "sequencer") == null) {
          this.warehouse.addWorker(new Sequencer(name, this.warehouse), "sequencer");
        }
        this.warehouse.getWorker(name, "sequencer").getReady();
      } else if (Pattern.matches("Sequencer \\w+ sequences", s)) {
        // Sequencer sequences
        String name = s.split("\\s")[1];
        ((Sequencer)this.warehouse.getWorker(name, "sequencer")).sequence();
      } else if (Pattern.matches("Loader \\w+ ready", s)) {
        // Loader ready
        String name = s.split("\\s")[1];
        if (warehouse.getWorker(name, "loader") == null) {
          this.warehouse.addWorker(new Loader(name, this.warehouse), "loader");
        }
        this.warehouse.getWorker(name, "loader").getReady();
      } else if (Pattern.matches("Loader \\w+ loads", s)) {
        // Loader loads
        String name = s.split("\\s")[1];
        ((Loader)this.warehouse.getWorker(name, "loader")).load();
      } else if (Pattern.matches("Replenisher \\w+ ready", s)) {
        // Replenisher ready
        String name = s.split("\\s")[1];
        if (warehouse.getWorker(name, "replenisher") == null) {
          this.warehouse.addWorker(new Replenisher(name, this.warehouse), "replenisher");
        }
      } else if (Pattern.matches("Replenisher \\w+ replenish [0-9]+", s)) {
        // Replenisher replenish
        String name = s.split("\\s")[1];
        int sku = Integer.parseInt(s.split("\\s")[skuIndex]);
        ((Replenisher)this.warehouse.getWorker(name, "replenisher")).replenish(sku);
      } else if (Pattern.matches("Sequencer \\w+ scans [0-9]+", s)) {
        String name = s.split("\\s")[1];
        int sku = Integer.parseInt(s.split("\\s")[skuIndex]);
        this.warehouse.getWorker(name, "sequencer").scan(sku);
      } else if (Pattern.matches("Loader \\w+ scans [0-9]+", s)) {
        String name = s.split("\\s")[1];
        int sku = Integer.parseInt(s.split("\\s")[skuIndex]);
        this.warehouse.getWorker(name, "loader").scan(sku);
      }
    }
    warehouse.outPutInventory();
  }

}
