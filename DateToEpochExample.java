import java.math.BigDecimal;

public class BigDecimalChecker {

    public static boolean containsSpecificBigDecimal(String str, BigDecimal specificBigDecimal) {
        // Check if the string contains the $ sign
        int dollarIndex = str.indexOf('$');
        if (dollarIndex == -1) {
            // $ sign not found, ignore comparison
            return false;
        }

        // Extract the substring after the $ sign
        String numericPart = str.substring(dollarIndex + 1);
        
        // Remove all non-numeric characters except for the decimal point and minus sign
        String cleanedNumericPart = numericPart.replaceAll("[^\\d.-]", "");

        try {
            // Convert the cleaned string to a BigDecimal
            BigDecimal bd = new BigDecimal(cleanedNumericPart);
            // Compare it with the specific BigDecimal
            return bd.equals(specificBigDecimal);
        } catch (NumberFormatException e) {
            // If conversion fails, return false
            return false;
        }
    }

    public static void main(String[] args) {
        String input = "rr $123.45";
        BigDecimal specificBigDecimal = new BigDecimal("123.45");
        boolean result = containsSpecificBigDecimal(input, specificBigDecimal);
        System.out.println("Does the string contain the specific BigDecimal? " + result);
    }
}
