package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.MasterSystem;

/**
 * Unit test for Picker class.
 */
public class PickerTest {

  private static MasterSystem masterSystem;
  private static TestFactory testFactory;


  @BeforeClass
  public static void setUpBeforeClass() {
    testFactory = new TestFactory();
    masterSystem = testFactory.getTestEnviroment();
  }

  @Before
  public void setUp() {

  }

  @After
  public void tearDown() {

  }

  @Test
  public void scanNotNeeded() {

  }

  @Test
  public void scanSuccess() {

  }

  @Test
  public void scanMismatch() {

  }


}