import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

class Product {
    private int id;
    private String type;
    private String name;
    private List<EligFundingAccount> eligFundingAccounts;

    // Constructor, getters, and setters
    public Product(int id, String type, String name, List<EligFundingAccount> eligFundingAccounts) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.eligFundingAccounts = eligFundingAccounts;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<EligFundingAccount> getEligFundingAccounts() {
        return eligFundingAccounts;
    }

    public void setEligFundingAccounts(List<EligFundingAccount> eligFundingAccounts) {
        this.eligFundingAccounts = eligFundingAccounts;
    }
}

class EligFundingAccount {
    private String fundingSource;
    private BigDecimal maxAmount;
    private BigDecimal minAmount;

    // Constructor, getters, and setters
    public EligFundingAccount(String fundingSource, BigDecimal maxAmount, BigDecimal minAmount) {
        this.fundingSource = fundingSource;
        this.maxAmount = maxAmount;
        this.minAmount = minAmount;
    }

    public String getFundingSource() {
        return fundingSource;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    // Method to create a deep copy of the funding account
    public EligFundingAccount copy() {
        return new EligFundingAccount(this.fundingSource, this.maxAmount, this.minAmount);
    }
}

public class ProductService {

    public int filterAndUpdateProducts(List<Product> products, String type, String name, String fundingSource, BigDecimal newMaxAmount, BigDecimal newMinAmount) {
        int updateCount = 0; // Counter to keep track of updates

        for (Product product : products) {
            if (product.getType().equals(type) && product.getName().equals(name)) {
                List<EligFundingAccount> updatedAccounts = new ArrayList<>();
                for (EligFundingAccount account : product.getEligFundingAccounts()) {
                    EligFundingAccount accountCopy = account.copy();
                    if (accountCopy.getFundingSource().equals(fundingSource)) {
                        accountCopy.setMaxAmount(newMaxAmount);
                        accountCopy.setMinAmount(newMinAmount);
                        updateCount++; // Increment counter when an update occurs
                    }
                    updatedAccounts.add(accountCopy);
                }
                // Replace the original list with the updated list
                product.setEligFundingAccounts(updatedAccounts);
            }
        }

        return updateCount; // Return the number of updates
    }

    public static void main(String[] args) {
        // Sample data for testing
        List<EligFundingAccount> fundingAccounts1 = new ArrayList<>();
        fundingAccounts1.add(new EligFundingAccount("source1", new BigDecimal("1000"), new BigDecimal("500")));
        fundingAccounts1.add(new EligFundingAccount("source2", new BigDecimal("2000"), new BigDecimal("1000")));

        List<EligFundingAccount> fundingAccounts2 = new ArrayList<>();
        fundingAccounts2.add(new EligFundingAccount("source3", new BigDecimal("3000"), new BigDecimal("1500")));
        fundingAccounts2.add(new EligFundingAccount("source4", new BigDecimal("4000"), new BigDecimal("2000")));

        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "type1", "name1", fundingAccounts1));
        products.add(new Product(2, "type2", "name2", fundingAccounts2));

        ProductService productService = new ProductService();
        int updateCount = productService.filterAndUpdateProducts(products, "type1", "name1", "source1", new BigDecimal("1500"), new BigDecimal("700"));

        System.out.println("Number of updates: " + updateCount);

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
