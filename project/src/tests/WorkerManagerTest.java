package tests;

import fascia.PickingRequestManager;
import java.lang.reflect.Field;
import java.util.HashMap;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.MasterSystem;
import worker.Loader;
import worker.Picker;
import worker.Replenisher;
import worker.Sequencer;
import worker.WorkerManager;

/**
 * Unit test for WorkerManager class.
 */
public class WorkerManagerTest {

  private static Field pickers;
  private static Field loaders;
  private static Field sequencers;
  private static Field replenishers;
  private static Field[] pickingRequestManagerFields;
  private TestFactory factory;
  private MasterSystem masterSystem;
  private WorkerManager workerManager;
  private HashMap<String, Picker> pickersMap;
  private HashMap<String, Loader> loadersMap;
  private HashMap<String, Sequencer> sequencersMap;
  private HashMap<String, Replenisher> replenishersMap;
  private HashMap<String, Object> pickingRequestManagerVars;

  /**
   * Setup the static fields once before all tests.
   */
  @BeforeClass
  public static void setUpBeforeClass() throws NoSuchFieldException {
    pickers = WorkerManager.class.getDeclaredField("pickers");
    loaders = WorkerManager.class.getDeclaredField("loaders");
    sequencers = WorkerManager.class.getDeclaredField("sequencers");
    replenishers = WorkerManager.class.getDeclaredField("replenishers");
    pickingRequestManagerFields = PickingRequestManager.class
        .getDeclaredFields();
    for (Field field : pickingRequestManagerFields) {
      field.setAccessible(true);
    }
    pickers.setAccessible(true);
    loaders.setAccessible(true);
    sequencers.setAccessible(true);
    replenishers.setAccessible(true);
    TestFactory.supressPrint();
  }

  /**
   * Set up the non-static variables for each test.
   */
  @Before
  public void setUp() throws IllegalAccessException {
    factory = new TestFactory();
    masterSystem = factory.getTestEnviroment();
    workerManager = masterSystem.getWorkerManager();
    pickersMap = (HashMap<String, Picker>) pickers.get(workerManager);
    loadersMap = (HashMap<String, Loader>) loaders.get(workerManager);
    sequencersMap = (HashMap<String, Sequencer>) sequencers.get(workerManager);
    replenishersMap = (HashMap<String, Replenisher>)
        replenishers.get(workerManager);
    pickingRequestManagerVars = new HashMap<>();
    for (Field field : pickingRequestManagerFields) {
      pickingRequestManagerVars.put(field.getName(), field.get(masterSystem
          .getPickingRequestManager()));
    }
  }

  /**
   * Test for the update method when the Observable is a Picker and the
   * argument is true. And there are available picking requests in the system.
   */
  @Test
  public void updateReadyPickerNotNull() {

  }

  /**
   * Test for the update method when the Observable is a Picker and the
   * argument is true. And there are no  available picking requests in the
   * system.
   */
  @Test
  public void updateReadyPickerNull() {

  }

  /**
   * Test for the update method when the Observable is a Sequencer and the
   * argument is true. And there are available picking requests in the system.
   */
  @Test
  public void updateReadySequencerNotNull() {

  }

  /**
   * Test for the update method when the Observable is a Sequencer and the
   * argument is true. And there are no  available picking requests in the
   * system.
   */
  @Test
  public void updateReadySequencerNull() {

  }

  /**
   * Test for the update method when the Observable is a Loader and the
   * argument is true. And there are available picking requests in the system.
   */
  @Test
  public void updateReadyLoaderNotNull() {

  }

  /**
   * Test for the update method when the Observable is a Loader and the
   * argument is true. And there are no  available picking requests in the
   * system.
   */
  @Test
  public void updateReadyLoaderNull() {

  }

  /**
   * Test for the update method when the Observable is a Picker and the
   * argument is false. And the Picker has null pickingRequest.
   */
  @Test
  public void updateFinishPickerNull() {

  }

  /**
   * Test for the update method when the Observable is a Picker and the
   * argument is false. And the Picker has scanCount != 8.
   */
  @Test
  public void updateFinishPickerFail() {

  }

  /**
   * Test for the update method when the Observable is a Picker and the
   * argument is false. And the Picker has scanCount == 8.
   */
  @Test
  public void updateFinishPickerSuccess() {

  }

  /**
   * Test for the update method when the Observable is a Sequencer and the
   * argument is false. And the Sequencer has null pickingRequest.
   */
  @Test
  public void updateFinishSequencerNull() {

  }

  /**
   * Test for the update method when the Observable is a Sequencer and the
   * argument is false. And the Sequencer has scanCount != 8.
   */
  @Test
  public void updateFinishSequencerFail() {

  }

  /**
   * Test for the update method when the Observable is a Sequencer and the
   * argument is false. And the Sequencer has scanCount == 8.
   */
  @Test
  public void updateFinishSequencerSuccess() {

  }

  /**
   * Test for the update method when the Observable is a Loader and the
   * argument is false. And the Loader has null pickingRequest.
   */
  @Test
  public void updateFinishLoaderNull() {

  }

  /**
   * Test for the update method when the Observable is a Loader and the
   * argument is false. And the Loader has scanCount != 8.
   */
  @Test
  public void updateFinishLoaderFail() {

  }

  /**
   * Test for the update method when the Observable is a Loader and the
   * argument is false. And the Loader has scanCount == 8.
   */
  @Test
  public void updateFinishLoaderSuccess() {

  }

  /**
   * Test for update method when it fails.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void updateFail() {
    workerManager.update(null, "");
  }


  @Test
  public void addLoader() {

  }

  @Test
  public void addSequencer() {

  }

  @Test
  public void addPicker() {

  }

  @Test
  public void addReplenisher() {

  }

  @Test
  public void getLoader() {

  }

  @Test
  public void getPicker() {

  }

  @Test
  public void getSequencer() {

  }

  @Test
  public void getReplenisher() {

  }

}