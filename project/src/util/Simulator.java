package util;

import fascia.Order;
import java.util.ArrayList;
import java.util.regex.Pattern;
import warehousefloor.Truck;
import warehousefloor.WarehouseFloor;
import worker.Loader;
import worker.Picker;
import worker.Replenisher;
import worker.Sequencer;

/**
 * A class to simulate real world events from an input file.
 *
 * @author Peijun
 */
public class Simulator {

  /**
   * The warehousefloor the Simulator is simulating.
   */
  private WarehouseFloor warehouseFloor;
  /**
   * The list of events the simulator is simulating.
   */
  private ArrayList<String> eventList;

  private MasterSystem masterSystem;

  /**
   * This initialize a simulator object.
   *
   * @param eventFile           the event file this will read from.
   * @param warehouseFilePath   the file to the initial state of the
   *                            warehousefloor
   * @param translationFilePath the file path to the translation table
   * @param traversalFilePath   the file path to the traversal table
   * @param outFilePath         the file path for output
   */
  public Simulator(String eventFile, String warehouseFilePath,
      String translationFilePath, String traversalFilePath,
      String outFilePath) {
    masterSystem = MasterSystemFactory.getMasterSystem(warehouseFilePath,
        translationFilePath, traversalFilePath, outFilePath);
    eventList = CsvReadWrite.readCsv(eventFile);
    masterSystem.getWarehouseFloor().addTruck(new Truck((0)));
    warehouseFloor = masterSystem.getWarehouseFloor();
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
    return Pattern.matches("\\w+ \\w+ scan \\w+", s);
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
        Order order = new Order(s, masterSystem.getSkuTranslator());
        masterSystem.getPickingRequestManager().addOrder(order);
      } else if (workerReady(s)) {
        createOrReadyWorker(getJob(s), getName(s));
      } else if (workerScan(s)) {
        scanHelper(getJob(s), getName(s), getSku(s));
      } else if (workerFinish(s)) {
        finishHelper(getJob(s), getName(s));
      } else if (workerRescan(s)) {
        rescanHelper(getJob(s), getName(s));
      }
      warehouseFloor.outPutResult();
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
      if (masterSystem.getWorkerManager().getPicker(name) == null) {
        masterSystem.getWorkerManager()
            .addPicker(new Picker(name, masterSystem));
      }
      masterSystem.getWorkerManager().getPicker(name).tryReady();
    } else if (job.equals(Sequencer.class.getSimpleName())) {
      if (masterSystem.getWorkerManager().getSequencer(name) == null) {
        masterSystem.getWorkerManager()
            .addSequencer(new Sequencer(name, masterSystem));
      }
      masterSystem.getWorkerManager().getSequencer(name).tryReady();
    } else if (job.equals(Loader.class.getSimpleName())) {
      if (masterSystem.getWorkerManager().getLoader(name) == null) {
        masterSystem.getWorkerManager()
            .addLoader(new Loader(name, masterSystem));
      }
      masterSystem.getWorkerManager().getLoader(name).tryReady();
    } else if (job.equals(Replenisher.class.getSimpleName())) {
      if (masterSystem.getWorkerManager().getReplenisher(name) == null) {
        masterSystem.getWorkerManager()
            .addReplenisher(new Replenisher(name, masterSystem));
      }
      masterSystem.getWorkerManager().getReplenisher(name).ready();
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
      masterSystem.getWorkerManager().getPicker(name).scan(sku);
    } else if (job.equals(Sequencer.class.getSimpleName())) {
      masterSystem.getWorkerManager().getSequencer(name).scan(sku);
    } else if (job.equals(Loader.class.getSimpleName())) {
      masterSystem.getWorkerManager().getLoader(name).scan(sku);
    } else if (job.equals(Replenisher.class.getSimpleName())) {
      masterSystem.getWorkerManager().getReplenisher(name).scan(sku);
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
      masterSystem.getWorkerManager().getPicker(name).finish();
    } else if (job.equals(Sequencer.class.getSimpleName())) {
      masterSystem.getWorkerManager().getSequencer(name).finish();
    } else if (job.equals(Loader.class.getSimpleName())) {
      masterSystem.getWorkerManager().getLoader(name).finish();
    } else if (job.equals(Replenisher.class.getSimpleName())) {
      masterSystem.getWorkerManager().getReplenisher(name).finish();
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
      masterSystem.getWorkerManager().getPicker(name).rescan();
    } else if (job.equals(Sequencer.class.getSimpleName())) {
      masterSystem.getWorkerManager().getSequencer(name).rescan();
    } else if (job.equals(Loader.class.getSimpleName())) {
      masterSystem.getWorkerManager().getLoader(name).rescan();
    }
  }
}
