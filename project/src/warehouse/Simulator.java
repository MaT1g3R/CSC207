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

  private WorkerManager workerManager;
  private PickingRequestManager pickingRequestManager;

  /**
   * This initialize a simulator object.
   *
   * @param eventFile           the event file this will read from.
   * @param warehouseFilePath   the file to the initial state of the warehouse
   * @param translationFilePath the file path to the translation table
   * @param traversalFilePath   the file path to the traversal table
   * @param outFilePath         the file path for output
   */
  public Simulator(String eventFile, String warehouseFilePath,
      String translationFilePath, String traversalFilePath,
      String outFilePath) {
    eventList = CsvReadWrite.readCsv(eventFile);

    FileSystem fileSystem = new FileSystem(
        new String[]{warehouseFilePath, translationFilePath, traversalFilePath},
        new String[]{outFilePath + "orders.csv", outFilePath + "final.csv"});

    SkuTranslator skuTranslator = new SkuTranslator(
        fileSystem.getFileContent(traversalFilePath),
        fileSystem.getFileContent(translationFilePath));

    workerManager = new WorkerManager();

    pickingRequestManager = new PickingRequestManager();

    warehouse = new Warehouse(
        fileSystem, skuTranslator, pickingRequestManager, workerManager,
        warehouseFilePath, outFilePath, 30);

    workerManager.setWarehouse(warehouse);
    pickingRequestManager.setWarehouse(warehouse);
    warehouse.addTruck(new Truck(0));
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
   * Check if the string is for a worker being ready.
   *
   * @param s the string to be checked
   * @return true if it's a worker trying to ready.
   */
  private boolean workerReady(String s) {
    return Pattern.matches("\\w+ \\w+ ready", s);
  }

  /**
   * Check if the string is for worker to scan.
   *
   * @param s the string to be checked
   * @return true if it's worker scan
   */
  private boolean workerScan(String s) {
    return Pattern.matches("\\w+ \\w+ scan [0-9]+", s);
  }

  /**
   * Check if the string is for worker to finish.
   *
   * @param s the string to be checked
   * @return true if it's worker finish
   */
  private boolean workerFinish(String s) {
    return Pattern.matches("\\w+ \\w+ finish", s);
  }

  /**
   * Check if the string is for worker to rescan.
   *
   * @param s the event string
   * @return true if it's worker to rescan
   */
  private boolean workerRescan(String s) {
    return Pattern.matches("\\w+ \\w+ rescan", s);
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
  private String getSku(String s) {
    return s.split("\\s")[3];
  }

  /**
   * Get the job of a worker.
   *
   * @param s the event string
   * @return the job of the worker
   */
  private String getJob(String s) {
    return s.split("\\s")[0];
  }


  /**
   * The main event loop.
   */
  public void run() {
    for (String s : eventList) {
      if (isOrder(s)) {
        pickingRequestManager.addOrder(s);
      } else if (workerReady(s)) {
        createOrReadyWorker(getJob(s), getName(s));
      } else if (workerScan(s)) {
        scanHelper(getJob(s), getName(s), getSku(s));
      } else if (workerFinish(s)) {
        finishHelper(getJob(s), getName(s));
      } else if (workerRescan(s)) {
        rescanHelper(getJob(s), getName(s));
      }
      warehouse.outPutResult();
    }
  }


  /**
   * A helper to check for worker and ready it.
   *
   * @param job  the job of the worker
   * @param name the name of the worker
   */

  private void createOrReadyWorker(String job, String name) {
    if (job.equals(Picker.class.getSimpleName())) {
      if (workerManager.getPicker(name) == null) {
        workerManager.addPicker(new Picker(name, warehouse));
      }
      workerManager.getPicker(name).tryReady();
    } else if (job.equals(Sequencer.class.getSimpleName())) {
      if (workerManager.getSequencer(name) == null) {
        workerManager.addSequencer(new Sequencer(name, warehouse));
      }
      workerManager.getSequencer(name).tryReady();
    } else if (job.equals(Loader.class.getSimpleName())) {
      if (workerManager.getLoader(name) == null) {
        workerManager.addLoader(new Loader(name, warehouse));
      }
      workerManager.getLoader(name).tryReady();
    } else if (job.equals(Replenisher.class.getSimpleName())) {
      if (workerManager.getReplenisher(name) == null) {
        workerManager.addReplenisher(new Replenisher(name, warehouse));
      }
      workerManager.getReplenisher(name).ready();
    }
  }

  /**
   * A helper method to deal with scanning.
   *
   * @param job  the job of the worker.
   * @param name the name of the worker.
   * @param sku  the sku the worker scanned.
   */
  private void scanHelper(String job, String name, String sku) {
    if (job.equals(Picker.class.getSimpleName())) {
      workerManager.getPicker(name).scan(sku);
    } else if (job.equals(Sequencer.class.getSimpleName())) {
      workerManager.getSequencer(name).scan(sku);
    } else if (job.equals(Loader.class.getSimpleName())) {
      workerManager.getLoader(name).scan(sku);
    } else if (job.equals(Replenisher.class.getSimpleName())) {
      workerManager.getReplenisher(name).scan(sku);
    }
  }

  /**
   * A helper method to deal with finishing.
   *
   * @param job  the job of the worker
   * @param name the name of the worker
   */
  private void finishHelper(String job, String name) {
    if (job.equals(Picker.class.getSimpleName())) {
      workerManager.getPicker(name).finish();
    } else if (job.equals(Sequencer.class.getSimpleName())) {
      workerManager.getSequencer(name).finish();
    } else if (job.equals(Loader.class.getSimpleName())) {
      workerManager.getLoader(name).finish();
    } else if (job.equals(Replenisher.class.getSimpleName())) {
      workerManager.getReplenisher(name).finish();
    }
  }

  /**
   * A helper to deal with rescan.
   *
   * @param job  the job of the worker.
   * @param name the name of the worker.
   */
  private void rescanHelper(String job, String name) {
    if (job.equals(Picker.class.getSimpleName())) {
      workerManager.getPicker(name).rescan();
    } else if (job.equals(Sequencer.class.getSimpleName())) {
      workerManager.getSequencer(name).rescan();
    } else if (job.equals(Loader.class.getSimpleName())) {
      workerManager.getLoader(name).rescan();
    }
  }
}
