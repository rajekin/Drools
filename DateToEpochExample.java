public EligFundingAccount findAccountBySource(List<EligFundingAccount> accounts, String fundingSource) {
        for (EligFundingAccount account : accounts) {
            if (account.getFundingSource().equals(fundingSource)) {
                return account;
            }
        }
        return null; // Return null if no matching account is found
    }
