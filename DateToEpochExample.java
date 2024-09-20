public class NameBuilder {

    public static String buildFullName(String[] nameParts) {
        // Use a StringBuilder to build the full name dynamically
        StringBuilder fullNameBuilder = new StringBuilder();

        // Iterate over each part in the array
        for (String part : nameParts) {
            // Add the part only if it's not null or empty
            if (part != null && !part.isEmpty()) {
                if (fullNameBuilder.length() > 0) {
                    // Add a space before adding the next part
                    fullNameBuilder.append(" ");
                }
                fullNameBuilder.append(part);
            }
        }

        // Convert the StringBuilder to a string
        return fullNameBuilder.toString();
    }

    public static void main(String[] args) {
        // Define an array for name parts: firstname, middlename, lastname, suffix
        String[] nameParts = {"John", "Paul", "Doe", "Jr."};

        // Build the full name using the array
        String fullName = buildFullName(nameParts);
        System.out.println(fullName);  // Output: "John Paul Doe Jr."
    }
}
