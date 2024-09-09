import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        List<RateType> rateTypes = // initialize your list;

        Optional<RateType> result = rateTypes.stream()
            .filter(rateType -> "ACTIVE".equals(rateType.getType()))
            .filter(rateType -> "a".equals(rateType.getDisplayName()))
            .filter(rateType -> rateType.getOrder() == 1)
            .findFirst();

        result.ifPresent(rateType -> {
            // Do something with the result
            System.out.println("Found RateType: " + rateType);
        });
    }
}
