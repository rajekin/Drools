 Steps Involved in Migrating Decision Service from ODM v8.11 to v9

As part of the migration from IBM ODM v8.11 to v9, below are the key steps involved:

🔄 Migration Steps Overview
Upgrade to Java 17

ODM v9 requires Java 17, so the project environment and dependencies must be updated accordingly.

Modify Java Code for Compatibility

Refactor Java methods and code segments that are not compatible with Java 17 (e.g., removed or deprecated APIs from Java 8).

Upgrade Jackson Library

Update Jackson to version 2.13 or higher to ensure compatibility with Java 17 and avoid serialization/deserialization issues.

Unit Testing

Rebuild the project and execute unit tests to verify the functional correctness of the rules post-migration.

Push Decision Service to Decision Center

Deploy the migrated decision service to Decision Center for testing and business validation.

Run Scenario Testing in Decision Center

Execute scenario tests and use the ODM compare tool to verify and compare results between v8.11 and v9 to ensure consistency.

Deploy to Rule Execution Server (RES)

Deploy the decision service to the v9 RES.

Compare API Results Between v8.11 and v9

Run test payloads against both v8.11 and v9 Rule Execution Server endpoints and compare the results to confirm expected behavior remains consistent.
