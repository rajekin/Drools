package com.example.Dashboard;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateToEpochExample {
    public static void main(String[] args) throws ParseException {
        // Given date string without a timestamp
        String dateString = "2023-10-20";

        // Concatenate a default time (e.g., midnight)
        dateString += " 00:00:00";

        // Create a SimpleDateFormat object to parse the date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Parse the date string to a Date object
        Date date = dateFormat.parse(dateString);

        // Get the epoch timestamp in milliseconds
        long epochTimestamp = date.getTime();

        System.out.println("Epoch Timestamp for " + dateString + ": " + epochTimestamp);
    }
}
