package util;

import fascia.PickingRequestManager;
import warehousefloor.WarehouseFloor;
import worker.WorkerManager;

/**
 * A class that creates MasterSystem.
 */
public final class MasterSystemFactory {

  /**
   * This should be never called.
   */
  private MasterSystemFactory() {
  }

  /**
   * Get a new instance of MasterSystem and all its sub systems set up properly.
   *
   * @param warehouseFilePath   the file to the initial state of the
   *                            warehousefloor
   * @param translationFilePath the file path to the translation table
   * @param traversalFilePath   the file path to the traversal table
   * @param outFilePath         the file path for output
   */
  public static MasterSystem getMasterSystem(
      String warehouseFilePath,
      String translationFilePath, String traversalFilePath,
      String outFilePath) {
    MasterSystem masterSystem = new MasterSystem();

    FileSystem fileSystem = new FileSystem(
        new String[]{warehouseFilePath, translationFilePath, traversalFilePath},
        new String[]{outFilePath + "orders.csv", outFilePath + "final.csv"});

    SkuTranslator skuTranslator = new SkuTranslator(
        fileSystem.getFileContent(traversalFilePath),
        fileSystem.getFileContent(translationFilePath));

    WorkerManager workerManager = new WorkerManager(masterSystem);

    PickingRequestManager pickingRequestManager = new PickingRequestManager();

    WarehouseFloor warehouseFloor = new WarehouseFloor(
        warehouseFilePath, outFilePath, masterSystem, 30);

    masterSystem.setAll(warehouseFloor, workerManager, pickingRequestManager,
        fileSystem, skuTranslator);
    return masterSystem;
  }
}
