 public static long getHoursDifference(Date passedInDate) {
        // Get the current date/time
        Date currentDate = new Date();
        
        // Calculate the difference in milliseconds
        long millisecondsDifference = currentDate.getTime() - passedInDate.getTime();
        
        // Convert milliseconds to hours
        long hoursDifference = TimeUnit.MILLISECONDS.toHours(millisecondsDifference);
        
        return hoursDifference;
    }
