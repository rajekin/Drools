Hi Dave,

Thanks for following up.

This email was primarily to keep Brian in the loop, as he wasn’t able to attend the meeting with Justin due to a scheduling conflict. To give more context — DAX encountered an issue in production where one of the decisions returned an incorrect response. Upon investigation, we found that a required attribute was missing in the request payload, which led to the issue.

In our discussion, Justin raised the question of how we can better handle such scenarios going forward. We talked about introducing business validation rules to flag missing attributes early and return meaningful error messages, rather than allowing the request to proceed and potentially result in a faulty decision.

No formal policy has been developed yet — this is more of an early exploration to see how we can strengthen validation logic and improve contract adherence across source systems. If this evolves into a broader policy or implementation approach, we’ll certainly document and communicate it to all DO teams accordingly.
