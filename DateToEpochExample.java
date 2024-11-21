// Initialize an array (example with nulls)
DepositRecomVO[] tgtDepositVO = new DepositRecomVO[4];
tgtDepositVO[0] = new DepositRecomVO(); // Valid object
tgtDepositVO[1] = null;                 // Null value
tgtDepositVO[2] = new DepositRecomVO(); // Valid object
tgtDepositVO[3] = null;                 // Null value

// Count non-null elements
int nonNullCount = 0;
for (int i = 0; i < tgtDepositVO.length; i++) {
    if (tgtDepositVO[i] != null) {
        nonNullCount++;
    }
}

// Create a new array for non-null elements
DepositRecomVO[] filteredArray = new DepositRecomVO[nonNullCount];
int index = 0;
for (int i = 0; i < tgtDepositVO.length; i++) {
    if (tgtDepositVO[i] != null) {
        filteredArray[index++] = tgtDepositVO[i];
    }
}

// Assign the filtered array back to tgtDepositVO
tgtDepositVO = filteredArray;

// Verify the result
System.out.println("Filtered array size: " + tgtDepositVO.length);
for (int i = 0; i < tgtDepositVO.length; i++) {
    System.out.println(tgtDepositVO[i]);
}
