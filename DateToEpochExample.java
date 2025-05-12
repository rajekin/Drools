public static void createOrderCardIneligibilityReason(String reason, String type, ServicingCardMgmtResponse resp) {
    List<OrderCardIneligibilityReason> ineligibilityReasons = resp.getOrderCardIneligibilityReasons();

    // Check if an entry with same reason and type already exists
    boolean exists = ineligibilityReasons.stream().anyMatch(r ->
        reason.equalsIgnoreCase(r.getReason()) && type.equalsIgnoreCase(r.getType())
    );

    // If not found, create and add a new one
    if (!exists) {
        OrderCardIneligibilityReason newReason = new OrderCardIneligibilityReason();
        newReason.setReason(reason);
        newReason.setType(type);
        ineligibilityReasons.add(newReason);
    }
}
