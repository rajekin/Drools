// Import necessary utilities if not already imported
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.List;

// Assuming tgtDepositVO is an array
List<DepositRecomVO> depositList = new ArrayList<>(Arrays.asList(tgtDepositVO));

// Remove null elements
depositList.removeIf(Objects::isNull);

// Convert back to array if needed
tgtDepositVO = depositList.toArray(new DepositRecomVO[depositList.size()]);
