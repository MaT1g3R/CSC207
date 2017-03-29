package util;

import fascia.PickingRequestManager;
import warehousefloor.WarehouseFloor;
import worker.WorkerManager;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * A class to keep track of all sub systems used in the program.
 *
 * @author Peijun
 */
public class MasterSystem {

  private WarehouseFloor warehouseFloor;
  private WorkerManager workerManager;
  private PickingRequestManager pickingRequestManager;
  private FileSystem fileSystem;
  private SkuTranslator skuTranslator;
  private Logger logger;
  private String outPutPath;

  /**
   * A setter for all the instance variables in this class.
   * Also sets the inventory of the warehouse floor.
   *
   * @param warehouseFloor        the warehouseFloor.
   * @param workerManager         the workerManager.
   * @param pickingRequestManager the PickingRequestManager.
   * @param fileSystem            the fileSystem.
   * @param skuTranslator         the skuTranslator.
   */
  public void setAll(
      WarehouseFloor warehouseFloor,
      WorkerManager workerManager,
      PickingRequestManager pickingRequestManager,
      FileSystem fileSystem,
      SkuTranslator skuTranslator,
      String outPutPath,
      boolean makeFile
  ) {
    this.outPutPath = outPutPath;
    this.warehouseFloor = warehouseFloor;
    this.workerManager = workerManager;
    this.pickingRequestManager = pickingRequestManager;
    this.fileSystem = fileSystem;
    this.skuTranslator = skuTranslator;
    this.warehouseFloor.setInventory();
    setLogger(makeFile);

  }

  private void setLogger(boolean makeFile) {
    if (makeFile){
    ConsoleHandler ch = new ConsoleHandler();
    this.logger = Logger.getLogger(getClass().getSimpleName());
    try {
      FileHandler fh = new FileHandler(outPutPath + "log.txt");
      fh.setFormatter(new SimpleFormatter());
      this.logger.addHandler(fh);
    } catch (IOException exception) {
      System.out.println("Logging file could not be opened. Logging only to console");
    }
    this.logger.addHandler(ch);}
  }

  /**
   * A getter for warehouseFloor.
   *
   * @return warehouseFloor
   */
  public WarehouseFloor getWarehouseFloor() {
    return warehouseFloor;
  }

  /**
   * A getter for workerManager.
   *
   * @return workerManager
   */
  public WorkerManager getWorkerManager() {
    return workerManager;
  }

  /**
   * A getter fot pickingRequestManager.
   *
   * @return pickingRequestManager
   */
  public PickingRequestManager getPickingRequestManager() {
    return pickingRequestManager;
  }

  /**
   * A getter for fileSystem.
   *
   * @return fileSystem
   */
  public FileSystem getFileSystem() {
    return fileSystem;
  }

  /**
   * A getter fot skuTranslator.
   *
   * @return skuTranslator
   */
  public SkuTranslator getSkuTranslator() {
    return skuTranslator;
  }

  public String getOutPutPath(){
    return outPutPath;
  }
}
