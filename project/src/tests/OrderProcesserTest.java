package tests;

import java.util.Arrays;
import java.util.LinkedList;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import warehouse.OrderProcesser;

/**
 * Unit test for the OrderProcesser class.
 */
public class OrderProcesserTest {

  /**
   * the OrderProcesser variable we will be using.
   */
  private OrderProcesser myOp;

  /**
   * The amout of orders sent out each time.
   */
  private static final int ORDER_AMOUNT = 4;

  /**
   * Create a new instance of OrderProcesser for each test.
   */
  @Before
  public void setUp() {
    myOp = new OrderProcesser();
  }

  /**
   * Clear the myOp variable after each test.
   */
  @After
  public void tearDown() {
    myOp = null;
  }

  /**
   * Test the addOrder method.
   */
  @Test
  public void addOrder() {
    myOp.addOrder("SES Blue");
    myOp.addOrder("S Gold");
    LinkedList<String> expected = new LinkedList<>();
    expected.add("SES Blue");
    expected.add("S Gold");
    Assert.assertEquals(expected, myOp.getOrders());
  }

  /**
   * Test sendOrder method when there're greater or equal
   * to 4 orders in the system.
   */
  @Test
  public void sendOrderSuccess() {
    String[] ordersArray = new String[]{"SES Blue", "SES Red", "SE Grey",
        "SE Grey", "S Beige"};
    LinkedList<String> orders = new LinkedList<>(Arrays.asList(ordersArray));
    LinkedList<String> sentExpected = new LinkedList<>(
        orders.subList(0, ORDER_AMOUNT));
    LinkedList<String> leftExpected = new LinkedList<>();
    leftExpected.add("S Beige");
    for (String s : orders) {
      myOp.addOrder(s);
    }
    LinkedList<String> sentResult = myOp.sendOrder();
    LinkedList<String> letfResult = myOp.getOrders();
    Assert.assertEquals(sentExpected, sentResult);
    Assert.assertEquals(leftExpected, letfResult);
  }

  /**
   * Test the sendOrder method when there're less than 4 orders in the system.
   */
  @Test
  public void sendOrderFail() {
    String[] ordersArray = new String[]{"SES Blue", "SES Red", "SE Grey"};
    LinkedList<String> orders = new LinkedList<>(Arrays.asList(ordersArray));
    LinkedList<String> leftExpected = new LinkedList<>(orders);
    for (String s : orders) {
      myOp.addOrder(s);
    }
    LinkedList<String> sentResult = myOp.sendOrder();
    Assert.assertNull(sentResult);
    Assert.assertEquals(leftExpected, myOp.getOrders());
  }

  /**
   * Test the getOrders method.
   */
  @Test
  public void getOrders() {
    String[] ordersArray = new String[]{"SES Blue", "SES Red", "SE Grey",
        "SE Grey", "S Beige"};
    LinkedList<String> orders = new LinkedList<>(Arrays.asList(ordersArray));
    for (String s : orders) {
      myOp.addOrder(s);
    }
    Assert.assertEquals(orders, myOp.getOrders());

  }

}
