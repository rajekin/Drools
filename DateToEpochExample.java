public static boolean allMembersHaveCardTokens(List<AccountHolder> accountHolders) {
        for (AccountHolder holder : accountHolders) {
            if (holder.cardDetails == null || holder.cardDetails.isEmpty()) {
                return false;
            }
            boolean hasValidToken = holder.cardDetails.stream()
                    .anyMatch(cd -> cd.cardToken != null && !cd.cardToken.trim().isEmpty());
            if (!hasValidToken) {
                return false;
            }
        }
        return true;
    }
}
