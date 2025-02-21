Subject: Frequent Issue with Ruleset Execution in Test RES

Dear [RES Admin's Name],

We have observed a recurring issue in our Test Rule Execution Server (RES) where a newly deployed ruleset with the same version is not referenced until the RES instance is restarted. This has been impacting both the Business Teams and IT Teams by slowing down the validation of the decision service.

To help address this, could you confirm the current value of ruleset.maxIdleTime in our Test RES? Ideally, this value should not be set to 0 and should be -1 in lower environments to ensure that newly deployed RuleApps are parsed immediately.

Please let us know if any adjustments can be made to resolve this issue effectively.
