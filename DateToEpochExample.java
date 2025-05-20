| **Issue ID** | **Area**                  | **Description**                                                                          | **Severity** | **Reproducibility** | **Screenshot Ref** |
| ------------ | ------------------------- | ---------------------------------------------------------------------------------------- | ------------ | ------------------- | ------------------ |
| ODM-V9-001   | Test Execution (DC)       | Test Suite remains at **100% status**, but results never load. Requires manual refresh.  | High         | Frequent            | IMG\_03F3C405...   |
| ODM-V9-002   | Test Report View (DC)     | Clicking report throws **“Unexpected error has occurred”** message, blocking analysis.   | Critical     | Always              | IMG\_C9D14659...   |
| ODM-V9-003   | UI Performance            | **“Working. More time is needed…”** message stalls UI for several minutes.               | Medium       | Frequent            | IMG\_15B6B803...   |
| ODM-V9-004   | Server Timeout            | Test execution fails with **xhrTimeout error**, even though timeout is 300000ms (5 min). | Critical     | Intermittent        | IMG\_ED807E89...   |
| ODM-V9-005   | Test Suite Execution Time | Some tests **stuck at 39% for 2+ hours** or **running over 1 hour** without completion.  | Critical     | Reproducible        | IMG\_5CC970EF...   |



1. Objective
To certify the stability and performance of IBM ODM V9 environment by executing rule test suites across different projects, and to identify blockers for production readiness.


3. Detailed Observations
Performance Bottlenecks:

Test execution often takes 10–15× longer than expected.

Long-running tests cause Decision Center UI to hang or time out.

Usability Concerns:

Frequent UI freezes with generic error messages.

Difficulty viewing or exporting test reports after execution.

Blocking Impact:

Unable to validate business rules execution at scale.

No visibility into test outcomes due to broken reporting.
