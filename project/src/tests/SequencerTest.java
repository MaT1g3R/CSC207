package tests;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.LinkedList;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import warehouse.Loader;
import warehouse.Order;
import warehouse.PickingRequest;
import warehouse.Sequencer;
import warehouse.SkuTranslator;
import warehouse.Warehouse;

public class SequencerTest {

  Warehouse warehouse;
  Loader loader;
  PickingRequest pickreq;
  PickingRequest pickreq2;
  PickingRequest pickreq3;
  ArrayList<Order> orders;
  ArrayList<Order> orders2;
  ArrayList<Order> orders3;
  Sequencer sequencer;

  int[] frontPal = {1, 3, 5, 7};
  int[] backPal = {2, 4, 6, 8};

  @Before
  public void setUp() throws Exception {

    SkuTranslator.setLocations("tests/translation.csv");
    SkuTranslator.setProperties("tests/traversal_table.csv");
    warehouse = new Warehouse("tests/initial.csv", "../", 30);
    orders = new ArrayList<>();
    orders2 = new ArrayList<>();
    orders3 = new ArrayList<>();

    orders.add(new Order("Order Green S"));
    orders.add(new Order("Order Green SE"));
    orders.add(new Order("Order Green SES"));
    orders.add(new Order("Order Green SEL"));

    orders2.add(new Order("Order White S"));
    orders2.add(new Order("Order White SE"));
    orders2.add(new Order("Order White SES"));
    orders2.add(new Order("Order White SEL"));

    orders3.add(new Order("Order Red S"));
    orders3.add(new Order("Order Red SE"));
    orders3.add(new Order("Order Red SES"));
    orders3.add(new Order("Order Red SEL"));

    warehouse.addOrder("Order Green S");
    warehouse.addOrder("Order Green SE");
    warehouse.addOrder("Order Green SES");
    warehouse.addOrder("Order Green SEL");

    warehouse.addOrder("Order White S");
    warehouse.addOrder("Order White SE");
    warehouse.addOrder("Order White SES");
    warehouse.addOrder("Order White SEL");

    warehouse.addOrder("Order Red S");
    warehouse.addOrder("Order Red SE");
    warehouse.addOrder("Order Red SES");
    warehouse.addOrder("Order Red SEL");

    pickreq = new PickingRequest(orders, 0);
//    pickreq2 = new PickingRequest(orders2, 1);
//    pickreq3 = new PickingRequest(orders3, 2);

//    warehouse.sendToLoading(pickreq3, frontPal, backPal);

    loader = new Loader("Phil", warehouse);
    sequencer = new Sequencer("Billy", warehouse);

    warehouse.addSequencer(sequencer);
    warehouse.addLoader(loader);

    warehouse.sendToMarshalling(pickreq);
    warehouse.sendToMarshalling(pickreq2);
    
    sequencer.ready();

//    warehouse.readySequencer(sequencer);
//    warehouse.readyLoader(loader);

//    warehouse.sendToMarshalling(pickreq3);
//    warehouse.sendToLoading(pickreq3, frontPal, backPal);

  }

  @After
  public void tearDown() throws Exception {
    warehouse = null;
    orders = null;
    pickreq = null;
    sequencer = null;
    loader = null;
  }

  @Test
  public void testReady() {
    boolean actual = false;
    if (sequencer.getScanCount() == 0
        && sequencer.getCurrPickingReq().equals(pickreq)
        && warehouse.getSequencer("Billy").getScanOrder()
        .equals(sequencer.getScanOrder())) {
      actual = true;
    }

    Assert.assertEquals(true, actual);
  }

  @Test
  public void testSequencer() {
    boolean actual = false;
    if (sequencer.getName().equals("Billy")
        && sequencer.getWorksAt().equals(warehouse)) {
      actual = true;
    }

    Assert.assertEquals(true, actual);
  }

  @Test
  public void testSequence() {
    
    PickingRequest newPick = new PickingRequest(orders2, 1);
    
    warehouse.sendToMarshalling(newPick);
    
    sequencer.addScanCount();
    sequencer.addScanCount();
    sequencer.addScanCount();
    sequencer.addScanCount();
    sequencer.addScanCount();
    sequencer.addScanCount();
    sequencer.addScanCount();
    sequencer.addScanCount();

    sequencer.setCurrPickingReq(newPick);
    sequencer.sequence();
      
    loader.ready();
    PickingRequest expected = loader.getCurrPickingReq();

    Assert.assertEquals(newPick, expected);
  }

  @Test
  public void testWorker() {
    boolean actual = false;
    if (sequencer.getName().equals("Billy")
        && sequencer.getWorksAt().equals(warehouse)) {
      actual = true;
    }

    Assert.assertEquals(true, actual);
  }

  @Test
  public void testGetScanOrder() {
    LinkedList<Integer> expected = new
        LinkedList<>(sequencer.getCurrPickingReq().getProperSkus());

    Assert.assertEquals(expected, sequencer.getScanOrder());
  }

  @Test
  public void testSetCurrPickingReq() {
    sequencer.setCurrPickingReq(pickreq);

    PickingRequest expected = pickreq;

    Assert.assertEquals(expected, sequencer.getCurrPickingReq());
  }

  @Test
  public void testScanResult() {
    fail("Not yet implemented"); // TODO
  }

  @Test
  public void testScan() {
    fail("Not yet implemented"); // TODO
  }

  @Test
  public void testGetWorksAt() {
    fail("Not yet implemented"); // TODO
  }

  @Test
  public void testGetCurrPickingReq() {
    fail("Not yet implemented"); // TODO
  }

  @Test
  public void testGetToBeScanned() {
    fail("Not yet implemented"); // TODO
  }

  @Test
  public void testSetToBeScanned() {
    fail("Not yet implemented"); // TODO
  }

  @Test
  public void testGetName() {
    String name = "Billy";
    Assert.assertEquals(name, sequencer.getName());
  }

  @Test
  public void testAddScanCount() {
    sequencer.addScanCount();
    int scanAmount = sequencer.getScanCount();

    Assert.assertEquals(1, scanAmount);
  }

  @Test
  public void testGetScanCount() {
    int scanAmount = sequencer.getScanCount();

    Assert.assertEquals(0, scanAmount);
  }

  @Test
  public void testResetScanCount() {
    sequencer.resetScanCount();
    int scanAmount = sequencer.getScanCount();

    Assert.assertEquals(0, scanAmount);
  }

}
