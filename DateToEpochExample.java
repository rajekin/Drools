public static boolean hasMatchingOpenToBuy(List<Account> accountList) {
        return accountList.stream()
                .map(Account::getOpenToBuy)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .values()
                .stream()
                .anyMatch(count -> count > 1);
    }
