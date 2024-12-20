// Method to sort accounts by openToBuy in ascending order
    public static List<Account> sortAccountsByOpenToBuy(List<Account> accountList) {
        accountList.sort(Comparator.comparing(Account::getOpenToBuy));
        return accountList;
    }
