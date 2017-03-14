package tests;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import warehouse.Truck;

/**
 * The unit tests for the Truck class.
 *
 * @author Chaitanya
 */
public class TruckTests {
  
  private Truck truck;
  
  @Before
  public void setUp() throws Exception {
    truck = new Truck(3);
  }
  
  @After
  public void tearDown() throws Exception {
    
  }

  @Test
  public void testUnSuccessfulAdd() {
    int[] cargo = {2,3,2,3};
    assertEquals(false, truck.addCargo(cargo, cargo, 23));  
  }
  
  @Test
  public void testSuccessfulAdd() {
    int[] cargo = {2,3,2,3};
    assertEquals(true, truck.addCargo(cargo, cargo, 3));
    assertEquals(true, truck.addCargo(cargo, cargo, 4)); 
  }
  
  @Test
  public void testIsFull(){
    assertEquals(false, truck.isFull());
    
    int[] cargo = {2,3,2,3};
    for(int i = 0; i < 19; i++){
      truck.addCargo(cargo, cargo, i + 3);
    }
    assertEquals(false, truck.isFull());
    truck.addCargo(cargo, cargo, 19 + 3);
    assertEquals(true, truck.isFull());
    assertEquals(false,truck.addCargo(cargo, cargo, 19 + 4));
  }

}
