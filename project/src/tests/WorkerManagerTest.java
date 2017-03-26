package tests;

import fascia.Order;
import fascia.PickingRequest;
import fascia.PickingRequestManager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.MasterSystem;
import worker.Loader;
import worker.Picker;
import worker.Replenisher;
import worker.Sequencer;
import worker.Worker;
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
  private static HashMap<String, Field> workerFields;
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
    workerFields = new HashMap<>();
    pickers = WorkerManager.class.getDeclaredField("pickers");
    loaders = WorkerManager.class.getDeclaredField("loaders");
    sequencers = WorkerManager.class.getDeclaredField("sequencers");
    replenishers = WorkerManager.class.getDeclaredField("replenishers");
    pickingRequestManagerFields = PickingRequestManager.class
        .getDeclaredFields();
    for (Field field : pickingRequestManagerFields) {
      field.setAccessible(true);
    }
    for (Field field : Worker.class.getDeclaredFields()) {
      field.setAccessible(true);
      workerFields.put(field.getName(), field);
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
  public void updateReadyPickerNotNull()
      throws IllegalAccessException, NoSuchFieldException {
    Picker picker = new Picker("Names are hard", masterSystem);
    ArrayList<Order> orders = factory.randomOrders(10);
    for (Order order : orders) {
      masterSystem.getPickingRequestManager().addOrder(order);
    }
    workerManager.update(picker, true);
    Assert.assertEquals(6, getOrdersFromPickManage().size());
    Assert.assertNotNull(currPicReq(picker));
    Assert.assertEquals(8, getPickerLocations(picker).size());
    readyNotNullHelper(picker, null);
  }

  /**
   * Test for the update method when the Observable is a Picker and the
   * argument is true. And there are no  available picking requests in the
   * system.
   */
  @Test
  public void updateReadyPickerNull()
      throws IllegalAccessException, NoSuchFieldException {
    Picker picker = new Picker("FeelsBadMan", masterSystem);
    workerManager.update(picker, true);
    readyNullHelper(picker);
    Assert.assertNull(getPickerLocations(picker));
  }

  /**
   * Test for the update method when the Observable is a Sequencer and the
   * argument is true. And there are available picking requests in the system.
   */
  @Test
  public void updateReadySequencerNotNull() throws IllegalAccessException {
    PickingRequest pickingRequest = factory.pickingRequest(0, masterSystem
        .getPickingRequestManager());
    LinkedList<PickingRequest> marshallingArea = getRequestsList(
        "marshallingArea");
    marshallingArea.add(pickingRequest);
    Sequencer sequencer = new Sequencer("I want C#", masterSystem);
    workerManager.update(sequencer, true);
    readyNotNullHelper(sequencer, pickingRequest);
    Assert.assertEquals(0, marshallingArea.size());
  }

  /**
   * Test for the update method when the Observable is a Sequencer and the
   * argument is true. And there are no  available picking requests in the
   * system.
   */
  @Test
  public void updateReadySequencerNull() throws IllegalAccessException {
    Sequencer sequencer = new Sequencer("baka", masterSystem);
    workerManager.update(sequencer, true);
    readyNullHelper(sequencer);
  }

  /**
   * Test for the update method when the Observable is a Loader and the
   * argument is true. And there are available picking requests in the system.
   */
  @Test
  public void updateReadyLoaderNotNull()
      throws IllegalAccessException, NoSuchFieldException {
    Loader loader = new Loader("Alwin Catindig", masterSystem);
    PickingRequest pickingRequest = factory.pickingRequest(0, masterSystem
        .getPickingRequestManager());
    String[][] pallets = factory.generatePallets(pickingRequest);
    getManagerPallets().put(pickingRequest.getId(), pallets);
    LinkedList<PickingRequest> loadingArea = getRequestsList("loadingArea");
    loadingArea.add(pickingRequest);
    workerManager.update(loader, true);
    readyNotNullHelper(loader, pickingRequest);
    Assert.assertEquals(0, loadingArea.size());
    Assert.assertArrayEquals(pallets, getLoaderPallets(loader));
  }

  /**
   * Test for the update method when the Observable is a Loader and the
   * argument is true. And there are no  available picking requests in the
   * system.
   */
  @Test
  public void updateReadyLoaderNull()
      throws IllegalAccessException, NoSuchFieldException {
    Loader loader = new Loader("yasennnnn!!!!!", masterSystem);
    workerManager.update(loader, true);
    readyNullHelper(loader);
    String[][] expected = new String[][]{new String[4], new String[4]};
    Assert.assertArrayEquals(expected, getLoaderPallets(loader));
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

  /**
   * A helper method to get all of Worker class's instance variables.
   *
   * @param worker    the worker object
   * @param fieldName the name of the field for the variable
   */
  private Object getWorkerVar(Worker worker, String fieldName)
      throws IllegalAccessException {
    return workerFields.get(fieldName).get(worker);
  }

  /**
   * A helper method for getting Picker's location variable.
   *
   * @param picker the picker object to get the var from
   */
  private ArrayList<String> getPickerLocations(Picker picker)
      throws NoSuchFieldException, IllegalAccessException {
    Field field = Picker.class.getDeclaredField("locations");
    field.setAccessible(true);
    return (ArrayList<String>) field.get(picker);
  }

  /**
   * A helper method to get the orders var from PickingRequest manager class.
   */
  private LinkedList<Order> getOrdersFromPickManage() {
    return (LinkedList<Order>) pickingRequestManagerVars.get("orders");
  }

  /**
   * A helper method to get toBeScanned var from a Worker instance.
   *
   * @param worker the Worker instance
   * @return the toBeScanned linked list
   */
  private LinkedList<String> getToBeScanned(Worker worker)
      throws IllegalAccessException {
    return (LinkedList<String>) getWorkerVar(worker, "toBeScanned");
  }

  /**
   * Get the currPickingReq var from an instance of Worker.
   *
   * @param worker the worker instance
   * @return the currPickingRequest
   */
  private PickingRequest currPicReq(Worker worker)
      throws IllegalAccessException {
    return (PickingRequest) workerFields.get("currPickingReq").get(worker);
  }

  /**
   * Return one of the 3 LinkedList PickingRequest from the
   * PickingRequestManager class.
   *
   * @param name the name of the field
   */
  private LinkedList<PickingRequest> getRequestsList(String name) {
    return (LinkedList<PickingRequest>) pickingRequestManagerVars.get(name);
  }

  /**
   * A helper method to test when a worker tries to ready without available
   * picking requests.
   *
   * @param worker the worker instance
   */
  private void readyNullHelper(Worker worker) throws IllegalAccessException {
    Assert.assertNull(currPicReq(worker));
    Assert.assertEquals(0, getWorkerVar(worker, "scanCount"));
    Assert.assertEquals(0, getToBeScanned(worker).size());
  }

  /**
   * A helper to test when a worker readies with availble picking requests.
   *
   * @param worker         the worker instance
   * @param pickingRequest the pickingRequest instance
   */
  private void readyNotNullHelper(Worker worker, PickingRequest pickingRequest)
      throws IllegalAccessException {
    if (pickingRequest != null) {
      Assert.assertEquals(pickingRequest, currPicReq(worker));
    }
    Assert.assertEquals(0, getWorkerVar(worker, "scanCount"));
    Assert.assertEquals(8, getToBeScanned(worker).size());
  }

  /**
   * A helper method to get access to the pallets hashmap in
   * PickingRequestManager.
   *
   * @return pallets hashmap
   */
  private HashMap<Integer, String[][]> getManagerPallets() {
    return (HashMap<Integer, String[][]>) pickingRequestManagerVars
        .get("pallets");
  }

  /**
   * A helper method for getting Loader's front and back pallets.
   *
   * @param loader the loader
   * @return front and back pallets
   */
  private String[][] getLoaderPallets(Loader loader)
      throws NoSuchFieldException, IllegalAccessException {
    Field frontPalletField = Loader.class.getDeclaredField("frontPallet");
    Field backPalletField = Loader.class.getDeclaredField("backPallet");
    frontPalletField.setAccessible(true);
    backPalletField.setAccessible(true);
    String[] frontPallet = (String[]) frontPalletField.get(loader);
    String[] backPallet = (String[]) backPalletField.get(loader);
    return new String[][]{frontPallet, backPallet};
  }
}