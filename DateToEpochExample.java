// Initialize an array (example with nulls)
DepositRecomVO[] tgtDepositVO = new DepositRecomVO[4];
tgtDepositVO[0] = new DepositRecomVO();
tgtDepositVO[1] = null;
tgtDepositVO[2] = new DepositRecomVO();
tgtDepositVO[3] = null;

// Count non-null elements
int count = 0;
for (DepositRecomVO vo : tgtDepositVO) {
    if (vo != null) {
        count++;
    }
}

// Create a new array for non-null elements
DepositRecomVO[] filteredArray = new DepositRecomVO[count];
int index = 0;
for (DepositRecomVO vo : tgtDepositVO) {
    if (vo != null) {
        filteredArray[index++] = vo;
    }
}

// Assign the filtered array back to tgtDepositVO
tgtDepositVO = filteredArray;

// Verify the result
System.out.println("Filtered array size: " + tgtDepositVO.length);
for (DepositRecomVO vo : tgtDepositVO) {
    System.out.println(vo);
}
