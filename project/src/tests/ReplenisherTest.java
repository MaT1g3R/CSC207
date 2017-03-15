package tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import warehouse.Replenisher;
import warehouse.Warehouse;

/**
 * A test file for replenisher.
 *
 * @author Peijun
 */
public class ReplenisherTest {

  private Replenisher replenisher;
  private Warehouse warehouse;

  @Before
  public void setUp() {
    warehouse = new Warehouse("tests/initial.csv", "", 30);
    replenisher = new Replenisher("Kappa", warehouse);
  }

  @After
  public void tearDown() {
    warehouse = null;
    replenisher = null;
  }

  @Test
  public void getName() {
    Assert.assertEquals("Kappa", replenisher.getName());
  }

  @Test
  public void badReplenish() {
    
  }

  @Test
  public void goodReplenish() {

  }

}