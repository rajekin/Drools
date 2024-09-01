import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Item {
    private int code;
    private String name;

    public Item(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Item{code=" + code + ", name='" + name + "'}";
    }
}

public class SequentialItems {

    public static List<Item> filterAndSequenceItems(List<Item> items) {
        // Filter out items with code > 3
        List<Item> filteredItems = items.stream()
                                        .filter(item -> item.getCode() <= 3)
                                        .collect(Collectors.toList());

        // Sort the filtered list by code
        filteredItems.sort((item1, item2) -> Integer.compare(item1.getCode(), item2.getCode()));

        // Find the minimum code after filtering
        int minCode = filteredItems.stream().mapToInt(Item::getCode).min().orElse(1);

        // Create a new list with sequential codes starting from the minimum code
        List<Item> sequentialItems = new ArrayList<>();
        int sequentialCode = minCode;

        for (Item item : filteredItems) {
            sequentialItems.add(new Item(sequentialCode++, item.getName()));
        }

        return sequentialItems;
    }

    public static void main(String[] args) {
        List<Item> items = new ArrayList<>();
        items.add(new Item(5, "Item E"));
        items.add(new Item(2, "Item B"));
        items.add(new Item(3, "Item C"));
        items.add(new Item(10, "Item J"));
        items.add(new Item(1, "Item A"));

        List<Item> sequentialItems = filterAndSequenceItems(items);

        // Print the filtered and sequential list
        sequentialItems.forEach(System.out::println);
    }
}
