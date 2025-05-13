public static boolean hasValidCardToken(AccountHolder holder) {
    if (holder.getCardDetails() == null || holder.getCardDetails().isEmpty()) {
        return false;
    }

    return holder.getCardDetails().stream()
        .anyMatch(cd -> cd.getCardToken() != null && !cd.getCardToken().trim().isEmpty());
}
