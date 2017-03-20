package warehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/**
 * A class to keep track of all workers.
 *
 * @author Peijun
 */
public class WorkerManager implements Observer {

  private HashMap<String, Picker> pickers = new HashMap<>();
  private HashMap<String, Loader> loaders = new HashMap<>();
  private HashMap<String, Sequencer> sequencers = new HashMap<>();
  private HashMap<String, Replenisher> replenishers = new HashMap<>();
  private Warehouse warehouse;

  /**
   * This method is called whenever the observed observableWorker is changed.
   *
   * @param observableWorker the Worker object to be updated.
   * @param isReady          the isReady boolean passed to this method.
   */
  @Override
  public void update(Observable observableWorker, Object isReady) {
    if (isReady instanceof Boolean && observableWorker instanceof Worker) {
      boolean readyBool = (boolean) isReady;
      Worker worker = (Worker) observableWorker;
      if (readyBool) {
        assignPickingRequest(worker);
        readyAction(worker);
      } else {
        finishAction(worker);
      }
    } else {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * Assign a picking request to worker based on its type.
   *
   * @param worker the worker to assign picking request to.
   */
  private void assignPickingRequest(Worker worker) {
    if (worker instanceof Picker) {
      worker.setCurrPickingReq(
          warehouse.getPickingRequestManager().getForPicking());
    } else if (worker instanceof Sequencer) {
      worker.setCurrPickingReq(warehouse
          .getPickingRequestManager().popRequest(Location.marshall));
    } else if (worker instanceof Loader) {
      worker.setCurrPickingReq(warehouse
          .getPickingRequestManager().popRequest(Location.load));
    }
  }

  /**
   * A helper method to make a worker become ready.
   *
   * @param worker the worker to become ready.
   */
  private void readyAction(Worker worker) {
    String job = worker.getClass().getSimpleName();
    String name = worker.getName();
    if (worker.getCurrPickingReq() == null) {
      System.out.println(job + " " + name + " tried to ready with no picking"
          + " request. Ready " + "action aborted.");
    } else {
      String displayString = job + " " + name + " is ready.";
      if (worker instanceof Picker) {
        ArrayList<String> toBeOptimized = new ArrayList<>();
        for (Order o : worker.getCurrPickingReq().getOrders()) {
          toBeOptimized.add(o.getSkus()[0]);
          toBeOptimized.add(o.getSkus()[1]);
        }
        ((Picker) worker).setLocations(WarehousePicking
            .optimize(toBeOptimized, worker.getWorksAt().getSkuTranslator()));
        displayString += "\nIt will go to locations:\n";
        for (String loc : ((Picker) worker).getLocations()) {
          displayString += loc + "\n";
        }
      } else if (worker instanceof Loader) {
        String[][] pallets = warehouse.getPickingRequestManager()
            .popPallets(worker.getCurrPickingReq().getId());
        ((Loader) worker).setPallets(pallets[0], pallets[1]);
      }
      worker.resetScanCount();
      worker.setToBeScanned(worker.getScanOrder());
      System.out.println(displayString);
    }
  }

  /**
   * Finish action for a worker.
   *
   * @param worker the worker whose finished
   */
  private void finishAction(Worker worker) {
    String job = worker.getClass().getSimpleName();
    String name = worker.getName();
    if (worker.getCurrPickingReq() == null) {
      System.out.println(job + " " + name + " tried to finish its job with no"
          + " picking request, finish action aborted.");
    } else {
      if (worker instanceof Loader) {
        loaderFinish((Loader) worker);
        ((Loader) worker).setPallets(new String[4], new String[4]);
      } else if (worker instanceof Picker) {
        pickerFinish((Picker) worker);
      } else if (worker instanceof Sequencer) {
        sequencerFinish((Sequencer) worker);
      }
    }
    worker.setCurrPickingReq(null);
    worker.resetScanCount();
  }

  /**
   * Helper for finish action when the worker is a picker.
   *
   * @param picker the finished picker.
   */
  private void pickerFinish(Picker picker) {
    if (picker.getScanCount() == 8) {
      picker.getCurrPickingReq().updateLocation(Location.marshall);
      System.out.println(
          "Picker " + picker.getName() + " has gone to marshalling area.");
    } else {
      picker.getCurrPickingReq().updateLocation(Location.pick);
      System.out.println("Picker " + picker.getName()
          + " tried to go to marshalling area with less than 8 fascias picked, "
          + "the picking request has been sent back to be picked again.");
    }
  }

  /**
   * Helper for finish action when the worker is a sequencer.
   *
   * @param sequencer the finished sequencer.
   */
  private void sequencerFinish(Sequencer sequencer) {
    if (sequencer.getScanCount() == 8) {
      LinkedList<String> skus = sequencer.getCurrPickingReq().getProperSkus();
      String[] frontPallet = new String[4];
      String[] backPallet = new String[4];
      for (int i = 0; i < 4; i++) {
        frontPallet[i] = skus.get(i);
        backPallet[i] = skus.get(i + 4);
      }
      sequencer.getCurrPickingReq().updateLocation(Location.load);
      sequencer.getPickingRequestManager()
          .putPalletes(new String[][]{frontPallet,
              backPallet}, sequencer.getCurrPickingReq().getId());
      System.out
          .println("The sequencer " + sequencer.getName() + " has finished "
              + "sequencing and sent the picking request for loading.");
    } else {
      sequencer.getCurrPickingReq().updateLocation(Location.pick);
      System.out.println("The sequencer tried to send an incomplete picking "
          + "request for loading, the picking request was sent to be re "
          + "picked instead.");
    }
  }

  /**
   * Helper for finish action when the worker is a loader.
   *
   * @param loader the finished loader.
   */
  private void loaderFinish(Loader loader) {
    if (loader.getScanCount() == 8) {
      Truck truck = loader.getWorksAt().getFirstNonFullTruck();
      if (truck.addCargo(loader.getFrontPallet(), loader.getBackPallet(), loader
          .getCurrPickingReq().getId()
      )) {
        System.out
            .println("Loader " + loader.getName() + " loaded picking request"
                + " " + String.valueOf(loader.getCurrPickingReq().getId()));
        for (Order o : loader.getCurrPickingReq().getOrders()) {
          loader.getWorksAt().logLoading(o.toString());
        }
      } else {
        System.out
            .println("Loader " + loader.getName() + " could not load picking "
                + "request " + String
                .valueOf(loader.getCurrPickingReq().getId())
                + "\nThe picking request is sent back to loading area.");
        loader.getCurrPickingReq().updateLocation(Location.load);
        loader.getPickingRequestManager()
            .putPalletes(
                new String[][]{loader.getFrontPallet(), loader.getBackPallet()},
                loader.getCurrPickingReq().getId());
      }
    } else {
      loader.getCurrPickingReq().updateLocation(Location.pick);
      System.out.println("The loader tried to load an incomplete picking "
          + "request, the picking request was sent to be re "
          + "picked instead.");
    }
  }

  /**
   * A setter for this.warehouse.
   *
   * @param warehouse the warehouse
   */
  public void setWarehouse(Warehouse warehouse) {
    this.warehouse = warehouse;
  }

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
