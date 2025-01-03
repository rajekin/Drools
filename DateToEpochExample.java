ODM Pipeline Deployment through Decision Center:

When deploying through the pipeline in the Decision Center (DC), the current version deployed in the rule execution server is not displayed. Additionally, the pipeline does not replace an already-deployed version.
We would like the pipeline to be enhanced so that it mirrors the functionality of the traditional DC deployment, including accurate version tracking and seamless replacement of existing deployments.
Automated Notifications for New Decisions in DC:

Currently, the BRMS team manually notifies the system administrator whenever a new decision is added to the DC.
This process should be automated to send email notifications whenever new decisions are added. This will ensure administrators can apply necessary security updates promptly.
Notifications for Decision Service Deployment to Production RES:

At present, the BRMS team does not receive notifications when a decision service is deployed from the Production DC to the Production RES.
We recommend automating notifications to the BRMS team whenever a decision service is deployed to the Production RES. These notifications should include details such as the name of the decision service and the deployed version.
Flexibility to Add New Projects in Review DC:

Currently, adding new projects in the review DC requires manual coordination with the system administrator.
We recommend modifying the system to allow the BRMS team the flexibility to add new projects independently.
Integration with Git for Source Control:

The BRMS team would like to use Git as the source control system for decisions. To support this, we request the ODM system administrator to upload the Maven Rules Compiler plugin to the Artifactory repository.
This will enable the BRMS team to automate the compilation of rules projects whenever they are checked into the source control system.
