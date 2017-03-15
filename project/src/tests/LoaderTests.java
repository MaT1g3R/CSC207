package tests;

import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import warehouse.Loader;
import warehouse.Order;
import warehouse.PickingRequest;
import warehouse.SkuTranslator;
import warehouse.Warehouse;

/**
 * The unit tests for the Loader class.
 *
 * @author Chaitanya
 */
public class LoaderTests {


  Warehouse warehouse;
  Loader loader;
  PickingRequest pick;
  ArrayList<Order> orders;
  int[] frontPal = {1, 3, 5, 7};
  int[] backPal = {2, 4, 6, 8};

  @Before
  public void setUp() throws Exception {
    SkuTranslator.setLocations("tests/translation.csv");
    SkuTranslator.setProperties("tests/traversal_table.csv");
    warehouse = new Warehouse("tests/initial.csv", "../", 30);
    orders = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      orders.add(new Order("Order Green SE"));
      warehouse.addOrder("Order Green SE");
    }
    pick = new PickingRequest(orders, 1);
    warehouse.sendToLoading(pick, frontPal, backPal);
    loader = new Loader("Joe", warehouse);
    warehouse.addLoader(loader);

  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testReady() {
    loader.ready();
  }

  @Test
  public void testAddPallets() {
    int[] frontPal = {1, 3, 5, 7};
    int[] backPal = {2, 4, 6, 8};
    loader.setPallets(frontPal, backPal);
  }

  @Test
  public void testBadLoad() {
    loader.load();
  }

  @Test
  public void testGoodLoad() {
    loader.ready();
    for (int i = 0; i < 8; i++) {
      loader.addScanCount();
    }
    loader.load();
  }

  @Test
  public void testGoodLoad1() {
    pick = new PickingRequest(orders, 0);
    warehouse.sendToLoading(pick, frontPal, backPal);
    loader.ready();
    for (int i = 0; i < 8; i++) {
      loader.addScanCount();
    }
    loader.load();
  }
}
