import org.apache.commons.lang3.SerializationUtils;

public class Main {
    public static void main(String[] args) {
        Address address = new Address("New York", "5th Avenue");
        Person originalPerson = new Person("John Doe", 30, address);

        // Perform deep clone
        Person clonedPerson = SerializationUtils.clone(originalPerson);

        // Print original and cloned objects
        System.out.println("Original: " + originalPerson);
        System.out.println("Cloned: " + clonedPerson);

        // Modify the cloned object to see if it affects the original
        clonedPerson.getAddress().setCity("Los Angeles");

        System.out.println("After modification:");
        System.out.println("Original: " + originalPerson);
        System.out.println("
