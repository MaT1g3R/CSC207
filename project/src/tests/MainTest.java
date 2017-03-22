package tests;

import org.junit.Assert;
import org.junit.Test;
import util.Main;

/**
 * Unit test for Main class.
 */
public class MainTest {

  @Test
  public void main() {
    try {
      String[] args = new String[]{"../events.txt"};
      Main.main(args);
    } catch (NullPointerException | IndexOutOfBoundsException ex) {
      Assert.fail();
    }
  }
}