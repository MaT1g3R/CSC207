package tests;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import warehouse.Replenisher;
import warehouse.SkuTranslator;
import warehouse.Warehouse;

/**
 * A test file for replenisher.
 *
 * @author Peijun
 */
public class ReplenisherTest {

  private Replenisher replenisher;
  private Warehouse warehouse;
  private ByteArrayOutputStream printStr;

  /**
   * Reset the print stream.
   */
  private void resetPrint() {
    printStr = new ByteArrayOutputStream();
    System.setOut(new PrintStream(printStr));
  }

  /**
   * Make new warehouse and Replenisher objects before each test case.
   */
  @Before
  public void setUp() {
    SkuTranslator.setLocations("tests/traversal_table.csv");
    SkuTranslator.setProperties("tests/translation.csv");
    warehouse = new Warehouse("tests/initial.csv", "tests/", 30);
    replenisher = new Replenisher("Kappa", warehouse);
  }

  /**
   * Test the Replenisher.getName() method.
   */
  @Test
  public void getName() {
    Assert.assertEquals("Kappa", replenisher.getName());
  }

  /**
   * Test the replenish method when there's no replenish needed.
   */
  @Test
  public void badReplenish() {
    resetPrint();
    replenisher.replenish(0);
    String displayString = "Unneeded replenish, nothing was added to the "
        + "inventory\n";
    Assert.assertEquals(displayString, printStr.toString());
  }

  /**
   * Test the replenish method when there's a replenish needed.
   */
  @Test
  public void goodReplenish() {
    resetPrint();
    int origSize = warehouse.getToBeReplenished().size();
    replenisher.replenish(19);
    int finalSize = warehouse.getToBeReplenished().size();
    String displayString = "Fascia of SKU 19 has been replenished.\n";

    Assert.assertTrue(origSize - 1 == finalSize && (displayString
        .equals(printStr.toString())));
  }

}