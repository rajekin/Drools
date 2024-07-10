import java.math.BigDecimal;
import java.util.List;

public class ProductService {

    public void filterAndUpdateProducts(List<Product> products, String type, String name, String fundingSource, BigDecimal newMaxAmount, BigDecimal newMinAmount) {
        for (Product product : products) {
            if (product.getType().equals(type) && product.getName().equals(name)) {
                for (EligFundingAccount account : product.getEligFundingAccounts()) {
                    if (account.getFundingSource().equals(fundingSource)) {
                        account.setMaxAmount(newMaxAmount);
                        account.setMinAmount(newMinAmount);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        // Sample data for testing
        List<EligFundingAccount> fundingAccounts1 = List.of(
            new EligFundingAccount("source1", new BigDecimal("1000"), new BigDecimal("500")),
            new EligFundingAccount("source2", new BigDecimal("2000"), new BigDecimal("1000"))
        );

        List<EligFundingAccount> fundingAccounts2 = List.of(
            new EligFundingAccount("source3", new BigDecimal("3000"), new BigDecimal("1500")),
            new EligFundingAccount("source4", new BigDecimal("4000"), new BigDecimal("2000"))
        );

        List<Product> products = List.of(
            new Product(1, "type1", "name1", fundingAccounts1),
            new Product(2, "type2", "name2", fundingAccounts2)
        );

        ProductService productService = new ProductService();
        productService.filterAndUpdateProducts(products, "type1", "name1", "source1", new BigDecimal("1500"), new BigDecimal("700"));

        // Print updated products for verification
        for (Product product : products) {
            System.out.println("Product ID: " + product.getId());
            System.out.println("Product Type: " + product.getType());
            System.out.println("Product Name: " + product.getName());
            for (EligFundingAccount account : product.getEligFundingAccounts()) {
                System.out.println("Funding Source: " + account.getFundingSource());
                System.out.println("Max Amount: " + account.getMaxAmount());
                System.out.println("Min Amount: " + account.getMinAmount());
            }
        }
    }
}
