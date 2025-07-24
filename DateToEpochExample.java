import java.util.Calendar;
import java.util.Date;

public class TestAccountVelocityUtils {

    public static void main(String[] args) {
        // Create two dates using Calendar
        Calendar cal1 = Calendar.getInstance();
        cal1.set(2025, Calendar.JULY, 24);  // 24 July 2025

        Calendar cal2 = Calendar.getInstance();
        cal2.set(2025, Calendar.JULY, 10);  // 10 July 2025

        Date date1 = cal1.getTime();
        Date date2 = cal2.getTime();

        // Call the method and print result
        Integer daysDiff = AccountVelocityUtils.daysBetweenDates(date1, date2);
        System.out.println("Days between dates: " + daysDiff);

        // Try with reversed dates
        Integer reversedDaysDiff = AccountVelocityUtils.daysBetweenDates(date2, date1);
        System.out.println("Days between reversed dates: " + reversedDaysDiff);

        // Try with same date
        Integer sameDayDiff = AccountVelocityUtils.daysBetweenDates(date1, date1);
        System.out.println("Days between same dates: " + sameDayDiff);
    }
}
