package tests;

import static org.junit.Assert.*;

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
  ArrayList<Order> orders;
  Sequencer sequencer;

  @Before
  public void setUp() throws Exception {
    
    SkuTranslator.setLocations("translation.csv");
    SkuTranslator.setProperties("traversal_table.csv");
    warehouse = new Warehouse("initial.csv", "../", 30);
    orders = new ArrayList<>();
    
    orders.add(new Order("Order Green S"));
    orders.add(new Order("Order Green SE"));
    orders.add(new Order("Order Green SES"));
    orders.add(new Order("Order Green SEL"));
    
    warehouse.addOrder("Order Green S");
    warehouse.addOrder("Order Green SE");
    warehouse.addOrder("Order Green SES");
    warehouse.addOrder("Order Green SEL");
    

    
    pickreq = new PickingRequest(orders, 1);
    sequencer = new Sequencer("Billy", warehouse);
    warehouse.addSequencer(sequencer);
    warehouse.sendToMarshalling(pickreq);
    
    warehouse.readySequencer(sequencer);
//    sequencer.ready();
  
//    sequencer.ready();
    

  }
  @After
  public void tearDown() throws Exception {
    warehouse = null;
    orders = null;
    pickreq = null;
    sequencer = null;
  }

  @Test
  public void testReady() {
    
//    sequencer.ready();

    boolean actual = false;
    if (sequencer.getScanCount() == 0 
        && sequencer.getCurrPickingReq().equals(pickreq)
        && warehouse.getSequencer("Billy").getScanOrder().equals(sequencer.getScanOrder())){
      actual = true;
    }
    
    Assert.assertEquals(true, actual);
  }

  @Test
  public void testSequencer() {
    boolean actual = false;
    if (sequencer.getName().equals("Billy")
        && sequencer.getWorksAt().equals(warehouse)){
      actual = true;
    }
    
    Assert.assertEquals(true, actual);
  }

  @Test
  public void testSequence() {
    fail("Not yet implemented"); // TODO
  }

  @Test
  public void testWorker() {
    boolean actual = false;
    if (sequencer.getName().equals("Billy")
        && sequencer.getWorksAt().equals(warehouse)){
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
    fail("Not yet implemented"); // TODO
  }

  @Test
  public void testAddScanCount() {
    fail("Not yet implemented"); // TODO
  }

  @Test
  public void testGetScanCount() {
    fail("Not yet implemented"); // TODO
  }

  @Test
  public void testResetScanCount() {
    fail("Not yet implemented"); // TODO
  }

}
