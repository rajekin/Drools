accounts.sort(Comparator.comparingInt(account -> {
            int index = priorityOrder.indexOf(account.getRelationship());
            return index >= 0 ? index : priorityOrder.size(); // non-priority go to end
        }));
