import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.Date;

public class DateHourDifferenceTest {

    @Test
    public void testGetHoursDifference() {
        // Create a Date object representing the passed-in date (including timezone)
        Date passedInDate = new Date(1634794800000L); // Example: 2021-10-21T12:00:00Z
        
        // Calculate the expected hours difference (assuming the current date/time is 2024-02-17T14:30:00Z)
        // You should adjust this according to your testing environment
        long expectedHoursDifference = 34336; // Example: 34336 hours between the two dates
        
        // Call the method to get the actual hours difference
        long actualHoursDifference = DateHourDifference.getHoursDifference(passedInDate);
        
        // Assert that the actual hours difference matches the expected hours difference
        assertEquals(expectedHoursDifference, actualHoursDifference);
    }
}
