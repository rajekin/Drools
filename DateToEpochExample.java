As we plan our migration, I wanted to highlight an important consideration regarding Java 17. While IBM ODM will continue to support rulesets built in older versions, upgrading to Java 17 will require us to update projects that use Java XML Binding (JAXB).

Since JAXB was removed from the JDK starting with Java 11 and later reintroduced as a separate module, any projects relying on JAXB APIs will need modifications to ensure compatibility. This includes:

Adding the necessary JAXB dependencies explicitly.
Updating project configurations to work with the latest JAXB libraries.
Testing to confirm proper functionality post-migration.
