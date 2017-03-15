package tests;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
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

  private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private ByteArrayOutputStream errContent = new ByteArrayOutputStream();


  @Before
  public void setUp() throws Exception {

    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));

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
  public void tearDown() throws Exception {

  }

  @Test
  public void testReady() {
    resetOutput();
    loader.ready();
    assertEquals("Loader Joe is ready to load.\r\n", outContent.toString());
  }

  @Test
  public void testAddPallets() {
    int[] frontPal = {1, 3, 5, 7};
    int[] backPal = {2, 4, 6, 8};
    loader.setPallets(frontPal, backPal);
  }

  @Test
  public void testBadLoad() {
    resetOutput();
    loader.load();
    assertEquals("The loader tried to load an incomplete picking request, "
        + "the picking request was sent to be re picked instead.\r\n", outContent.toString());
  }

  @Test
  public void testGoodLoad() {
    loader.ready();
    resetOutput();
    for (int i = 0; i < 8; i++) {
      loader.addScanCount();
    }
    loader.load();
    assertEquals("Loader Joe could not load picking request 1\n"
        + "The picking request is sent back to loading area.\r\n", outContent.toString());
  }

  @Test
  public void testGoodLoad1() {
    resetOutput();
    pick = new PickingRequest(orders, 0);
    warehouse.sendToLoading(pick, frontPal, backPal);
    loader.ready();
    for (int i = 0; i < 8; i++) {
      loader.addScanCount();
    }
    loader.load();
    assertEquals("Loader Joe is ready to load.\r\n" + "Loader Joe loaded picking request 0\r\n",
        outContent.toString());
  }

  public void resetOutput() {
    outContent = new ByteArrayOutputStream();
    errContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }
}
