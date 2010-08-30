package athos.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

/**
 * Tests creation and query on clock class.  
 * 
 * @author Hongbing Kou
 * @version $Id: TestClock.java 281 2005-11-10 22:25:19Z hongbing $
 */
public class ClockTest extends TestCase {
  private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");;

/**
   * Tests creation of time clock. 
   *
   * @throws Exception If error while testing.
   */
  public void testCreation() throws Exception {
	Date date = simpleDateFormat.parse("01/01/2005 00:00:00");
    Clock clock = new Clock(date);
    assertEquals("Test year of clock", 2005, clock.getYear());
    assertEquals("Test month of clock", 0, clock.getMonth());
    assertEquals("Test day of clock", 1, clock.getDay());
    
    assertEquals("Test hour of clock", 0, clock.getHour());
    assertEquals("Test minute of clock", 0, clock.getMinute());
    assertEquals("Test second of clock", 0, clock.getSecond());
    assertEquals("Test clock string", "01/01/2005 00:00:00", clock.toString());
    
    Date date2 = simpleDateFormat.parse("12/31/2005 23:59:59");
    Clock clock2 = new Clock(date2);
    assertEquals("Test yearin december", 2005, clock2.getYear());
    assertEquals("Test month in december", 11, clock2.getMonth());
    assertEquals("Test day of clock", 31, clock2.getDay());
    
    assertEquals("Test hour of clock", 23, clock2.getHour());
    assertEquals("Test minute of clock", 59, clock2.getMinute());
    assertEquals("Test second of clock", 59, clock2.getSecond());
    
    Date dateA = simpleDateFormat.parse("01/01/2005 00:00:00");
    Clock clockA = new Clock(dateA);
    
    Date dateB = simpleDateFormat.parse("01/02/2005 00:00:00");
    Clock clockB = new Clock(dateB);
    assertEquals("Time difference", 24 * 60 * 60, Clock.diff(clockA, clockB));
  }
  
  /**
   * Tests the comparison of two clock objects. 
   * 
   * @throws Exception If error on testing.
   */
  public void testComparison() throws Exception {
    //MM/DD/YYYY HH:MM:SS    
    Date date1 = simpleDateFormat.parse("02/21/2005 07:00:00");
    Clock clock1 = new Clock(date1);

    Date date2 = simpleDateFormat.parse("02/21/2005 07:00:00");
    Clock clock2 = new Clock(date2);
    assertEquals("Tests two identical clocks", clock1, clock2);
    
    Date date3 = simpleDateFormat.parse("02/21/2005 07:00:01");
    Clock clock3 = new Clock(date3);
    assertTrue("Tests if 1 second off is correct", clock3.compareTo(clock1) > 0);
    assertTrue("Tests if 1 second off is correct", clock1.compareTo(clock3) < 0);
  }
}
