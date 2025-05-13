public static void addEligibleAccountHolder(Response resp, String memberNumber) {
    AccountHolder holder = new AccountHolder();
    holder.setMemberNumber(memberNumber);
    resp.getEligibleAccountHolders().add(holder);
}


public static void addOrUpdateIneligibleAccountHolder(Response resp, String memberNumber, String reason) {
    List<AccountHolder> ineligibleList = resp.getIneligibleAccountHolders();

    for (AccountHolder holder : ineligibleList) {
        if (holder.getMemberNumber().equals(memberNumber)) {
            // ✅ Fetch and update the reasons list
            List<String> reasons = holder.getReasons();
            if (reasons == null) {
                reasons = new ArrayList<>();
                holder.setReasons(reasons);
            }
            if (!reasons.contains(reason)) {
                reasons.add(reason);
            }
            return;
        }
    }

    // ✅ Not found — create new holder
    AccountHolder newHolder = new AccountHolder();
    newHolder.setMemberNumber(memberNumber);
    List<String> reasons = new ArrayList<>();
    reasons.add(reason);
    newHolder.setReasons(reasons);
    ineligibleList.add(newHolder);
}
